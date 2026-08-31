package ca.concordia.encs.citydata.core.implementations;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * This producer can load JSON from a file or remotely via an HTTP request.
 *
 * @author Gabriel C. Ullmann
 * @since 2024-12-01
 */

public non-sealed class JSONProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	public JSONProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public JSONProducer(final String filePath) {
		super(filePath);
	}

	@Override
	public void fetch() {
		beforeFetch();
		try (InputStream inputStream = obtainInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			final JsonElement parsedElement = JsonParser.parseReader(reader);
			final ArrayList<JsonObject> jsonOutput = new ArrayList<>();
			jsonOutput.add(wrapAsObject(parsedElement));
			
			this.setResult(jsonOutput);
			this.applyOperation();
		} catch (Exception e) {
			throw new RuntimeException("Failed to read JSON file: " + this.getFilePath() + " (" + e.getClass().getSimpleName() + ": " + e.getMessage() + ")", e);
		}
//		final ArrayList<JsonObject> jsonOutput = new ArrayList<>();
//
//		// Use ByteArrayOutputStream to fetch data
//
//		OutputStream outputStream = this.fetchFromPath();
//		String inputJson = outputStream.toString();
//
//		// Convert JSON string to object
//		final JsonElement inputJsonElement = JsonParser.parseString(inputJson);
//
//		JsonObject outputJsonObject = new JsonObject();
//		if (inputJsonElement.isJsonArray()) {
//			outputJsonObject.add("result", inputJsonElement);
//		} else {
//			outputJsonObject = inputJsonElement.getAsJsonObject();
//		}
//
//		jsonOutput.add(outputJsonObject);
//		this.setResult(jsonOutput);
//		this.applyOperation();
	}

	protected JsonObject wrapAsObject(JsonElement parsedElement) {
		if (parsedElement.isJsonArray()) {
			JsonObject wrapped = new JsonObject();
			wrapped.add("result", parsedElement.getAsJsonArray());
			return wrapped;
		}
		return parsedElement.getAsJsonObject();
	}

	protected InputStream obtainInputStream() {
		return this.fetchStream();
	}

	protected void beforeFetch() {
		
	}
}