package ca.concordia.encs.citydata.test.producers;

/*@SpringBootTest(classes = { AppConfig.class })
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")

public class TXTGeoLocationProducerTest extends AbstractTest {
	
	@Test
	void testGeoNamesCitiesProducer() throws Exception {
		String jsonPayload = PayloadFactory.getExampleQuery("TXTProducer");
		mockMvc.perform(post("/apply/sync").header("Authorization", "Bearer " + getToken())
				.contentType(MediaType.APPLICATION_JSON).content(jsonPayload)).andExpect(status().isOk())
				.andExpect(content().string(containsString("\"countryCode\":\"AE\"")));
	}
		
		
}
*/