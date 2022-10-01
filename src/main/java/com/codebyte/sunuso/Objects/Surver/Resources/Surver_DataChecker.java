package com.codebyte.sunuso.Objects.Surver.Resources;

import com.codebyte.sunuso.Objects.Surver.UsuarioSurver;
import com.codebyte.sunuso.OutputFilesWritter.CreateFile;
import com.codebyte.sunuso.OutputFilesWritter.Resources.FileExtention;
import com.codebyte.sunuso.Resources.TextManagement;

import java.util.ArrayList;

public class Surver_DataChecker {
    public static void CheckAndWriteCSV(final ArrayList<UsuarioSurver> Usuarios, final String Name) {
        String Data = "";
        if (Usuarios != null) {
            if (!Usuarios.isEmpty()) {
                Data = Usuarios.get(0).CSV_String_headers() + TextManagement.NewLine();
                for (UsuarioSurver Usuario : Usuarios) {
                    Data += Usuario.CSV_String_Data() + TextManagement.NewLine();
                }
                CreateFile.Writefile(Name, FileExtention.csv, Data);
            }
        }
    }

    public static void CheckAndWriteTXT(final ArrayList<UsuarioSurver> Usuarios, final String Name) {
        String Data = "";
        if (Usuarios != null) {
            if (!Usuarios.isEmpty()) {
                for (UsuarioSurver Usuario : Usuarios) {
                    Data += Usuario.toString() + TextManagement.NewLine(2);
                }
                CreateFile.Writefile(Name, FileExtention.txt, Data);
            }
        }
    }

    public static void OnlyWriteTXT(final String Text, final String Name){
        CreateFile.Writefile(Name, FileExtention.txt, Text);
    }
}
