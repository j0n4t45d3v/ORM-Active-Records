package org.example;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Main {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
       Teste teste = new Teste();
       Teste teste1 = new Teste();
       teste1.setId(5);
       teste1.setName("Teste Insert");
       teste1.save();

       List<Teste> teste2 = teste.findAll();
        Gson gson = new Gson();
        System.out.println(gson.toJson(teste2));
    }
}