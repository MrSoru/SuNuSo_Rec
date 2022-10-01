package com.codebyte.sunuso.ThreadManager.Procesos;

import com.codebyte.sunuso.Conections.NetChecker;
import com.codebyte.sunuso.Objects.Moodle.ConexionMoodle;
import com.codebyte.sunuso.Objects.Moodle.Moodle_Curso;
import com.codebyte.sunuso.Objects.Moodle.Resources.Courses;
import com.codebyte.sunuso.Objects.Moodle.Resources.Response;
import com.codebyte.sunuso.Objects.Moodle.Resources.UsuarioGenerico;
import com.codebyte.sunuso.Objects.Moodle.WebServiceCreator.MoodleWerbServCreator;
import com.codebyte.sunuso.Objects.Surver.MixedQuerys.ConexionSurver;
import com.codebyte.sunuso.Objects.Surver.Surver_Asignacion_Cursos;
import com.codebyte.sunuso.Objects.Surver.Surver_Control_Puesto;
import com.codebyte.sunuso.Objects.Surver.Surver_Curso;
import com.codebyte.sunuso.Objects.Surver.UsuarioSurver;
import com.codebyte.sunuso.OutputFilesWritter.CreateFile;
import com.codebyte.sunuso.OutputFilesWritter.Resources.FileExtention;
import com.codebyte.sunuso.Resources.Configuration;
import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.Resources.TextManagement;
import com.codebyte.sunuso.WindowsEventLogWritter.EntryType;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Funciones {
    Configuration Conf;

    String LogMsg;

    WindowsLog WinLog = new WindowsLog();

    String ThreadName;

    NetChecker NetCheck;

    ConexionSurver SurverConn;

    ConexionMoodle MoodleConn;

    public Funciones() {
        this.WinLog.setLogName(Information.LogName);
        this.WinLog.setProgramName(Information.ProgramName);
    }

    public Funciones(Configuration Conf, String TheadName) {
        this.WinLog.setLogName(Information.LogName);
        this.WinLog.setProgramName(Information.ProgramName);
        this.ThreadName = TheadName;
        this.Conf = Conf;
        this.SurverConn = new ConexionSurver(Conf, TheadName);
        this.MoodleConn = new ConexionMoodle(Conf, TheadName);
    }

    public Configuration getConf() {
        return this.Conf;
    }

    public void setConf(Configuration Conf) {
        this.Conf = Conf;
    }

    private boolean CheckConnection(String Descripcion) {
        String LogCheckConnection_Surver = "Log " + Information.getFuente() + ":" + TextManagement.NewLine(2);
        String LogCheckConnection_Moodle = "Log " + Information.getPlataforma() + ":" + TextManagement.NewLine(2);
        Boolean SurverErrnoFlag = false;
        Boolean MoodleErrnoFlag = false;
        if (this.SurverConn == null) {
            LogCheckConnection_Surver += "Error en las credenciales de conexion a Base de datos (" + Information.getFuente() + ")" + TextManagement.NewLine();
            SurverErrnoFlag = true;
        } else if (!this.SurverConn.CheckConection(this.ThreadName)) {
            LogCheckConnection_Surver += "Conexion no válida (" + Information.Fuente + "); Intentando destruir conexion..." + TextManagement.NewLine();
            LogCheckConnection_Surver += "Resultado DestruirConexión(): " + (this.SurverConn.DestroyConnection() ? "Completo." : "Con Error.") + TextManagement.NewLine();
            this.SurverConn = null;
            this.SurverConn = new ConexionSurver(Conf, this.ThreadName);
            LogCheckConnection_Surver += "El estado de la conexión es: " + ((this.SurverConn == null) ? "nula" : "Instanciada") + TextManagement.NewLine();
            if (this.SurverConn == null && !this.MoodleConn.CheckConection(this.ThreadName)) {
                SurverErrnoFlag = true;
            }
        }
        if (this.MoodleConn == null) {
            LogCheckConnection_Moodle += "Error en las credenciales de conexion a Base de datos (" + Information.getPlataforma() + ")" + TextManagement.NewLine();
            MoodleErrnoFlag = true;
        } else if (!this.MoodleConn.CheckConection(this.ThreadName)) {
            LogCheckConnection_Moodle += "Conexion no válida (" + Information.getPlataforma() + "); Intentando destruir conexion..." + TextManagement.NewLine();
            LogCheckConnection_Moodle += "Resultado DestruirConexión(): " + (this.MoodleConn.DestroyConnection() ? "Completo." : "Con Error.") + TextManagement.NewLine();
            this.MoodleConn = null;
            this.MoodleConn = new ConexionMoodle(Conf, this.ThreadName);
            LogCheckConnection_Moodle += "El estado de la conexión es: " + ((this.MoodleConn == null) ? "nula" : "Instanciada") + TextManagement.NewLine();
            if (this.MoodleConn == null && !this.MoodleConn.CheckConection(this.ThreadName)) {
                MoodleErrnoFlag = true;
            }
        }

        if (SurverErrnoFlag || MoodleErrnoFlag) {
            this.WinLog.WriteEvent(Descripcion + TextManagement.NewLine(2)
                    + LogCheckConnection_Surver + TextManagement.NewLine(3)
                    + TextManagement.Separator() + TextManagement.NewLine(4)
                    + LogCheckConnection_Moodle, EntryType.Warning, 303);
            return false;
        } else {
            return true;
        }
    }

    public synchronized boolean EnrolltoPlatfrom() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("DarDeAlta()"))
            return false;
        String LogString = "Evento Dar de Alta iniciado el:" + (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss")).format(new Date()) + TextManagement.NewLine();
        ArrayList<UsuarioSurver> Usuarios = this.SurverConn.GetInscriptions();
        if (Usuarios != null) {
            if (!Usuarios.isEmpty())
                for (UsuarioSurver Usuario : Usuarios) {
                    UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), Usuario.getRFC());
                    if (UsuarioGen == null) {
                        Response Res = MoodleWerbServCreator.CreateUser1(this.Conf.getCLI_WEBSERVICE_ROOT(),
                                this.Conf.getCLI_WEBSERVICE_TOKEN(),
                                Usuario.getUsuario(),
                                "Cursos123",
                                Usuario.getNombre(),
                                Usuario.getPaterno() + " " + Usuario.getPaterno(),
                                Usuario.getCorreo(),
                                Usuario.getRFC(),
                                Usuario.getSegmento(),
                                Usuario.getEmpresa(),
                                Usuario.getPuesto());
                        if (Res == null) {
                            Usuario.setReason("No se logró inscribir el usuario, (Revisar datos de usuario/Duplicidad en la plataforma/Error de respuesta Webservices/Error de Conexión");
                            continue;
                        }
                        if (this.SurverConn.setSync(Integer.valueOf(Usuario.getId_empleado()))) {
                            Usuario.setReason("Completa!");
                            continue;
                        }
                        Usuario.setReason("completa en " + Information.Plataforma + "! Pero No se acualizSincronizado en " + Information.Fuente);
                        continue;
                    }
                    if (this.SurverConn.setSync(Integer.valueOf(Usuario.getId_empleado()))) {
                        Usuario.setReason("El usuario ya estInscrito");
                        continue;
                    }
                    Usuario.setReason("El usuario ya estInscrito Pero No se acualizSincronizado en " + Information.Fuente);
                }
        }        else {
            return false;
        }
            String DataText = "";
            DataText += ""
                    + TextManagement.NewTab(3) + "-----------------------------------------------------------------------------------" + TextManagement.NewLine()
                    + TextManagement.NewTab(3) + "-               Resultados               -" + TextManagement.NewLine()
                    + TextManagement.NewTab(3) + "-----------------------------------------------------------------------------------" + TextManagement.NewLine(3);
            DataText += UsuarioSurver.GetConsoleDisplayTable(Usuarios) + TextManagement.NewLine(2)
                    + TextManagement.Separator() + TextManagement.NewLine();

            CreateFile.Writefile("Log_DarDeAlta_1", FileExtention.txt, DataText);

            TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
            LogString += "Dar de Alta Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
            LogString += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
            WinLog.WriteEvent(LogString, EntryType.Information, 1);
            return true;
    }

    public void EnrollUsers() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("InscribirUsuarios()"))
            return;
        ArrayList<Surver_Asignacion_Cursos> CurAs = SurverConn.GetCourseInscriptions();
        String ultimoCurso = "";
        String LogMsg = "Evento Inscribir usuarios iniciado el:" + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine();
        String ErrnoMsg = "";
        Courses CursoMoodle = null;
        if (CurAs != null && !CurAs.isEmpty())
            for (Surver_Asignacion_Cursos CurA : CurAs) {
                if (!ultimoCurso.equals(CurA.getCurso())) {
                    SurverConn.ActualizarCursoSurv_ischanged(CurA.getCurso());
                    CursoMoodle = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), CurA.getCurso());
                    if (CursoMoodle == null) {
                        ErrnoMsg += "El curso " +  CurA.getCurso() + " No existe/Está Inactivo en moodle " + TextManagement.NewLine();
                        continue;
                    }
                    ultimoCurso = CurA.getCurso();
                    UsuarioGenerico usuarioGenerico = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), CurA.getRFC());
                    if (usuarioGenerico == null) {
                        UsuarioSurver Us = this.SurverConn.GetUsuarioporRFC(CurA.getRFC());
                        if (Us != null)
                            ErrnoMsg += "El usuario con RFC: " + CurA.getRFC() + "No fue inscrito al curso: " + TextManagement.NewLine() + CurA.getCurso() + "Ya que no existe en la plataforma " + Information.Plataforma + TextManagement.NewLine();
                        continue;
                    }
                    if (!isEnrolled2Course(usuarioGenerico, CursoMoodle) &&
                            MoodleWerbServCreator.asignarUsuarioCurso(this.Conf
                                    .getCLI_WEBSERVICE_ROOT(), this.Conf
                                    .getCLI_WEBSERVICE_TOKEN(), usuarioGenerico
                                    .getID(), CursoMoodle
                                    .getID(), "5")) {
                        UsuarioSurver Us = this.SurverConn.GetUsuarioporRFC(CurA.getRFC());
                        if (Us != null)
                            ErrnoMsg += "El usuario: " + Us.getUsuario() + " - " + Us.getRFC() + "Ha sido inscrito al curso: " + CurA.getCurso() + TextManagement.NewLine();
                    }
                    continue;
                }
                UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), CurA.getRFC());
                if (UsuarioGen == null) {
                    UsuarioSurver Us = this.SurverConn.GetUsuarioporRFC(CurA.getRFC());
                    if (Us != null)
                        ErrnoMsg += "El usuario: " + Us.getUsuario() + " No fue inscrito al curso: " + CurA.getCurso() + "Ya que no existe en la plataforma en linea Moodle" + Information.Plataforma + TextManagement.NewLine();
                    continue;
                }
                if (!isEnrolled2Course(UsuarioGen, CursoMoodle) &&
                        MoodleWerbServCreator.asignarUsuarioCurso(this.Conf
                                .getCLI_WEBSERVICE_ROOT(), this.Conf
                                .getCLI_WEBSERVICE_TOKEN(), UsuarioGen
                                .getID(), CursoMoodle
                                .getID(), "5")) {
                    UsuarioSurver Us = this.SurverConn.GetUsuarioporRFC(CurA.getRFC());
                    if (Us != null)
                        ErrnoMsg += "El usuario: " + Us.getUsuario() + "Ha sido inscrito al curso: " + TextManagement.NewLine() + CurA.getCurso() + TextManagement.NewLine() + TextManagement.Separator();
                }
            }
        
        //PROCESO DE ACTUALIZAR EL CAMPO IS CHANGED 
        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;

        ErrnoMsg += TextManagement.Separator() + TextManagement.NewLine(4);

        CreateFile.Writefile("Log_Inscribir_Usuarios_2", FileExtention.txt, ErrnoMsg);

        LogMsg += "Evento Inscribir usuarios Terminado el :" + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
        LogMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(LogMsg, EntryType.Information, 2);
    }

    public void UpdateUserInfo_RFC() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("ActualizarInformacionUsuario_RFC()")) {
            return;
        }
        ArrayList<UsuarioGenerico> Usuarios = MoodleWerbServCreator.AllUsers(Conf.getCLI_WEBSERVICE_ROOT(), Conf.getCLI_WEBSERVICE_TOKEN());
        UsuarioSurver Usuario_Surver;
        String Exportmsg = "Evento Actualizar informacion de usuario por RFC Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
        String LogString = "";
        if (Usuarios != null)
            for (UsuarioGenerico Usuario : Usuarios) {
                Usuario_Surver = this.SurverConn.GetUsuarioporRFC(Usuario.getIdNumber());
                if (Usuario_Surver != null) {
                    if (Usuario_Surver.getId_empleado() != -1) {
                        if (MoodleWerbServCreator.ModificarUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), Usuario.getID(), Usuario_Surver))
                            LogString += "Nuevos datos de usuario " + Usuario.getUserName() + ": " + Usuario_Surver.getUsuario() + ", " + Usuario_Surver.GetNombreCompleto() + ", " + Usuario_Surver.getEmpresa() + ", " + Usuario_Surver.getSegmento() + ", " + Usuario_Surver.getRFC() + TextManagement.NewLine();
                        continue;
                    }
                    LogString += "El usuario" + Usuario.getUserName() + " RFC: " + Usuario.getUserName() + " No existe en " + Usuario.getIdNumber() + Information.Fuente + TextManagement.NewLine();
                }
            }
        CreateFile.Writefile("Log_Actualizar_usuario_por_RFC_3", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        Exportmsg += TextManagement.Separator() + TextManagement.NewLine(4);
        Exportmsg += "Actualizar usuario por RFC Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        Exportmsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(Exportmsg, EntryType.Information, 3);
    }

    public void UpdateUserInfo_UserName() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("ActualizarInformacionUsuario_UserName()")) {
            return;
        }
        ArrayList<UsuarioGenerico> Usuarios = MoodleWerbServCreator.AllUsers(Conf.getCLI_WEBSERVICE_ROOT(), Conf.getCLI_WEBSERVICE_TOKEN());
        String exportmsg = "Evento Actualizar informacion de usuario por Username Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
        String LogString = "";
        if (Usuarios != null)
            for (UsuarioGenerico Usuario : Usuarios) {
                UsuarioSurver Usuario_Surver = this.SurverConn.GetUsuarioporUsername(Usuario.getUserName());
                if (Usuario_Surver != null) {
                    if (Usuario_Surver.getId_empleado() != -1) {
                        if (MoodleWerbServCreator.ModificarUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), Usuario.getID(), Usuario_Surver))
                            LogString += "Nuevos datos de usuario " + Usuario.getUserName() + ": " + Usuario_Surver.getUsuario() + ", " + Usuario_Surver.GetNombreCompleto() + ", " + Usuario_Surver.getEmpresa() + ", " + Usuario_Surver.getSegmento() +", "+ Usuario_Surver.getRFC() + TextManagement.NewLine();
                        continue;
                    }
                    LogString += "El usuario " + Usuario.getUserName() + " RFC: " + Usuario.getUserName() + " No existe en " + Information.Fuente  + TextManagement.NewLine();
                }
            }
        CreateFile.Writefile("Log_Actualizar_usuario_por_Username_4", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportmsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportmsg += "Actualizar usuario por Username Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportmsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportmsg, EntryType.Information, 4);
    }

    public void SyncCourses_Moodle2Surver() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("SyncCourses_Moodle2Surver()")) {
            return;
        }
        String Exportmsg = "Evento Sincronizar Cursos " + Information.Plataforma + " -> " + Information.Fuente + " Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        Surver_Curso Curso_Surver = new Surver_Curso();
        ArrayList<Moodle_Curso> Cursos_Moodle = this.MoodleConn.getNotSincronizedMoodleCourses();
        if (Cursos_Moodle != null)
            for (Moodle_Curso curso : Cursos_Moodle) {
                ArrayList<Surver_Curso> Cursos_Surver = this.SurverConn.getCoursesByShortname(curso.getShortname());
                if (Cursos_Surver == null) {
                    Curso_Surver.setFromMoodleCourse(curso);
                    if (this.SurverConn.GuardarCurso(true, Curso_Surver)) {
                        LogString += "Nuevo Curso Registrado en: " + Information.Fuente + " -> " + Information.Fuente + Curso_Surver.getNombre_Corto() + TextManagement.NewLine();
                        if (this.MoodleConn.GuardarSincronizado(curso.getId()))
                            LogString += "Curso en Moodle Actualizado: " + curso.getShortname() + TextManagement.NewLine(2);
                    }
                    continue;
                }
                Curso_Surver.setFromMoodleCourse(curso);
                if (this.SurverConn.GuardarCurso(false, Curso_Surver))
                    LogString += "Actualizacion de informacion de curso en " + Information.Fuente + Curso_Surver.getNombre_Corto() + TextManagement.NewLine(2);
            }
        CreateFile.Writefile("Log_Sincronizar_Cursos_5", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        Exportmsg += TextManagement.Separator() + TextManagement.NewLine(4);
        Exportmsg += "Evento Sincronizar Cursos " + Information.Plataforma + " -> " + Information.Fuente + " Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        Exportmsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(Exportmsg, EntryType.Information, 5);
    }

    public void SyncCourses_Surver2Moodle() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("SyncCourses_Surver2Moodle()"))
            return;
        String exportmsg = "Evento Sincronizar Cursos " + Information.Fuente + " -> " + Information.Plataforma + " Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
        String LogString = "";
        ArrayList<Surver_Curso> Surver_Cursos = this.SurverConn.getCourses();
        if (Surver_Cursos != null)
            for (Surver_Curso surver_curso : Surver_Cursos) {
                Courses CursoMood = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), surver_curso.getNombre_Corto());
                if (CursoMood == null) {
                    Response Res = MoodleWerbServCreator.CreateGenericCourse(this.Conf.getCLI_WEBSERVICE_TOKEN(), this.Conf.getCLI_WEBSERVICE_ROOT(), surver_curso);
                    if (Res != null) {
                        LogString += "Nuevo curso registrado en: " + Information.Plataforma + " -> " + surver_curso.getNombre_Corto() + TextManagement.NewLine(2);
                        continue;
                    }
                    LogString += "No se Logró registrar el curso en " + Information.Plataforma + " Informacion de " + Information.Fuente + " -> " + surver_curso.getNombre_Corto() + TextManagement.NewLine(2);
                    continue;
                }
                Moodle_Curso Mod = new Moodle_Curso();
                Mod.setFromGenericCourse(CursoMood);
                if (MoodleWerbServCreator.UpdateCourse(this.Conf.getCLI_WEBSERVICE_TOKEN(), this.Conf.getCLI_WEBSERVICE_ROOT(), Mod, surver_curso)) {
                    LogString += "Curso de " + Information.Plataforma + " actualizado con los datos: " + surver_curso.getNombre_Corto() + TextManagement.NewLine(2);
                    continue;
                }
                LogString += "Curso de " + Information.Plataforma + " no se logró actualizar con datos: " + surver_curso.getNombre_Corto() + TextManagement.NewLine(2);
            }

        CreateFile.Writefile("Log_Sincronizar_Cursos_6", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportmsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportmsg += "Sincronizar Cursos " + Information.Fuente + " -> " + Information.Plataforma + " Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportmsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportmsg, EntryType.Information, 6);
    }

    public void CRONTasks() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("CRONTasks()")) {
            return;
        }
        String LogString = "Evento Tareas de CRON Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        LogString += MoodleWerbServCreator.runtasks(Conf.getCLI_CRON_PATH(), Conf.getCLI_CRON_KEY()) + TextManagement.NewLine(4) + TextManagement.Separator() + TextManagement.NewLine();

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        LogString += TextManagement.Separator() + TextManagement.NewLine(4);
        LogString += "Evento Tareas de CRON Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        LogString += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(LogString, EntryType.Information, 7);
    }

    public void SuspenderUsuarios() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("SuspenderUsuarios")) {
            return;
        }
        String ExportMsg = "Evento Suspender Usuarios Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
        String LogString = "";
        ArrayList<UsuarioSurver> Users2Suspend = this.SurverConn.GetUninscriptions();
        if (Users2Suspend != null)
            for (UsuarioSurver us : Users2Suspend) {
                UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                if (UsuarioGen != null) {
                    if (MoodleWerbServCreator.SuspenderUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), UsuarioGen.getID())) {
                        LogString += "El usuario " + UsuarioGen.getUserName() + " fue suspendido en " + Information.Plataforma + TextManagement.NewLine();
                        continue;
                    }
                    LogString += "Error El Usuario " + UsuarioGen.getUserName() + " __no__ fue suspendido en " + Information.Plataforma + TextManagement.NewLine();
                    continue;
                }
                UsuarioSurver usuarioSurver = this.SurverConn.GetUsuarioporRFC(us.getRFC());
                if (usuarioSurver == null) {
                    LogString += "El usuario no existe en " + Information.Plataforma + " ->" + UsuarioGen.getUserName() + TextManagement.NewLine();
                    continue;
                }
                LogString += "El RFC: " + us.getRFC() + " No existe en " + Information.Fuente + TextManagement.NewLine(2);
            }
        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        CreateFile.Writefile("Log_Suspender_Usuarios_8", FileExtention.txt, LogString);

        ExportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        ExportMsg += "Evento Suspender Usuarios Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        ExportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(ExportMsg, EntryType.Information, 8);
    }

    public void reactivateUsers() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("reactivateUsers()"))
            return;
        String exportMsg = "Evento Reactivado Usuarios Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        ArrayList<UsuarioSurver> Users2Suspend = this.SurverConn.GetReactivations();
        if (Users2Suspend != null)
            for (UsuarioSurver us : Users2Suspend) {
                UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                if (UsuarioGen != null) {
                    if (MoodleWerbServCreator.ActivarUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), UsuarioGen.getID())) {
                        LogString += "El usuario " + UsuarioGen.getUserName() + " fue Reactivado en " + Information.Plataforma + TextManagement.NewLine();
                        continue;
                    }
                    LogString += "Error El Usuario " + UsuarioGen.getUserName() + " __no__ fue Reactivado en " + Information.Plataforma + TextManagement.NewLine();
                    continue;
                }
                UsuarioSurver usuarioSurver = this.SurverConn.GetUsuarioporRFC(us.getRFC());
                if (usuarioSurver == null) {
                    LogString += "El usuario no existe en " + Information.Plataforma + " ->" + usuarioSurver.toStringOneLine() + TextManagement.NewLine(2);
                    continue;
                }
                LogString += "El RFC: " + us.getRFC() + " No existe en " + Information.Fuente + TextManagement.NewLine(2);
            }
        CreateFile.Writefile("Log_Reactivado_Usuarios_9", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "Evento Reactivado Usuarios Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 9);
    }

    public void AudPt1_UnEnrollments011() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AudPt1_UnEnrollments011()"))
            return;
        String exportMsg = "Evento AuditoriaParte1 DesInscribir usuarios Inactivos Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        ArrayList<Surver_Asignacion_Cursos> Users2Suspend = this.SurverConn.GetCourseUninscriptions_Empl0_Cur1_Per1();
        if (Users2Suspend != null)
            for (Surver_Asignacion_Cursos us : Users2Suspend) {
                UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                if (UsuarioGen != null) {
                    Courses Curs = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getCurso());
                    if (Curs != null) {
                        UsuarioSurver Usuario_Surver = this.SurverConn.GetUsuarioporRFC(us.getRFC());
                        if (Usuario_Surver != null) {
                            if (isEnrolled2Course(UsuarioGen, Curs)) {
                                if (MoodleWerbServCreator.bajaCursoUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), UsuarioGen.getID(), Curs.getID())) {
                                    LogString += "El Usuario " + Usuario_Surver.getUsuario() + " Fue suspendido del curso: " + Curs.getNombreCompleto() + " (" + Curs.getNombreCorto() + ") ";
                                    continue;
                                }
                                LogString += "Error; el Usuario " + Usuario_Surver.getUsuario() + "(" + Usuario_Surver.getRFC() + ")" + " no fue suspendido del curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                                continue;
                            }
                            LogString += "El usuario " + UsuarioGen.getUserName() + "(" + UsuarioGen.getIdNumber() + ")" + " No se encuentra en el curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                        }
                    }
                }
            }
        CreateFile.Writefile("Log_AuditoriaParte1_90", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "Evento AuditoriaParte1 DesInscribir usuarios Inactivos  Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 90);
    }

    public void AudPt2_UnEnrollments101() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AudPt2_UnEnrollments101()"))
            return;
        String exportMsg = "Evento AuditoriaParte2 DesInscribir usuarios Inactivos Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        ArrayList<Surver_Asignacion_Cursos> Users2Suspend = this.SurverConn.GetCourseUninscriptions_Empl1_Cur0_Per1();
        if (Users2Suspend != null)
            for (Surver_Asignacion_Cursos us : Users2Suspend) {
                Courses Curs = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getCurso());
                if (Curs != null) {
                    ArrayList<UsuarioGenerico> EnrrolledUsers = MoodleWerbServCreator.getUsersFromCourse(Conf.getCLI_WEBSERVICE_ROOT(), Conf.getCLI_WEBSERVICE_TOKEN(), Curs.getID());
                    for (UsuarioGenerico User : EnrrolledUsers){
                        MoodleWerbServCreator.bajaCursoUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(),this.Conf.getCLI_WEBSERVICE_TOKEN(),User.getID(),Curs.getID());
                    }
                    LogString += ("Los Usuarios del curso (" + Curs.getNombreCorto() + ") "+ Curs.getNombreMostrado() + " fueron Desinscritos");
                }
            }
        CreateFile.Writefile("Log_AuditoriaParte2_91", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "Evento AuditoriaParte2 DesInscribir usuarios Inactivos  Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 91);
    }

    public void AudPt3_UnEnrollments100() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AudPt3_UnEnrollments100()"))
            return;
        String exportMsg = "Evento AuditoriaParte3 DesInscribir usuarios Inactivos Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
        String LogString = "";
        ArrayList<Surver_Asignacion_Cursos> Users2Suspend = this.SurverConn.GetCourseUninscriptions_Empl1_Cur0_Per0();
        if (Users2Suspend != null)
            for (Surver_Asignacion_Cursos us : Users2Suspend) {
                UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                if (UsuarioGen != null) {
                    Courses Curs = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getCurso());
                    if (Curs != null) {
                        UsuarioSurver Usuario_Surver = this.SurverConn.GetUsuarioporRFC(us.getRFC());
                        if (Usuario_Surver != null) {
                            if (isEnrolled2Course(UsuarioGen, Curs)) {
                                if (MoodleWerbServCreator.bajaCursoUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), UsuarioGen.getID(), Curs.getID())) {
                                    LogString += "El Usuario " + Usuario_Surver.getUsuario() + " Fue suspendido del curso: " + Curs.getNombreCompleto() + " (" + Curs.getNombreCorto() + ") ";
                                    continue;
                                }
                                LogString += "Error; el Usuario " + Usuario_Surver.getUsuario() + " no fue suspendido del curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                                continue;
                            }
                            LogString += "El usuario " + UsuarioGen.getUserName() + " No se encuentra en el curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                        }
                    }
                }
            }
        CreateFile.Writefile("Log_AuditoriaParte3_92", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "Evento AuditoriaParte3 DesInscribir usuarios Inactivos  Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 92);
    }

    public void AudPt4_UnEnrollments000() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AudPt4_UnEnrollments000()"))
            return;
        String exportMsg = "Evento AuditoriaParte4 DesInscribir usuarios Inactivos Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        ArrayList<Surver_Asignacion_Cursos> Users2Suspend = this.SurverConn.GetCourseUninscriptions_Empl0_Cur0_Per0();
        if (Users2Suspend != null)
            for (Surver_Asignacion_Cursos us : Users2Suspend) {
                UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                if (UsuarioGen != null) {
                    Courses Curs = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getCurso());
                    if (Curs != null) {
                        UsuarioSurver Usuario_Surver = this.SurverConn.GetUsuarioporRFC(us.getRFC());
                        if (Usuario_Surver != null) {
                            if (isEnrolled2Course(UsuarioGen, Curs)) {
                                if (MoodleWerbServCreator.bajaCursoUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), UsuarioGen.getID(), Curs.getID())) {
                                    LogString += "El Usuario " + Usuario_Surver.getUsuario() + " Fue suspendido del curso: " + Curs.getNombreCompleto() + " (" + Curs.getNombreCorto() + ") ";
                                    continue;
                                }
                                LogString += "Error; el Usuario " + Usuario_Surver.getUsuario() + " no fue suspendido del curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                                continue;
                            }
                            LogString += "El usuario " + UsuarioGen.getUserName() + " No se encuentra en el curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                        }
                    }
                }
            }
        CreateFile.Writefile("Log_AuditoriaParte4_93", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "AuditoriaParte4 DesInscribir usuarios Inactivos  Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 93);
    }

    public void AudPt5_UnEnrollments110() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AudPt5_UnEnrollments110()"))
            return;
        String exportMsg = "Evento AuditoriaParte4 DesInscribir usuarios Inactivos Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        ArrayList<Surver_Asignacion_Cursos> Users2Suspend = this.SurverConn.GetCourseUninscriptions_Empl1_Cur1_Per0();
        if (Users2Suspend != null)
            for (Surver_Asignacion_Cursos us : Users2Suspend) {
                UsuarioGenerico UsuarioGen = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                if (UsuarioGen != null) {
                    Courses Curs = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getCurso());
                    if (Curs != null) {
                        UsuarioSurver Usuario_Surver = this.SurverConn.GetUsuarioporRFC(us.getRFC());
                        if (Usuario_Surver != null) {
                            if (isEnrolled2Course(UsuarioGen, Curs)) {
                                if (MoodleWerbServCreator.bajaCursoUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), UsuarioGen.getID(), Curs.getID())) {
                                    LogString += "El Usuario " + Usuario_Surver.getUsuario() + " Fue suspendido del curso: " + Curs.getNombreCompleto() + " (" + Curs.getNombreCorto() + ") ";
                                    continue;
                                }
                                LogString += "Error; el Usuario " + Usuario_Surver.getUsuario() + " no fue suspendido del curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                                continue;
                            }
                            LogString += "El usuario " + UsuarioGen.getUserName() + " No se encuentra en el curso: " + Curs.getNombreCorto() + TextManagement.NewLine(2);
                        }
                    }
                }
            }
        CreateFile.Writefile("Log_AuditoriaParte5_94", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "AuditoriaParte5 DesInscribir usuarios Inactivos  Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 94);
    }

    public void AudPt5_enrollByChange(ArrayList<Surver_Control_Puesto> Controles) {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AudPt5_enrollByChange()"))
            return;
        String exportMsg = "Evento AudPt5_enrollByChange Inscribir a cursos faltantes por cambio de control de puesto Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        if (Controles != null)
            for (Surver_Control_Puesto Control : Controles) {
                Surver_Curso Curso = new Surver_Curso();
                ArrayList<Surver_Curso> Cursos = this.SurverConn.getCoursesDiferences(Control.getNuevo_perfil(), Control.getViejo_perfil());
                if (Cursos != null)
                    for (Surver_Curso Curso1 : Cursos) {
                        Courses CurMood = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), Curso1.getNombre_Corto());
                        if (CurMood != null) {
                            UsuarioSurver us = this.SurverConn.GetUsuarioporIdSurver(Control.getId_empleado());
                            if (us != null) {
                                UsuarioGenerico usMood = null;
                                usMood = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                                if (usMood != null) {
                                    if (!isEnrolled2Course(usMood, CurMood)) {
                                        if (MoodleWerbServCreator.asignarUsuarioCurso(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), usMood.getID(), CurMood.getID(), "5")) {
                                            LogString += "El Usuario " + us.toStringOneLine() + " fue dado de alta en el curso: " + CurMood.getNombreCorto() + TextManagement.NewLine(2);
                                            continue;
                                        }
                                        LogString += "Error... el Usuario " + us.toStringOneLine() + " no fue dado de alta en el curso: " + CurMood.getNombreCorto() + TextManagement.NewLine(2);
                                        continue;
                                    }
                                    LogString += "El usuario " + us.toStringOneLine() + " Ya se encuentra en el curso: " + CurMood.getNombreCorto() + TextManagement.NewLine(2);
                                }
                            }
                            continue;
                        }
                        LogString += "El curso: " + Curso1.getNombre_Corto() + " No existe en " + Information.Plataforma + TextManagement.NewLine(2);
                    }
            }
        CreateFile.Writefile("Log_AuditoriaParte5_94", FileExtention.txt, LogString);


        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "Evento AuditoriaParte5 Inscribir a cursos faltantes por cambio de control de puesto Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 94);
    }

    public void AudPt6_UnenrollByChange(ArrayList<Surver_Control_Puesto> Controles) {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AudPt6_UnenrollByChange()"))
            return;
        String exportMsg = "Evento AudPt6_UnenrollByChange Inscribir a cursos faltantes por cambio de control de puesto Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        if (Controles != null)
            for (Surver_Control_Puesto Control : Controles) {
                Surver_Curso Curso = new Surver_Curso();
                ArrayList<Surver_Curso> Cursos = this.SurverConn.getCoursesDiferences(Control.getViejo_perfil(), Control.getNuevo_perfil());
                if (Cursos != null)
                    for (Surver_Curso Curso1 : Cursos) {
                        Courses CurMood = MoodleWerbServCreator.searchCourseShortName(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), Curso1.getNombre_Corto());
                        if (CurMood != null) {
                            UsuarioSurver us = this.SurverConn.GetUsuarioporIdSurver(Control.getId_empleado());
                            if (us != null) {
                                UsuarioGenerico usMood = MoodleWerbServCreator.buscarUsuario_RFC(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), us.getRFC());
                                if (usMood != null) {
                                    if (isEnrolled2Course(usMood, CurMood)) {
                                        if (MoodleWerbServCreator.bajaCursoUsuario(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN(), usMood.getID(), CurMood.getID())) {
                                            LogString += "El Usuario " + us.toStringOneLine() + " fue dado de baja del curso: " + CurMood.getNombreCorto() + TextManagement.NewLine(2);
                                            continue;
                                        }
                                        LogString += "Error... el Usuario " + us.toStringOneLine() + " no fue dado de baja en el curso: " + CurMood.getNombreCorto() + TextManagement.NewLine(2);
                                        continue;
                                    }
                                    LogString += "El usuario " + us.toStringOneLine() + "no existe en el curso: " + CurMood.getNombreCorto() + TextManagement.NewLine(2);
                                }
                            }
                            continue;
                        }
                        LogString += "El curso: " + Curso1.getNombre_Corto() + " No existe en " + Information.Plataforma + TextManagement.NewLine(2);
                    }
            }
        CreateFile.Writefile("Log_AuditoriaParte6_95", FileExtention.txt, LogString);

        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        exportMsg += TextManagement.Separator() + TextManagement.NewLine(4);
        exportMsg += "Evento AuditoriaParte6 Des-Inscribir a cursos Sobrantes por cambio de control de puesto Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(exportMsg, EntryType.Information, 95);
    }

    public void AuditoriasControlPuesto() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("AuditoriasControlPuesto()"))
            return;
        String LogString = "Evento Auditorias Control Puesto Iniciado el: " + (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss")).format(new Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();
        ArrayList<Surver_Control_Puesto> Controles = this.SurverConn.getContolCambios_SinSync();
        AudPt5_enrollByChange(Controles);
        AudPt6_UnenrollByChange(Controles);
        if (Controles != null)
            for (Surver_Control_Puesto Controle : Controles) {
                if (!this.SurverConn.SetContolCambiosInactivo(Controle))
                    LogString += "Error al inactivar Control_puesto " + LogString + Controle.toString();
            }
        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000;
        LogString += TextManagement.Separator() + TextManagement.NewLine(4);
        LogString += "Evento Auditorias Control Puesto Terminado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine()
                + TextManagement.Separator() + TextManagement.NewLine();
        LogString += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        WinLog.WriteEvent(LogString, EntryType.Information, 95);
    }

    private boolean isEnrolled2Course(UsuarioGenerico Usuario, Courses Curso) {
        try {
            ArrayList<UsuarioGenerico> EnrrolledUsers = MoodleWerbServCreator.getUsersFromCourse(Conf.getCLI_WEBSERVICE_ROOT(), Conf.getCLI_WEBSERVICE_TOKEN(), Curso.getID());
            if (EnrrolledUsers != null) {
                if (EnrrolledUsers.stream().anyMatch(EnrrolledUser -> (EnrrolledUser.getIdNumber().equals(Usuario.getIdNumber())))) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("a");
            return false;
        }

        return false;
    }

    public void SyncEmployees2Surver() {
        long TimeTranscurred = System.nanoTime();
        if (!CheckConnection("SyncEmployees2Surver()"))
            return;
        String exportMsg = "Evento AudPt6_UnenrollByChange Inscribir a cursos faltantes por cambio de control de puesto Iniciado el: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new java.util.Date()) + TextManagement.NewLine() + TextManagement.Separator() + TextManagement.NewLine();

        String LogString = "";
        ArrayList<UsuarioGenerico> UsuariosMoodle = MoodleWerbServCreator.AllUsers(this.Conf.getCLI_WEBSERVICE_ROOT(), this.Conf.getCLI_WEBSERVICE_TOKEN());
        if (UsuariosMoodle != null) {
            UsuariosMoodle = LimpiarUsuarios(UsuariosMoodle);
            for (UsuarioGenerico usuarioGenerico : UsuariosMoodle) {
                LogString += "Procesando: " + usuarioGenerico.getIdNumber() + " - " + usuarioGenerico.getUserName();
                UsuarioSurver us = this.SurverConn.GetUsuarioporRFC(usuarioGenerico.getIdNumber());
                if (us != null) {
                    this.SurverConn.setSync(us.getId_empleado());
                    continue;
                }
                LogString += " No se obtuvo el usuario en Surver RFC: " + usuarioGenerico.getIdNumber() + " Conexion a Surver: " + this.SurverConn.getEstadoConexion();
                us = this.SurverConn.GetUsuarioporUsername(usuarioGenerico.getUserName());
                if (us != null) {
                    this.SurverConn.setSync(us.getId_empleado());
                    continue;
                }
                LogString += " No se obtuvo el usuario en Surver -> " + usuarioGenerico.getUserName();
            }
        } else {
            LogString += "No se obtuvieron usuarios desde moodle";
        }
        TimeTranscurred = (System.nanoTime() - TimeTranscurred) / 1000000000L;
        CreateFile.Writefile("Log_SyncEmployees2Surver_97", FileExtention.txt, LogString);

        exportMsg += TextManagement.Separator();
        exportMsg += "Evento Auditoria Set Sinconizado a Moodle Terminado el: " + (new SimpleDateFormat("dd/MM/yyyy hh:mm:ss")).format(new Date()) + TextManagement.NewLine() + TextManagement.Separator();
        exportMsg += "Tiempo Transcurrido de sistema: " + TextManagement.TransformSecondsToTime(TimeTranscurred);
        this.WinLog.WriteEvent(exportMsg, EntryType.Information, 97);
    }

    private ArrayList<UsuarioGenerico> LimpiarUsuarios(ArrayList<UsuarioGenerico> UsuariosMoodle) {
        ArrayList<UsuarioGenerico> UsuariosErroneos = new ArrayList<>();
        ArrayList<UsuarioGenerico> UsuariosCompletos = new ArrayList<>();
        for (UsuarioGenerico usuarioGenerico : UsuariosMoodle) {
            if (!usuarioGenerico.getIdNumber().equals("") || usuarioGenerico.getIdNumber() != null) {
                if (usuarioGenerico.getIdNumber().length() == 12 || usuarioGenerico.getIdNumber().length() == 13) {
                    UsuariosCompletos.add(usuarioGenerico);
                } else {
                    UsuariosErroneos.add(usuarioGenerico);
                }
            } else {
                UsuariosErroneos.add(usuarioGenerico);
            }
        }
        if (!UsuariosErroneos.isEmpty()) {
            String Data = new UsuarioGenerico().DataStringHeaderCSV() + TextManagement.NewLine();
            Data = UsuariosErroneos.stream().map(Usuario -> Usuario.DataStirngCSV() + TextManagement.NewLine()).reduce(Data, String::concat);
            CreateFile.Writefile("Usuarios erroneros desde Moodle2Surver RFC o Usuario", FileExtention.csv, Data);
        }

        return UsuariosCompletos.isEmpty() ? null : UsuariosCompletos;
    }
}
