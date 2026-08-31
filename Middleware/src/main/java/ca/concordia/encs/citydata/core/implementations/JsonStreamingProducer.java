package ca.concordia.encs.citydata.core.implementations;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.JsonParser;

import ca.concordia.encs.citydata.core.contracts.IOperation;
import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

public non-sealed class JsonStreamingProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject>{

		public JsonStreamingProducer(final String filePath, final RequestOptions fileOptions) {
			super(filePath, fileOptions);
		}

		public JsonStreamingProducer(final String filePath) {
			super(filePath);
		}

		@Override
		public void fetch() {
			beforeFetch();
			ArrayList<JsonObject> matches = new ArrayList<>();
			IOperation<JsonObject> operation = this.getOperation();

			try (InputStream inputStream = obtainInputStream();
					JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				reader.beginArray();
				while (reader.hasNext()) {
					JsonObject record = parseRecord(reader);
					if (operation != null) {
						matches.addAll(operation.apply(new ArrayList<>(List.of(record))));
					} else {
						matches.add(record);
					}
				}
				reader.endArray();
			} catch (IOException e) {
				throw new MiddlewareException.DatasetNotFound("Error streaming JSON data");
			}

			this.setResult(matches);
			this.notifyObservers();
		}

		protected void beforeFetch() {
		}

		protected InputStream obtainInputStream() {
			return this.fetchStream();
		}

		protected JsonObject parseRecord(JsonReader reader) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		}

}
