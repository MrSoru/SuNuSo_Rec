package com.codebyte.sunuso.Objects.Moodle.Resources;

public class ParamItem {
    private String Name;

    private String Value;

    public ParamItem() {}

    public ParamItem(String Name, String Value) {
        this.Name = Name;
        this.Value = Value;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getValue() {
        return this.Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }
}
