package ca.concordia.encs.citydata.producers;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.contracts.IOperation;
import ca.concordia.encs.citydata.core.contracts.IRunner;
import ca.concordia.encs.citydata.core.implementations.JSONProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

/**
 * This Producer outputs GeoJSON geometries for a given city.
 *
 * @author Gabriel C. Ullmann, Minette Zongo, Sikandar Ejaz
 * @since 2025-05-28
 */

public class JSONGeometryProducer extends JSONProducer {

	private JSONProducer jsonProducer;

	public void setCity(String city) {
		if (city != null) {
			jsonProducer = new JSONProducer("./src/test/resources/" + city + "_geometries.json", null);
		} else {
			throw new InvalidParameterException("Please provide a city name to the producer.");
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setOperation(IOperation operation) {
		this.jsonProducer.setOperation(operation);
	}

	public JSONGeometryProducer() {
		super(null, null);
	}

	public JSONGeometryProducer(String filePath, RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	@Override
	public ArrayList<JsonObject> getResult() {
		return this.jsonProducer.getResult();
	}

	@Override
	public void fetch() {
		this.jsonProducer.fetch();
	}

	@Override
	public void addObserver(final IRunner aRunner) {
		this.jsonProducer.addObserver(aRunner);
	}
}