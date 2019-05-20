package com.planeTicketManager.repository;

import com.microsoft.sqlserver.jdbc.SQLServerXADataSource;
import com.planeTicketManager.helpers.PropertyReader;
import com.planeTicketManager.models.DBSettings;

import javax.sql.PooledConnection;
import java.io.IOException;
import java.sql.SQLException;

public class DataSourceSingleton {

    static SQLServerXADataSource ds = null;

    public static PooledConnection getPooledConnection() throws IOException, SQLException {
        if (ds == null) {
            DBSettings settings;
            PropertyReader reader = new PropertyReader();

            settings = reader.readSettings();
            ds = new SQLServerXADataSource();
            ds.setTrustServerCertificate(false);
            ds.setUser( settings.getUser() );
            ds.setServerName( settings.getHostName());
            ds.setEncrypt(true);
            ds.setPassword( settings.getPassword() );
            ds.setHostNameInCertificate( "*.database.windows.net" );
            ds.setLoginTimeout(90);
            ds.setPortNumber(1433);
            ds.setDatabaseName( settings.getDbName() );
        }

        return ds.getPooledConnection();
    }


}
