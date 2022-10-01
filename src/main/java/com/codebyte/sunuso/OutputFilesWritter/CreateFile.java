package com.codebyte.sunuso.OutputFilesWritter;

import com.codebyte.sunuso.OutputFilesWritter.Resources.FileExtention;
import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.Resources.TextManagement;
import com.codebyte.sunuso.Tokenizer.OsRecognizer;
import com.codebyte.sunuso.WindowsEventLogWritter.EntryType;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFile {

    public static boolean Writefile(String Nombre, FileExtention FileExt, String Contenido) {
        String FileName = Nombre + "." + FileExt.toString();
        File file = new File(OsRecognizer.getrunningpath() + "\\Resources\\" + FileName);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(Contenido);
            myWriter.close();
        } catch (IOException e) {
            WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
            WinLog.WriteEvent("Hubo un error al escribir el archivo: " + file.getPath() + TextManagement.NewLine(3) + e.getMessage(), EntryType.Error, 200);
            return false;
        }

        return true;
    }

    public static boolean Writefile(File Arch, String Nombre, FileExtention FileExt, String Contenido){
        String FileName = Nombre + "." + FileExt.toString();
        File file = Arch;

        if (file.exists()) {
            file.delete();
        }

        try {
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(Contenido);
            myWriter.close();
        } catch (IOException e) {
            WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
            WinLog.WriteEvent("Hubo un error al escribir el archivo: " + file.getPath() + TextManagement.NewLine(3) + e.getMessage(), EntryType.Error, 200);
            return false;
        }

        return true;
    }
}