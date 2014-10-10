package hr.fer.zari.rasip.tiger.handler;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.domain.SensorField;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GSNHandler implements CollectingUnitHandler {

	protected GSNHandler() {}
	
	protected Base64 base64Gen = new Base64();
	
	@Override
	public List<Sensor> fetchSensors(CollectingUnit collectingUnit) {
		
		List<Map<String, Object>> sensorsData;
		try {
			InputStream inStream = getInputStreamForSensorsFetch(collectingUnit);
			sensorsData = inputStreamToJsonMap(inStream);
			
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
		List<Sensor> sensors = parseData(sensorsData, collectingUnit);

		return sensors;
	}
	
	@SuppressWarnings("unchecked")
	private List<Sensor> parseData(List<Map<String, Object>> sensorsData, CollectingUnit collectingUnit) {
		List<Sensor> list = new ArrayList<>(sensorsData.size());
		for (Map<String,Object> map : sensorsData) {
			Sensor sensor = new Sensor();
			sensor.setName((String) map.get("name"));
			sensor.setCollectingUnit(collectingUnit);
			
			List<Map<String, String>> fields = (List<Map<String, String>>) map.get("fields");
			for (Map<String, String> field : fields) {
				SensorField mt = new SensorField();
				String type = convertToStandardType(collectingUnit, field.get("type"));
				if(type == null){
					continue;
				}
				mt.setName(field.get("name"));
				mt.setUnit(type);
				mt.setDescription(field.get("description"));
				sensor.addSensorField(mt);
				
			}
			
			list.add(sensor);
		}
		
		return list;
	}

	private String convertToStandardType(CollectingUnit collectingUnit, String type) {
		Set<String> set = collectingUnit.getType().getSupportedMeasurementUnits();
		String standardType = null;
		for (String supportedType : set) {
			if(supportedType.equalsIgnoreCase(type)){
				standardType = supportedType;
			}
		}
		
		return standardType;
	}

	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> inputStreamToJsonMap(InputStream inStream) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(inStream, List.class);
	}

	private InputStream getInputStreamForSensorsFetch(CollectingUnit cu) throws IOException{
		InputStream inStream = null; 
		
		String url = String.format("%s/rest/sensors",cu.getLocationUrl());
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			
			Map<String, String> headers = headersForRequestToCollectingUnitInternal(cu);
			for (Entry<String, String> entry : headers.entrySet()) {
				connection.setRequestProperty(entry.getKey(), entry.getValue());
			}

			int responseCode = connection.getResponseCode();
			if(responseCode >= 200 && responseCode < 400){
				inStream = connection.getInputStream();
			}
		
		return inStream;
	}

	@Override
	public Map<String, String> headersForRequestToCollectingUnit(CollectingUnit collectingUnit) {
		return headersForRequestToCollectingUnitInternal(collectingUnit);
	}
	
	private Map<String, String> headersForRequestToCollectingUnitInternal(CollectingUnit collectingUnit) {
		Map<String, String> headers = new HashMap<>(1);
		String authHeader = createBasicAuthorization(collectingUnit);
		headers.put("Authorization", authHeader);

		return headers;
	}
	
	private String createBasicAuthorization(CollectingUnit collectingUnit) {
		String username = collectingUnit.getUsername();
		String password = collectingUnit.getPassword();
		
		String userAndPass = String.format("%s:%s",	username, password);
		String base64EncodedAuth = base64Gen.encodeAsString(userAndPass.getBytes());

		String authHeader = String.format("Basic %s", base64EncodedAuth);
		return authHeader;
	}

	@Override
	public String convertMeasuredValue(SensorField sensorField, Object value) {
		return value.toString();
	}
	
}
