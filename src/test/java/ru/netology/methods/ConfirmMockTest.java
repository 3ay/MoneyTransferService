package ru.netology.methods;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.netology.dao.Log;
import ru.netology.dao.TransferInfoDAO;
import ru.netology.model.Amount;
import ru.netology.model.TransferStatus;
import ru.netology.services.impl.LogServiceImpl;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConfirmMockTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    LogServiceImpl logService;
    private static Log log;
    @BeforeEach
    void initData()
    {
        TransferInfoDAO transferInfo = new TransferInfoDAO();

        transferInfo.setOperationId(1117L);
        transferInfo.setCardFromNumber("4978123456789001");
        transferInfo.setCardToNumber("4123456789012345");
        transferInfo.setAmount(new Amount(550,"RUB"));
        transferInfo.setCommission(0.01D);

        log = new Log();
        log.setLogId(1L);
        log.setCode("1918");
        log.setStatus(TransferStatus.COMPLETED);
        log.setStartTime(new Date());
        log.setUpdateTime(new Date());
        log.setTransferInfoDAO(transferInfo);

        Mockito.when(logService.findLogByTransferId(1117L)).thenReturn(log);
    }
    @Test
    void return200() throws Exception {
        String requestBody = "{\n" +
                "  \"code\": \"1524\",\n" +
                "  \"operationId\": \"1117\"\n" +
                "}\n";
        mockMvc.perform(
                        post("/confirmOperation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value(1117L));
        Assertions.assertEquals(TransferStatus.COMPLETED,log.getStatus());
    }
    @Test
    void return400IdNegative() throws Exception {
        String requestBody = "{\n" +
                "  \"code\": \"1524\",\n" +
                "  \"operationId\": \"-145\"\n" +
                "}\n";
        mockMvc.perform(
                        post("/confirmOperation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

}
