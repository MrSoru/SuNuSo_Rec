package com.codebyte.sunuso;

import com.codebyte.sunuso.OutputFilesWritter.CreateFile;
import com.codebyte.sunuso.OutputFilesWritter.Resources.FileExtention;
import com.codebyte.sunuso.Resources.Configuration;
import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.Resources.TextManagement;
import com.codebyte.sunuso.ThreadManager.HiloProceso;
import com.codebyte.sunuso.ThreadManager.Procesos.Funciones;
import com.codebyte.sunuso.Tokenizer.Encoder;
import com.codebyte.sunuso.Tokenizer.ExportXML;
import com.codebyte.sunuso.Tokenizer.ObjectAttrib;
import com.codebyte.sunuso.Tokenizer.OsRecognizer;
import com.codebyte.sunuso.Tokenizer.Tokenizer;
import com.codebyte.sunuso.WindowsEventLogWritter.EntryType;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author Arturo Meza Garcia
 */
public class Cli {

    private static WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
    private static final File Arch = new File(OsRecognizer.getrunningpath() + "\\Config.txt");
    private static final File InstalationFullPath = new File(OsRecognizer.getrunningpath() + "\\Resources\\Config");
    private static boolean ArchivoNuevo = false;
    private static boolean LaKeyEsNula = false;
    private static HiloProceso[] Hilos;
    private static String[] paramshilo;
    private static boolean esPrimerUso;

    private static boolean runAsService = false;

    private static ArrayList<String[]> ParametrosHilos = new ArrayList<>();

    public synchronized static void main(String[] args) {
        if (args.length != 0) {
            switch (args[0]) {
                case "-Service":
                    runAsService = true;
                    System.out.println("Parametro Rcibido!");
                    break;
                case "help":
                case "?":
                case "-help":
                case "/help":
                case "-?":
                default:
                    System.out.println(Ayuda());
                    System.exit(1);
                    break;
            }
        }
        if (CrearArchivos()) {
            if (CargarConfiguracion() != null) {
                if (esPrimerUso) {
                    StartFirstSteps();
                }
                try {
                    StartMenu();
                } catch (IOException ex) {
                    WinLog.WriteEvent("Hubo un problema al leer datos de consola...", EntryType.Error, 0);
                }
            } else {
                WinLog.WriteEvent("La configuracion contiene un valor por defecto o no hay configuracion aplicable, cerrando...", EntryType.Error, 110);
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }

    private synchronized static void StartMenu() throws IOException {
        BufferedReader Read = new BufferedReader(new InputStreamReader(System.in));
        String ReadedLine = "";
        System.out.print(TextManagement.NewLine(2) + PrintMenu());
        Configuration Conf = CargarConfiguracion();
        CargarHilos();
        if (runAsService) {
            generarHilos(ParametrosHilos, Conf);
            IniciarHilos();
            System.out.println("programa iniciado como servicio...");
        }
        while (!(ReadedLine = Read.readLine()).equals("Si, Aqui hubo un bug, debería ser el argumento 0 pero no funciona originalmente sería 0 en vez de este texto taaan largo :D")) {
            switch (ReadedLine) {
                case "1":
                    if (Hilos == null) {
                        generarHilos(ParametrosHilos, Conf);
                        IniciarHilos();
                    } else {
                        apagarHilos();
                        generarHilos(ParametrosHilos, Conf);
                        IniciarHilos();
                    }
                    break;
                case "2":
                    if (Hilos != null) {
                        apagarHilos();
                    }
                    break;
                case "3":
                    MostrarHilosActivos();
                    break;
                case "4":
                    LimpiarConsola();
                    break;
                case "5":
                    Ayuda();
                    break;
                case "#":
                    apagarHilos();
                    Conf = CargarConfiguracion();
                    ParametrosHilos.clear();
                    CargarHilos();
                    generarHilos(ParametrosHilos, Conf);
                    System.out.println("Refrescado de configuración e hilos Completo");
                    break;
                case "0":
                    System.out.println("Saliendo de " + Information.ProgramName + " por favor, espere...");
                    apagarHilos();
                    System.exit(0);
                    break;
            }
            System.out.print(TextManagement.NewLine(2) + PrintMenu());
        }
    }

    private static String PrintMenu() {
        String Texto = GetHeader(true)
                + TextManagement.NewLine()
                + "Opciones:" + TextManagement.NewLine()
                + "1. Iniciar/Reiniciar" + TextManagement.NewLine()
                + "2. Detener" + TextManagement.NewLine()
                + "3. Mostrar Hilos Activos" + TextManagement.NewLine()
                + "4. Limpiar Consola" + TextManagement.NewLine()
                + "5. Informacion" + TextManagement.NewLine()
                + "#. Recargar Configuración" + TextManagement.NewLine()
                + "0. Salir del programa" + TextManagement.NewLine()
                + "-SuNuSo~ Esperando opción... " + TextManagement.NewLine();
        return Texto;
    }

    //<editor-fold defaultstate="collapsed" desc="Funciones">
    private synchronized static boolean CrearArchivos() {
        String LogInicio = GetHeader(false);
        boolean creaciondearchivo = false;
        if (!InstalationFullPath.exists()) {//si la ruta de archivos no existe
            if (InstalationFullPath.mkdirs()) {//si se puede crear la ruta
                LogInicio += "Direccion de Archivos de configuracion" + TextManagement.NewLine() + InstalationFullPath.getAbsolutePath() + TextManagement.NewLine();
                if (!Arch.exists()) {
                    if (CrearEjemplo(Arch)) {
                        LogInicio += "Archivo de Ejemplo de configuracion creado en: " + Arch.getAbsolutePath();
                        creaciondearchivo = true;
                        ArchivoNuevo = true;
                    } else {
                        LogInicio += "Error al crear Archivo de Ejemplo de configuracion en: " + Arch.getAbsolutePath();
                    }
                } else {
                    LogInicio += "Archivo de configuracion encontrado en: " + Arch.getAbsolutePath();
                    creaciondearchivo = true;
                }
            } else {
                LogInicio += "Error al crear direccion de instalacion";
            }
        } else {
            if (!Arch.exists()) {
                if (CrearEjemplo(Arch)) {
                    LogInicio += "Archivo de Ejemplo de configuracion creado en: " + Arch.getAbsolutePath();
                    creaciondearchivo = true;
                    ArchivoNuevo = true;
                } else {
                    LogInicio += "Error al crear Archivo de Ejemplo de configuracion en: " + Arch.getAbsolutePath();
                }
            } else {
                LogInicio += "Archivo de configuracion encontrado en: " + Arch.getAbsolutePath();
                creaciondearchivo = true;
            }
        }
        if (creaciondearchivo) {
            WinLog.WriteEvent(LogInicio, EntryType.Information, 100);
        } else {
            WinLog.WriteEvent(LogInicio, EntryType.Warning, 100);
        }
        return creaciondearchivo;
    }

    public synchronized static String GetExampleConfig() {
        return "#\t\t\t(No borrar \"~\")\n" +
                "KeyConexiones\t\t\t~[C:\\CodeByte\\SuNuSo\\Config\\PostProduccion.txt\n" +
                "Conexiones\t\t\t~{C:\\CodeByte\\SuNuSo\\Config\\PostProduccion.xml\n" +
                "RutaWebservice\t\t\t~[http://rfv-leaning/webservice/rest/server.php\n" +
                "Token\t\t\t\t~[ad1af163db751c09cf8fe37f5e73a552\n" +
                "CRON\t\t\t\t~[http://rfv-leaning/admin/cron.php\n" +
                "PCron\t\t\t\t~[Dev_Moodle_20\n" +
                "EsPrimerUso\t\t\t~[true\n" +
                "#Cantidad_de_tiempo Demarca la cantidad de tiempo según Tipo_Hora (15-2=15 Minutos)\n" +
                "\n" +
                "\n" +
                "\n" +
                "#Tipo_hora = -1=Deshabilitado | 0=Milisegundos | 1=Segundos | 2=Minutos | 3=Horas\n" +
                "\n" +
                "#Configuración de tiempos: \n" +
                "\n" +
                "#\t\t\t\t\t\t\t(No borrar \"-\")\n" +
                "#\t\t\t\t\tCantidad_de_Tiempo\t\tTipo_Hora\n" +
                "hilo_Alta\t\t\t~\t\t\t15\t-\t-1\n" +
                "hilo_InscribirUsuarios\t\t~\t\t\t17 \t-\t-1\n" +
                "hilo_Actualizar_RFC\t\t~\t\t\t0\t-\t-1\n" +
                "hilo_Actualizar_Username\t~\t\t\t1\t-\t-1\n" +
                "hilo_SyncMoodSurv\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_SyncSurvMood\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_CRON\t\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_Suspender\t\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_Reactivar\t\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_AuditoriasPt1\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_AuditoriasPt2\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_AuditoriasPt3\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_AuditoriasPt4\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_AuditoriasPt5\t\t~\t\t\t1\t-\t-1\n" +
                "hilo_AuditoriasControlPuesto\t~\t\t\t1\t-\t-1\n" +
                "#Hilo de alto riesgo [SyncUsersMoodle2Surver] Puede causar inconsistencia de Datos (Saca info de usuario desde moodle para insertarla en surver)\n" +
                "#No activar#hilo_SyncUsersMoodle2Surver\t~\t\t\t-1\t-\t-1\n" +
                "#hilo_[Nombre de hilo] ~t-Tt Si se ha de declarar otro hilo en sistema\n" +
                "#Este es un comentario\n" +
                "#Las lineas que inician con \"#\" son Comentarios\n" +
                "#El archivo debe ser guardado con codificación ANSI";
    }

    private synchronized static String GetHeader(boolean EsCMD) {
        if (EsCMD) {
            String Tittle = Information.LogName + " " + Information.ProgramName + Information.Version + " " + Information.Acronimo;
            Tittle = "-" + TextManagement.addRawTab() + Tittle + TextManagement.addRawTab() + "-";

            Tittle = TextManagement.Separator(Tittle.length()) + TextManagement.NewLine()
                    + Tittle + TextManagement.NewLine()
                    + TextManagement.Separator(Tittle.length()) + TextManagement.NewLine();
            return Tittle;
        }
        return ""
                + TextManagement.NewTab(3) + "------------------------------------------------------------------" + TextManagement.NewLine()
                + TextManagement.NewTab(3) + "|" + Information.LogName + " " + Information.ProgramName + " " + Information.Version + " " + Information.Acronimo + "|" + TextManagement.NewLine()
                + TextManagement.NewTab(3) + "------------------------------------------------------------------" + TextManagement.NewLine();
    }

    private synchronized static boolean CrearEjemplo(File Arch) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(Arch, true));
            writer.write(GetExampleConfig());
            writer.close();
            return true;
        } catch (IOException ex) {
            Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Cli.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    private synchronized static void CargarHilos() {
        String TextoLeido = "";
        if (!ArchivoNuevo) {
            TextoLeido = Tokenizer.getConfigText_fromfile(Arch);
            String[] Lines = TextoLeido.split(TextManagement.NewLine());
            String[][] Table = new String[Lines.length][2];
            for (int i = 0; i < Lines.length; i++) {
                Table[i][0] = Lines[i].split("~")[0];
                Table[i][1] = Lines[i].split("~")[1];
            }
            if (!esConfiguracionPorDefecto(Table)) {
                return;
            }
            for (int i = 0; i < Table.length; i++) {
                switch (Table[i][0]) {
                    default:
                        try {
                            if (isThread(Table[i][0])) {
                                if (!AddDecodedThread(Table[i][0], Table[i][1])) {
                                    WinLog.WriteEvent("Parametro Erronero de declaración de hilo " + Lines[i] + "En la linea " + (i + 1), EntryType.Error, 110);
                                }
                            }
                        } catch (ArrayIndexOutOfBoundsException E) {
                            WinLog.WriteEvent("Error al leer Configuracion de hilo en la linea " + (i + 1), EntryType.Error, 110);
                        }
                }
            }
        }
    }

    private synchronized static Configuration CargarConfiguracion() {
        String TextoLeido = "";
        String keygen = null;
        Configuration LaConfiguracion = new Configuration();
        String LogCarga ="";
        if (!ArchivoNuevo) {
            TextoLeido = Tokenizer.getConfigText_fromfile(Arch);
            String[] Lines = TextoLeido.split(TextManagement.NewLine());
            String[][] Table = new String[Lines.length][2];
            for (int i = 0; i < Lines.length; i++) {
                try{
                    Table[i][0] = Lines[i].split("~")[0];
                Table[i][1] = Lines[i].split("~")[1];
                }catch (IndexOutOfBoundsException e){
                    LogCarga+="Error al cargar argumentos, propiedad Legible no."+ (i+1)+TextManagement.NewLine();
                }
                
            }
            if (!LogCarga.equals("")) {
                WinLog.WriteEvent(LogCarga, EntryType.Error, 110);
                return null;
            }
            if (!esConfiguracionPorDefecto(Table)) {
                return null;
            }
            for (int i = 0; i < Table.length; i++) {
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
                    case "EsPrimerUso":
                        esPrimerUso = Table[i][1].toLowerCase().equals("true");
                        break;
                    default:
                        try {
                        if (isThread(Table[i][0])) {
                        } else {
                            WinLog.WriteEvent("Error al leer Configuracion de Hilo: " + (i + 1), EntryType.Error, 110);
                        }
                    } catch (ArrayIndexOutOfBoundsException E) {
                        WinLog.WriteEvent("Error al leer Configuracion de hilo en la linea " + (i + 1), EntryType.Error, 110);
                    }
                }
            }
            if (LaKeyEsNula && keygen != null && !keygen.trim().isEmpty()) {
                LaConfiguracion = setDataBaseConfigfromXML(LaConfiguracion, Arch, keygen);
            }
        }
        return LaConfiguracion;
    }

    private synchronized static boolean esConfiguracionPorDefecto(String[][] Table) {
        String StringLog = "";
        for (int i = 0; i < Table.length; i++) {
            if (Table[i][1].contains("[") || Table[i][1].contains("]")) {
                StringLog += "Error al cargar configuracion en parametro: " + Table[i][0] + " ; Con valor:" + Table[i][1] + " En la linea legible: " + (i + 1) + TextManagement.NewLine();
            }
        }
        if (!StringLog.isBlank()) {
            WinLog.WriteEvent(StringLog, EntryType.Error, 110);
            return false;
        }
        return true;
    }

    private synchronized static Configuration setDataBaseConfigfromXML(Configuration LaConfiguracion, File conf_fileXML, String Keygen) {
        if (Keygen == null) {
            LaKeyEsNula = true;
            return LaConfiguracion;
        } else {
            LaKeyEsNula = false;
        }
        ArrayList<ArrayList> arrs = ExportXML.ReadXMLFile(conf_fileXML);
        String Key = Keygen;
        ArrayList<ObjectAttrib> AD = Encoder.StartDecode(Key, arrs.get(0));
        ArrayList<ObjectAttrib> mdl = Encoder.StartDecode(Key, arrs.get(1));
        Configuration Conf = LaConfiguracion;

        for (ObjectAttrib objectAttrib : AD) {
            switch (objectAttrib.getTittle()) {
                case "DatabaseName":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_DB_NAME(objectAttrib.getValue());
                    break;
                case "User":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_USER(objectAttrib.getValue());
                    break;
                case "Password":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD(objectAttrib.getValue());
                    break;
                case "Location":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_PATH(objectAttrib.getValue());
                    break;
                case "Port":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_PORT(objectAttrib.getValue());
                    break;
                case "Schema":
                    Conf.setPostgreSQL_ADEMPIERE_SERVER_SCHEMA(objectAttrib.getValue());
                    break;
                default:
                    throw new AssertionError();
            }
        }
        for (ObjectAttrib objectAttrib : mdl) {
            switch (objectAttrib.getTittle()) {
                case "DatabaseName":
                    Conf.setPostgreSQL_MOODLE_SERVER_DB_NAME(objectAttrib.getValue());
                    break;
                case "User":
                    Conf.setPostgreSQL_MOODLE_SERVER_USER(objectAttrib.getValue());
                    break;
                case "Password":
                    Conf.setPostgreSQL_MOODLE_SERVER_USER_PASSWORD(objectAttrib.getValue());
                    break;
                case "Location":
                    Conf.setPostgreSQL_MOODLE_SERVER_PATH(objectAttrib.getValue());
                    break;
                case "Port":
                    Conf.setPostgreSQL_MOODLE_SERVER_PORT(objectAttrib.getValue());
                    break;
                case "Schema":
                    Conf.setPostgreSQL_MOODLE_SERVER_SCHEMA(objectAttrib.getValue());
                    break;
                default:
                    throw new AssertionError();
            }
        }
        return Conf;
    }

    private synchronized static boolean isThread(String Property) {
        return (Property.contains("hilo_") && !Property.startsWith("#"));

    }

    private synchronized static boolean AddDecodedThread(String Name, String Values) {
        String Accion = Name.replace("hilo_", "");
        String ParametrosTiempo[];
        if (Values.contains("--")) {
            ParametrosTiempo = Values.replace("--", "&-").split("&");
        } else {
            ParametrosTiempo = Values.split("-");
        }
        try {
            ParametrosHilos.add(new String[]{Accion, ParametrosTiempo[0], ParametrosTiempo[1]});
            return true;
        } catch (ArrayIndexOutOfBoundsException E) {

        }
        return false;
    }

    private synchronized static void generarHilos(final ArrayList<String[]> paramshilo, final Configuration Conf) {
        if (!paramshilo.isEmpty()) {
            Hilos = new HiloProceso[paramshilo.size()];
            int Tiempo;
            int TipoTiempo;
            String Accion;
            for (int i = 0; i < paramshilo.size(); i++) {
                Accion = paramshilo.get(i)[0];
                try {
                    Tiempo = Integer.parseInt(paramshilo.get(i)[1].trim());
                    TipoTiempo = Integer.parseInt(paramshilo.get(i)[2].trim());
                    if (TipoTiempo >= -1 && TipoTiempo <= 3) {
                        if (TipoTiempo == -1) {
                            continue; //no genera el hilo y continua <for>
                        }
                        Hilos[i] = new HiloProceso(Accion, Conf);
                        Hilos[i].SetSleepingtime(Tiempo, TipoTiempo);
                        Hilos[i].setName(Accion);
                    }
                } catch (NumberFormatException ex) {
                    System.out.println(ex.getMessage());
                    Hilos = null;
                    break;
                }
            }
        }
    }

    private synchronized static void apagarHilos() {
        if (Hilos == null) {
            return;
        }
        boolean areStoped = false;
        String Indicadores = ""; //O Encendido * Apagado

        for (HiloProceso Hilo : Hilos) {
            if (Hilo != null) {//si el hilo fue inicializado
                Hilo.setAliveT(false);
                Hilo.interrupt();
            }
        }
        String IndicadoresCont = "Estado: ";
        while (!areStoped) {
            Indicadores = "Estado: ";

            for (int i = 0; i < Hilos.length; i++) {

                if (Hilos[i] != null) {
                    if (Hilos[i].isAlive()) {
                        Indicadores += "O ";
                    } else {
                        Indicadores += "- ";
                    }
                }

            }
            if (!IndicadoresCont.equals(Indicadores)) {
                IndicadoresCont = Indicadores;
                System.out.println(IndicadoresCont);
            }
            if (!Indicadores.contains("O")) {
                IndicadoresCont = Indicadores;
                areStoped = true;
            }

        }
        Hilos = null;
    }

    private static void IniciarHilos() {

        for (HiloProceso Hilo : Hilos) {
            if (Hilo != null) {
                Hilo.setAliveT(true);
                Hilo.start();
            }
        }
    }

    private synchronized static String Ayuda() {
        return "La utilización de la interfaz en modo Consola consiste en seleccionar una opción con el numero correspondiente" + TextManagement.NewLine()
                + "Las configuraciones pueden ser generadas al inicio de este programa en la ruta" + TextManagement.NewLine(2)
                + OsRecognizer.getrunningpath() + TextManagement.NewLine(3)
                + Information.ProgramName + " " + Information.Version + " " + TextManagement.NewLine() + Information.getDevs()
                + TextManagement.NewLine(2)
                + "Parametros: "
                + TextManagement.NewLine()
                + "-Service\t\t\t\t:\tCorre el programa como servicio"
                + TextManagement.NewLine(4);

    }

    private synchronized static void LimpiarConsola() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ex) {
            WinLog.WriteEvent("Error al ejecutar comando \"cls\"" + TextManagement.NewLine(3) + TextManagement.Separator() + TextManagement.NewLine() + ex.getMessage(), EntryType.Error, 0);
        }
    }

    private static void MostrarHilosActivos() {
        String LogString = TextManagement.NewLine(2) + TextManagement.Separator(35) + TextManagement.NewLine();
        if (Hilos != null) {
            for (HiloProceso Hilo : Hilos) {
                if (Hilo != null) {
                    LogString += Hilo.GetLatMessage() + TextManagement.NewLine()
                            + TextManagement.Separator(32) + TextManagement.NewLine(2);
                }
            }
        }
        System.out.println(LogString);
    }

    private static void ReescribirConfig() {
        String TextoLeido = Tokenizer.GetFulltext_fromfile(Arch);
        String[] Lines = TextoLeido.split(TextManagement.NewLine());
        String TextRes = "";
        for (String Line : Lines) {
            if (Line.startsWith("EsPrimerUso")) {
                TextRes += "#" + Line.replace("true", "false") + TextManagement.NewLine();
            } else {
                TextRes += Line + TextManagement.NewLine();
            }
        }
        CreateFile.Writefile(Arch, "Conf", FileExtention.txt, TextRes);
    }

    private static void StartFirstSteps() {
        System.out.println("Iniciando primeros pasos antes de ejecucicompleta..." + TextManagement.NewLine() + "Esto puede tardar un momento..." +
                TextManagement.NewLine() + TextManagement.Separator(32) + TextManagement.NewLine());
        System.out.println("Cargando Configuraciones...");
        Configuration Conf = CargarConfiguracion();
        System.out.println("Inicializando Primeros pasos...");
        Funciones Func = new Funciones(Conf, "First steps");
        System.out.println("Ejecutando paso 1 (Sincronizando Cursos " + Information.Plataforma + " a " + Information.Fuente + ")...");
        Func.SyncCourses_Moodle2Surver();
        System.out.println("Reescribiendo Configuración");
        ReescribirConfig();
        System.out.println("Terminado...");
        CargarHilos();
        System.out.println("Presiona Enter para continuar...");
        try {
            (new BufferedReader(new InputStreamReader(System.in))).readLine();
        } catch (IOException iOException) {
        }
        LimpiarConsola();
    }
//</editor-fold>

}
