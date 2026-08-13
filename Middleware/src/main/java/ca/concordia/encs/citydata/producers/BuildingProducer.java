package ca.concordia.encs.citydata.producers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.core.implementations.JSONProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * This Producer outputs metadata about a building, such as floors, zones and sensors.
 * @author Gabriel C. Ullmann, Sikandar Ejaz, Minette Zongo
 * @since 2025-05-28
 */

public class BuildingProducer extends JSONProducer {

	private String filePath;

	public BuildingProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public BuildingProducer(final String filePath) {
		super(filePath);
	}
	
	
	public void setBuildingName(String buildingName) {
		if (buildingName == null) {
			throw new InvalidParameterException("Please provide a building name to the producer.");
		}
		if (buildingName.endsWith(".geojson") || buildingName.endsWith(".json")) {
			this.filePath = "./src/test/resources/" + buildingName;
		} else {
			this.filePath = "./src/test/resources/" + buildingName + "_building.json";
		}
//		if (buildingName != null) {
//			if (buildingName.endsWith(".geojson")) {
//				this.filePath = "./src/test/resources/" + buildingName;
//			} else if (buildingName.endsWith(".json")) {
//				this.filePath = "./src/test/resources/" + buildingName;
//			} else {
//				this.filePath = "./src/test/resources/" + buildingName + "_building.json";
//			}
//		} else {
//			throw new InvalidParameterException("Please provide a building name to the producer.");
//		}
	}

	@Override
	protected InputStream obtainInputStream() {

		// 1. Try classpath first
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

		// 2. Fall back to filesystem
		if (inputStream == null) {
			try {
				inputStream = Files.newInputStream(Paths.get(filePath));
			} catch (Exception e) {
				throw new RuntimeException("Building file not found on classpath or filesystem: " + filePath, e);
			}
		}
		return inputStream;
	}
}
