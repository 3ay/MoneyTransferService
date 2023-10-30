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
        String requestBody = String.format(
                "{\"cardFromNumber\":\"%s\"," +
                        "\"cardToNumber\":\"%s\"," +
                        " \"cardFromCVV\": \"%s\",\n" +
                        " \"cardFromValidTill\": \"%s\",\n" +
                        "\"amount\":" +
                        "{\"value\":%d,\"currency\":\"%s\"}}",
                "1234567812345678",
                "8765432187654321",
                "126",
                "11/24",
                100,
                "USD"
        );
        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }
    @Test
    void return400IncorrectCardFromNumberTest() throws Exception {
        String requestBody = String.format(
                "{%n" +
                        "  \"cardFromNumber\": \"%s\",%n" +
                        "  \"cardToNumber\": \"%s\",%n" +
                        "  \"cardFromCVV\": \"%s\",%n" +
                        "  \"cardFromValidTill\": \"%s\",%n" +
                        "  \"amount\": {%n" +
                        "    \"currency\": \"%s\",%n" +
                        "    \"value\": %d%n" +
                        "  }%n" +
                        "}",
                "782",
                "8566161424813126",
                "197",
                "11/24",
                "RUB",
                3500000
        );
        mockMvc.perform(
                        post("/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
    @Test
    void return500IncorrectBodyTest() throws Exception {
        String requestBody = String.format(
                "{%n" +
                        "  \"cardFromNumber\": \"%s\",%n" +
                        "  \"cardToNumber\": \"%s\",%n" +
                        "  \"cardFromCVV\": \"%s\",%n" +
                        "  \"cardFromValidTill\": \"%s\",%n" +
                        "}",
                "782",
                "8566161424813126",
                "197",
                "11/24"
        );
        mockMvc.perform(
                        post("/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is(500))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
}
