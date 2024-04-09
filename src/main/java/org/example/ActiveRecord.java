package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class ActiveRecord {
    public static String TABLE_NAME = "";

    public <T> List<T> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        List<T> result = new ArrayList<>();
        try {
            Connection con = Dao.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                T value = (T) this.getClass().newInstance();
                Field[] fields = this.getClass().getDeclaredFields();
                for (Field field : fields) {
                    Method setMethod = reflectionSetMethod(field);

                     Object valueDb;
                    if (field.getType().equals(int.class)) {
                        valueDb = rs.getInt(field.getName());
                    } else {
                        valueDb = rs.getString(field.getName());
                    }
                    setMethod.invoke(value, valueDb);
                }
                result.add(value);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private Method reflectionSetMethod(Field field) throws NoSuchMethodException {
        String[] splitFieldName = field.getName().split("");
        splitFieldName[0] = splitFieldName[0].toUpperCase();
        String fieldNameCameOnCase = String.join("", splitFieldName);
        Class<? extends ActiveRecord> clazz = this.getClass();
        Class<?> paramType = field.getType();
        String methodName = "set" + fieldNameCameOnCase;
        return clazz.getMethod(methodName, paramType);
    }

    public String getFields() {
        Field[] fields = this.getClass().getDeclaredFields();
        StringBuilder stringFields = new StringBuilder();
        for (Field field : fields) {
            stringFields.append(",").append(field.getName());
        }
        return stringFields.substring(1);
    }

    @Override
    public String toString() {
        Field[] fields = this.getClass().getDeclaredFields();
        return this.getFields();
    }
}
