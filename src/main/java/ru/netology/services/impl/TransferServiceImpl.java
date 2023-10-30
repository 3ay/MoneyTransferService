package ru.netology.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.dao.LogDAO;
import ru.netology.dao.TransferInfoDAO;
import ru.netology.exceptions.CodeExpiredException;
import ru.netology.exceptions.IdNotFoundException;
import ru.netology.model.*;
import ru.netology.services.LogService;
import ru.netology.services.TransferService;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final double rubCommission = 0.01;
    private final double usdCommission = 0.02;
    private final double eurCommission = 0.03;

    private final long maximumCodeLifeTime = 180000;

    private final LogService logService;

    @Override
    public TransferInfoResponse transferMoney(TransferInfoRequest transferInfoRequest) {

        TransferInfoDAO transferInfoDAO = new TransferInfoDAO();
        TransferInfoResponse transferInfoResponse = new TransferInfoResponse();
        transferInfoDAO.setAmount(transferInfoRequest.getAmount());
        transferInfoDAO.setCommission(calculateCommission(transferInfoRequest.getAmount().getCurrency()));
        transferInfoDAO.setCardFromNumber(transferInfoRequest.getCardFromNumber());
        transferInfoDAO.setCardToNumber(transferInfoRequest.getCardToNumber());
        transferInfoDAO.setOperationId(UUID.randomUUID().toString());
        logService.addLog(transferInfoDAO);
        String operationId = transferInfoDAO.getOperationId();
        transferInfoResponse.setOperationId(operationId);
        return transferInfoResponse;
    }

    @Override
    public ConfirmOperationResponse confirmOperation(ConfirmOperationRequest confirmOperationRequest) {
        LogDAO logDAO = logService.findLogByTransferId(confirmOperationRequest.getOperationId());
        checkLogNotNull(logDAO, confirmOperationRequest.getOperationId());

        updateLogDetails(logDAO, confirmOperationRequest);

        if (isCodeExpired(logDAO.getStartTime())) {
            setLogStatusAndSave(logDAO, TransferStatus.EXPIRED);
            throw new CodeExpiredException("code is expired, code lifetime is 3 minutes");
        }

        setLogStatusAndSave(logDAO, TransferStatus.COMPLETED);
        return createResponse(logDAO);
    }

    private void checkLogNotNull(LogDAO logDAO, String operationId) {
        if (Objects.isNull(logDAO)) {
            throw new IdNotFoundException(operationId);
        }
    }

    private void updateLogDetails(LogDAO logDAO, ConfirmOperationRequest confirmOperationRequest) {
        logDAO.setUpdateTime(new Date());
        logDAO.setCode(confirmOperationRequest.getCode());
    }

    private boolean isCodeExpired(Date startTime) {
        return !isDateWithin3MinutesOfNow(startTime);
    }

    private void setLogStatusAndSave(LogDAO logDAO, TransferStatus status) {
        logDAO.setStatus(status);
        logService.updateLog(logDAO.getLogId(), logDAO);
    }

    private ConfirmOperationResponse createResponse(LogDAO logDAO) {
        ConfirmOperationResponse response = new ConfirmOperationResponse();
        response.setOperationId(logDAO.getTransferInfoDAO().getOperationId());
        return response;
    }


    private double calculateCommission(String currency) {
        switch (currency) {
            case "RUB":
                return rubCommission;
            case "USD":
                return usdCommission;
            case "EUR":
                return eurCommission;
            default:
                return 0.0;
        }
    }

    private boolean isDateWithin3MinutesOfNow(Date date) {
        Date currentDate = new Date();
        long differenceInMillis = currentDate.getTime() - date.getTime();
        return Math.abs(differenceInMillis) <= maximumCodeLifeTime;
    }
}
