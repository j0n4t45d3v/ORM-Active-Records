package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadProperties {
    public static <T> Properties getProperties(Class<T> classObject){
        Properties properties = new Properties();
        ClassLoader classLoader = classObject.getClassLoader();
        try(InputStream fileProperties = classLoader.getResourceAsStream("application.properties")) {
            properties.load(fileProperties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
