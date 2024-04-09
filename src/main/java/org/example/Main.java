package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
       Teste teste = new Teste();

       teste.findAll();
    }
}