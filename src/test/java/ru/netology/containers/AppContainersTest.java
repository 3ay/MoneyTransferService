package ru.netology.containers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.exceptions.ErrorResponse;
import ru.netology.model.Amount;
import ru.netology.model.ConfirmOperationRequest;
import ru.netology.model.TransferInfoRequest;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppContainersTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Container
    private static final GenericContainer<?> appContainer = new GenericContainer<>("transfer_app:latest")
            .withExposedPorts(7070);
    private TransferInfoRequest initTransferInfoRequest ()
    {
        TransferInfoRequest transferInfo = new TransferInfoRequest();
        transferInfo.setCardFromNumber("4978123456789001");
        transferInfo.setCardToNumber("4123456789012345");
        transferInfo.setAmount(new Amount(550,"RUB"));
        transferInfo.setCardFromCVV("115");
        transferInfo.setCardFromValidTill("04/24");
        return transferInfo;
    }
    private TransferInfoRequest initInvalidTransferInfoRequest()
    {
        TransferInfoRequest transferInfo = new TransferInfoRequest();
        transferInfo.setCardFromNumber("4978123456789001");
        transferInfo.setCardToNumber("4123456789012345");
        transferInfo.setAmount(new Amount(550,"RUB"));
        transferInfo.setCardFromCVV("115");
        transferInfo.setCardFromValidTill("0424");
        return transferInfo;
    }
    @Test
    void return400InvalidCard() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "http://localhost:" + appContainer.getMappedPort(7070) + "/transfer",
                initInvalidTransferInfoRequest(),
                ErrorResponse.class);

        assertThat(response.getStatusCode().value())
                .isEqualTo(400);
        assertThat(Objects.requireNonNull(response.getBody()).getMessage())
                .isNotEmpty()
                .contains("Expiry date must be 4 characters long");

    }
    @Test
    void return500StrangeRequest() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "http://localhost:" + appContainer.getMappedPort(7070) + "/confirmOperation",
                initTransferInfoRequest(),
                ErrorResponse.class);

        assertThat(response.getStatusCode().value())
                .isEqualTo(500);

        assertThat(Objects.requireNonNull(response.getBody()).getMessage())
                .isNotEmpty()
                .contains("An unexpected error occurred");
    }
}
