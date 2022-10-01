package com.codebyte.sunuso.Objects.Moodle.WebServiceConsumer;

import com.codebyte.sunuso.Resources.Information;
import com.codebyte.sunuso.WindowsEventLogWritter.EntryType;
import com.codebyte.sunuso.WindowsEventLogWritter.WindowsLog;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

public class Consumer {
    public static Document getResponse(String URL) {
        try {
            String ProcessedURL = URL;
            URL url = new URL(ProcessedURL);
            HttpURLConnection conection = (HttpURLConnection)url.openConnection();
            conection.setRequestMethod("GET");
            conection.setDoOutput(true);
            conection.connect();
            InputStream input = new BufferedInputStream(conection.getInputStream());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(input);
            String a = doc.toString();
            return doc;
        } catch (MalformedURLException ex) {
            WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
            WinLog.WriteEvent(ex.getMessage(), EntryType.Error, 400);
            return null;
        } catch (IOException|javax.xml.parsers.ParserConfigurationException|org.xml.sax.SAXException ex) {
            WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
            WinLog.WriteEvent(ex.getMessage(), EntryType.Error, 401);
            return null;
        }
    }

    public static String getResponseJson(String URL) {
        try {
            URL url = new URL(URL);
            URLConnection conection = url.openConnection();
            InputStream input = new BufferedInputStream(conection.getInputStream());
            String Texto = (new BufferedReader(new InputStreamReader(input))).lines().collect(Collectors.joining("\n"));
            String Textov2 = Texto.replace("[", "");
            Textov2 = Textov2.replace("]", "");
            return Textov2;
        } catch (MalformedURLException ex) {
            WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
            WinLog.WriteEvent(ex.getMessage(), EntryType.Error, 400);
            return null;
        } catch (IOException ex) {
            WindowsLog WinLog = new WindowsLog(Information.ProgramName, Information.LogName);
            WinLog.WriteEvent(ex.getMessage(), EntryType.Error, 400);
            return null;
        }
    }

    public static String getResponseString(String URL) {
        try {
            String ProcessedURL = URL;
            URL url = new URL(ProcessedURL);
            HttpURLConnection conection = (HttpURLConnection)url.openConnection();
            conection.setRequestMethod("GET");
            conection.setDoOutput(true);
            conection.connect();
            InputStream input = new BufferedInputStream(conection.getInputStream());
            String Texto = (new BufferedReader(new InputStreamReader(input))).lines().collect(Collectors.joining("\n"));
            byte[] bytes2 = Texto.getBytes(Charset.forName("ISO-8859-1"));
            return new String(bytes2, Charset.forName("UTF-8"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (ProtocolException ex) {
            return "protocolo Erroneo\n\n\n" + ex.getMessage();
        } catch (IOException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
        return "Sin Respuesta";
    }

    public static String ReplaceTypes(String URL) {
        String ProcessedURL = URL;
        if (ProcessedURL.contains(" "))
            ProcessedURL = ProcessedURL.replace(" ", "%20");
        if (ProcessedURL.contains("ñ"))
            ProcessedURL = ProcessedURL.replace("ñ", "%F1");
        if (ProcessedURL.contains("Ñ"))
            ProcessedURL = ProcessedURL.replace("Ñ", "%D1");
        return ProcessedURL;
    }
}
