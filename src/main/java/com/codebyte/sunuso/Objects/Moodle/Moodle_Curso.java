package com.codebyte.sunuso.Objects.Moodle;

import com.codebyte.sunuso.Objects.Moodle.Resources.Courses;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;

public class Moodle_Curso {
    private Integer id;

    private String Fullname;

    private String Shortname;

    private String Summary;

    private boolean Sincronizado;

    WindowsLog WinLog;

    public String getSummary() {
        return this.Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public int getId() {
        return this.id.intValue();
    }

    public void setId(int id) {
        this.id = Integer.valueOf(id);
    }

    public String getFullname() {
        return this.Fullname;
    }

    public void setFullname(String Fullname) {
        this.Fullname = Fullname;
    }

    public String getShortname() {
        return this.Shortname;
    }

    public void setShortname(String Shortname) {
        this.Shortname = Shortname;
    }

    public void setSincronizado(boolean Sincronizado) {
        this.Sincronizado = Sincronizado;
    }

    public boolean getSincronizado() {
        return this.Sincronizado;
    }

    public void setFromGenericCourse(Courses CursoMood) {
        this.id = Integer.valueOf(Integer.parseInt(CursoMood.getID()));
        this.Fullname = CursoMood.getNombreCompleto();
        this.Shortname = CursoMood.getNombreCorto();
        this.Summary = CursoMood.getDescripcion();
        this.Sincronizado = true;
    }
}
