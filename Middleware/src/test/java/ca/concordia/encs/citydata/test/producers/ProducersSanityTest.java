package ca.concordia.encs.citydata.test.producers;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.operations.JsonFilterOperation;
import ca.concordia.encs.citydata.operations.StandardFilteringOperation;
import ca.concordia.encs.citydata.operations.TemporalAggregationOperation;
import ca.concordia.encs.citydata.producers.JSONBuildingProducer;
import ca.concordia.encs.citydata.producers.EnergyConsumptionProducer;
import ca.concordia.encs.citydata.producers.CSVEnvironmentalSensorProducer;
import ca.concordia.encs.citydata.producers.TXTGeoLocationProducer;
import ca.concordia.encs.citydata.producers.CSVRoomOccupancyProducer;

public class ProducersSanityTest {

	@Test
	public void testBuildingProducer() {
		final JSONBuildingProducer producer = new JSONBuildingProducer(null, null);
		producer.setBuildingName("mock");
		producer.fetch();
		ArrayList<JsonObject> result = producer.getResult();
		System.out.println(result);
	}

	@Test
	public void testEnergyConsumptionProducer() {
		final EnergyConsumptionProducer producer1 = new EnergyConsumptionProducer(null);
		producer1.setCity("montreal");
		producer1.setStartDatetime("2021-09-01 00:00:00");
		producer1.setEndDatetime("2021-09-01 23:59:00");
		producer1.setClientId(1);
		producer1.validateParams();
		producer1.buildQuery();
		producer1.fetch();
		ArrayList<JsonArray> result = producer1.getResult();
		System.out.println(result);
	}

	@Test
	public void testEnvironmentalSensorProducer() {
		final CSVEnvironmentalSensorProducer producer = new CSVEnvironmentalSensorProducer(null);
		producer.setFilePath("temperature.csv");
		final StandardFilteringOperation operation = new StandardFilteringOperation();
		operation.setSensorId("12504");
		operation.setRoom("221");
		operation.setDate("2025-07-01");

		producer.setOperation(operation);
		producer.fetch();

		ArrayList<String> result = producer.getResult();
		System.out.println(result);
	}

	@Test
	public void testRoomOccupancyProducer() {
		final CSVRoomOccupancyProducer producer = new CSVRoomOccupancyProducer(null);
		producer.setFilePath("occupancy.csv");

		final TemporalAggregationOperation operation = new TemporalAggregationOperation();
		operation.setRoom("411");
		operation.setDate("2025-07-10");
		operation.setStartTime("15:30:00");
		operation.setEndTime("16:00:00");

		producer.setOperation(operation);
		producer.fetch();
		ArrayList<String> result = producer.getResult();
		System.out.println(result);
	}
	
	@Test
	public void testGeoNamesCitiesProducer() {
		final TXTGeoLocationProducer producer = new TXTGeoLocationProducer(null);
		producer.setFilePath("geolocation/cities15000.txt");
		
		final JsonFilterOperation operation = new JsonFilterOperation();
		operation.setKey("countryCode");
		operation.setValue("AE");
		
		producer.setOperation(operation);
		producer.fetch();
		ArrayList<JsonObject> result = producer.getResult();
		System.out.println("Result size: " + result.size());
	    result.forEach(System.out::println);
	}

	/* TODO: uncomment and make it run
	@Test
		 GeometryProducer wraps JSONProducer and applies MergeOperation
			public void testGeometryProducer() {
				final GeometryProducer producer = new GeometryProducer();
				producer.setCity("montreal");
				
				final operation
			}*/
}
