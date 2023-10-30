package ru.netology.methods;

import org.junit.Assert;
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
import ru.netology.dao.LogDAO;
import ru.netology.dao.TransferInfoDAO;
import ru.netology.model.Amount;
import ru.netology.model.TransferStatus;
import ru.netology.services.impl.LogServiceImpl;

import java.util.Calendar;
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
    private static LogDAO logDAO;
    @BeforeEach
    void initData()
    {
        TransferInfoDAO transferInfo = new TransferInfoDAO();

        transferInfo.setOperationId("1117L");
        transferInfo.setCardFromNumber("4978123456789001");
        transferInfo.setCardToNumber("4123456789012345");
        transferInfo.setAmount(new Amount(550,"RUB"));
        transferInfo.setCommission(0.01D);

        logDAO = new LogDAO();
        logDAO.setLogId("a47te10s-58tt-4372-t567-0e02s2s3t479");
        logDAO.setStatus(TransferStatus.ACCEPTED);
        logDAO.setStartTime(new Date());
        logDAO.setUpdateTime(new Date());
        logDAO.setTransferInfoDAO(transferInfo);

        Mockito.when(logService.findLogByTransferId("1117L")).thenReturn(logDAO);
    }
    @Test
    void return200() throws Exception {
        String requestBody = String.format(
                "{%n  \"code\": \"%s\",%n  \"operationId\": \"%s\"%n}",
                "1524",
                "1117L"
        );
        mockMvc.perform(
                        post("/confirmOperation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").value("1117L"));
        Assertions.assertEquals(TransferStatus.COMPLETED, logDAO.getStatus());
    }
    @Test
    void return400IdNegative() throws Exception {
        String requestBody = String.format(
                "{%n  \"code\": \"%s\",%n  \"operationId\": \"%s\"%n}",
                "-145",
                "1117L"
        );
        mockMvc.perform(
                        post("/confirmOperation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
    @Test
    void return400IdNotFound() throws Exception {
        String requestBody = String.format(
                "{%n  \"code\": \"%s\",%n  \"operationId\": \"%s\"%n}",
                "1918",
                "15A4L"
        );
        mockMvc.perform(
                        post("/confirmOperation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }
    @Test
    void return400CodeExpired() throws Exception {
        String requestBody = String.format(
                "{%n  \"code\": \"%s\",%n  \"operationId\": \"%s\"%n}",
                "1918",
                "1117L"
        );
        Date logDate = logDAO.getStartTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(logDate);
        calendar.add(Calendar.MINUTE, 4);
        Date updatedDate = calendar.getTime();
        logDAO.setStartTime(updatedDate);
        mockMvc.perform(
                        post("/confirmOperation")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.message").isNotEmpty());
        logDAO.setStartTime(logDate);
    }

}
