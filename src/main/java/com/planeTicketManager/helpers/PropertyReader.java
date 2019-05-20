package com.planeTicketManager.helpers;

import com.planeTicketManager.models.DBSettings;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertyReader {
    private DBSettings dbSettings = new DBSettings();
    InputStream inputStream;

    public DBSettings readSettings() throws IOException {
        try {
            Properties prop = new Properties();
            String propFilename = "dbconfig.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFilename);

            if (inputStream != null)
                prop.load(inputStream);
            else
                throw new FileNotFoundException();

            dbSettings.setDriver(prop.getProperty("db.driver"));
            dbSettings.setHostName(prop.getProperty("db.hostName"));
            dbSettings.setDbName(prop.getProperty("db.dbName"));
            dbSettings.setPassword(prop.getProperty("db.password"));
            dbSettings.setUser(prop.getProperty("db.user"));
        }catch (Exception e) {

        }finally {
            inputStream.close();
        }
        return dbSettings;
//        ResourceBundle reader = ResourceBundle.getBundle("dbconfig.properties");
//
//        return reader.getString("db.driver");
    }

}
