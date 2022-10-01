package com.codebyte.sunuso.Objects.Surver;

public class Surver_Asignacion_Cursos {
    private String RFC;

    private String Curso;

    private int Moodle_curso_id = -1;

    public Surver_Asignacion_Cursos(String RFC, String Curso) {
        this.RFC = RFC;
        this.Curso = Curso;
    }

    public Surver_Asignacion_Cursos() {}

    public int getMoodle_curso_id() {
        return this.Moodle_curso_id;
    }

    public void setMoodle_curso_id(int Moodle_curso_id) {
        this.Moodle_curso_id = Moodle_curso_id;
    }

    public String getRFC() {
        return this.RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getCurso() {
        return this.Curso;
    }

    public void setCurso(String Curso) {
        this.Curso = Curso;
    }

    public String toString() {
        return "Surver_Asignacion_Cursos{RFC=" + this.RFC + ", Curso=" + this.Curso + "}";
    }

    public boolean isEmpty() {
        return (this.RFC.isEmpty() || this.Curso.isEmpty() || this.Moodle_curso_id == -1);
    }

    public boolean isNull() {
        return (this.RFC == null || this.Curso == null || this.Moodle_curso_id == -1);
    }
}
