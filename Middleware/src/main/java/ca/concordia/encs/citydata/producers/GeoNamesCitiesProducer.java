package ca.concordia.encs.citydata.producers;

import org.springframework.security.core.context.SecurityContextHolder;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.implementations.TXTProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;
import ca.concordia.encs.citydata.services.DatasetAccessService;

public class GeoNamesCitiesProducer extends TXTProducer {

	private String metadataPath;
	public GeoNamesCitiesProducer(String filePath) {
		super(filePath);
	}
	
	public GeoNamesCitiesProducer(final String filePath, RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}
	
	public void setMetadataPath (String metadataPath) {
		this.metadataPath = metadataPath;
	}
	
	@Override
	protected void beforeFetch() {
			if (metadataPath != null) {
				String username = SecurityContextHolder.getContext().getAuthentication().getName();
				new DatasetAccessService().checkAuthorisationForPath(username, metadataPath);
			}
	}
	
	@Override
	protected JsonObject parseRecord(String[] f) {
		JsonObject record = new JsonObject();
		record.addProperty("geonameId", f[0]);
		record.addProperty("name", f[1]);
		record.addProperty("latitude", f[4]);
		record.addProperty("longitude", f[5]);
		record.addProperty("countryCode", f[8]);
		record.addProperty("population", f[14]);
		return record;
	}

}
