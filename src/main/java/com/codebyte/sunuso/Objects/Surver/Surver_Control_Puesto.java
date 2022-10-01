package com.codebyte.sunuso.Objects.Surver;

import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;
import java.sql.Date;

public class Surver_Control_Puesto {
    private int id_moodle_contol_puesto;

    private int id_empleado;

    private int nuevo_perfil;

    private int viejo_perfil;

    private Date Creado;

    private Date Actualizado;

    boolean Sincronized;

    protected WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);

    public int getId_moodle_contol_puesto() {
        return this.id_moodle_contol_puesto;
    }

    public void setId_moodle_contol_puesto(int id_moodle_contol_puesto) {
        this.id_moodle_contol_puesto = id_moodle_contol_puesto;
    }

    public int getId_empleado() {
        return this.id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public int getNuevo_perfil() {
        return this.nuevo_perfil;
    }

    public void setNuevo_perfil(int nuevo_perfil) {
        this.nuevo_perfil = nuevo_perfil;
    }

    public int getViejo_perfil() {
        return this.viejo_perfil;
    }

    public void setViejo_perfil(int viejo_perfil) {
        this.viejo_perfil = viejo_perfil;
    }

    public Date getCreado() {
        return this.Creado;
    }

    public void setCreado(Date Creado) {
        this.Creado = Creado;
    }

    public Date getActualizado() {
        return this.Actualizado;
    }

    public void setActualizado(Date Actualizado) {
        this.Actualizado = Actualizado;
    }

    public boolean isSincronized() {
        return this.Sincronized;
    }

    public void setSincronized(boolean Sincronized) {
        this.Sincronized = Sincronized;
    }
}
