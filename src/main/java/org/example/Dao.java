package org.example;

import org.example.util.LoadProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryFriend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class Dao {
    private final static Logger LOG = LoggerFactory.getLogger(Dao.class);
    private Properties prop = LoadProperties.getProperties(this.getClass());
    private static Dao instance;
    private Connection connection;

    private Dao() {
        try{
            this.loadDriveDb();
            String url = this.prop.getProperty("active.database.url");
            String username = this.prop.getProperty("active.database.username");
            String password = this.prop.getProperty("active.database.password");

            this.connection = DriverManager.getConnection(url, username, password);
            LOG.info("Banco Conectado Com Sucesso!");
        }catch (Exception error){
            error.printStackTrace();
            LOG.error("ERROR: {}, CAUSE LOCALIZED: {}", error.getMessage(), error.getLocalizedMessage());
        }
    }

    private void loadDriveDb() throws ClassNotFoundException {
        String driveDb = this.prop.getProperty("active.database.drive");
        Class.forName(driveDb);
    }
    public static Dao getInstance(){
        if(instance == null) {
            instance = new Dao();
        }
        return instance;
    }

    public Connection getConnection(){
        return this.connection;
    }
}
