package bankmonitor.model.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerTest extends IntegrationTest {

    @Test
    void test_transaction_creation() throws Exception {
        int givenAmount = 100;
        String givenReference = "BM_2023_101";

        final MockHttpServletResponse response = mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "amount": %s, "reference": "%s" }
                                """.formatted(givenAmount, givenReference)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final String timestamp = JsonPath.parse(response.getContentAsString()).read("$.timestamp");
        final Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            [{
                                "id": %s,
                                "timestamp": "%s",
                                "data": "{ \\"amount\\": %s, \\"reference\\": \\"%s\\" }\\n",
                                "reference": "%s",
                                "amount": %s
                            }
                            ]
                        """.formatted(id, timestamp, givenAmount, givenReference, givenReference, givenAmount)));
    }

    @Test
    void test_transaction_update() throws Exception {
        int givenAmount = 500;
        String givenReference = "BM_2023_101";

        final MockHttpServletResponse response = mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "amount": %s, "reference": "%s" }
                                """.formatted(givenAmount, givenReference)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        final String timestamp = JsonPath.parse(response.getContentAsString()).read("$.timestamp");
        final Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        int updateAmount = 200;
        String updateReference = "BM_2024_101";

        mvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "amount": %s, "reference": "%s" }
                                """.formatted(updateAmount, updateReference)))
                .andExpect(status().isOk());

        mvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                            [{
                                "id": %s,
                                "timestamp": "%s",
                                "data": "{\\"reference\\":\\"%s\\",\\"amount\\":%s}",
                                "reference": "%s",
                                "amount": %s
                            }
                            ]
                        """.formatted(id, timestamp, updateReference, updateAmount, updateReference, updateAmount)));
    }

    @Test
    void test_invalid_transaction_update() throws Exception {
        mvc.perform(put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "amount": 100, "reference": "ASD" }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_invalid_transaction_creation() throws Exception {
        mvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { THIS IS a very veryyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy
                                LONG text.
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

}
