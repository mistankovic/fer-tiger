package hr.fer.zari.rasip.tiger.controller;

import hr.fer.zari.rasip.tiger.domain.Measurement;
import hr.fer.zari.rasip.tiger.rest.model.MeasurementGraphModel;
import hr.fer.zari.rasip.tiger.rest.model.MeasurementRestModel;
import hr.fer.zari.rasip.tiger.service.MeasurementService;
import hr.fer.zari.rasip.tiger.util.StringUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/measurement")
public class MeasurementController {

	private static final String CSV_DELIMITER = "\t";
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private static final Logger logger = LoggerFactory.getLogger(MeasurementController.class);
	
	private MeasurementService measurementService;
	private Map<String, List<Measurement>> preparedMeasurements = new HashMap<>();

	@Autowired
	public MeasurementController(MeasurementService measurementService) {
		this.measurementService = measurementService;
	}

	@RequestMapping(value="/graph", method=RequestMethod.POST)
	public List<MeasurementGraphModel> graphMeasurements(@RequestBody List<Long> fields, 
			@RequestParam @DateTimeFormat(pattern=DATE_TIME_FORMAT) Date from, 
			@RequestParam @DateTimeFormat(pattern=DATE_TIME_FORMAT) Date to){
		
		List<Measurement> measurements = getMeasurements(fields, from, to);
		
		return measurementService.convertToGraphModel(measurements);
	}
	
	@RequestMapping(value="/prepare", method=RequestMethod.POST)
	public String prepareMeasurements(@RequestBody List<Long> fields, 
			@RequestParam @DateTimeFormat(pattern=DATE_TIME_FORMAT) Date from, 
			@RequestParam @DateTimeFormat(pattern=DATE_TIME_FORMAT) Date to) {
		
		List<Measurement> measurements = getMeasurements(fields, from, to);
		String token = addToPreparedMeasurements(measurements);

		return token;
	}

	@RequestMapping(value="{key}/csv", method=RequestMethod.GET, produces="text/csv")
	public void exportAsCSV(@PathVariable String key, HttpServletResponse response){
		List<Measurement> measurements = preparedMeasurements.remove(key);
		if(measurements != null){

			measurementService.orderBySensorFileAndDateMeasured(measurements);
			List<MeasurementRestModel> models = measurementService.convert(measurements);
			try {
				convertToCSV(models, response.getOutputStream());
				return;
			} catch (IOException e) {
				logger.warn("exportAsCSV():", e);
			}
		}
		
		throw new RuntimeException("Unknown key: " + key);
	}
	
	private String addToPreparedMeasurements(List<Measurement> measurements) {
		int byteArrayLen = 8;
		String token = StringUtils.randomToken(byteArrayLen);
		while(preparedMeasurements.containsKey(token)){
			token = StringUtils.randomToken(byteArrayLen);
		}
		preparedMeasurements.put(token, measurements);
		return token;
	}
	
	private List<Measurement> getMeasurements(List<Long> fields, Date from, Date to){
		List<Measurement> measurements = new ArrayList<>();
		for (Long fieldId : fields) {
			List<Measurement> sensorFieldMeasurements = measurementService.findAllWithSensorIdBetweenDates(fieldId, from, to);
			measurements.addAll(sensorFieldMeasurements);
		}
		
		return measurements;
	}
	
	private void convertToCSV(List<MeasurementRestModel> measurements, OutputStream outputStream) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
		
		writer.write("field");
		writer.write(CSV_DELIMITER);
		writer.write("measured");
		writer.write(CSV_DELIMITER);
		writer.write("value");
		writer.newLine();

		for (MeasurementRestModel mrm : measurements) {
			writer.write(mrm.getSensorFieldName());
			writer.write(CSV_DELIMITER);
			
			Long timestamp = mrm.getDateMeasured().getTime();
			writer.write(timestamp.toString());
			writer.write(CSV_DELIMITER);
			
			writer.write(mrm.getValue());
			writer.newLine();
		}
		
		writer.flush();
	}
}
