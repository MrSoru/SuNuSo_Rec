package com.codebyte.sunuso.Objects.Surver;

import com.codebyte.sunuso.OutputFilesWritter.CreateFile;
import com.codebyte.sunuso.OutputFilesWritter.Resources.FileExtention;
import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.Resources.TextManagement;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;

import java.util.ArrayList;

public class UsuarioSurver {
    private String Nombre;
    private String Paterno;
    private String Materno;
    private String RFC;
    private String Usuario;
    private String Correo;
    private String Empresa;
    private String Segmento;
    private String NSS;
    private String Puesto;
    private int id_empleado = -1;
    private String Reason;
    WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);

    public UsuarioSurver() {
    }

    public UsuarioSurver(String Nombre, String Paterno, String Materno, String RFC, String Usuario, String Correo, String Empresa, String Segmento, String NSS) {
        this.Nombre = Nombre;
        this.Paterno = Paterno;
        this.Materno = Materno;
        this.RFC = RFC;
        this.Usuario = Usuario;
        this.Correo = Correo;
        this.Empresa = Empresa;
        this.Segmento = Segmento;
        this.NSS = NSS;
    }

    public int getId_empleado() {
        return this.id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNSS() {
        return this.NSS;
    }

    public void setNSS(String NSS) {
        this.NSS = NSS;
    }

    public String getNombre() {
        return this.Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getPaterno() {
        return this.Paterno;
    }

    public void setPaterno(String Paterno) {
        this.Paterno = Paterno;
    }

    public String getMaterno() {
        return this.Materno;
    }

    public void setMaterno(String Materno) {
        this.Materno = Materno;
    }

    public String getRFC() {
        return this.RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getUsuario() {
        return this.Usuario;
    }

    public void setUsuario(String Usuario) {
        this.Usuario = Usuario;
    }

    public String getCorreo() {
        return this.Correo.replace("ñ", "n")
                .replace("Ñ", "N")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u");
    }

    public void setCorreo(String Correo) {
        this.Correo = Correo;
    }

    public String getEmpresa() {
        return this.Empresa;
    }

    public void setEmpresa(String Empresa) {
        this.Empresa = Empresa;
    }

    public String getSegmento() {
        return this.Segmento;
    }

    public void setSegmento(String Segmento) {
        this.Segmento = Segmento;
    }

    public String getPuesto() {return Puesto;}

    public void setPuesto(String puesto) {Puesto = puesto;}

    public String getReason() {
        return this.Reason;
    }

    public void setReason(String Reason) {
        this.Reason = Reason;
    }

    public String GetNombreCompleto() {
        return this.Nombre + " " + this.Paterno + " " + this.Materno;
    }

    public String toString() {
        return "Usuario_Surver{" + TextManagement.NewLine() + "Nombre = " + this.Nombre +
                TextManagement.NewLine() + ", Paterno = " + this.Paterno +
                TextManagement.NewLine() + ", Materno = " + this.Materno +
                TextManagement.NewLine() + ", RFC = " + (
                (this.RFC == null) ? "[SIN RFC]" : this.RFC) + TextManagement.NewLine() + ", Usuario = " + this.Usuario +
                TextManagement.NewLine() + ", Correo = " + this.Correo +
                TextManagement.NewLine() + ", Empresa = " + this.Empresa +
                TextManagement.NewLine() + ", Segmento = " + this.Segmento + "}";
    }

    public String toStringOneLine() {
        return "Usuario_Surver{Nombre = " + this.Nombre + ", Paterno = " + this.Paterno + ", Materno = " + this.Materno + ", RFC = " + (

                (this.RFC == null) ? "[SIN RFC]" : this.RFC) + ", Usuario = " + this.Usuario + ", Correo = " + this.Correo + ", Empresa = " + this.Empresa + ", Segmento = " + this.Segmento + "}";
    }

    public String CSV_String_Data() {
        return Nombre + ","
                + Paterno + ","
                + Materno + ","
                + (RFC == null ? "[SIN RFC]" : RFC) + ","
                + Usuario + ","
                + Correo + ","
                + Empresa + ","
                + Segmento + ",";
    }

    public String CSV_String_headers() {
        return "Nombre,"
                + "Paterno,"
                + "Materno,"
                + "RFC,"
                + "Usuario,"
                + "Correo,"
                + "Empresa,"
                + "Segmento,";
    }

    public boolean isEmpty() {
        return (this.Correo.isEmpty() || this.Empresa
                .isEmpty() || this.Materno
                .isEmpty() || this.Nombre
                .isEmpty() || this.Paterno
                .isEmpty() || this.RFC
                .isEmpty() || this.Segmento
                .isEmpty() || this.Usuario
                .isEmpty() || this.NSS
                .isEmpty());
    }

    public boolean isNull() {
        return (this.Correo == null || this.Empresa == null || this.Materno == null || this.Nombre == null || this.Paterno == null || this.RFC == null || this.Segmento == null || this.Usuario == null || this.NSS == null);
    }

    public static String GetConsoleDisplayTable(ArrayList<UsuarioSurver> Usuarios) {
        int Cols[] = {0, 0, 0, 0, 0};
        //Obtener El máximo de caracteres
        String Text2return = "";
        for (UsuarioSurver DinoUsuario : Usuarios) {
            if (Cols[0] < DinoUsuario.getEmpresa().length()) {
                Cols[0] = DinoUsuario.getEmpresa().length();
            }
            if (Cols[1] < DinoUsuario.getSegmento().length()) {
                Cols[1] = DinoUsuario.getSegmento().length();
            }
            if (Cols[2] < DinoUsuario.GetNombreCompleto().length()) {
                Cols[2] = DinoUsuario.GetNombreCompleto().length();
            }
            if (Cols[3] < DinoUsuario.getRFC().length()) {
                Cols[3] = DinoUsuario.getRFC().length();
            }
            if (Cols[4] < DinoUsuario.getUsuario().length()) {
                Cols[4] = DinoUsuario.getUsuario().length();
            }
        }
        //Se decide si es un ultiplo de 8 de no serlo aplic formula, si lo es no hagas nada
        for (int i = 0; i < Cols.length; i++) {
            // if (Cols[i] % 8 != 0) {
            Cols[i] = (((Cols[i] + 8) / 8) * 8);
            /// }
        }
        /*
        for (UsuarioSurver Usuario1 : Usuarios) {
            int campo1 = Usuario1.getEmpresa().length();
            int campo2 = Usuario1.getSegmento().length();
            int campo3 = Usuario1.GetNombreCompleto().length();
            int campo4 = Usuario1.getRFC().length();
            int campo5 = Usuario1.getUsuario().length();
            System.out.println(Cols[0] + "-" + campo1 + " = " + (Cols[0] - campo1) + "/8 = " + Math.ceil((double) ((double) (Cols[0] - campo1) / 8)));
            System.out.println(Cols[1] + "-" + campo2 + " = " + (Cols[1] - campo2) + "/8 = " + Math.ceil((double) ((double) (Cols[1] - campo2) / 8)));
            System.out.println(Cols[2] + "-" + campo3 + " = " + (Cols[2] - campo3) + "/8 = " + Math.ceil((double) ((double) (Cols[2] - campo3) / 8)));
            System.out.println(Cols[3] + "-" + campo4 + " = " + (Cols[3] - campo4) + "/8 = " + Math.ceil((double) ((double) (Cols[3] - campo4) / 8)));
            System.out.println(Cols[4] + "-" + campo5 + " = " + (Cols[4] - campo5) + "/8 = " + Math.ceil((double) ((double) (Cols[4] - campo5) / 8)));
            System.out.println("-------------------------------------------------------------------------------------------------------");
        }
        Text2return
                += "Empresa"        + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[0] - "Empresa".length())           / 8)))
                + "Segmento"        + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[1] - "Segmento".length())          / 8)))
                + "Nombre_Completo" + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[2] - "Nombre_Completo".length())   / 8)))
                + "RFC"             + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[3] - "RFC".length())               / 8)))
                + "Usuario"         + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[4] - "Usuario".length())           / 8)))
                + "Descripción"     + TextManagement.NewLine();
         */
        Text2return
                += "Empresa" + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[0] - "Empresa".length()) / 8)))
                + "Segmento" + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[1] - "Segmento".length()) / 8)))
                + "Nombre_Completo" + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[2] - "Nombre_Completo".length()) / 8)))
                + "RFC" + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[3] - "RFC".length()) / 8)))
                + "Usuario" + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[4] - "Usuario".length()) / 8)))
                + "Descripción" + TextManagement.NewLine();
        for (UsuarioSurver DinoUsuario : Usuarios) {
            int campo1 = DinoUsuario.getEmpresa().length();
            int campo2 = DinoUsuario.getSegmento().length();
            int campo3 = DinoUsuario.GetNombreCompleto().length();
            int campo4 = DinoUsuario.getRFC().length();
            int campo5 = DinoUsuario.getUsuario().length();
            Text2return
                    += DinoUsuario.getEmpresa() + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[0] - campo1) / 8)))
                    + DinoUsuario.getSegmento() + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[1] - campo2) / 8)))
                    + DinoUsuario.GetNombreCompleto() + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[2] - campo3) / 8)))
                    + DinoUsuario.getRFC() + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[3] - campo4) / 8)))
                    + DinoUsuario.getUsuario() + TextManagement.NewTab((int) Math.ceil((double) ((double) (Cols[4] - campo5) / 8)))
                    + DinoUsuario.getReason() + TextManagement.NewLine();
        }
        CreateFile.Writefile("Usuarios inscritos", FileExtention.txt, Text2return);
        return Text2return;
    }
}
