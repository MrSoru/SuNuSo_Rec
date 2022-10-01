package com.codebyte.sunuso.Tokenizer;

public class ObjectAttrib {
    private String Name;

    private String Tittle;

    private String Value;

    public ObjectAttrib() {}

    public ObjectAttrib(String Name, String Tittle, String Value) {
        this.Name = Name;
        this.Tittle = Tittle;
        this.Value = Value;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getTittle() {
        return this.Tittle;
    }

    public void setTittle(String Tittle) {
        this.Tittle = Tittle;
    }

    public String getValue() {
        return this.Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String toString() {
        return "ObjectAttrib{Name=" + this.Name + ", Tittle=" + this.Tittle + ", Value=" + this.Value + "}";
    }
}
