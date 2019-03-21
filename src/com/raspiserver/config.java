package com.raspiserver;

import java.io.*;
import java.util.Properties;
import java.io.FileInputStream;

/**
 * Created by phtms on 15.02.2017.
 */
public class config extends Thread{
    static public Properties ustawienia = new Properties();

    public config(){
    }

    static void loadConfig(){
        try {
            ustawienia.load(new FileInputStream("/home/pi/IdeaProjects/RaspiServer/classes/config.properties"));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    static void saveConfig(){
        try {
            ustawienia.store(new FileOutputStream("/home/pi/IdeaProjects/RaspiServer/classes/config.properties"), "");

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
