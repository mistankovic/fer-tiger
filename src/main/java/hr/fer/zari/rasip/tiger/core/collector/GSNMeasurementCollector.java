package hr.fer.zari.rasip.tiger.core.collector;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Measurement;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.domain.SensorField;
import hr.fer.zari.rasip.tiger.handler.CollectingUnitHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GSNMeasurementCollector implements MeasurementCollector {
	
	private static final Logger logger = LoggerFactory.getLogger(GSNMeasurementCollector.class);
	
	private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
	
	private CollectingPool collectingPool;
	private CollectingUnit collectingUnit;
	private CollectingUnitHandler cuHandler;

	
	protected GSNMeasurementCollector(CollectingPool collectingPool,
			CollectingUnit collectingUnit, CollectingUnitHandler cuHandler) {
		super();
		this.collectingUnit = collectingUnit;
		this.collectingPool = collectingPool;
		this.cuHandler = cuHandler;
	}

	@Override
	public List<Measurement> collect() {
		Date to = new Date();
		Date from = collectingUnit.getLastCollected() != null ? collectingUnit.getLastCollected() : new Date(0) ;
		
		DateFormat dateFormat = createDateFormat();
		String urlParams = String.format("?from=%s&to=%s", dateFormat.format(from), dateFormat.format(to));
		Map<String, String> headers = cuHandler.headersForRequestToCollectingUnit(collectingUnit);
		
		Set<Sensor> sensors = collectingUnit.getSensors();
		List<Future<List<Measurement>>> futures = new ArrayList<>(sensors.size());
		for (Sensor sensor : sensors) {
			String urlBase = String.format("%s/rest/sensors/%s/", 
					collectingUnit.getLocationUrl(), sensor.getName()) + "%s" + urlParams;
			
			for (SensorField sf : sensor.getSensorFields()) {
				String url = String.format(urlBase, sf.getName());
				GSNCollectingJob job = new GSNCollectingJob(url, headers, sf);
				Future<List<Measurement>> future = collectingPool.submit(job);
				futures.add(future);
			}
		}
		
		List<Measurement> measured = new ArrayList<>();
		for (Future<List<Measurement>> future : futures) {
			List<Measurement> result = null;
			try {
				result = future.get();
			} catch (InterruptedException | ExecutionException e) {
				logger.warn("Error occured in collecting measurements", e);
			}
			if(result != null){
				measured.addAll(result);
			}
		}
		
		return measured;
	}

	private DateFormat createDateFormat() {
		return new SimpleDateFormat(DATE_TIME_PATTERN);
	}

	private class GSNCollectingJob implements Callable<List<Measurement>> {

		String url;
		Map<String, String> headers;
		DateFormat df;
		SensorField sensorField;

		public GSNCollectingJob(String url, Map<String, String> headers, SensorField sensorField) {
			this.url = url;
			this.headers = headers;
			this.df = createDateFormat();
			this.sensorField = sensorField;
		}

		@Override
		public List<Measurement> call() {
			List<Measurement> result = null;
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(url)
						.openConnection();
				if (headers != null) {
					for (Entry<String, String> headerEntry : headers.entrySet()) {
						conn.setRequestProperty(headerEntry.getKey(), headerEntry.getValue());
					}
				}
				int respCode = conn.getResponseCode();
				if (respCode >= 200 && respCode < 400) {
					ObjectMapper mapper = new ObjectMapper();
					Map<String, Object> map = mapper.readValue(conn.getInputStream(),
							new TypeReference<HashMap<String, Object>>() {});
					
					result = readMeasures(map);
				}
				else{
					logger.info("call() - server responded on url {} with status code {}", url, respCode);
				}
			} catch (IOException e) {
				logger.warn("call()", e);
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		private List<Measurement> readMeasures(Map<String, Object> map) {
			Iterable<String> timestamps = (Iterable<String>) map
					.get("timestamps");
			Iterable<Object> values = (Iterable<Object>) map.get("values");

			Iterator<String> timestampsIterator = timestamps.iterator();
			Iterator<Object> valuesiIterator = values.iterator();

			List<Measurement> measurements = new ArrayList<>();
			while (valuesiIterator.hasNext() && timestampsIterator.hasNext()) {
				Object value = valuesiIterator.next();
				String timestamp = timestampsIterator.next();
				Date dateMeasured = null;
				try {
					dateMeasured = df.parse(timestamp);
				} catch (ParseException e) {
					logger.warn("readMeasures()", e);
				}
				
				String stringValue = cuHandler.convertMeasuredValue(sensorField, value);
				Measurement measurement = new Measurement(stringValue, sensorField.getId(), dateMeasured);
				measurements.add(measurement);
			}

			return measurements;
			
		}
	}

}
