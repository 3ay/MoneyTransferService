package ru.netology.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import ru.netology.dao.LogDAO;
import ru.netology.repository.OperationLogRepository;

import java.util.UUID;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Repository
public class OperationLogRepositoryImpl implements OperationLogRepository {
    private final ConcurrentHashMap<String, LogDAO> logMap = new ConcurrentHashMap<>();

    private final String LOG_PATH = "src/main/resources/logDAO.json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    public OperationLogRepositoryImpl() {
        loadLogs();
    }

    private synchronized void loadLogs() {
        try {
            File file = new File(LOG_PATH);
            if (file.exists() && file.length() > 0) {
                ConcurrentHashMap<String, LogDAO> tmpMap = objectMapper.readValue(file, objectMapper.getTypeFactory()
                        .constructMapType(ConcurrentHashMap.class, String.class, LogDAO.class));
                logMap.putAll(tmpMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void saveLogs(LogDAO log) {
        String logId = UUID.randomUUID().toString();
        log.setLogId(logId);
        logMap.put(logId, log);
        saveToFile();
    }

    @Override
    public void updateLog(String key, LogDAO updatedLog) {
        logMap.replace(key, updatedLog);
        saveToFile();
    }

    private synchronized void saveToFile() {
        try (FileWriter writer = new FileWriter(LOG_PATH, false)) {
            objectWriter.writeValue(writer, logMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
