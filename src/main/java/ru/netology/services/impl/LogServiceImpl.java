package ru.netology.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netology.dao.Log;
import ru.netology.dao.TransferInfoDAO;
import ru.netology.model.TransferStatus;
import ru.netology.repository.LogRepository;
import ru.netology.services.LogService;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    private final LogRepository repository;

    @Transactional
    @Override
    public void addLog(TransferInfoDAO transferInfoDAO) {
        Log log = new Log();
        log.setStatus(TransferStatus.ACCEPTED);
        log.setTransferInfoDAO(transferInfoDAO);
        log.setStartTime(new Date());
        log.setUpdateTime(new Date());
        repository.saveLogs(log);
    }

    @Transactional
    @Override
    public ConcurrentHashMap<Long, Log> getAllLogs() {
        return repository.getLogMap();
    }
    @Transactional
    @Override
    public Log findLogByTransferId(long idToFind) {
        Optional<Log> value = getAllLogs().values().stream()
                .filter(log -> log.getTransferInfoDAO().getOperationId() == idToFind)
                .findFirst();
        return value.orElse(null);
    }

    @Transactional
    @Override
    public void updateLog(Long key, Log updatedLog) {
        repository.updateLog(key, updatedLog);
    }
}