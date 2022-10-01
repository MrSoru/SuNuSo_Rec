package com.codebyte.sunuso.Objects.Moodle.Resources;

public class ExceptionMoodle {
    private String MESSAGE;

    private String DEBUGINFO;

    public ExceptionMoodle() {}

    public ExceptionMoodle(String MESSAGE, String DEBUGINFO) {
        this.MESSAGE = MESSAGE;
        this.DEBUGINFO = DEBUGINFO;
    }

    public String getMESSAGE() {
        return this.MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getDEBUGINFO() {
        return this.DEBUGINFO;
    }

    public void setDEBUGINFO(String DEBUGINFO) {
        this.DEBUGINFO = DEBUGINFO;
    }

    public String toString() {
        return "ExceptionMoodle{MESSAGE=" + this.MESSAGE + ", DEBUGINFO=" + this.DEBUGINFO + "}";
    }

    public boolean isBlankData() {
        return (this.MESSAGE.isBlank() && this.DEBUGINFO.isBlank());
    }

    public boolean isNullData() {
        return (this.MESSAGE == null && this.DEBUGINFO == null);
    }
}
