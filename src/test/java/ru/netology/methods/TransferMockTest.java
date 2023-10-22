package ru.netology.methods;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransferMockTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    public void transferMoneyReturnsCorrectOperationId() throws Exception {
    String requestBody ="{\"cardFromNumber\":\"1234567812345678\"," +
            "\"cardToNumber\":\"8765432187654321\"," +
            " \"cardFromCVV\": \"126\",\n" +
            " \"cardFromValidTill\": \"11/24\",\n" +
            "\"amount\":" +
            "{\"value\":100.0,\"currency\":\"USD\"}}";
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
    @Test
    void return400IncorrectCardFromNumberTest() throws Exception {
        String requestBody = "{\n" +
                "  \"cardFromNumber\": \"782\",\n" +
                "  \"cardToNumber\": \"8566161424813126\",\n" +
                "  \"cardFromCVV\": \"197\",\n" +
                "  \"cardFromValidTill\": \"11/24\",\n" +
                "  \"amount\": {\n" +
                "    \"currency\": \"RUB\",\n" +
                "    \"value\": 3500000\n" +
                "  }\n" +
                "}\n";
        mockMvc.perform(
                        post("/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
