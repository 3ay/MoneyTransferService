package ru.netology.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.netology.dao.Log;
import ru.netology.dao.TransferInfoDAO;
import ru.netology.exceptions.IdNotFoundException;
import ru.netology.exceptions.RequestTimeoutException;
import ru.netology.model.*;
import ru.netology.services.LogService;
import ru.netology.services.TransferService;
import ru.netology.util.UniqueIdGenerator;

import java.util.Date;

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
        transferInfoDAO.setOperationId(UniqueIdGenerator.generateUniqueOperationId());
        logService.addLog(transferInfoDAO);
        Long operationId = transferInfoDAO.getOperationId();
        transferInfoResponse.setOperationId(String.valueOf(operationId));
        return transferInfoResponse;
    }

    @Override
    public ConfirmOperationResponse confirmOperation(ConfirmOperationRequest confirmOperationRequest) {
        Log log = logService.findLogByTransferId(Long.valueOf(confirmOperationRequest.getOperationId()));
        if (log == null) {
            throw new IdNotFoundException(confirmOperationRequest.getOperationId());
        }
        log.setUpdateTime(new Date());
        log.setCode(confirmOperationRequest.getCode());

        if (!isDateWithin3MinutesOfNow(log.getStartTime())) {
            log.setStatus(TransferStatus.EXPIRED);
            logService.updateLog(log.getLogId(), log);
            throw new RequestTimeoutException("code is expired, code lifetime is 3 minutes");
        } else {
            log.setStatus(TransferStatus.COMPLETED);
            logService.updateLog(log.getLogId(), log);
        }
        ConfirmOperationResponse response = new ConfirmOperationResponse();
        response.setOperationId(String.valueOf(log.getTransferInfoDAO().getOperationId()));
        return response;
    }

    private double calculateCommission(String currency) {
        if ("RUB".equals(currency)) {
            return rubCommission;
        } else if ("USD".equals(currency)) {
            return usdCommission;
        } else if ("EUR".equals(currency)) {
            return eurCommission;
        }
        return 0.0;
    }

    private boolean isDateWithin3MinutesOfNow(Date date) {
        Date currentDate = new Date();
        long differenceInMillis = currentDate.getTime() - date.getTime();
        return Math.abs(differenceInMillis) <= maximumCodeLifeTime;
    }
}
