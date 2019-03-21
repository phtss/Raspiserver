package com.raspiserver;

import java.net.*;
import java.net.ServerSocket;
import java.io.*;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Created by phtms on 09.02.2017.
 */
public class Effector extends Thread {
    private int position;
    private int Move;
    private byte[] steps = new byte[8];
    private DataInputStream inputNetwork;
    private ServerSocket server;

    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput[] piny = {
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "1", PinState.LOW),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "2", PinState.LOW),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "3", PinState.LOW),
            gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "4", PinState.LOW)
    };
    final GpioStepperMotorComponent motor = new GpioStepperMotorComponent(piny);

    public Effector() {
        steps[0] = (byte) 0b0001;
        steps[1] = (byte) 0b0011;
        steps[2] = (byte) 0b0010;
        steps[3] = (byte) 0b0110;
        steps[4] = (byte) 0b0100;
        steps[5] = (byte) 0b1100;
        steps[6] = (byte) 0b1000;
        steps[7] = (byte) 0b1001;

        motor.setStepInterval(2);
        motor.setStepSequence(steps);

        motor.setStepsPerRevolution(4096);

        Move = position = Integer.parseInt(config.ustawienia.getProperty("position"));

    }


    @Override
    public void run(){
        while(true) {
            changeposition(Move);

            try {
                SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(9004);
                SSLSocket socket = (SSLSocket) server.accept();
                inputNetwork = new DataInputStream(socket.getInputStream());
                Move = inputNetwork.readInt();
                server.close();
                inputNetwork.close();

            } catch (Exception e) {
            }
        }
    }

    private void changeposition(int move){
       if(Move != position){
           System.out.print("Pozycja\n" + (move - position)*4096 / 100 + "\n");
           motor.step(((move - position) * 4096) / 100);
           config.ustawienia.setProperty("position", String.valueOf(move));
           config.saveConfig();
       }

       position = move;

    }
}
