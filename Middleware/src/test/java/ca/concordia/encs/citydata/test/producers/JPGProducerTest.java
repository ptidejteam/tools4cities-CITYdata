package ca.concordia.encs.citydata.test.producers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import ca.concordia.encs.citydata.core.configs.AppConfig;
import ca.concordia.encs.citydata.test.AbstractTest;
import ca.concordia.encs.citydata.test.PayloadFactory;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

/**
 * JPGProducer tests
 *
 * @author Minette Zongo
 * @since 2026-09-15
 */

@SpringBootTest(classes = { AppConfig.class })
@AutoConfigureMockMvc
@ComponentScan(basePackages = "ca.concordia.encs.citydata.core")
public class JPGProducerTest extends AbstractTest {

    @Test
    void testJPGProducer() throws Exception {
    	String jsonPayload = PayloadFactory.getExampleQuery("JPGProducer");
        mockMvc.perform(post("/apply/sync")
                .header("Authorization", "Bearer " + getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));
    }
}
