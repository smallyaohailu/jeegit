package com.jeegit.common.log.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperLogEvent {

    private String title;
    private String businessType;
    private String method;
    private String requestMethod;
    private String requestUrl;
    private String requestParam;
    private String responseResult;
    private String clientIp;
    private String operName;
    private String errorMsg;
    private int status;
    private long costTime;
    private LocalDateTime operTime;
}
