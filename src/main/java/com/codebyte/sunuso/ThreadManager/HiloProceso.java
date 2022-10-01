package com.codebyte.sunuso.ThreadManager;

import com.codebyte.sunuso.Resources.Configuration;
import com.codebyte.sunuso.Resources.TextManagement;
import com.codebyte.sunuso.ThreadManager.Procesos.Funciones;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HiloProceso extends Thread {

    private volatile boolean Alive = true;

    private volatile String Argument = "";

    private volatile long SleepingTime = 120L;

    private volatile String rev_Msg = getArgument() + "\tSin revisión...";

    Configuration Config;

    Funciones funciones;

    public HiloProceso(Configuration DeCryptConfig) {
        this.Config = DeCryptConfig;
    }

    public HiloProceso(String Arg, Configuration DeCryptConfig) {
        this.Config = DeCryptConfig;
        this.Argument = Arg;
    }

    public boolean isAliveT() {
        return this.Alive;
    }

    public synchronized void setAliveT(boolean Alive) {
        this.Alive = Alive;
    }

    public String getArgument() {
        return this.Argument;
    }

    public synchronized void setArgument(String Argument) {
        this.Argument = Argument;
    }

    public long getSleepingTime() {
        return this.SleepingTime;
    }

    public void setSleepingTime(long SleepingTime) {
        this.SleepingTime = SleepingTime;
    }

    public boolean Argument(String Arg) {
        switch (Arg) {
            case "Alta":
                this.funciones.EnrolltoPlatfrom();
                return true;
            case "InscribirUsuarios":
                this.funciones.EnrollUsers();
                return true;
            case "Actualizar_RFC":
                this.funciones.UpdateUserInfo_RFC();
                return true;
            case "Actualizar_Username":
                this.funciones.UpdateUserInfo_UserName();
                return true;
            case "SyncMoodSurv":
                this.funciones.SyncCourses_Moodle2Surver();
                return true;
            case "SyncSurvMood":
                this.funciones.SyncCourses_Surver2Moodle();
                return true;
            case "CRON":
                this.funciones.CRONTasks();
                return true;
            case "Suspender":
                this.funciones.SuspenderUsuarios();
                return true;
            case "Reactivar":
                this.funciones.reactivateUsers();
                return true;
            case "AuditoriasPt1":
                this.funciones.AudPt1_UnEnrollments011();
                return true;
            case "AuditoriasPt2":
                this.funciones.AudPt2_UnEnrollments101();
                return true;
            case "AuditoriasPt3":
                this.funciones.AudPt3_UnEnrollments100();
                return true;
            case "AuditoriasPt4":
                this.funciones.AudPt4_UnEnrollments000();
                return true;
            case "AuditoriasPt5":
                this.funciones.AudPt5_UnEnrollments110();
                return true;
            case "AuditoriasControlPuesto":
                this.funciones.AuditoriasControlPuesto();
                return true;
            case "SyncUsersMoodle2Surver":
                this.funciones.SyncEmployees2Surver();
                return true;
            case "Apagar":
                setAliveT(false);
                return false;
        }
        setAliveT(false);
        return false;
    }

    public boolean SetSleepingtime(int Value, int Type) {
        switch (Type) {
            case 0:
                this.SleepingTime = Value;
                return true;
            case 1:
                this.SleepingTime = (Value * 1000);
                return true;
            case 2:
                this.SleepingTime = (Value * 60000);
                return true;
            case 3:
                this.SleepingTime = (Value * 3600000);
                return true;
        }
        this.SleepingTime = Value;
        return true;
    }

    public String GetLatMessage() {
        return this.rev_Msg;
    }

    public void run() {
        setAliveT(true);
        this.funciones = new Funciones(this.Config, this.Argument);
        do {
            Long TimeResult = Long.valueOf(System.nanoTime());
            try {
                if (Argument(this.Argument)) {
                    TimeResult = Long.valueOf((System.nanoTime() - TimeResult.longValue()) / 1000000000L);
                    rev_Msg = "Última revision de:\t" + GetTabbedArgument() + TextManagement.NewLine()
                            + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                            + "\tTiempo Transcurrido: " + TextManagement.TransformSecondsToTime(TimeResult) + TextManagement.NewLine();
                    Thread.sleep(this.SleepingTime);
                }
            } catch (InterruptedException ex) {
                setAliveT(false);
            }
        } while (this.Alive);
    }

    private String GetTabbedArgument() {
        int maxSizeText = 8 * 4;
        int ArgumentLen = getArgument().length();
        int TabsResult = (int) Math.ceil((Double) ((maxSizeText - ArgumentLen) / 8.0));
        return getArgument() + TextManagement.NewTab(TabsResult);
    }
}
