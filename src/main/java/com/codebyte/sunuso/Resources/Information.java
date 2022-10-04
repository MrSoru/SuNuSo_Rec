package com.codebyte.sunuso.Resources;

import java.util.ArrayList;

public class Information {
    public static String Version = " v2.5.1 rev2";

    public static String ProgramName = "SuNuSo";

    public static String LogName = "CodeByte";

    public static String Acronimo = "(Surver No user System Operation)";

    public static String Fuente = "Surver";

    public static String Plataforma = "Moodle";

    private static ArrayList<String> Devs = new ArrayList<>();

    public static String getDevs() {
        Devs.add("Arturo Meza Garcia AMG (Mr.S) - Escritor inicial " + ProgramName+"\n");
        Devs.add("Gustavo Padilla Ruiz - Desarrollador actual " + Fuente+"\n");
        Devs.add("Victor Vicente Rivera Hernandez - Coordinador WEB"+"\n");
        Devs.add("Luis Eduardo Aguliera Arias - Tester :D"+"\n");
        String String2Return = "";
        return Devs.stream().reduce(String2Return, String::concat);
    }

    public static String getVersion() {
        return Version;
    }

    public static String getProgramName() {
        return ProgramName;
    }

    public static String getLogName() {
        return LogName;
    }

    public static String getAcronimo() {
        return Acronimo;
    }

    public static String getFuente() {
        return Fuente;
    }

    public static String getPlataforma() {
        return Plataforma;
    }
}
