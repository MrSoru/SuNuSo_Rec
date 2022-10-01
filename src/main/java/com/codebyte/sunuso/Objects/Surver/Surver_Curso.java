package com.codebyte.sunuso.Objects.Surver;

import com.codebyte.sunuso.Objects.Moodle.Moodle_Curso;
import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;
import java.sql.Date;

public class Surver_Curso {
    private int moodle_curso_id;

    private Date Creado;

    private int creadoPor;

    private Date Actualizado;

    private int ActualizadoPor;

    private String Nombre_Curso;

    private String Nombre_Corto;

    private String Descripcion_curso;

    private boolean activo;

    protected WindowsLog WinLog;

    private final String SuNuSoVersion = "v 2.0";

    public Surver_Curso() {
        this.WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
    }

    public Surver_Curso(int moodle_curso_id, Date Creado, int creadoPor, Date Actualizado, int ActualizadoPor, String Nombre_Curso, String Descripcion_curso, boolean activo) {
        this.moodle_curso_id = moodle_curso_id;
        this.Creado = Creado;
        this.creadoPor = creadoPor;
        this.Actualizado = Actualizado;
        this.ActualizadoPor = ActualizadoPor;
        this.Nombre_Curso = Nombre_Curso;
        this.Descripcion_curso = Descripcion_curso;
        this.activo = activo;
        this.WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
    }

    public int getMoodle_curso_id() {
        return this.moodle_curso_id;
    }

    public void setMoodle_curso_id(int moodle_curso_id) {
        this.moodle_curso_id = moodle_curso_id;
    }

    public Date getCreado() {
        return this.Creado;
    }

    public void setCreado(Date Creado) {
        this.Creado = Creado;
    }

    public int getCreadoPor() {
        return this.creadoPor;
    }

    public void setCreadoPor(int creadoPor) {
        this.creadoPor = creadoPor;
    }

    public Date getActualizado() {
        return this.Actualizado;
    }

    public void setActualizado(Date Actualizado) {
        this.Actualizado = Actualizado;
    }

    public int getActualizadoPor() {
        return this.ActualizadoPor;
    }

    public void setActualizadoPor(int ActualizadoPor) {
        this.ActualizadoPor = ActualizadoPor;
    }

    public String getNombre_Curso() {
        return this.Nombre_Curso;
    }

    public void setNombre_Curso(String Nombre_Curso) {
        this.Nombre_Curso = Nombre_Curso;
    }

    public String getDescripcion_curso() {
        return this.Descripcion_curso;
    }

    public void setDescripcion_curso(String Descripcion_curso) {
        this.Descripcion_curso = Descripcion_curso;
    }

    public boolean getesActivo() {
        return this.activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre_Corto() {
        return this.Nombre_Corto;
    }

    public void setNombre_Corto(String Nombre_Corto) {
        this.Nombre_Corto = Nombre_Corto;
    }

    public String toString() {
        return "Surver_Curso{moodle_curso_id=" + this.moodle_curso_id + ", Creado=" + this.Creado + ", creadoPor=" + this.creadoPor + ", Actualizado=" + this.Actualizado + ", ActualizadoPor=" + this.ActualizadoPor + ", Nombre_Curso=" + this.Nombre_Curso + ", Nombre_Corto=" + this.Nombre_Corto + ", Descripcion_curso=" + this.Descripcion_curso + ", activo=" + this.activo + "}";
    }

    public void setFromMoodleCourse(Moodle_Curso curso) {
        String Leyenda = "Curso Generado autompor interfaz " + Information.ProgramName + Information.Version + " " + Information.Acronimo;
        setActivo(true);
        setNombre_Curso(curso.getFullname());
        setNombre_Corto(curso.getShortname());
        setActualizadoPor(873);
        setCreadoPor(873);
        setDescripcion_curso((curso.getSummary() == null) ? (curso.getSummary().isBlank() ? Leyenda : curso.getSummary()) : Leyenda);
    }
}
