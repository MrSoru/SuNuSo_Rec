package com.codebyte.sunuso.Resources;

/**
 *
 * @author Arturo Meza Garcia
 */
public class TextManagement {

    public static String SingleQuote(final String Text) {
        return "'" + Text + "'";
    }

    public static String DouleQuote(final String Text) {
        return "\"" + Text + "\"";
    }

    public static String NewLine() {
        return "\n";
    }

    public static String NewLine(int NumberOfLines) {
        if (NumberOfLines <= 1) {
            return NewLine();
        }
        String OutPut = "";
        for (int i = 0; i < Math.abs(NumberOfLines); i++) {
            OutPut += NewLine();
        }
        return OutPut;
    }

    public static String NewTab() {
        return "\t";
    }

    public static String NewTab(int NumberOfLines) {
//        if (NumberOfLines<=1) {
//            return NewTab();
//        }
        String OutPut = "";
        for (int i = 0; i < Math.abs(NumberOfLines); i++) {
            OutPut += NewTab();
        }
        return OutPut;
    }

    public static String addRawTab(){
        return"        ";
    }

    public static String addRawTab(int NumberOfTabs){
        String Output="";
        if (NumberOfTabs<=1) {
            return addRawTab();
        }
        for (int i = 0; i < Math.abs(NumberOfTabs); i++) {
            Output+=addRawTab();
        }
        return addRawTab();
    }

    public static String Separator() {
        String Line = "";
        for (int i = 0; i < 30; i++) {
            Line += "-";
        }
        return Line;
    }

    public static String Separator(int With, int Lines) {
        String Line = "";
        if (With <= 0) {
            With=30;
        }
        if (Lines <=1 ) {
            return Separator(With,2);
        }
        for (int i = 0; i < Lines; i++) {
            for (int j = 0; j < Math.abs(With); j++) {
                Line += "-";
            }
            Line+=NewLine();
        }
        return Line;
    }

    public static String Separator(int With) {
        String Line = "";
        for (int i = 0; i < Math.abs(With); i++) {
            Line += "-";
        }
        return Line;
    }

    public static String Transform4CMD(String Texto){
        return Texto.replace(NewLine(), "`n").replace("\t", "`t");
    }

    public static String DoubleCMDQuotes(String Texto){
        return "\\\""+Texto+"\\\"";
    }



    public static String TransformSecondsToTime(long Seconds) {
        long hours = Seconds / 3600;
        long minutes = (Seconds % 3600) / 60;
        long seconds = Seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}