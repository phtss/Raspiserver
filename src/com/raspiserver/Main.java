package com.raspiserver;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.io.*;


public class Main {
    //Inicjacja GPIO, Lampka LED sygnalizuje uruchomienie się systemu RaspiServer
    static final GpioController gpio = GpioFactory.getInstance();
    static final GpioPinDigitalOutput ServerOn = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "LEDON", PinState.HIGH);

    public static void main(String[] args) throws IOException {
        // Załadowanie ustawien z pliku
        System.setProperty("javax.net.ssl.keyStore", "/home/pi/IdeaProjects/RaspiServer/classes/server.ks");
        System.setProperty("javax.net.ssl.keyStorePassword", "pg2017");
        config.loadConfig();



        // Stworzenie obiektów dla elementów podłączonych do GPIO
        Sensor CzujnikRuchu = new Sensor(gpio.provisionDigitalInputPin(RaspiPin.GPIO_13),1, 2);
        Sensor CzujnikGazu = new Sensor(gpio.provisionDigitalInputPin(RaspiPin.GPIO_12), 2, 1);
        Effector SilnikKrokowy = new Effector();
        ChangeData WysylanieDanych = new ChangeData(9008, 2);
        ChangeData OdbieranieDanych = new ChangeData(9007, 1);
        SmartPlug gniazdko = new SmartPlug();

        // Uruchomianie wątków
        //CzujnikGazu.start();
        //CzujnikRuchu.start();
        SilnikKrokowy.start();
        WysylanieDanych.start();
        OdbieranieDanych.start();
        gniazdko.start();
    }
}