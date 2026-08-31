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
	private boolean structured = false;
	
	public TXTProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public TXTProducer(final String filePath) {
		super(filePath);
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	public void setStructured(Boolean structured) {
		this.structured = structured;
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
	
	// Only called when structured = true. Overrided in concrete producers
	protected JsonObject parseRecord(String[] fields) {
		return rawRecord(String.join(delimiter, fields));
	}
		
	// Default output: implemented for users to access raw data from hub
	protected JsonObject rawRecord(String line) {
		JsonObject record = new JsonObject();
		record.addProperty("raw", line);
		return record;
	}
	
	
	
}
