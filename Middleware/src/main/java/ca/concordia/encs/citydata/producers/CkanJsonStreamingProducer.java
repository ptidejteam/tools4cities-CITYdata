package ca.concordia.encs.citydata.producers;

import java.io.InputStream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.implementations.JSONProducer;
import ca.concordia.encs.citydata.core.implementations.JsonStreamingProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;

public class CkanJsonStreamingProducer extends JsonStreamingProducer {
	private String datasetName;
	private String resourceId;
	private String resolvedResourceUrl;

	public CkanJsonStreamingProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public CkanJsonStreamingProducer(final String filePath) {
		super(filePath);
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
	
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	public void setUrl(String url) {
		this.setFilePath(url);
	}

	@Override
	protected InputStream obtainInputStream() {
		if (resolvedResourceUrl == null) {
			resolvedResourceUrl = resolveResourceUrl();
		}
		this.setFilePath(resolvedResourceUrl);
		return this.fetchStream();
	}

	private String resolveResourceUrl() {
		String actionUrl = this.getFilePath();
		actionUrl += (resourceId != null)
				? "/action/package_show?id=" + resourceId
				: "/action/package_show?id=" + datasetName;	
		
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.setMethod("GET");

		JSONProducer metadataProducer = new JSONProducer(actionUrl, requestOptions);
		metadataProducer.fetch(); // no observer needed: empty runners set, notifyObservers() is a safe no-op

		JsonObject result = metadataProducer.getResult().getFirst().getAsJsonObject("result");
		JsonObject resource = (resourceId != null) ? result : findJsonResource(result);
		return resource.get("url").getAsString();
	}
	
	private JsonObject findJsonResource(JsonObject datasetMetadata) {
		for (JsonElement res : datasetMetadata.getAsJsonArray("resources")) {
			JsonObject resource = res.getAsJsonObject();
			if ("JSON".equalsIgnoreCase(resource.get("format").getAsString())) {
				return resource;
			}
		}
		throw new MiddlewareException.DatasetNotFound("No JSON resource for dataset: " + datasetName);
	}
}
