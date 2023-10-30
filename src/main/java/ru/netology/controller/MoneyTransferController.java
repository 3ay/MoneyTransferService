package ru.netology.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import ru.netology.model.ConfirmOperationRequest;
import ru.netology.model.ConfirmOperationResponse;
import ru.netology.model.TransferInfoRequest;
import ru.netology.model.TransferInfoResponse;

import javax.validation.Valid;

public interface MoneyTransferController {
    ResponseEntity<TransferInfoResponse> transferMoney(@Valid @RequestBody TransferInfoRequest requestInfo);
    ResponseEntity<ConfirmOperationResponse> confirmOperation(@Valid @RequestBody ConfirmOperationRequest confirmOperationRequest);
}
