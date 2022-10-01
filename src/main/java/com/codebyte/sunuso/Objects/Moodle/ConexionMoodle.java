package com.codebyte.sunuso.Objects.Moodle;

import com.codebyte.sunuso.Conections.ConectionPostgreSQL;
import com.codebyte.sunuso.Resources.Configuration;
import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.Resources.TextManagement;
import com.codebyte.sunuso.WindowsEventLogWritter.EntryType;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ConexionMoodle extends ConectionPostgreSQL {
    protected WindowsLog WinLog;

    private final String ThreadName;

    public ConexionMoodle(final Configuration Config,final String ThreadName) {
        super(Config, 1);
        this.ThreadName = ThreadName;
        this.WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
    }

    public boolean GuardarSincronizado(final Integer id) {
        String QRY = "UPDATE mdl_course set Sincronizado_Surver=true WHERE id=" + id;
        if (id == null)
            return false;
        try {
            PreparedStatement Pstm = this.conn.prepareStatement(QRY);
            Pstm.addBatch();
            Pstm.executeBatch();
            Pstm.close();
            return true;
        } catch (SQLException | NullPointerException Ex) {
            this.WinLog.WriteEvent("No se logró actualizar el dato Moodle_curso.GuardarSincronizado()" + TextManagement.NewLine(3) + Ex.getMessage(), EntryType.Error, 301);
            return false;
        }
    }

    public ArrayList<Moodle_Curso> getAllMoodleCourses() {
        ArrayList<Moodle_Curso> Cursos = new ArrayList<>();
        String QRY = "SELECT id, fullname, shortname, summary, Sincronizado_Surver FROM mdl_course";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Moodle_Curso Curso_Moodle = new Moodle_Curso();
                Curso_Moodle.setFullname(RS.getString("fullname"));
                Curso_Moodle.setShortname(RS.getString("shortname"));
                Curso_Moodle.setId(RS.getInt("Id"));
                Curso_Moodle.setSummary(RS.getString("summary").replaceAll("\\<.*?\\>", ""));
                Curso_Moodle.setSincronizado(RS.getBoolean("Sincronizado_Surver"));
                Cursos.add(Curso_Moodle);
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Moodle_curso.getAllMoodleCourses()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
        }
        return Cursos.isEmpty() ? null : Cursos;
    }

    public ArrayList<Moodle_Curso> getSincronizedMoodleCourses() {
        ArrayList<Moodle_Curso> Cursos = new ArrayList<>();
        String QRY = "SELECT id, fullname, shortname, summary, Sincronizado_Surver FROM mdl_course where Sincronizado_Surver=false";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Moodle_Curso Curso_Moodle = new Moodle_Curso();
                Curso_Moodle.setFullname(RS.getString("fullname"));
                Curso_Moodle.setShortname(RS.getString("shortname"));
                Curso_Moodle.setId(RS.getInt("Id"));
                Curso_Moodle.setSummary(RS.getString("summary").replaceAll("\\<.*?\\>", ""));
                Curso_Moodle.setSincronizado(RS.getBoolean("Sincronizado_Surver"));
                Cursos.add(Curso_Moodle);
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Moodle_curso.getSincronizedMoodleCourses()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
        }
        return Cursos.isEmpty() ? null : Cursos;
    }

    public ArrayList<Moodle_Curso> getNotSincronizedMoodleCourses() {
        ArrayList<Moodle_Curso> Cursos = new ArrayList<>();
        String QRY = "SELECT id, fullname, shortname, summary, Sincronizado_Surver FROM mdl_course where Sincronizado_Surver=false";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Moodle_Curso Curso_Moodle = new Moodle_Curso();
                Curso_Moodle.setFullname(RS.getString("fullname"));
                Curso_Moodle.setShortname(RS.getString("shortname"));
                Curso_Moodle.setId(RS.getInt("Id"));
                Curso_Moodle.setSummary(RS.getString("summary").replaceAll("\\<.*?\\>", ""));
                Curso_Moodle.setSincronizado(RS.getBoolean("Sincronizado_Surver"));
                Cursos.add(Curso_Moodle);
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Moodle_curso.getNotSincronizedMoodleCourses()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
        }
        return Cursos.isEmpty() ? null : Cursos;
    }
}