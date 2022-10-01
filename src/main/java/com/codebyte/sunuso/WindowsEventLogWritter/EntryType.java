package com.codebyte.sunuso.WindowsEventLogWritter;

public enum EntryType {
    Error("Error"),
    Information("Information"),
    FailureAudit("FailureAudit"),
    SuccessAudit("SuccessAudit"),
    Warning("Warning");

    private final String text;

    EntryType(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
