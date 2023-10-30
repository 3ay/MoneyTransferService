package ru.netology.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.dao.LogDAO;
import ru.netology.dao.TransferInfoDAO;
import ru.netology.model.TransferStatus;
import ru.netology.repository.impl.OperationLogRepositoryImpl;
import ru.netology.services.LogService;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final OperationLogRepositoryImpl repository;

    @Transactional
    @Override
    public void addLog(TransferInfoDAO transferInfoDAO) {
        LogDAO logDAO = new LogDAO();
        logDAO.setStatus(TransferStatus.ACCEPTED);
        logDAO.setTransferInfoDAO(transferInfoDAO);
        logDAO.setStartTime(new Date());
        logDAO.setUpdateTime(new Date());
        repository.saveLogs(logDAO);
    }

    @Transactional
    @Override
    public ConcurrentHashMap<String, LogDAO> getAllLogs() {
        return repository.getLogMap();
    }

    @Transactional
    @Override
    public LogDAO findLogByTransferId(String idToFind) {
        Optional<LogDAO> value = getAllLogs().values().stream()
                .filter(log -> log.getTransferInfoDAO().getOperationId().equals(idToFind))
                .findFirst();
        return value.orElse(null);
    }

    @Transactional
    @Override
    public void updateLog(String key, LogDAO updatedLogDAO) {
        repository.updateLog(key, updatedLogDAO);
    }
}