package com.codebyte.sunuso.OutputFilesWritter.Resources;

public enum FileExtention {
    csv("csv"),
    txt("txt");

    private final String text;

    FileExtention(String text) {
        this.text = text;
    }

    public String toString() {
        return this.text;
    }
}
