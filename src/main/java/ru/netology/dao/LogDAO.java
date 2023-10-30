package ru.netology.dao;

import lombok.Getter;
import lombok.Setter;
import ru.netology.model.TransferStatus;

import java.util.Date;

@Getter
@Setter
public class LogDAO {
    private String logId;
    private String code;
    private TransferStatus status;
    private Date startTime;
    private Date updateTime;
    private TransferInfoDAO transferInfoDAO;
}
