package com.codebyte.sunuso.Tokenizer;

import com.codebyte.sunuso.Objects.Moodle.Resources.ParamItem;
import com.codebyte.sunuso.Resources.Configuration;
import com.codebyte.sunuso.Resources.TextManagement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arturo Meza Garcia
 */
public class Tokenizer {

    /**
     * <p>
     * Guarda un archivo del en la ruta especificada </p>
     *
     * @param file Archivo destino
     * @param Text Texto a guardar
     *
     * @return boolean si el cahivo fue escrito correctamente
     */
    public static boolean SaveKEYtext(final File file, final String Text) {
        if (file.exists()) {
            file.delete();
        }
        try {
            Charset chars = Charset.forName("UTF-8");
            String Key64 = Base64.getEncoder().encodeToString(Text.getBytes(chars));
            ParamItem Key = new ParamItem("Key", Key64);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);
            // escribe los objetos en un archivo 
            o.writeObject(Key);
            o.close();
            f.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * <p>
     * Obtiene el archivo guardado de la ruta epecifica</p>
     *
     * @param file archivo a recuperar
     *
     * @return ParamItem Clase de almacenamiento de parametros y valores
     */
    public static ParamItem getKeyText(final File file) {
        try {

            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            //Lee el objeto de un archivo
            ParamItem Obj = (ParamItem) oi.readObject();
            oi.close();
            fi.close();
            byte[] decodedBytes = Base64.getDecoder().decode(Obj.getValue());
            Charset chars = Charset.forName("UTF-8");
            Obj.setValue(new String(decodedBytes, chars));
            return Obj;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static boolean SaveConfiguration(File file, Configuration configuration) {

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream o = new ObjectOutputStream(f);
            // escribe los objetos en un archivo
            o.writeObject(configuration);
            o.close();
            f.close();
            return true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public static Configuration GetConfiguration(final File file) {
        try {
            Class.forName("com.codebyte.sunuso.Resources.Configuration");
            FileInputStream fi = null;
            try {
                fi = new FileInputStream(file);
                ObjectInputStream oi = new ObjectInputStream(fi);
                //Lee el objeto de un archivo
                Object obj2 = oi.readObject();
                Configuration Obj = (Configuration) obj2;
                oi.close();
                fi.close();
                return Obj;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } finally {
                try {
                    fi.close();
                } catch (IOException ex) {
                    Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
            }
        } catch (ClassNotFoundException ex) {
            //System.out.println("Asu, se rompio la busqueda");
            return null;
        }
    }

    public static Configuration TokenEncription(Configuration Conf) {
        //File file = new File(ObjectAttrib.class.getResource(".").getPath() + "Konfig.raw");
        File file = new File(OsRecognizer.getrunningpath() + "\\Resources\\Config\\Konfig.raw");
        Configuration Confi = Conf;
        //ADEmpiere Info
        Confi.setPostgreSQL_ADEMPIERE_SERVER_DB_NAME(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_DB_NAME())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_PATH(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_PATH())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_PORT(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_PORT())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_USER(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_USER())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_SCHEMA(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_SCHEMA())
        );
        //Moodle Info   
        Confi.setPostgreSQL_MOODLE_SERVER_DB_NAME(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_DB_NAME())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_PATH(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_PATH())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_PORT(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_PORT())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_USER(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_USER())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_USER_PASSWORD(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_USER_PASSWORD())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_SCHEMA(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_SCHEMA())
        );
        //Moodle Tokens
        Confi.setCLI_WEBSERVICE_ROOT(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_WEBSERVICE_ROOT())
        );
        Confi.setCLI_WEBSERVICE_TOKEN(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_WEBSERVICE_TOKEN())
        );
        Confi.setCLI_CURRENT_USER_PASSWORD(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CURRENT_USER_PASSWORD())
        );
        Confi.setCLI_CURRENT_USER(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CURRENT_USER())
        );
        Confi.setInstallationPath(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getInstallationPath())
        );
        //cron
        Confi.setCLI_CRON_PATH(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CRON_PATH())
        );
        Confi.setCLI_CRON_KEY(
                Encoder.StartEncodesingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CRON_KEY())
        );
        return Confi;
    }

    public static Configuration TokenUnEncription(Configuration Conf) {
        //File file =new File(ObjectAttrib.class.getResource(".").getPath() + "Konfig.raw");
        File file = new File(OsRecognizer.getrunningpath() + "\\Resources\\Config\\Konfig.raw");
        Configuration Confi = Conf;
        //ADEmpiere Info
        Confi.setPostgreSQL_ADEMPIERE_SERVER_DB_NAME(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_DB_NAME())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_PATH(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_PATH())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_PORT(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_PORT())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_USER(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_USER())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD())
        );
        Confi.setPostgreSQL_ADEMPIERE_SERVER_SCHEMA(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_ADEMPIERE_SERVER_SCHEMA())
        );
        //Moodle Info
        Confi.setPostgreSQL_MOODLE_SERVER_DB_NAME(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_DB_NAME())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_PATH(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_PATH())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_PORT(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_PORT())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_USER(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_USER())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_USER_PASSWORD(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_USER_PASSWORD())
        );
        Confi.setPostgreSQL_MOODLE_SERVER_SCHEMA(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getPostgreSQL_MOODLE_SERVER_SCHEMA())
        );
        //Moodle Tokens
        Confi.setCLI_WEBSERVICE_ROOT(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_WEBSERVICE_ROOT())
        );
        Confi.setCLI_WEBSERVICE_TOKEN(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_WEBSERVICE_TOKEN())
        );
        Confi.setCLI_CURRENT_USER_PASSWORD(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CURRENT_USER_PASSWORD())
        );
        Confi.setCLI_CURRENT_USER(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CURRENT_USER())
        );
        Confi.setInstallationPath(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getInstallationPath())
        );
        //cron
        Confi.setCLI_CRON_PATH(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CRON_PATH())
        );
        Confi.setCLI_CRON_KEY(
                Encoder.StartDecodeSingle(Tokenizer.getKeyText(file).getValue(),
                        Conf.getCLI_CRON_KEY())
        );
        return Confi;
    }

    public static String getConfigText_fromfile(File Archivo) {
        try {
            BufferedReader BuffRead = new BufferedReader(new FileReader(Archivo));
            String TextRead = "";
            String Line = "";
            while ((Line = BuffRead.readLine()) != null) {
                if (!Line.contains("#") && !Line.isBlank()) {
                        TextRead += Line.replace("\t","") + TextManagement.NewLine();
                }
            }
            BuffRead.close();
            return TextRead;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
    
    public static String GetFulltext_fromfile(File Archivo) {
        try {
            BufferedReader BuffRead = new BufferedReader(new FileReader(Archivo));
            String TextRead = "";
            String Line = "";
            while ((Line = BuffRead.readLine()) != null) {
                        TextRead += Line + TextManagement.NewLine();
            }
            BuffRead.close();
            return TextRead;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tokenizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

}
//return Tokenizer.TokenUnEncription(Tokenizer.GetConfiguration(new File(OsRecognizer.getrunningpath() + "\\Resources\\Config\\Config.raw")));

/////////////////GUARDAR CONF
/*
        //Evento Aceptar
        if (ComprobarConfiguracion()) {
            //obtener Key y decodificar XML para asignar valoes a la clase config
            setDataBaseConfigfromXML();
            //Obtener los demas campos y asignarlos en la clase configuracion
            getDataConfiguration();
            //Codificar todos los datos de la clase configuracion y asignar configuracion para ingresar datos de codificacion
            Conf = Tokenizer.TokenEncription(Conf);
            //exportar datos de configuracion a archivo
            //Tokenizer.SaveConfiguration((new File(ObjectAttrib.class.getResource(".").getPath()) + "config.raw"), Conf);
            Tokenizer.SaveConfiguration((new File(InstalationPath + "\\Resources\\Config\\Config.raw")), Conf);
            setConections(
                    Tokenizer.TokenUnEncription(//desencripta configuracion
                            Tokenizer.GetConfiguration(new File(InstalationPath + "\\Resources\\Config\\Config.raw"))//obtiene archivo de configuracion encriptado
                    )
            );//aplica configuraciones de Conexiones de baes de datos
            Conf = Tokenizer.TokenUnEncription(Tokenizer.GetConfiguration(new File(InstalationPath + "\\Resources\\Config\\Config.raw")));
            this.Configpane.setVisible(false);
            setfocusComponents();
            Automatico.setEnabled(true);
            dumpConfigdata();
        }
 */