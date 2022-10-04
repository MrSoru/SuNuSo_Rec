package com.codebyte.sunuso.Objects.Surver.MixedQuerys;

import com.codebyte.sunuso.Conections.ConectionPostgreSQL;
import com.codebyte.sunuso.Objects.Surver.Resources.Surver_DataChecker;
import com.codebyte.sunuso.Objects.Surver.Surver_Asignacion_Cursos;
import com.codebyte.sunuso.Objects.Surver.Surver_Control_Puesto;
import com.codebyte.sunuso.Objects.Surver.Surver_Curso;
import com.codebyte.sunuso.Objects.Surver.UsuarioSurver;
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

public class ConexionSurver extends ConectionPostgreSQL {
    protected WindowsLog WinLog;

    private String ThreadName;

    private final Configuration Conf;

    public ConexionSurver(final Configuration Conf, final String ThreadName) {
        super(Conf, 0);
        this.ThreadName = ThreadName;
        this.Conf = Conf;
        this.WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
    }

    public ArrayList<UsuarioSurver> GetInscriptions() {
        ArrayList<UsuarioSurver> Usuarios = new ArrayList<>();
        ArrayList<UsuarioSurver> ErrnoUsuarios = new ArrayList<>();
        String QRY = "SELECT Empl.Nombre                                                                         AS Nombre\n" +
                "     , Empl.paterno                                                                             AS Paterno\n" +
                "     , COALESCE(Empl.materno,'')                                                                AS Materno\n" +
                "     , COALESCE(Empl.RFC,'')                                                                    AS RFC\n" +
                "     , COALESCE(Empl.NSS,'')                                                                    AS NSS\n" +
                "     , REPLACE(LOWER(Empl.usuario), ' ', '_')                                                   AS Usuario\n" +
                "     , Empl.id_empleado                                                                         AS id_empleado\n" +
                "     , CONCAT(REPLACE(REPLACE(LOWER(Empl.usuario), 'ñ', 'n'), ' ', '_'), '@cursos.com.mx')      AS Correo\n" +
                "     , COALESCE(pue.nombre_puesto,'')                                                           AS Nombre_Puesto\n" +
                "--info adicional\n" +
                "     , COALESCE(Empr.empresa_nombre,'')                                                         AS Empresa\n" +
                "     , COALESCE(Seg.nombre,'')                                                                  AS Segmento\n" +
                "FROM Empleado Empl\n" +
                "         INNER JOIN (SELECT Ev_Puesto_id\n" +
                "                     FROM moodle_curso_perfil\n" +
                "                     WHERE activo = TRUE\n" +
                "                     GROUP BY ev_puesto_id) ev_p\n" +
                "                    ON Empl.Ev_Puesto_id = ev_p.Ev_Puesto_id\n" +
                "         INNER JOIN segmento AS Seg\n" +
                "                    ON Seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa AS Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_empresa\n" +
                "         LEFT JOIN ev_puesto AS pue\n" +
                "                   ON Empl.ev_puesto_id = Pue.ev_puesto_id\n" +
                "WHERE Empl.Activo = TRUE\n" +
                "  AND (sincronizado_moodle = FALSE OR sincronizado_moodle ISNULL)\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)\n" +
                "ORDER BY Empl.Nombre, Empl.Paterno, Empl.Materno";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                UsuarioSurver Usuario = new UsuarioSurver();
                Usuario.setId_empleado(RS.getInt("id_empleado"));
                Usuario.setNombre(RS.getString("Nombre"));
                Usuario.setPaterno(RS.getString("Paterno"));
                Usuario.setMaterno(RS.getString("Materno"));
                Usuario.setRFC(RS.getString("RFC"));
                Usuario.setNSS(RS.getString("NSS"));
                Usuario.setUsuario(RS.getString("Usuario"));
                Usuario.setCorreo(RS.getString("Correo"));
                Usuario.setEmpresa(RS.getString("Empresa"));
                Usuario.setSegmento(RS.getString("Segmento"));
                Usuario.setPuesto(RS.getString("Nombre_Puesto"));
                if (Usuario.getRFC() == null) {
                    ErrnoUsuarios.add(Usuario);
                    continue;
                }
                if (Usuario.getRFC().trim().isBlank()) {
                    ErrnoUsuarios.add(Usuario);
                    continue;
                }
                Usuarios.add(Usuario);
            }
            STM.close();
            RS.close();
            Surver_DataChecker.CheckAndWriteCSV(ErrnoUsuarios, "Usuarios Sin RFC Surver para inscribir");
            Surver_DataChecker.CheckAndWriteTXT(ErrnoUsuarios, "Usuarios Sin RFC Surver para inscribir");
            return Usuarios;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetInscriptions()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        } catch (NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetInscriptions()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<UsuarioSurver> GetReactivations() {
        ArrayList<UsuarioSurver> Usuarios = new ArrayList<>();
        ArrayList<UsuarioSurver> ErrnoUsuarios = new ArrayList<>();
        String QRY = "SELECT Empl.Nombre                                                                         AS Nombre\n" +
                "     , Empl.paterno                                                                        AS Paterno\n" +
                "     , COALESCE(Empl.materno,'')                                                           AS Materno\n" +
                "     , COALESCE(Empl.RFC,'')                                                               AS RFC\n" +
                "     , COALESCE(Empl.NSS,'')                                                               AS NSS\n" +
                "     , REPLACE(LOWER(Empl.usuario), ' ', '_')                                              AS Usuario\n" +
                "     , Empl.id_empleado                                                                    AS id_empleado\n" +
                "     , CONCAT(REPLACE(REPLACE(LOWER(Empl.usuario), 'ñ', 'n'), ' ', '_'), '@cursos.com.mx') AS Correo\n" +
                "--info adicional\n" +
                "     , COALESCE(Empr.empresa_nombre,'')                                                                 AS Empresa\n" +
                "     , COALESCE(Seg.nombre,'')                                                                          AS Segmento\n" +
                "FROM Empleado Empl\n" +
                "         INNER JOIN (SELECT Ev_Puesto_id\n" +
                "                     FROM moodle_curso_perfil\n" +
                "                     WHERE activo = TRUE\n" +
                "                     GROUP BY ev_puesto_id) ev_p\n" +
                "                    ON Empl.Ev_Puesto_id = ev_p.Ev_Puesto_id\n" +
                "         INNER JOIN segmento AS Seg\n" +
                "                    ON Seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa AS Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_empresa\n" +
                "WHERE Empl.Activo = TRUE\n" +
                "  AND (sincronizado_moodle = TRUE)\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)\n" +
                "ORDER BY Empl.Nombre, Empl.Paterno, Empl.Materno";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                UsuarioSurver Usuario = new UsuarioSurver();
                Usuario.setId_empleado(RS.getInt("id_empleado"));
                Usuario.setNombre(RS.getString("Nombre"));
                Usuario.setPaterno(RS.getString("Paterno"));
                Usuario.setMaterno(RS.getString("Materno"));
                Usuario.setRFC(RS.getString("RFC"));
                Usuario.setNSS(RS.getString("NSS"));
                Usuario.setUsuario(RS.getString("Usuario"));
                Usuario.setCorreo(RS.getString("Correo"));
                Usuario.setEmpresa(RS.getString("Empresa"));
                Usuario.setSegmento(RS.getString("Segmento"));
                if (Usuario.getRFC() == null) {
                    ErrnoUsuarios.add(Usuario);
                    continue;
                }
                if (Usuario.getRFC().trim().isBlank()) {
                    ErrnoUsuarios.add(Usuario);
                    continue;
                }
                Usuarios.add(Usuario);
            }
            STM.close();
            RS.close();
            Surver_DataChecker.CheckAndWriteCSV(ErrnoUsuarios, "Usuarios Sin RFC Surver para inscribir");
            Surver_DataChecker.CheckAndWriteTXT(ErrnoUsuarios, "Usuarios Sin RFC Surver para inscribir");
            return Usuarios;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetInscriptions()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        } catch (NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetInscriptions()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<UsuarioSurver> GetUninscriptions() {
        ArrayList<UsuarioSurver> Usuarios = new ArrayList<>();
        ArrayList<UsuarioSurver> ErrnoUsuarios = new ArrayList<>();
        String QRY = "SELECT Empl.Nombre                                                                                    AS Nombre\n" +
                "     , Empl.paterno                                                                                        AS Paterno\n" +
                "     , COALESCE(Empl.materno,'')                                                                           AS Materno\n" +
                "     , COALESCE(Empl.RFC,'')                                                                               AS RFC\n" +
                "     , COALESCE(Empl.NSS,'')                                                                               AS NSS\n" +
                "     , REPLACE(LOWER(Empl.usuario), ' ', '_')                                                              AS Usuario\n" +
                "     , Empl.id_empleado                                                                                    AS id_empleado\n" +
                "     , CONCAT(REPLACE(REPLACE(LOWER(Empl.usuario), 'ñ', 'n'), ' ', '_'), '@cursos.com.mx')                 AS Correo\n" +
                "--info adicional\n" +
                "     , COALESCE(Empr.empresa_nombre,'')                                                                    AS Empresa\n" +
                "     , COALESCE(Seg.nombre,'')                                                                             AS Segmento\n" +
                "FROM Empleado Empl\n" +
                "         INNER JOIN (SELECT Ev_Puesto_id\n" +
                "                     FROM moodle_curso_perfil\n" +
                "                     GROUP BY ev_puesto_id) ev_p\n" +
                "                    ON Empl.Ev_Puesto_id = ev_p.Ev_Puesto_id\n" +
                "                        AND Empl.Activo = FALSE\n" +
                "                        AND (Empl.RFC IS NOT NULL OR Empl.RFC = '')\n" +
                "         INNER JOIN segmento AS Seg\n" +
                "                    ON Seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa AS Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_empresa\n" +
                "WHERE Empl.sincronizado_moodle = TRUE\n" +
                "  AND Empl.Activo = FALSE\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                UsuarioSurver Usuario = new UsuarioSurver();
                Usuario.setId_empleado(RS.getInt("id_empleado"));
                Usuario.setNombre(RS.getString("Nombre"));
                Usuario.setPaterno(RS.getString("Paterno"));
                Usuario.setMaterno(RS.getString("Materno"));
                Usuario.setRFC(RS.getString("RFC"));
                Usuario.setUsuario(RS.getString("Usuario"));
                Usuario.setCorreo(RS.getString("Correo"));
                Usuario.setEmpresa(RS.getString("Empresa"));
                Usuario.setSegmento(RS.getString("Segmento"));
                if (Usuario.getRFC() == null) {
                    ErrnoUsuarios.add(Usuario);
                    continue;
                }
                if (Usuario.getRFC().trim().isBlank()) {
                    ErrnoUsuarios.add(Usuario);
                    continue;
                }
                Usuarios.add(Usuario);
            }
            STM.close();
            RS.close();
            Surver_DataChecker.CheckAndWriteCSV(ErrnoUsuarios, "Usuarios Sin RFC Surver para DesInscribir");
            Surver_DataChecker.CheckAndWriteTXT(ErrnoUsuarios, "Usuarios Sin RFC Surver para DesInscribir");
            return Usuarios;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetUninscriptions()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        } catch (NullPointerException Ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetUninscriptions()" + TextManagement.NewLine(3) + Ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<Surver_Asignacion_Cursos> GetCourseInscriptions() {
        ArrayList<Surver_Asignacion_Cursos> Asignaciones = new ArrayList<>();
        String QRY = "SELECT\n" +
                "--info basica empleado\n" +
                "    Empl.RFC               AS RFC\n" +
                "--infocurso\n" +
                "     , Cur.moodle_curso_id AS idCurso\n" +
                "--,Cur.nombre_Curso AS Curso\n" +
                "     , Cur.Nombre_Corto    AS Curso\n" +
                "--,Cur.Descripcion_Curso AS Curso\n" +
                "FROM moodle_curso_Perfil Cur_Per\n" +
                "         INNER JOIN moodle_Curso Cur\n" +
                "                    ON Cur_Per.Moodle_Curso_id = Cur.Moodle_Curso_id\n" +
                "         INNER JOIN Empleado Empl\n" +
                "                    ON Empl.ev_puesto_Id = Cur_Per.ev_puesto_id\n" +
                "         INNER JOIN Segmento seg\n" +
                "                    ON seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa empr\n" +
                "                    ON empr.id_empresa = Seg.id_empresa\n" +
                "WHERE Empl.activo = TRUE\n" +
                "  AND Cur.activo = TRUE\n" +
                "  AND Cur_Per.activo = TRUE\n" +
                "  AND Empl.sincronizado_moodle = TRUE\n" +
                "  AND empr.id_empresa NOT IN (6, 7)\n" +
                "  AND Cur.ischanged=true \n" +
                "ORDER BY Cur.Nombre_Corto";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Asignacion_Cursos Asignacion = new Surver_Asignacion_Cursos();
                Asignacion.setRFC(RS.getString("RFC"));
                Asignacion.setCurso(RS.getString("Curso"));
                Asignacion.setMoodle_curso_id(RS.getInt("idCurso"));
                Asignaciones.add(Asignacion);
            }
            STM.close();
            RS.close();
            return Asignaciones;
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseInscriptions()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<Surver_Asignacion_Cursos> GetCourseUninscriptions_Empl0_Cur1_Per1() {
        ArrayList<Surver_Asignacion_Cursos> Asignaciones = new ArrayList<>();
        String QRY = "SELECT\n" +
                "--info basica empleado\n" +
                "    Empl.RFC               AS RFC\n" +
                "--infocurso\n" +
                "     , Cur.moodle_curso_id AS idCurso\n" +
                "--,Cur.nombre_Curso AS Curso\n" +
                "     , Cur.Nombre_Corto    AS Curso\n" +
                "--,Cur.Descripcion_Curso AS Curso\n" +
                "FROM moodle_curso_Perfil Cur_Per\n" +
                "         INNER JOIN moodle_Curso Cur\n" +
                "                    ON Cur_Per.Moodle_Curso_id = Cur.Moodle_Curso_id\n" +
                "         INNER JOIN Empleado Empl\n" +
                "                    ON Empl.ev_puesto_Id = Cur_Per.ev_puesto_id\n" +
                "         INNER JOIN Segmento Seg\n" +
                "                    ON Seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_Segmento\n" +
                "WHERE Empl.activo = FALSE\n" +
                "  AND Cur.activo = TRUE\n" +
                "  AND Cur_Per.activo = TRUE\n" +
                "  AND (RFC IS NOT NULL OR RFC = '')\n" +
                "  AND Empl.sincronizado_moodle = TRUE\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)\n" +
                "ORDER BY Cur.Nombre_Corto";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Asignacion_Cursos Asignacion = new Surver_Asignacion_Cursos();
                Asignacion.setRFC(RS.getString("RFC"));
                Asignacion.setCurso(RS.getString("Curso"));
                Asignaciones.add(Asignacion);
            }
            STM.close();
            RS.close();
            return Asignaciones;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl0_Cur1_Per1()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        } catch (NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl0_Cur1_Per1()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<Surver_Asignacion_Cursos> GetCourseUninscriptions_Empl1_Cur0_Per1() {
        ArrayList<Surver_Asignacion_Cursos> Asignaciones = new ArrayList<>();
        String QRY = "SELECT\n" +
                "     Cur.Nombre_Corto    AS Curso\n" +
                "FROM  moodle_Curso Cur\n" +
                "WHERE Cur.activo = false\n" +
                "ORDER BY Cur.Nombre_Corto\n";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Asignacion_Cursos Asignacion = new Surver_Asignacion_Cursos();
                Asignacion.setCurso(RS.getString("Curso"));
                Asignaciones.add(Asignacion);
            }
            STM.close();
            RS.close();
            return Asignaciones;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl1_Cur0_Per1()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        } catch (NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl1_Cur0_Per1()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<Surver_Asignacion_Cursos> GetCourseUninscriptions_Empl1_Cur0_Per0() {
        ArrayList<Surver_Asignacion_Cursos> Asignaciones = new ArrayList<>();
        String QRY = "SELECT\n" +
                "--info basica empleado\n" +
                "    Empl.RFC               AS RFC\n" +
                "--infocurso\n" +
                "     , Cur.moodle_curso_id AS idCurso\n" +
                "--,Cur.nombre_Curso AS Curso\n" +
                "     , Cur.Nombre_Corto    AS Curso\n" +
                "--,Cur.Descripcion_Curso AS Curso\n" +
                "FROM moodle_curso_Perfil Cur_Per\n" +
                "         INNER JOIN moodle_Curso Cur\n" +
                "                    ON Cur_Per.Moodle_Curso_id = Cur.Moodle_Curso_id\n" +
                "         INNER JOIN Empleado Empl\n" +
                "                    ON Empl.ev_puesto_Id = Cur_Per.ev_puesto_id\n" +
                "         INNER JOIN Segmento Seg\n" +
                "                    ON Seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_Segmento\n" +
                "WHERE Empl.activo = TRUE\n" +
                "  AND Cur.activo = FALSE\n" +
                "  AND Cur_Per.activo = FALSE\n" +
                "  AND (RFC IS NOT NULL OR RFC = '')\n" +
                "  AND Empl.sincronizado_moodle = TRUE\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)\n" +
                //+ "AND AND Empl.id_empleado=1006\n" +///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                "ORDER BY Cur.Nombre_Corto";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Asignacion_Cursos Asignacion = new Surver_Asignacion_Cursos();
                Asignacion.setRFC(RS.getString("RFC"));
                Asignacion.setCurso(RS.getString("Curso"));
                Asignacion.setMoodle_curso_id(RS.getInt("idCurso"));
                Asignaciones.add(Asignacion);
            }
            STM.close();
            RS.close();
            return Asignaciones;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl1_Cur0_Per0()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        } catch (NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl1_Cur0_Per0()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<Surver_Asignacion_Cursos> GetCourseUninscriptions_Empl1_Cur1_Per0() {
        ArrayList<Surver_Asignacion_Cursos> Asignaciones = new ArrayList<>();
        String QRY = "SELECT\n" +
                "--info basica empleado\n" +
                "    Empl.RFC               AS RFC\n" +
                "--infocurso\n" +
                "     , Cur.moodle_curso_id AS idCurso\n" +
                "--,Cur.nombre_Curso AS Curso\n" +
                "     , Cur.Nombre_Corto    AS Curso\n" +
                "--,Cur.Descripcion_Curso AS Curso\n" +
                "FROM moodle_curso_Perfil Cur_Per\n" +
                "         INNER JOIN moodle_Curso Cur\n" +
                "                    ON Cur_Per.Moodle_Curso_id = Cur.Moodle_Curso_id\n" +
                "         INNER JOIN Empleado Empl\n" +
                "                    ON Empl.ev_puesto_Id = Cur_Per.ev_puesto_id\n" +
                "         INNER JOIN Segmento Seg\n" +
                "                    ON Seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_Segmento\n" +
                "WHERE Empl.activo = TRUE\n" +
                "  AND Cur.activo = TRUE\n" +
                "  AND Cur_Per.activo = FALSE\n" +
                "  AND (RFC IS NOT NULL OR RFC = '')\n" +
                "  AND Empl.sincronizado_moodle = TRUE\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)\n" +
                "ORDER BY Cur.Nombre_Corto";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Asignacion_Cursos Asignacion = new Surver_Asignacion_Cursos();
                Asignacion.setRFC(RS.getString("RFC"));
                Asignacion.setCurso(RS.getString("Curso"));
                Asignacion.setMoodle_curso_id(RS.getInt("idCurso"));
                Asignaciones.add(Asignacion);
            }
            STM.close();
            RS.close();
            return Asignaciones;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl1_Cur1_Per0()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public ArrayList<Surver_Asignacion_Cursos> GetCourseUninscriptions_Empl0_Cur0_Per0() {
        ArrayList<Surver_Asignacion_Cursos> Asignaciones = new ArrayList<>();
        String QRY = "SELECT\n" +
                "--info basica empleado\n" +
                "    Empl.RFC               AS RFC\n" +
                "--infocurso\n" +
                "     , Cur.moodle_curso_id AS idCurso\n" +
                "--,Cur.nombre_Curso AS Curso\n" +
                "     , Cur.Nombre_Corto    AS Curso\n" +
                "--,Cur.Descripcion_Curso AS Curso\n" +
                "FROM moodle_curso_Perfil Cur_Per\n" +
                "         INNER JOIN moodle_Curso Cur\n" +
                "                    ON Cur_Per.Moodle_Curso_id = Cur.Moodle_Curso_id\n" +
                "         INNER JOIN Empleado Empl\n" +
                "                    ON Empl.ev_puesto_Id = Cur_Per.ev_puesto_id\n" +
                "         INNER JOIN Segmento Seg\n" +
                "                    ON Seg.id_segmento = Empl.id_segmento\n" +
                "         INNER JOIN Empresa Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_Segmento\n" +
                "WHERE Empl.activo = FALSE\n" +
                "  AND Cur.activo = FALSE\n" +
                "  AND Cur_Per.activo = FALSE\n" +
                "  AND (RFC IS NOT NULL OR RFC = '')\n" +
                "  AND Empl.sincronizado_moodle = TRUE\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)\n" +
                "ORDER BY Cur.Nombre_Corto";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Asignacion_Cursos Asignacion = new Surver_Asignacion_Cursos();
                Asignacion.setRFC(RS.getString("RFC"));
                Asignacion.setCurso(RS.getString("Curso"));
                Asignacion.setMoodle_curso_id(RS.getInt("idCurso"));
                Asignaciones.add(Asignacion);
            }
            STM.close();
            RS.close();
            return Asignaciones;
        } catch (SQLException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl0_Cur0_Per0()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        } catch (NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetCourseUninscriptions_Empl0_Cur0_Per0()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public UsuarioSurver GetUsuarioporRFC(final String RFC) {
        UsuarioSurver UsuarioSur = new UsuarioSurver();
        String QRY = "SELECT Empl.Nombre                                                                    AS Nombre\n" +
                "     , Empl.paterno                                                                        AS Paterno\n" +
                "     , COALESCE(Empl.materno, '')                                                          AS Materno\n" +
                "     , COALESCE(Empl.RFC, '')                                                              AS RFC\n" +
                "     , COALESCE(Empl.NSS, '')                                                              AS NSS\n" +
                "     , REPLACE(LOWER(Empl.usuario), ' ', '_')                                              AS Usuario\n" +
                "     , Empl.id_empleado                                                                    AS id_empleado\n" +
                "     , CONCAT(REPLACE(REPLACE(LOWER(Empl.usuario), 'ñ', 'n'), ' ', '_'), '@cursos.com.mx') AS Correo\n" +
                "     , COALESCE(pue.nombre_puesto, '')                                                     AS Nombre_Puesto\n" +
                "--info adicional\n" +
                "     , COALESCE(Empr.empresa_nombre,'')                                                                 AS Empresa\n" +
                "     , COALESCE(Seg.nombre,'')                                                                          AS Segmento\n" +
                "FROM Empleado Empl\n" +
                "         INNER JOIN segmento AS Seg\n" +
                "                    ON Seg.id_segmento = empl.id_segmento\n" +
                "         INNER JOIN Empresa AS Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_empresa\n" +
                "         LEFT JOIN ev_puesto AS pue\n" +
                "                   ON Empl.ev_puesto_id = Pue.ev_puesto_id\n" +
                "WHERE empl.rfc = '" + RFC + "'\n" +
                "  AND Empr.id_empresa NOT IN (6, 7);";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                UsuarioSur.setNombre(RS.getString("Nombre"));
                UsuarioSur.setPaterno(RS.getString("Paterno"));
                UsuarioSur.setMaterno(RS.getString("Materno"));
                UsuarioSur.setRFC(RS.getString("RFC"));
                UsuarioSur.setNSS(RS.getString("NSS"));
                UsuarioSur.setUsuario(RS.getString("Usuario"));
                UsuarioSur.setCorreo(RS.getString("Correo"));
                UsuarioSur.setEmpresa(RS.getString("Empresa"));
                UsuarioSur.setSegmento(RS.getString("Segmento"));
                UsuarioSur.setId_empleado(RS.getInt("id_empleado"));
                UsuarioSur.setPuesto(RS.getString("Nombre_Puesto"));
            }
            STM.close();
            RS.close();
            return UsuarioSur;
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetUsuarioporRFC()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public UsuarioSurver GetUsuarioporUsername(final String Usuario) {
        UsuarioSurver UsuarioSur = new UsuarioSurver();
        String QRY = "SELECT Empl.Nombre                                                                                      AS Nombre\n" +
                "     , Empl.paterno                                                                                     AS Paterno\n" +
                "     , COALESCE(Empl.materno,'')                                                                        AS Materno\n" +
                "     , COALESCE(Empl.RFC,'')                                                                            AS RFC\n" +
                "     , COALESCE(Empl.NSS,'')                                                                            AS NSS\n" +
                "     , REPLACE(LOWER(Empl.usuario), ' ', '_')                                                           AS Usuario\n" +
                "     , Empl.id_empleado                                                                                 AS id_empleado\n" +
                "     , CONCAT(REPLACE(REPLACE(LOWER(Empl.usuario), 'ñ', 'n'), ' ', '_'), '@cursos.com.mx')              AS Correo\n" +
                "     , COALESCE(pue.nombre_puesto,'')                                                                   AS Nombre_Puesto\n" +
                "--info adicional\n" +
                "     , COALESCE(Empr.empresa_nombre,'')                                                                 AS Empresa\n" +
                "     , COALESCE(Seg.nombre,'')                                                                          AS Segmento\n" +
                "FROM Empleado Empl\n" +
                "         INNER JOIN segmento AS Seg\n" +
                "                    ON Seg.id_segmento = empl.id_segmento\n" +
                "         INNER JOIN Empresa AS Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_empresa\n" +
                "         LEFT JOIN ev_puesto AS pue\n" +
                "                   ON Empl.ev_puesto_id = Pue.ev_puesto_id\n" +
                "WHERE empl.usuario ='" + Usuario + "'\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                UsuarioSur.setNombre(RS.getString("Nombre"));
                UsuarioSur.setPaterno(RS.getString("Paterno"));
                UsuarioSur.setMaterno(RS.getString("Materno"));
                UsuarioSur.setRFC(RS.getString("RFC"));
                UsuarioSur.setNSS(RS.getString("NSS"));
                UsuarioSur.setUsuario(RS.getString("Usuario"));
                UsuarioSur.setCorreo(RS.getString("Correo"));
                UsuarioSur.setEmpresa(RS.getString("Empresa"));
                UsuarioSur.setSegmento(RS.getString("Segmento"));
                UsuarioSur.setId_empleado(RS.getInt("id_empleado"));
                UsuarioSur.setPuesto(RS.getString("Nombre_Puesto"));
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetUsuarioporUsername()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
        return UsuarioSur;
    }

    public UsuarioSurver GetUsuarioporIdSurver(final int id) {
        UsuarioSurver UsuarioSur = null;
        String QRY = "SELECT Empl.Nombre                                                                              AS Nombre\n" +
                "     , Empl.paterno                                                                             AS Paterno\n" +
                "     , COALESCE(Empl.materno,'')                                                                AS Materno\n" +
                "     , COALESCE(Empl.RFC,'')                                                                    AS RFC\n" +
                "     , COALESCE(Empl.NSS,'')                                                                    AS NSS\n" +
                "     , REPLACE(LOWER(Empl.usuario), ' ', '_')                                              AS Usuario\n" +
                "     , Empl.id_empleado                                                                    AS id_empleado\n" +
                "     , CONCAT(REPLACE(REPLACE(LOWER(Empl.usuario), 'ñ', 'n'), ' ', '_'), '@cursos.com.mx') AS Correo\n" +
                "     , COALESCE(pue.nombre_puesto,'')                                                                   AS Nombre_Puesto\n" +
                "--info adicional\n" +
                "     , COALESCE(Empr.empresa_nombre,'')                                                                 AS Empresa\n" +
                "     , COALESCE(Seg.nombre,'')                                                                          AS Segmento\n" +
                "FROM Empleado Empl\n" +
                "         INNER JOIN segmento AS Seg\n" +
                "                    ON Seg.id_segmento = empl.id_segmento\n" +
                "         INNER JOIN Empresa AS Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_empresa\n" +
                "         LEFT JOIN ev_puesto AS pue\n" +
                "                   ON Empl.ev_puesto_id = Pue.ev_puesto_id\n" +
                "WHERE empl.id_empleado = " + id + "\n" +
                "  AND Empr.id_empresa NOT IN (6, 7)";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                UsuarioSur = new UsuarioSurver();
                UsuarioSur.setNombre(RS.getString("Nombre"));
                UsuarioSur.setPaterno(RS.getString("Paterno"));
                UsuarioSur.setMaterno(RS.getString("Materno"));
                UsuarioSur.setRFC(RS.getString("RFC"));
                UsuarioSur.setNSS(RS.getString("NSS"));
                UsuarioSur.setUsuario(RS.getString("Usuario"));
                UsuarioSur.setCorreo(RS.getString("Correo"));
                UsuarioSur.setEmpresa(RS.getString("Empresa"));
                UsuarioSur.setSegmento(RS.getString("Segmento"));
                UsuarioSur.setPuesto(RS.getString("Nombre_Puesto"));
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: ConexionSurver.GetUsuarioporIdSurver()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
        return UsuarioSur;
    }

    public boolean setSync(final Integer idUsuario) {
        String QRY = "UPDATE refividrio.empleado\n" +
                "    SET sincronizado_moodle=true\n" +
                "    WHERE id_empleado=" + idUsuario + " ;";
        if (idUsuario.intValue() == -1)
            return false;
        try {
            PreparedStatement Pstm = this.conn.prepareStatement(QRY);
            Pstm.addBatch();
            Pstm.executeBatch();
            Pstm.close();
            return true;
        } catch (SQLException | NullPointerException Ex) {
            this.WinLog.WriteEvent("No se logró actualizar el dato UsuarioSurver.setSync()" + TextManagement.NewLine(3) + Ex.getMessage(), EntryType.Error, 301);
            return false;
        }
    }

    public boolean GuardarCurso(final boolean EsInsert, final Surver_Curso Curso) {
        if (EsInsert)
            return GuardarCursoSurv(Curso);
        return ActualizarCursoSurv(Curso);
    }

    private boolean GuardarCursoSurv(final Surver_Curso Curso) {
        String QRY = "INSERT INTO moodle_curso (creado, creadoPor, actualizado, actualizadoPor, nombre_curso, descripcion_curso, activo, Nombre_Corto) values (NOW(),?,NOW(),?,?,?,?,?)";
        try {
            PreparedStatement PS = this.conn.prepareStatement(QRY);
            PS.setInt(1, Curso.getCreadoPor());
            PS.setInt(2, Curso.getActualizadoPor());
            PS.setString(3, Curso.getNombre_Curso());
            PS.setString(4, Curso.getDescripcion_curso());
            PS.setBoolean(5, Curso.getesActivo());
            PS.setString(6, Curso.getNombre_Corto());
            PS.addBatch();
            PS.executeBatch();
            return true;
        } catch (SQLException | NullPointerException Ex) {
            this.WinLog.WriteEvent("No se logró insertar datos: Surver_Curso.Guardar()" + TextManagement.NewLine(3) + Ex.getMessage(), EntryType.Warning, 302);
            return false;
        }
    }

    private boolean ActualizarCursoSurv(final Surver_Curso Curso) {
        String QRY = "UPDATE moodle_curso\n" +
                "    SET actualizado=NOW(), actualizadopor=?, nombre_curso=?, descripcion_curso=?, activo=?, Nombre_Corto=?\n" +
                "    WHERE Nombre_Corto=?;";
        try {
            PreparedStatement PS = this.conn.prepareStatement(QRY);
            PS.setInt(1, Curso.getActualizadoPor());
            PS.setString(2, Curso.getNombre_Curso());
            PS.setString(3, Curso.getDescripcion_curso());
            PS.setBoolean(4, Curso.getesActivo());
            PS.setString(5, Curso.getNombre_Corto());
            PS.setString(6, Curso.getNombre_Corto());
            PS.addBatch();
            String q = PS.toString();
            PS.executeBatch();
            PS.close();
            return true;
        } catch (SQLException | NullPointerException Ex) {
            this.WinLog.WriteEvent("No se logró actualizar datos: Surver_Curso.Actualizar()" + TextManagement.NewLine(3) + Ex.getMessage(), EntryType.Warning, 301);
            return false;
        }
    }

    public ArrayList<Surver_Curso> getCoursesByoccupation(final int Occupation) {
        ArrayList<Surver_Curso> Cursos = new ArrayList<>();
        String QRY = "SELECT Cur.*\n" +
                "FROM moodle_curso Cur\n" +
                "INNER JOIN moodle_curso_perfil Cur_Per\n" +
                "on cur.moodle_curso_id=cur_per.moodle_curso_id\n" +
                "WHERE Cur.activo=true\n" +
                "AND cur_per.ev_puesto_id=" + Occupation + "\n" +
                "";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Curso Curso = new Surver_Curso();
                Curso.setMoodle_curso_id(RS.getInt("Moodle_curso_id"));
                Curso.setCreado(RS.getDate("Creado"));
                Curso.setCreadoPor(RS.getInt("creadoPor"));
                Curso.setActualizado(RS.getDate("actualizado"));
                Curso.setActualizadoPor(RS.getInt("actualizadopor"));
                Curso.setNombre_Curso(RS.getString("nombre_curso"));
                Curso.setDescripcion_curso(RS.getString("Descripcion_curso"));
                Curso.setActivo(RS.getBoolean("activo"));
                Curso.setNombre_Corto(RS.getString("Nombre_Corto"));
                Cursos.add(Curso);
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Surver_Curso.getCoursesByoccupation()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
        }
        return Cursos.isEmpty() ? null : Cursos;
    }

    public ArrayList<Surver_Curso> getCourses() {
        ArrayList<Surver_Curso> Cursos = new ArrayList<>();
        String QRY = "SELECT Cur.*\n" +
                "FROM moodle_curso Cur\n" +
                "WHERE Cur.activo=true\n" +
                "";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Curso Curso = new Surver_Curso();
                Curso.setMoodle_curso_id(RS.getInt("Moodle_curso_id"));
                Curso.setCreado(RS.getDate("Creado"));
                Curso.setCreadoPor(RS.getInt("creadoPor"));
                Curso.setActualizado(RS.getDate("actualizado"));
                Curso.setActualizadoPor(RS.getInt("actualizadopor"));
                Curso.setNombre_Curso(RS.getString("nombre_curso"));
                Curso.setDescripcion_curso(RS.getString("Descripcion_curso"));
                Curso.setActivo(RS.getBoolean("activo"));
                Curso.setNombre_Corto(RS.getString("Nombre_Corto"));
                Cursos.add(Curso);
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Surver_Curso.getCoursesByoccupation()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
        }
        return Cursos.isEmpty() ? null : Cursos;
    }

    public ArrayList<Surver_Curso> getCoursesByShortname(final String ShortName) {
        ArrayList<Surver_Curso> Cursos = new ArrayList<>();
        String QRY = "SELECT Cur.*\n" +
                "FROM moodle_curso Cur\n" +
                "WHERE Cur.activo=true\n" +
                "AND Cur.Nombre_Corto=" + TextManagement.SingleQuote(ShortName) + "\n" +
                "";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Curso Curso = new Surver_Curso();
                Curso.setMoodle_curso_id(RS.getInt("Moodle_curso_id"));
                Curso.setCreado(RS.getDate("Creado"));
                Curso.setCreadoPor(RS.getInt("creadoPor"));
                Curso.setActualizado(RS.getDate("actualizado"));
                Curso.setActualizadoPor(RS.getInt("actualizadopor"));
                Curso.setNombre_Curso(RS.getString("nombre_curso"));
                Curso.setDescripcion_curso(RS.getString("Descripcion_curso"));
                Curso.setActivo(RS.getBoolean("activo"));
                Curso.setNombre_Corto(RS.getString("Nombre_Corto"));
                Cursos.add(Curso);
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Surver_Curso.getCoursesByoccupation()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
        }
        return Cursos.isEmpty() ? null : Cursos;
    }

    public Surver_Curso getCourseByID(final int ID) {
        Surver_Curso Curso = new Surver_Curso();
        String QRY = "SELECT Cur.*\n" +
                "FROM moodle_curso Cur\n" +
                "WHERE Cur.activo=true\n" +
                "AND cur_per.moodle_curso_id=" + ID + "\n" +
                "";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Curso.setMoodle_curso_id(RS.getInt("Moodle_curso_id"));
                Curso.setCreado(RS.getDate("Creado"));
                Curso.setCreadoPor(RS.getInt("creadoPor"));
                Curso.setActualizado(RS.getDate("actualizado"));
                Curso.setActualizadoPor(RS.getInt("actualizadopor"));
                Curso.setNombre_Curso(RS.getString("nombre_curso"));
                Curso.setDescripcion_curso(RS.getString("Descripcion_curso"));
                Curso.setActivo(RS.getBoolean("activo"));
                Curso.setNombre_Corto(RS.getString("Nombre_Corto"));
            }
            STM.close();
            RS.close();
            return Curso;
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Surver_Curso.getCourseByID()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public boolean getCourseByShortName(final String ShortName) {
        Surver_Curso Curso = new Surver_Curso();
        String QRY = "SELECT Cur.*\n" +
                "FROM moodle_curso Cur\n" +
                "WHERE Cur.activo=true\n" +
                "AND cur_per.Nombre_Corto=" + ShortName + "\n" +
                "";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Curso.setMoodle_curso_id(RS.getInt("Moodle_curso_id"));
                Curso.setCreado(RS.getDate("Creado"));
                Curso.setCreadoPor(RS.getInt("creadoPor"));
                Curso.setActualizado(RS.getDate("actualizado"));
                Curso.setActualizadoPor(RS.getInt("actualizadopor"));
                Curso.setNombre_Curso(RS.getString("nombre_curso"));
                Curso.setDescripcion_curso(RS.getString("Descripcion_curso"));
                Curso.setActivo(RS.getBoolean("activo"));
                Curso.setNombre_Corto(RS.getString("Nombre_Corto"));
            }
            STM.close();
            RS.close();
            return true;
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Surver_Curso.getCourseByID()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return false;
        }
    }

    public ArrayList<Surver_Curso> getCoursesDiferences(final int param1, final int param2) {
        ArrayList<Surver_Curso> Cursos = new ArrayList<>();
        String QRY = "(SELECT Cur.*\n" +
                "FROM moodle_curso Cur\n" +
                "INNER JOIN moodle_curso_perfil Cur_Per\n" +
                "on cur.moodle_curso_id=cur_per.moodle_curso_id\n" +
                "WHERE Cur.activo=true\n" +
                "AND cur_per.ev_puesto_id=" + param2 + ")--viejo (en que se encuentra actualmente)\n" +
                "EXCEPT\n" +
                "(SELECT Cur.*\n" +
                "FROM moodle_curso Cur\n" +
                "INNER JOIN moodle_curso_perfil Cur_Per\n" +
                "on cur.moodle_curso_id=cur_per.moodle_curso_id\n" +
                "WHERE Cur.activo=true\n" +
                "AND cur_per.ev_puesto_id=" + param1 + ")";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Curso Curso = new Surver_Curso();
                Curso.setMoodle_curso_id(RS.getInt("Moodle_curso_id"));
                Curso.setCreado(RS.getDate("Creado"));
                Curso.setCreadoPor(RS.getInt("creadoPor"));
                Curso.setActualizado(RS.getDate("actualizado"));
                Curso.setActualizadoPor(RS.getInt("actualizadopor"));
                Curso.setNombre_Curso(RS.getString("nombre_curso"));
                Curso.setDescripcion_curso(RS.getString("Descripcion_curso"));
                Curso.setActivo(RS.getBoolean("activo"));
                Curso.setNombre_Corto(RS.getString("Nombre_Corto"));
                Cursos.add(Curso);
            }
            STM.close();
            RS.close();
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Surver_Curso.getCoursesDiferences()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
        }
        return Cursos.isEmpty() ? null : Cursos;
    }

    public ArrayList<Surver_Control_Puesto> getContolCambios_SinSync() {
        ArrayList<Surver_Control_Puesto> Controles = new ArrayList<>();
        String QRY = "SELECT pu.* FROM moodle_Control_Puesto AS pu  \n" +
                "INNER JOIN Empleado Empl\n" +
                "ON pu.id_empleado=Empl.id_empleado \n" +
                "         INNER JOIN segmento AS Seg\n" +
                "                    ON Seg.id_segmento = empl.id_segmento\n" +
                "         INNER JOIN Empresa AS Empr\n" +
                "                    ON Empr.id_empresa = Seg.id_empresa\n" +
                "         LEFT JOIN ev_puesto AS pue\n" +
                "                   ON Empl.ev_puesto_id = Pue.ev_puesto_id\n" +
                "WHERE \n" +
                "not (pu.viejo_perfil is null or pu.nuevo_perfil is null) \n" +
                "AND pu.Sincronized=false\n" +
                "AND Empr.id_empresa NOT IN (6, 7)\n" +
                "AND Empl.sincronizado_moodle = TRUE\n";
        try {
            Statement STM = this.conn.createStatement();
            ResultSet RS = STM.executeQuery(QRY);
            while (RS.next()) {
                Surver_Control_Puesto Control = new Surver_Control_Puesto();
                Control.setActualizado(RS.getDate("actualizado"));
                Control.setCreado(RS.getDate("Creado"));
                Control.setId_empleado(RS.getInt("id_empleado"));
                Control.setId_moodle_contol_puesto(RS.getInt("id_moodle_contol_puesto"));
                Control.setNuevo_perfil(RS.getInt("nuevo_perfil"));
                Control.setViejo_perfil(RS.getInt("viejo_perfil"));
                Control.setSincronized(RS.getBoolean("Sincronized"));
                Controles.add(Control);
            }
            STM.close();
            RS.close();
            return Controles;
        } catch (SQLException | NullPointerException ex) {
            this.WinLog.WriteEvent("No se logró obtener datos: Surver_Control_Puesto.getContolCambios_SinSync()" + TextManagement.NewLine(3) + ex.getMessage(), EntryType.Warning, 300);
            return null;
        }
    }

    public boolean SetContolCambiosInactivo(final Surver_Control_Puesto SCP) {
        String QRY = "UPDATE moodle_Control_Puesto set Sincronized=true WHERE id_moodle_contol_puesto=" + SCP.getId_moodle_contol_puesto();
        try {
            PreparedStatement Pstm = this.conn.prepareStatement(QRY);
            Pstm.addBatch();
            Pstm.executeBatch();
            Pstm.close();
            return true;
        } catch (SQLException | NullPointerException Ex) {
            this.WinLog.WriteEvent("No se logró actualizar el dato Surver_Control_Puesto.SetInactivo()" + TextManagement.NewLine(3) + Ex.getMessage(), EntryType.Error, 301);
            return false;
        }
    }

    public String getEstadoConexion() {
        return (this.conn == null) ?
                "Conexion nula" : (
                CheckConection(this.ThreadName) ?
                        "Conexion abrierta" :
                        "Conexion Cerrada");
    }

    public boolean ActualizarCursoSurv_ischanged(final String Curso) {
        String QRY = "UPDATE moodle_curso\n" +
                "    SET actualizado=NOW(), ischanged=false\n" +
                "    WHERE Nombre_Corto=?;";
        try {
            PreparedStatement PS = this.conn.prepareStatement(QRY);
            PS.setString(1, Curso);
            PS.addBatch();
            PS.executeBatch();
            PS.close();
            return true;
        } catch (SQLException | NullPointerException Ex) {
            this.WinLog.WriteEvent("No se logró actualizar datos: Surver_Curso.ActualizarCursoSurv_ischanged()" + TextManagement.NewLine(3) + Ex.getMessage(), EntryType.Warning, 501);
            return false;
        }
    }
}
