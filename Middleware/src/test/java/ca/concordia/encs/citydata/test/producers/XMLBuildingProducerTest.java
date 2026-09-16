package ca.concordia.encs.citydata.test.producers;


import java.util.ArrayList;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;

import ca.concordia.encs.citydata.core.configs.AppConfig;
import ca.concordia.encs.citydata.test.AbstractTest;
import ca.concordia.encs.citydata.test.PayloadFactory;


@SpringBootTest(classes = { AppConfig.class })
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")

public class XMLBuildingProducerTest extends AbstractTest {

//	@Test
//	public void testXmlBuildingProducer() {
//		final XMLBuildingProducer producer = new XMLBuildingProducer(null);
//		producer.setFilePath("./src/test/resources/Building.xml");
//		producer.fetch();
//
//		ArrayList<JsonObject> result = producer.getResult();
//		System.out.println("Result size: " + result.size());
//		result.forEach(System.out::println);
//
//		assertFalse(result.isEmpty(), "Should produce at least one building record");
//		assertEquals("Montreal", result.get(0).get("city").getAsString());
//	}
	
	@Test
	void testGeoNamesCitiesProducer() throws Exception {
		String jsonPayload = PayloadFactory.getExampleQuery("XmlProducer");
		mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
				.contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andExpect(status().isOk())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("montreal")))
		.andExpect(content().string(containsString("CA")))
		.andExpect(content().string(containsString("CAD")));
	}
}