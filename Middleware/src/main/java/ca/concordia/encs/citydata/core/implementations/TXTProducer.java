package ca.concordia.encs.citydata.core.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

public non-sealed class TXTProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {
	
	private String delimiter = "\t"; 
	
	public TXTProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public TXTProducer(final String filePath) {
		super(filePath);
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	@Override
	public void fetch() {
		beforeFetch();
		ArrayList<JsonObject> records = new ArrayList<>(); 
		try (InputStream inputStream = obtainInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
			String line;
			while ((line = reader.readLine()) != null) { 
				if (!line.isBlank()) {
					records.add(parseRecord(line.split(delimiter, -1)));
				} 
			} 	
		} catch (IOException e) {
					throw new MiddlewareException.DatasetNotFound("Error processing TXT data");
		}
		this.setResult(records);
		this.applyOperation();				
	}
	
	// For authorization checks - if the user has the right to access a specific producer. Implemented within the producers
	protected void beforeFetch() {
		
	}
	
	protected InputStream obtainInputStream() {
		return this.fetchStream();
	}
	
	protected JsonObject parseRecord(String[] fields) {
		JsonObject record = new JsonObject();
		for (int i = 0; i <=fields.length; i++) {
			record.addProperty("field" +i, fields[i]);
		}
		return record;
	}
	
}
