package ca.concordia.encs.citydata.producers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Element;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.implementations.XmlProducer;
import ca.concordia.encs.citydata.core.utils.RequestOptions;
import ca.concordia.encs.citydata.services.DatasetAccessService;



public class XMLBuildingProducer extends XmlProducer {

	private String metadataPath;

	public XMLBuildingProducer(String filePath) {
		super(filePath);
	}

	public XMLBuildingProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public void setMetadataPath(String metadataPath) {
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
	protected JsonObject parseRecord(Element element) {
		JsonObject record = new JsonObject();
		record.addProperty("id", element.getAttribute("id"));
		record.addProperty("address", element.getElementsByTagName("address").item(0).getTextContent().trim());
		record.addProperty("city", element.getElementsByTagName("city").item(0).getTextContent().trim());
		record.addProperty("yearBuilt", element.getElementsByTagName("yearBuilt").item(0).getTextContent().trim());
		record.addProperty("floors", element.getElementsByTagName("floors").item(0).getTextContent().trim());
		return record;
	}
}