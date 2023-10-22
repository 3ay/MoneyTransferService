package ru.netology.services;

import ru.netology.dao.Log;
import ru.netology.dao.TransferInfoDAO;

import java.util.concurrent.ConcurrentHashMap;

public interface LogService {
    void addLog(TransferInfoDAO transferInfoDAO);
    ConcurrentHashMap<Long, Log> getAllLogs();
    void updateLog(Long key, Log updatedLog);
    Log findLogByTransferId(long idToFind);

}
