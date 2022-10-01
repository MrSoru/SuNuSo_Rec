package com.codebyte.sunuso.Resources;

import java.io.Serializable;

public class Configuration implements Serializable {
    private static final long serialVersionUID = 13012022L;

    public static final Configuration Instancia = new Configuration();

    String PostgreSQL_MOODLE_SERVER_USER_PASSWORD;

    String PostgreSQL_MOODLE_SERVER_USER;

    String PostgreSQL_MOODLE_SERVER_DB_NAME;

    String PostgreSQL_MOODLE_SERVER_PATH;

    String PostgreSQL_MOODLE_SERVER_PORT;

    String PostgreSQL_MOODLE_SERVER_SCHEMA;

    String PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD;

    String PostgreSQL_ADEMPIERE_SERVER_USER;

    String PostgreSQL_ADEMPIERE_SERVER_DB_NAME;

    String PostgreSQL_ADEMPIERE_SERVER_PATH;

    String PostgreSQL_ADEMPIERE_SERVER_PORT;

    String PostgreSQL_ADEMPIERE_SERVER_SCHEMA;

    String CLI_CURRENT_USER;

    String CLI_CURRENT_USER_PASSWORD;

    String CLI_WEBSERVICE_ROOT;

    String CLI_WEBSERVICE_TOKEN;

    String InstallationPath;

    String CLI_CRON_PATH;

    String CLI_CRON_KEY;

    public Configuration() {}

    public Configuration(String PostgreSQL_MOODLE_SERVER_USER_PASSWORD, String PostgreSQL_MOODLE_SERVER_USER, String PostgreSQL_MOODLE_SERVER_DB_NAME, String PostgreSQL_MOODLE_SERVER_PATH, String PostgreSQL_MOODLE_SERVER_PORT, String PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD, String PostgreSQL_ADEMPIERE_SERVER_USER, String PostgreSQL_ADEMPIERE_SERVER_DB_NAME, String PostgreSQL_ADEMPIERE_SERVER_PATH, String PostgreSQL_ADEMPIERE_SERVER_PORT, String CLI_CURRENT_USER, String CLI_CURRENT_USER_PASSWORD, String CLI_WEBSERVICE_ROOT, String CLI_WEBSERVICE_TOKEN, String InstallationPath, String CLI_CRON_PATH, String CLI_CRON_KEY) {
        this.PostgreSQL_MOODLE_SERVER_USER_PASSWORD = PostgreSQL_MOODLE_SERVER_USER_PASSWORD;
        this.PostgreSQL_MOODLE_SERVER_USER = PostgreSQL_MOODLE_SERVER_USER;
        this.PostgreSQL_MOODLE_SERVER_DB_NAME = PostgreSQL_MOODLE_SERVER_DB_NAME;
        this.PostgreSQL_MOODLE_SERVER_PATH = PostgreSQL_MOODLE_SERVER_PATH;
        this.PostgreSQL_MOODLE_SERVER_PORT = PostgreSQL_MOODLE_SERVER_PORT;
        this.PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD = PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD;
        this.PostgreSQL_ADEMPIERE_SERVER_USER = PostgreSQL_ADEMPIERE_SERVER_USER;
        this.PostgreSQL_ADEMPIERE_SERVER_DB_NAME = PostgreSQL_ADEMPIERE_SERVER_DB_NAME;
        this.PostgreSQL_ADEMPIERE_SERVER_PATH = PostgreSQL_ADEMPIERE_SERVER_PATH;
        this.PostgreSQL_ADEMPIERE_SERVER_PORT = PostgreSQL_ADEMPIERE_SERVER_PORT;
        this.CLI_CURRENT_USER = CLI_CURRENT_USER;
        this.CLI_CURRENT_USER_PASSWORD = CLI_CURRENT_USER_PASSWORD;
        this.CLI_WEBSERVICE_ROOT = CLI_WEBSERVICE_ROOT;
        this.CLI_WEBSERVICE_TOKEN = CLI_WEBSERVICE_TOKEN;
        this.InstallationPath = InstallationPath;
        this.CLI_CRON_PATH = CLI_CRON_PATH;
        this.CLI_CRON_KEY = CLI_CRON_KEY;
    }

    public String getCLI_CURRENT_USER() {
        return this.CLI_CURRENT_USER;
    }

    public void setCLI_CURRENT_USER(String CLI_CURRENT_USER) {
        this.CLI_CURRENT_USER = CLI_CURRENT_USER;
    }

    public String getCLI_CURRENT_USER_PASSWORD() {
        return this.CLI_CURRENT_USER_PASSWORD;
    }

    public void setCLI_CURRENT_USER_PASSWORD(String CLI_CURRENT_USER_PASSWORD) {
        this.CLI_CURRENT_USER_PASSWORD = CLI_CURRENT_USER_PASSWORD;
    }

    public String getPostgreSQL_MOODLE_SERVER_USER_PASSWORD() {
        return this.PostgreSQL_MOODLE_SERVER_USER_PASSWORD;
    }

    public void setPostgreSQL_MOODLE_SERVER_USER_PASSWORD(String PostgreSQL_MOODLE_SERVER_USER_PASSWORD) {
        this.PostgreSQL_MOODLE_SERVER_USER_PASSWORD = PostgreSQL_MOODLE_SERVER_USER_PASSWORD;
    }

    public String getPostgreSQL_MOODLE_SERVER_USER() {
        return this.PostgreSQL_MOODLE_SERVER_USER;
    }

    public void setPostgreSQL_MOODLE_SERVER_USER(String PostgreSQL_MOODLE_SERVER_USER) {
        this.PostgreSQL_MOODLE_SERVER_USER = PostgreSQL_MOODLE_SERVER_USER;
    }

    public String getPostgreSQL_MOODLE_SERVER_DB_NAME() {
        return this.PostgreSQL_MOODLE_SERVER_DB_NAME;
    }

    public void setPostgreSQL_MOODLE_SERVER_DB_NAME(String PostgreSQL_MOODLE_SERVER_DB_NAME) {
        this.PostgreSQL_MOODLE_SERVER_DB_NAME = PostgreSQL_MOODLE_SERVER_DB_NAME;
    }

    public String getPostgreSQL_MOODLE_SERVER_PATH() {
        return this.PostgreSQL_MOODLE_SERVER_PATH;
    }

    public void setPostgreSQL_MOODLE_SERVER_PATH(String PostgreSQL_MOODLE_SERVER_PATH) {
        this.PostgreSQL_MOODLE_SERVER_PATH = PostgreSQL_MOODLE_SERVER_PATH;
    }

    public String getPostgreSQL_MOODLE_SERVER_PORT() {
        return this.PostgreSQL_MOODLE_SERVER_PORT;
    }

    public void setPostgreSQL_MOODLE_SERVER_PORT(String PostgreSQL_MOODLE_SERVER_PORT) {
        this.PostgreSQL_MOODLE_SERVER_PORT = PostgreSQL_MOODLE_SERVER_PORT;
    }

    public String getPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD() {
        return this.PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD;
    }

    public void setPostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD(String PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD) {
        this.PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD = PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD;
    }

    public String getPostgreSQL_ADEMPIERE_SERVER_USER() {
        return this.PostgreSQL_ADEMPIERE_SERVER_USER;
    }

    public void setPostgreSQL_ADEMPIERE_SERVER_USER(String PostgreSQL_ADEMPIERE_SERVER_USER) {
        this.PostgreSQL_ADEMPIERE_SERVER_USER = PostgreSQL_ADEMPIERE_SERVER_USER;
    }

    public String getPostgreSQL_ADEMPIERE_SERVER_DB_NAME() {
        return this.PostgreSQL_ADEMPIERE_SERVER_DB_NAME;
    }

    public void setPostgreSQL_ADEMPIERE_SERVER_DB_NAME(String PostgreSQL_ADEMPIERE_SERVER_DB_NAME) {
        this.PostgreSQL_ADEMPIERE_SERVER_DB_NAME = PostgreSQL_ADEMPIERE_SERVER_DB_NAME;
    }

    public String getPostgreSQL_ADEMPIERE_SERVER_PATH() {
        return this.PostgreSQL_ADEMPIERE_SERVER_PATH;
    }

    public void setPostgreSQL_ADEMPIERE_SERVER_PATH(String PostgreSQL_ADEMPIERE_SERVER_PATH) {
        this.PostgreSQL_ADEMPIERE_SERVER_PATH = PostgreSQL_ADEMPIERE_SERVER_PATH;
    }

    public String getPostgreSQL_ADEMPIERE_SERVER_PORT() {
        return this.PostgreSQL_ADEMPIERE_SERVER_PORT;
    }

    public void setPostgreSQL_ADEMPIERE_SERVER_PORT(String PostgreSQL_ADEMPIERE_SERVER_PORT) {
        this.PostgreSQL_ADEMPIERE_SERVER_PORT = PostgreSQL_ADEMPIERE_SERVER_PORT;
    }

    public String getCLI_WEBSERVICE_ROOT() {
        return this.CLI_WEBSERVICE_ROOT;
    }

    public void setCLI_WEBSERVICE_ROOT(String CLI_WEBSERVICE_ROOT) {
        this.CLI_WEBSERVICE_ROOT = CLI_WEBSERVICE_ROOT;
    }

    public String getCLI_WEBSERVICE_TOKEN() {
        return this.CLI_WEBSERVICE_TOKEN;
    }

    public void setCLI_WEBSERVICE_TOKEN(String CLI_WEBSERVICE_TOKEN) {
        this.CLI_WEBSERVICE_TOKEN = CLI_WEBSERVICE_TOKEN;
    }

    public String getInstallationPath() {
        return this.InstallationPath;
    }

    public void setInstallationPath(String InstallationPath) {
        this.InstallationPath = InstallationPath;
    }

    public String getCLI_CRON_PATH() {
        return this.CLI_CRON_PATH;
    }

    public void setCLI_CRON_PATH(String CLI_CRON_PATH) {
        this.CLI_CRON_PATH = CLI_CRON_PATH;
    }

    public String getCLI_CRON_KEY() {
        return this.CLI_CRON_KEY;
    }

    public void setCLI_CRON_KEY(String CLI_CRON_KEY) {
        this.CLI_CRON_KEY = CLI_CRON_KEY;
    }

    public String getPostgreSQL_MOODLE_SERVER_SCHEMA() {
        return this.PostgreSQL_MOODLE_SERVER_SCHEMA;
    }

    public void setPostgreSQL_MOODLE_SERVER_SCHEMA(String PostgreSQL_MOODLE_SERVER_SCHEMA) {
        this.PostgreSQL_MOODLE_SERVER_SCHEMA = PostgreSQL_MOODLE_SERVER_SCHEMA;
    }

    public String getPostgreSQL_ADEMPIERE_SERVER_SCHEMA() {
        return this.PostgreSQL_ADEMPIERE_SERVER_SCHEMA;
    }

    public void setPostgreSQL_ADEMPIERE_SERVER_SCHEMA(String PostgreSQL_ADEMPIERE_SERVER_SCHEMA) {
        this.PostgreSQL_ADEMPIERE_SERVER_SCHEMA = PostgreSQL_ADEMPIERE_SERVER_SCHEMA;
    }

    public String toString() {
        return "Configuration{PostgreSQL_MOODLE_SERVER_USER_PASSWORD=" + this.PostgreSQL_MOODLE_SERVER_USER_PASSWORD + ", PostgreSQL_MOODLE_SERVER_USER=" + this.PostgreSQL_MOODLE_SERVER_USER + ", PostgreSQL_MOODLE_SERVER_DB_NAME=" + this.PostgreSQL_MOODLE_SERVER_DB_NAME + ", PostgreSQL_MOODLE_SERVER_PATH=" + this.PostgreSQL_MOODLE_SERVER_PATH + ", PostgreSQL_MOODLE_SERVER_PORT=" + this.PostgreSQL_MOODLE_SERVER_PORT + ", PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD=" + this.PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD + ", PostgreSQL_ADEMPIERE_SERVER_USER=" + this.PostgreSQL_ADEMPIERE_SERVER_USER + ", PostgreSQL_ADEMPIERE_SERVER_DB_NAME=" + this.PostgreSQL_ADEMPIERE_SERVER_DB_NAME + ", PostgreSQL_ADEMPIERE_SERVER_PATH=" + this.PostgreSQL_ADEMPIERE_SERVER_PATH + ", PostgreSQL_ADEMPIERE_SERVER_PORT=" + this.PostgreSQL_ADEMPIERE_SERVER_PORT + ", CLI_CURRENT_USER=" + this.CLI_CURRENT_USER + ", CLI_CURRENT_USER_PASSWORD=" + this.CLI_CURRENT_USER_PASSWORD + ", CLI_WEBSERVICE_ROOT=" + this.CLI_WEBSERVICE_ROOT + ", CLI_WEBSERVICE_TOKEN=" + this.CLI_WEBSERVICE_TOKEN + ", InstallationPath=" + this.InstallationPath + ", CLI_CRON_PATH=" + this.CLI_CRON_PATH + ", CLI_CRON_KEY=" + this.CLI_CRON_KEY + "}";
    }

    public Object readResolve() {
        return this;
    }

    public boolean isnulldata() {
        return (this.PostgreSQL_MOODLE_SERVER_USER_PASSWORD == null && this.PostgreSQL_MOODLE_SERVER_DB_NAME == null && this.PostgreSQL_MOODLE_SERVER_PATH == null && this.PostgreSQL_MOODLE_SERVER_PORT == null && this.PostgreSQL_MOODLE_SERVER_SCHEMA == null && this.PostgreSQL_MOODLE_SERVER_USER == null && this.PostgreSQL_ADEMPIERE_SERVER_USER_PASSWORD == null && this.PostgreSQL_ADEMPIERE_SERVER_DB_NAME == null && this.PostgreSQL_ADEMPIERE_SERVER_PATH == null && this.PostgreSQL_ADEMPIERE_SERVER_PORT == null && this.PostgreSQL_ADEMPIERE_SERVER_SCHEMA == null && this.PostgreSQL_ADEMPIERE_SERVER_USER == null && this.CLI_CURRENT_USER == null && this.CLI_CURRENT_USER_PASSWORD == null && this.CLI_WEBSERVICE_ROOT == null && this.CLI_WEBSERVICE_TOKEN == null && this.InstallationPath == null && this.CLI_CRON_PATH == null && this.CLI_CRON_KEY == null);
    }
}
