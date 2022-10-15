package com.mohey.commonlogger.model;

import lombok.Getter;

@Getter
public class LogModel {
    protected String sessionId;
    protected LogType type;
    protected String time;
    protected String serviceName;
    protected String path;
    protected String remoteAddress;
    protected Object headers;
    protected Integer statusCode;

    protected String method;

    public LogModel setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public LogModel setType(LogType type) {
        this.type = type;
        return this;
    }

    public LogModel setTime(String time) {
        this.time = time;
        return this;
    }

    public LogModel setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public LogModel setPath(String path) {
        this.path = path;
        return this;
    }

    public LogModel setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    public LogModel setHeaders(Object headers) {
        this.headers = headers;
        return this;
    }

    public LogModel setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public LogModel setMethod(String method) {
        this.method = method;
        return this;
    }

    @Override
    public String toString() {
        return "LogModel{" +
                "sessionId='" + sessionId + '\'' +
                ", type=" + type +
                ", time='" + time + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", path='" + path + '\'' +
                ", remoteAddress='" + remoteAddress + '\'' +
                ", headers=" + headers +
                ", statusCode=" + statusCode +
                ", method='" + method + '\'' +
                '}';
    }
}


