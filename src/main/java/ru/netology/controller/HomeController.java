package ru.netology.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.model.ConfirmOperationRequest;
import ru.netology.model.ConfirmOperationResponse;
import ru.netology.model.TransferInfoRequest;
import ru.netology.model.TransferInfoResponse;
import ru.netology.services.TransferService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final TransferService transferService;
    @PostMapping("/transfer")
    public ResponseEntity<TransferInfoResponse> transferMoney(@Valid @RequestBody TransferInfoRequest requestInfo)
    {
        TransferInfoResponse response = transferService.transferMoney(requestInfo);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/confirmOperation")
    public ResponseEntity<ConfirmOperationResponse> confirmOperation(@Valid @RequestBody ConfirmOperationRequest confirmOperationRequest)
    {
        ConfirmOperationResponse response = transferService.confirmOperation(confirmOperationRequest);
        return ResponseEntity.ok(response);
    }
}
