package ca.concordia.encs.citydata.operations;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ca.concordia.encs.citydata.PayloadFactory;
import ca.concordia.encs.citydata.config.TestConfig;
import ca.concordia.encs.citydata.core.BaseIntegrationTest;
import ca.concordia.encs.citydata.core.configs.AppConfig;

/**
 * Integration tests for GeoJsonFilterOperation.
 *
 * @author Aboolfazl Rezaei
 * @since 2026-02-25
 */
@SpringBootTest(classes = { AppConfig.class, TestConfig.class })
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class GeoJsonFilterOperationTest extends BaseIntegrationTest {

	@Test
	@Disabled("DHN buildings dataset is too large for heap-constrained CI — known CKANProducer limitation")
	public void testFilterDhnBuildingsByPointRadius() throws Exception {
		String jsonPayload = PayloadFactory.getExampleQuery("dhnBuildingsGeoJsonFilterByPointRadius");
		mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
				.contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andExpect(status().isOk());
	}

	@Test
	public void testFilterDhnRoadsByPointRadius() throws Exception {
		String jsonPayload = PayloadFactory.getExampleQuery("dhnRoadsGeoJsonFilterByPointRadius");
		mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
				.contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andExpect(status().isOk());
	}

	@Test
	public void testMissingRadiusParameterReturnsError() throws Exception {
		String jsonPayload = PayloadFactory.getExampleQuery("dhnRoadsGeoJsonFilterByPointRadius");
		JsonObject jsonObject = com.google.gson.JsonParser.parseString(jsonPayload).getAsJsonObject();
		JsonArray withParams = jsonObject.getAsJsonArray("apply").get(0).getAsJsonObject()
				.getAsJsonArray("withParams");
		for (int i = 0; i < withParams.size(); i++) {
			if (withParams.get(i).getAsJsonObject().get("name").getAsString().equals("radiusMeters")) {
				withParams.remove(i);
				break;
			}
		}
		mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
				.contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString()))
				.andExpect(status().is5xxServerError());
	}

	@Test
	public void testInvalidJsonReturnsClientError() throws Exception {
		String brokenJson = PayloadFactory.getInvalidJson();
		mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
				.contentType(MediaType.APPLICATION_JSON).content(brokenJson))
				.andExpect(status().is4xxClientError());
	}
}
