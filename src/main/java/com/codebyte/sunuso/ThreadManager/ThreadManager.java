package com.codebyte.sunuso.ThreadManager;

import com.codebyte.sunuso.Resources.Configuration;
import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.Resources.TextManagement;
import com.codebyte.sunuso.Tokenizer.Encoder;
import com.codebyte.sunuso.Tokenizer.ExportXML;
import com.codebyte.sunuso.Tokenizer.ObjectAttrib;
import com.codebyte.sunuso.Tokenizer.OsRecognizer;
import com.codebyte.sunuso.Tokenizer.Tokenizer;
import com.codebyte.sunuso.WindowsEventLogWritter.EntryType;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadManager extends Thread {
    private static WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);

    private static final File Arch = new File(OsRecognizer.getrunningpath() + "\\Config.txt");

    private static final File InstalationFullPath = new File(OsRecognizer.getrunningpath() + "\\Resources\\Config");

    private static boolean ArchivoNuevo = false;

    private static boolean LaKeyEsNula = false;

    private static ArrayList<String[]> ParametrosHilos = (ArrayList)new ArrayList<>();

    public static volatile String LastOption = "$";

    public static boolean Funcionando = false;

    public void run() {
        if (CrearArchivos());
    }

    private static boolean CrearArchivos() {
        String LogInicio = GetHeader(false);
        boolean creaciondearchivo = false;
        if (!InstalationFullPath.exists()) {
            if (InstalationFullPath.mkdirs()) {
                LogInicio = LogInicio + "Direccion de Archivos de configuracion" + LogInicio + TextManagement.NewLine() + InstalationFullPath.getAbsolutePath();
                if (!Arch.exists()) {
                    if (CrearEjemplo(Arch)) {
                        LogInicio = LogInicio + "Archivo de Ejemplo de configuracion creado en: " + LogInicio;
                        creaciondearchivo = true;
                        ArchivoNuevo = true;
                    } else {
                        LogInicio = LogInicio + "Error al crear Archivo de Ejemplo de configuracion en: " + LogInicio;
                    }
                } else {
                    LogInicio = LogInicio + "Archivo de configuracion encontrado en: " + LogInicio;
                    creaciondearchivo = true;
                }
            } else {
                LogInicio = LogInicio + "Error al crear direccion de instalacion";
            }
        } else if (!Arch.exists()) {
            if (CrearEjemplo(Arch)) {
                LogInicio = LogInicio + "Archivo de Ejemplo de configuracion creado en: " + LogInicio;
                creaciondearchivo = true;
                ArchivoNuevo = true;
            } else {
                LogInicio = LogInicio + "Error al crear Archivo de Ejemplo de configuracion en: " + LogInicio;
            }
        } else {
            LogInicio = LogInicio + "Archivo de configuracion encontrado en: " + LogInicio;
            creaciondearchivo = true;
        }
        if (creaciondearchivo) {
            WinLog.WriteEvent(LogInicio, EntryType.Information, 100);
        } else {
            WinLog.WriteEvent(LogInicio, EntryType.Warning, 100);
        }
        return creaciondearchivo;
    }

    public static String GetExampleConfig() {
        return "#\t\t\t(No borrar \"~\")\nKeyConexiones\t\t\t~C:\\Users\\Estad\\Documents\\SuNuSo Build\\Resources\\Config\\ConfigReal.txt\nConexiones\t\t\t~C:\\Users\\Estad\\Documents\\SuNuSo Build\\Resources\\Config\\ConfigReal.xml\nRutaWebservice\t\t\t~https://tics.refividrio.com.mx/rfv-cursos/webservice/rest/server.php\nToken\t\t\t\t~2d04c1cf788cf1e58e00e3b6c718f56c\nCRON\t\t\t\t~https://tics.refividrio.com.mx/rfv-cursos/admin/cron.php\nPCron\t\t\t\t~Dev_Moodle_20\n#EsPrimerUso\t\t\t~false\n#Cantidad_de_tiempo Demarca la cantidad de tiempo segTipo_Hora ( 15-2 = 15 Minutos)\n\n\n\n#Tipo_hora = -1=Deshabilitado | 0=Milisegundos | 1=Segundos | 2=Minutos | 3=Horas\n\n#Configuracide tiempos: \n\n#\t\t\t\t\t\t\t(No borrar \"-\")\n#\t\t\t\t\tCantidad_de_Tiempo\t\tTipo_Hora\nhilo_Alta\t\t\t~\t\t\t15\t-\t-1\nhilo_InscribirUsuarios\t\t~\t\t\t15 \t-\t-1\nhilo_Actualizar_RFC\t\t~\t\t\t15\t-\t-1\nhilo_Actualizar_Username\t~\t\t\t15\t-\t-1\nhilo_SyncMoodSurv\t\t~\t\t\t15\t-\t-1\nhilo_SyncSurvMood\t\t~\t\t\t15\t-\t-1\nhilo_CRON\t\t\t~\t\t\t5\t-\t-1\nhilo_Suspender\t\t\t~\t\t\t1\t-\t-1\nhilo_Reactivar\t\t\t~\t\t\t1\t-\t-1\nhilo_AuditoriasPt1\t\t~\t\t\t15\t-\t-1\nhilo_AuditoriasPt2\t\t~\t\t\t15\t-\t-1\nhilo_AuditoriasPt3\t\t~\t\t\t15\t-\t-1\nhilo_AuditoriasPt4\t\t~\t\t\t15\t-\t-1\nhilo_AuditoriasPt5\t\t~\t\t\t15\t-\t-1\nhilo_AuditoriasControlPuesto\t~\t\t\t1\t-\t-1\nhilo_SyncUsersMoodle2Surver\t~\t\t\t5\t-\t-1\n#hilo_[Nombre de hilo] ~t-Tt Si se ha de declarar otro hilo en sistema\n#Este es un comentario\n#Las lineas que inician con \"#\" son Comentarios\n#El archivo debe ser guardado con codificaciANSI";
    }

    private static String GetHeader(boolean EsCMD) {
        if (EsCMD) {
            String Tittle = Information.LogName + " " + Information.LogName + Information.ProgramName + Information.Version;
            Tittle = "-" + TextManagement.addRawTab() + Tittle + TextManagement.addRawTab() + "-";
            Tittle = TextManagement.Separator(Tittle.length()) + TextManagement.Separator(Tittle.length()) + TextManagement.NewLine() + Tittle + TextManagement.Separator(Tittle.length());
            return Tittle;
        }
        return
                TextManagement.NewTab(3) + "------------------------------------------------------------------" + TextManagement.NewTab(3) + TextManagement.NewLine() + "|" +
                        TextManagement.NewTab(3) + Information.LogName + " " + Information.ProgramName + Information.Version + "|" + Information.Acronimo + TextManagement.NewLine() + "------------------------------------------------------------------" +
                        TextManagement.NewTab(3);
    }

    private static boolean CrearEjemplo(File Arch) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(Arch, true));
            writer.write(GetExampleConfig());
            writer.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ThreadManager.class.getName()).log(Level.SEVERE, (String)null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadManager.class.getName()).log(Level.SEVERE, (String)null, ex);
            }
        }
        return false;
    }

    private static Configuration CargarConfiguracion() {
        String TextoLeido = "";
        String keygen = null;
        Configuration LaConfiguracion = new Configuration();
        if (!ArchivoNuevo) {
            TextoLeido = Tokenizer.getConfigText_fromfile(Arch);
            String[] Lines = TextoLeido.split(TextManagement.NewLine());
            String[][] Table = new String[Lines.length][2];
            String[] Plitted = Lines[0].split("~");
            int i;
            for (i = 0; i < Lines.length; i++) {
                Table[i][0] = Lines[i].split("~")[0];
                Table[i][1] = Lines[i].split("~")[1];
            }
            if (!esConfiguracionPorDefecto(Table))
                return null;
            for (i = 0; i < Table.length; i++) {
                switch (Table[i][0]) {
                    case "Conexiones":
                        LaConfiguracion = setDataBaseConfigfromXML(LaConfiguracion, new File(Table[i][1]), keygen);
                        break;
                    case "RutaWebservice":
                        LaConfiguracion.setCLI_WEBSERVICE_ROOT(Table[i][1]);
                        break;
                    case "Token":
                        LaConfiguracion.setCLI_WEBSERVICE_TOKEN(Table[i][1]);
                        break;
                    case "CRON":
                        LaConfiguracion.setCLI_CRON_PATH(Table[i][1]);
                        break;
                    case "PCron":
                        LaConfiguracion.setCLI_CRON_KEY(Table[i][1]);
                        break;
                    case "KeyConexiones":
                        keygen = Tokenizer.getConfigText_fromfile(new File(Table[i][1])).replace(TextManagement.NewLine(), "");
                        break;
                    default:
                        try {
                            if (isThread(Table[i][0])) {
                                if (!AddDecodedThread(Table[i][0], Table[i][1]))
                                    WinLog.WriteEvent("Parametro Erronero de declaracide hilo " + Lines[i] + "En la linea " + i + 1, EntryType.Error, 110);
                                break;
                            }
                            WinLog.WriteEvent("Error al leer Configuracion Linea: " + i + 1, EntryType.Error, 110);
                        } catch (ArrayIndexOutOfBoundsException E) {
                            WinLog.WriteEvent("Error al leer Configuracion en la linea " + i + 1, EntryType.Error, 110);
                        }
                        break;
                }
            }
            if (LaKeyEsNula && keygen != null && !keygen.trim().isEmpty())
                LaConfiguracion = setDataBaseConfigfromXML(LaConfiguracion, Arch, keygen);
        }
        return LaConfiguracion;
    }

    private static boolean esConfiguracionPorDefecto(String[][] Table) {
        for (int i = 0; i < Table.length; i++) {
            if (Table[i][1].contains("[") || Table[i][1].contains("]")) {
                WinLog.WriteEvent("Error al cargar configuracion en parametro: " + Table[i][0] + " ; Con valor:" + Table[i][1] + " En la linea: " + i + 1, EntryType.Error, 110);
                return false;
            }
        }
        return true;
    }

    private static Configuration setDataBaseConfigfromXML(Configuration LaConfiguracion, File conf_fileXML, String Keygen) {
        if (Keygen == null) {
            LaKeyEsNula = true;
            return LaConfiguracion;
        }
        LaKeyEsNula = false;
        ArrayList<ArrayList> arrs = ExportXML.ReadXMLFile(conf_fileXML);
        String Key = Keygen;
        ArrayList<ObjectAttrib> AD = Encoder.StartDecode(Key, arrs.get(0));
        ArrayList<ObjectAttrib> mdl = Encoder.StartDecode(Key, arrs.get(1));
        Configuration Conf = LaConfiguracion;
        for (ObjectAttrib objectAttrib : AD) {
            switch (objectAttrib.getTittle()) {
                case "DatabaseName":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_DB_NAME(objectAttrib.getValue());
                    continue;
                case "User":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_USER(objectAttrib.getValue());
                    continue;
                case "Password":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD(objectAttrib.getValue());
                    continue;
                case "Location":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_PATH(objectAttrib.getValue());
                    continue;
                case "Port":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_PORT(objectAttrib.getValue());
                    continue;
                case "Schema":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_SCHEMA(objectAttrib.getValue());
                    continue;
            }
            throw new AssertionError();
        }
        for (ObjectAttrib objectAttrib : mdl) {
            switch (objectAttrib.getTittle()) {
                case "DatabaseName":
                    Conf.setPostgreSQL_MOODLE_SERVER_DB_NAME(objectAttrib.getValue());
                    continue;
                case "User":
                    Conf.setPostgreSQL_MOODLE_SERVER_USER(objectAttrib.getValue());
                    continue;
                case "Password":
                    Conf.setPostgreSQL_MOODLE_SERVER_USER_PASSWORD(objectAttrib.getValue());
                    continue;
                case "Location":
                    Conf.setPostgreSQL_MOODLE_SERVER_PATH(objectAttrib.getValue());
                    continue;
                case "Port":
                    Conf.setPostgreSQL_MOODLE_SERVER_PORT(objectAttrib.getValue());
                    continue;
                case "Schema":
                    Conf.setPostgreSQL_MOODLE_SERVER_SCHEMA(objectAttrib.getValue());
                    continue;
            }
            throw new AssertionError();
        }
        return Conf;
    }

    private static boolean isThread(String Property) {
        return (Property.contains("hilo_") && !Property.startsWith("#"));
    }

    private static boolean AddDecodedThread(String Name, String Values) {
        String Accion = Name.replace("hilo_", "");
        String[] ParametrosTiempo = Values.split("-");
        try {
            ParametrosHilos.add(new String[] { Accion, ParametrosTiempo[0], ParametrosTiempo[1] });
            return true;
        } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            return false;
        }
    }
}
