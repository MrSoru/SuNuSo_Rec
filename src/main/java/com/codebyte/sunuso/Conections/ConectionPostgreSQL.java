package com.codebyte.sunuso.Conections;

import com.codebyte.sunuso.Resources.Configuration;
import com.codebyte.sunuso.Resources.Information;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConectionPostgreSQL {
    public Connection conn;

    private String DBLocation;

    private String DBPort;

    private String DBName;

    private String DBUser;

    private String DBUserPassword;

    private String Schema;

    private String Threadname;

    public ConectionPostgreSQL() {}

    public ConectionPostgreSQL(String DBLocation, String DBPort, String DBName, String DBUser, String DBUserPassword, String Schema) {
        this.DBLocation = DBLocation;
        this.DBPort = DBPort;
        this.DBName = DBName;
        this.DBUser = DBUser;
        this.DBUserPassword = DBUserPassword;
        this.Schema = Schema;
    }

    public ConectionPostgreSQL(Configuration Config) {
        this.DBLocation = Config.getPostgreSQL_ADEMPIERE_SERVER_PATH();
        this.DBPort = Config.getPostgreSQL_ADEMPIERE_SERVER_PORT();
        this.DBName = Config.getPostgreSQL_ADEMPIERE_SERVER_DB_NAME();
        this.DBUser = Config.getPostgreSQL_ADEMPIERE_SERVER_USER();
        this.DBUserPassword = Config.getPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD();
        this.Schema = Config.getPostgreSQL_ADEMPIERE_SERVER_SCHEMA();
    }

    public ConectionPostgreSQL(Configuration Conf, int NumeroConexion) {
        switch (NumeroConexion) {
            case 0:
                this.DBLocation = Conf.getPostgreSQL_ADEMPIERE_SERVER_PATH();
                this.DBPort = Conf.getPostgreSQL_ADEMPIERE_SERVER_PORT();
                this.DBName = Conf.getPostgreSQL_ADEMPIERE_SERVER_DB_NAME();
                this.DBUser = Conf.getPostgreSQL_ADEMPIERE_SERVER_USER();
                this.DBUserPassword = Conf.getPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD();
                this.Schema = Conf.getPostgreSQL_ADEMPIERE_SERVER_SCHEMA();
                return;
            case 1:
                this.DBLocation = Conf.getPostgreSQL_MOODLE_SERVER_PATH();
                this.DBPort = Conf.getPostgreSQL_MOODLE_SERVER_PORT();
                this.DBName = Conf.getPostgreSQL_MOODLE_SERVER_DB_NAME();
                this.DBUser = Conf.getPostgreSQL_MOODLE_SERVER_USER();
                this.DBUserPassword = Conf.getPostgreSQL_MOODLE_SERVER_USER_PASSWORD();
                this.Schema = Conf.getPostgreSQL_MOODLE_SERVER_SCHEMA();
                return;
        }
        throw new AssertionError();
    }

    public Connection getConn(String Threadname) {
        try {
            String url = "jdbc:postgresql://" + this.DBLocation.trim() + ":" + this.DBPort.trim() + "/" + this.DBName.trim();
            Properties props = new Properties();
            props.setProperty("user", this.DBUser.trim());
            props.setProperty("password", this.DBUserPassword.trim());
            props.setProperty("ssl", "false");
            props.setProperty("ApplicationName", Information.ProgramName + " " + Information.Version + " -EAX-" + Threadname);
            Class.forName("org.postgresql.Driver");
            this.conn = DriverManager.getConnection(url, props);
            this.conn.setSchema(this.Schema);
        } catch (SQLException|ClassNotFoundException sQLException) {}
        return this.conn;
    }

    public boolean DestroyConnection() {
        try {
            this.conn.close();
            this.conn=null;
            return true;
        } catch (SQLException ex) {
            this.conn=null;
            return false;
        }
    }

    public boolean CheckConection(String ThreadName) {
        this.Threadname = ThreadName;
        try {
            if (this.conn == null)
                getConn(this.Threadname);
            if (this.conn != null)
                return this.conn.isValid(10);
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }
}
