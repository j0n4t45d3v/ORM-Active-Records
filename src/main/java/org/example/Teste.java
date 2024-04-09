package org.example;

public class Teste extends ActiveRecord{
    private int id;
    private String name;
    static {
        TABLE_NAME = "users";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
