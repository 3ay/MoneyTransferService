package ru.netology.services;

import ru.netology.dao.LogDAO;
import ru.netology.dao.TransferInfoDAO;

import java.util.concurrent.ConcurrentHashMap;

public interface LogService {
    void addLog(TransferInfoDAO transferInfoDAO);
    ConcurrentHashMap<String, LogDAO> getAllLogs();
    void updateLog(String key, LogDAO updatedLogDAO);
    LogDAO findLogByTransferId(String idToFind);

}
