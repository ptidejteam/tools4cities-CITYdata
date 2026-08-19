package ca.concordia.encs.citydata.test.producers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;

import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.core.configs.AppConfig;
import ca.concordia.encs.citydata.operations.JsonFilterOperation;
import ca.concordia.encs.citydata.producers.TXTGeoLocationProducer;
import ca.concordia.encs.citydata.test.AbstractTest;
import ca.concordia.encs.citydata.test.PayloadFactory;

@SpringBootTest(classes = { AppConfig.class })
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")

public class TXTGeoLocationProducerTest extends AbstractTest {
	
//	@Test
//	void testGeoNamesCitiesProducer() throws Exception {
//		String jsonPayload = PayloadFactory.getExampleQuery("TXTProducer");
//		mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
//				.contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andExpect(status().isOk())
//				.andExpect(content().string(containsString("\"countryCode\":\"AE\"")));
//	}
		
		
}
