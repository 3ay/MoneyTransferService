package ru.netology.repository;

import ru.netology.dao.LogDAO;

public interface OperationLogRepository {
    void saveLogs(LogDAO log);
    void updateLog(String key, LogDAO updatedLog);
}
