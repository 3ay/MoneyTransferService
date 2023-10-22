package ru.netology.services;

import ru.netology.model.ConfirmOperationRequest;
import ru.netology.model.ConfirmOperationResponse;
import ru.netology.model.TransferInfoRequest;
import ru.netology.model.TransferInfoResponse;

public interface TransferService {
    TransferInfoResponse transferMoney(TransferInfoRequest transferInfoRequest);
    ConfirmOperationResponse confirmOperation(ConfirmOperationRequest confirmOperationRequest);
}
