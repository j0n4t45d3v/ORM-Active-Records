package org.example.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class LoadPropertiesTest {

    @Test
    void getProperties(){
        Properties properties = LoadProperties.getProperties(this.getClass());
        String urlExpected = "jdbc:mysql://localhost:3306/active-records";
        Assertions.assertEquals(urlExpected, properties.getProperty("active.database.url"));
        Assertions.assertEquals("root", properties.getProperty("active.database.username"));
        Assertions.assertEquals("root", properties.getProperty("active.database.password"));
    }

}
