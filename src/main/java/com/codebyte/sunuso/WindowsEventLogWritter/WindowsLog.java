package com.codebyte.sunuso.WindowsEventLogWritter;

import com.codebyte.sunuso.Resources.TextManagement;
import java.io.IOException;

public class WindowsLog {
    private String ProgramName;

    private String LogName;

    private Runtime Proc;

    public WindowsLog(String ProgramName, String LogName, Runtime Proc) {
        this.ProgramName = ProgramName;
        this.LogName = LogName;
        this.Proc = Proc;
    }

    public WindowsLog(String ProgramName, String LogName) {
        this.ProgramName = ProgramName;
        this.LogName = LogName;
        this.Proc = Runtime.getRuntime();
    }

    public WindowsLog() {}

    public String getLogName() {
        return this.LogName;
    }

    public void setLogName(String LogName) {
        this.LogName = LogName;
    }

    public Runtime getProc() {
        return this.Proc;
    }

    public void setProc(Runtime Proc) {
        this.Proc = Proc;
    }

    public String getProgramName() {
        return this.ProgramName;
    }

    public void setProgramName(String ProgramName) {
        this.ProgramName = ProgramName;
    }

    public boolean WriteEvent(String Message, EntryType entryType, int ID) {
        String Command = "powershell -command " + TextManagement.DouleQuote("Write-EventLog -LogName " + this.LogName + " -EventID " + (

                (ID <= 0) ? 1 : ID) + " -EntryType " + entryType
                .toString() + " -Source " + this.ProgramName + " -Message " +

                TextManagement.DoubleCMDQuotes(TextManagement.Transform4CMD(Message)));
        try {
            Runtime.getRuntime().exec(Command);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
