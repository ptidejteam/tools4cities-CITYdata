package ca.concordia.encs.citydata.test.producers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import ca.concordia.encs.citydata.core.configs.AppConfig;
import ca.concordia.encs.citydata.operations.MergeOperation;
import ca.concordia.encs.citydata.producers.EnergyConsumptionProducer;
import ca.concordia.encs.citydata.producers.JSONGeometryProducer;
import ca.concordia.encs.citydata.test.AbstractTest;

/**
 * Tests the API endpoint with the merge operation between EnergyConsumption and Geometry producers
 *
 * @author Minette Zongo M., Sikandar Ejaz
 * @since 2025-04-29 
 */

@SpringBootTest(classes = { AppConfig.class })
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")

public class JSONGeometryProducerTest extends AbstractTest {

	private final String city = "";

	@BeforeEach
	void setUp() {
		JSONGeometryProducer geometryProducer = new JSONGeometryProducer("./src/test/resources/montreal_geometries.json", null);
		geometryProducer.setCity(city);
		EnergyConsumptionProducer energyConsumptionProducer = new EnergyConsumptionProducer(city);
		MergeOperation mergeOperation = new MergeOperation();
	}

	@Test
	public void testMergeOperationViaAPI() throws Exception {
		/*	TODO: uncomment this test and make it run
		// Get example query using the PayloadFactory
				String jsonPayload = PayloadFactory.getExampleQuery("mergeEnergyConsumptionAndGeometries");
		
				mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
						.contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andExpect(status().isOk())
						.andExpect(content().string(containsString("result")));*/
	}
}
