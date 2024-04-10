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

    public <T extends ActiveRecord>  List<T> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        List<T> result = new ArrayList<>();
        try {
            Connection con = Dao.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                T clazzInstance = (T) this.getClass().getDeclaredConstructor().newInstance();
                Field[] fields = this.getClass().getDeclaredFields();
                for (Field field : fields) {
                    Method setMethod = reflectionSetMethod(field);
                    Object value;
                    if (field.getType().equals(int.class)) {
                        value = rs.getInt(field.getName());
                    } else {
                        value = rs.getString(field.getName());
                    }
                    setMethod.invoke(clazzInstance, value);
                }
                result.add(clazzInstance);
            }
            return result;
        } catch (SQLException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        try{
            StringBuilder dbFields = new StringBuilder();
            List<Object> values = new ArrayList<>();

            for(Field field : this.getClass().getDeclaredFields()){
                dbFields.append(",").append(field.getName());
                Method getMethod = reflectionGetMethod(field);
                values.add(getMethod.invoke(this));
            }
            dbFields = new StringBuilder(dbFields.substring(1));
            String interrogacao = ",?".repeat(2);
            interrogacao = interrogacao.substring(1);
            String sql = "INSERT INTO "+ TABLE_NAME+" ("+dbFields+") VALUES ("+interrogacao+")";
            System.out.println(sql);

            Connection con = Dao.getInstance().getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, (int) values.get(0));
            stmt.setString(2, (String) values.get(1));
            int result = stmt.executeUpdate();
            if(result == 0){
                throw new RuntimeException("Error ao inserir");
            }
            stmt.close();
        }catch (Exception error){
            throw new RuntimeException(error);
        }
    }

    private Method reflectionSetMethod(Field field) throws NoSuchMethodException {
        return this.reflectionGettterSettterMethod("set", field);
    }

    private Method reflectionGetMethod(Field field) throws NoSuchMethodException {
        return this.reflectionGettterSettterMethod("get", field);
    }

    private Method reflectionGettterSettterMethod(String prefix, Field field) throws NoSuchMethodException {
        String[] splitFieldName = field.getName().split("");
        splitFieldName[0] = splitFieldName[0].toUpperCase();
        String fieldNameCameOnCase = String.join("", splitFieldName);
        Class<? extends ActiveRecord> clazz = this.getClass();
        Class<?> paramType = field.getType();
        String methodName = prefix + fieldNameCameOnCase;
        return switch (prefix){
            case "get" -> clazz.getMethod(methodName);
            case "set" -> clazz.getMethod(methodName, paramType);
            default -> throw new NoSuchMethodException();
        };
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
