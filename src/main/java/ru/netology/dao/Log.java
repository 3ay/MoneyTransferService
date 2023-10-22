package ru.netology.dao;

import lombok.Data;
import ru.netology.model.TransferStatus;

import java.util.Date;

@Data
public class Log {
    Long logId;
    String code;
    TransferStatus status;
    Date startTime;
    Date updateTime;
    TransferInfoDAO transferInfoDAO;
}
