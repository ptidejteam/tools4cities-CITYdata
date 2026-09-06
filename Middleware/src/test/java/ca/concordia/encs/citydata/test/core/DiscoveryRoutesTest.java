package ca.concordia.encs.citydata.test.core;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import ca.concordia.encs.citydata.core.configs.AppConfig;
import ca.concordia.encs.citydata.test.AbstractTest;
import ca.concordia.encs.citydata.test.config.TestConfig;

/**
 * Discovery routes test
 *
 * @author Sikandar Ejaz
 * @since 2025-06-18
 */

@SpringBootTest(classes = { AppConfig.class, TestConfig.class })
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class DiscoveryRoutesTest extends AbstractTest {

	@Test
	public void testRoutesList() throws Exception {
		mockMvc.perform(get("/routes/list")).andExpect(status().isOk())
				.andExpect(content().string(containsString("Method: [")));

	}

	@Test
	public void testOperationsList() throws Exception {
		mockMvc.perform(get("/operations/list")).andExpect(status().isOk())
				.andExpect(content().string(containsString("ca.concordia.encs.citydata")));
	}

	@Test
	public void testProducersList() throws Exception {
		mockMvc.perform(get("/producers/list")).andExpect(status().isOk())
				.andExpect(content().string(containsString("ca.concordia.encs.citydata")));
	}

	@Test
	void testListOperationsController() throws Exception {
		mockMvc.perform(get("/operations/list")).andExpect(status().is2xxSuccessful())
				.andExpect(content().string(containsString("ca.concordia.encs.citydata.operations.MergeOperation")))
				.andExpect(content().string(containsString("targetProducerParams")))
				.andExpect(content().string(containsString("targetProducer")))
				.andExpect(content()
						.string(containsString("ca.concordia.encs.citydata.operations.GeoJsonFilterOperation")))
				.andExpect(content().string(containsString("centerLongitude")))
				.andExpect(content().string(containsString("centerLatitude")))
				.andExpect(content().string(containsString("radiusMeters")));
	}

	@Test
	void testListProducerController() throws Exception {
		mockMvc.perform(get("/producers/list")).andExpect(status().is2xxSuccessful())
				.andExpect(content()
						.string(containsString("ca.concordia.encs.citydata.producers.EnergyConsumptionProducer")))
				.andExpect(content().string(containsString("operation")))
				.andExpect(content().string(containsString("city")));
	}

	@Test
	void testRouteController() throws Exception {
		mockMvc.perform(get("/routes/list")).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$[?(@ =~ /.*\\/operations\\/list.*/)]").exists())
				.andExpect(content().string(containsString("/operations/list")))
				.andExpect(content().string(containsString("Method: [GET]")));
	}
}
