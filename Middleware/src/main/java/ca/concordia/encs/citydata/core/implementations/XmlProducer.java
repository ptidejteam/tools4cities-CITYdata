package ca.concordia.encs.citydata.core.implementations;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.contracts.IProducer;
import ca.concordia.encs.citydata.core.exceptions.MiddlewareException;
import ca.concordia.encs.citydata.core.utils.RequestOptions;



public non-sealed class XmlProducer extends AbstractProducer<JsonObject> implements IProducer<JsonObject> {

	public XmlProducer(final String filePath, final RequestOptions fileOptions) {
		super(filePath, fileOptions);
	}

	public XmlProducer(final String filePath) {
		super(filePath);
	}

	@Override
	public void fetch() {
		beforeFetch();
		try (InputStream inputStream = obtainInputStream()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputStream);

			ArrayList<JsonObject> records = parseRecords(doc);
			this.setResult(records);
			this.applyOperation();
		} catch (IOException | SAXException | ParserConfigurationException e) {
			throw new MiddlewareException.DatasetNotFound("Error processing XML data");
		}
	}

	
	protected void beforeFetch() {

	}

	protected InputStream obtainInputStream() {
		return this.fetchStream();
	}

	
	protected ArrayList<JsonObject> parseRecords(Document doc) {
		ArrayList<JsonObject> records = new ArrayList<JsonObject>();
		NodeList children = doc.getDocumentElement().getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				records.add(parseRecord((Element) node));
			}
		}
		return records;
	}

	
	protected JsonObject parseRecord(Element element) {
		JsonObject record = new JsonObject();
		record.addProperty("raw", element.getTextContent().trim());
		return record;
	}
}