package com.raspiserver;

import com.pi4j.io.gpio.*;

/**
 * Created by phtms on 22.02.2017.
 */
public class SmartPlug extends Thread {
    static final GpioController gpio = GpioFactory.getInstance();
    static final GpioPinDigitalOutput pinON = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22);
    static final GpioPinDigitalOutput pinOFF = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_26);

    public void run(){
        while(true) {
            if (Boolean.parseBoolean(config.ustawienia.getProperty("SmartPlug"))){
                pinON.pulse(200, true);

            }
            else{
                pinOFF.pulse(200, true);
            }
            try{
                Thread.sleep(10000);
            } catch(Exception e){
            }
        }
    }
}
