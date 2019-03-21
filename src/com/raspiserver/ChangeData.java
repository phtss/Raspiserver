package com.raspiserver;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Properties;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

/**
 * Created by phtms on 16.02.2017.
 */
public class ChangeData extends Thread {
    private ObjectInputStream inputNetwork;
    private ObjectOutputStream outputNetwork;
    private ServerSocket server;
    private int Port;
    private int Mode;

    ChangeData(int port, int mode){
        Port = port;
        Mode = mode;
    }

    public void run(){
        if(Mode==1) WaitForData();
        if(Mode==2) SendData();
    }

    private void WaitForData(){
        while(true) {
            try {
                //server = new ServerSocket(Port);
                //Socket phoneSocket = server.accept();
                SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(Port);
                SSLSocket socket = (SSLSocket) server.accept();
                inputNetwork = new ObjectInputStream(socket.getInputStream());

               // inputNetwork = new ObjectInputStream(phoneSocket.getInputStream());
                config.ustawienia = (Properties) inputNetwork.readObject();
                Main.ServerOn.setState(false);
                System.out.print("Zmienilem ustawienia! \n");
                System.out.print("Sensor1: " + config.ustawienia.getProperty("Sensor1") + "\n");
                System.out.print("Sensor2: " + config.ustawienia.getProperty("Sensor2") + "\n");
                System.out.print("Smartplug: " + config.ustawienia.getProperty("SmartPlug") + "\n");
                server.close();

            } catch (Exception e) {
                System.out.print("Czekam! " + e + "\n");
            }
            try{
                Thread.sleep(100);
                Main.ServerOn.setState(true);
            } catch (Exception e){

            }
        }
    }

    private void SendData(){
          while(true) {
            try {
                SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
                SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(Port);
                SSLSocket socket = (SSLSocket) server.accept();
                System.out.print("Wysylam! \n");
                outputNetwork = new ObjectOutputStream(socket.getOutputStream());
                outputNetwork.writeObject(config.ustawienia);
                outputNetwork.flush();
            } catch (Exception e) {
            }
        }
    }
}
