package ru.netology.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.controller.MoneyTransferController;
import ru.netology.model.ConfirmOperationRequest;
import ru.netology.model.ConfirmOperationResponse;
import ru.netology.model.TransferInfoRequest;
import ru.netology.model.TransferInfoResponse;
import ru.netology.services.TransferService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MoneyTransferControllerImpl implements MoneyTransferController {
    private final TransferService transferService;

    @Override
    @PostMapping("/transfer")
    public ResponseEntity<TransferInfoResponse> transferMoney(@Valid @RequestBody TransferInfoRequest requestInfo) {
        TransferInfoResponse response = transferService.transferMoney(requestInfo);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/confirmOperation")
    public ResponseEntity<ConfirmOperationResponse> confirmOperation(@Valid @RequestBody ConfirmOperationRequest confirmOperationRequest) {
        ConfirmOperationResponse response = transferService.confirmOperation(confirmOperationRequest);
        return ResponseEntity.ok(response);
    }
}
