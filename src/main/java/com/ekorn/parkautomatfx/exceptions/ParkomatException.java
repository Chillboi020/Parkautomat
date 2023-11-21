package com.ekorn.parkautomatfx.exceptions;

public class ParkomatException extends RuntimeException {
    private final String title;
    private final String header;
    private final String msg;

    public ParkomatException(String title, String header, String msg) {
        this.title = title;
        this.header = header;
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public String getHeader() {
        return header;
    }

    public String getMsg() {
        return header + ": " + msg;
    }
}
