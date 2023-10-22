package ru.netology.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import ru.netology.dao.Log;
import ru.netology.util.UniqueIdGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LogRepository {
    @Getter
    private ConcurrentHashMap<Long, Log> logMap;

    private final String LOG_PATH = "src/main/resources/log.json";
    private final ObjectMapper objectMapper;
    private final ObjectWriter objectWriter;
    public LogRepository() {
        this.objectMapper = new ObjectMapper();
        this.logMap = new ConcurrentHashMap<>();
        loadLogs();
        this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
    }

    private void loadLogs() {
        try {
            File file = new File(LOG_PATH);
            if (file.exists() && file.length() > 0) {
                logMap = objectMapper.readValue(file, objectMapper.getTypeFactory()
                        .constructMapType(ConcurrentHashMap.class, Long.class, Log.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveLogs(Log log) {
        long logId = UniqueIdGenerator.generateUniqueId();
        log.setLogId(logId);
        logMap.put(logId, log);
        try (FileWriter writer = new FileWriter(LOG_PATH, false)) {
            objectWriter.writeValue(writer, logMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateLog(Long key, Log updatedLog) {
        if (logMap.containsKey(key)) {
            logMap.put(key, updatedLog);
            try (FileWriter writer = new FileWriter(LOG_PATH, false)) {
                objectWriter.writeValue(writer, logMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Ключ не найден: " + key);
        }
    }
}
