package org.example;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ActiveRecord {
    public static String TABLE_NAME = "";

    public void findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        try {
            Connection con = Dao.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Field[] fields = this.getClass().getDeclaredFields();
                for (Field field : fields) {
                    System.out.println(rs.getString(field.getName()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void getFields() {
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder stringfields = new StringBuilder();
        for (Field field : fields) {
            stringfields.append("," + field.getName());
        }

        System.out.println(stringfields.toString().substring(1) + ", TABELA: " + TABLE_NAME);
    }
}
