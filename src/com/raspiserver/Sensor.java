package com.raspiserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.pi4j.io.gpio.*;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * Created by phtms on 09.02.2017.
 */
public class Sensor extends Thread {
    static private DataOutputStream outputNetwork;
    static private ServerSocket server;

    private int id;
    private int Type;
    private boolean enabled;
    private GpioPinDigitalInput Sensor ;

    final GpioController gpio = GpioFactory.getInstance();

    public Sensor (GpioPinDigitalInput a, int ID, int type){
        Sensor = a;
        id = ID;
        Type = type;
    }

    @Override
    public void run() {
        while(true) {
            //if(Boolean.parseBoolean(config.ustawienia.getProperty("Sensor" + id)) && Sensor.isLow() && Type == 1) sendMSG(id);
            //if(Boolean.parseBoolean(config.ustawienia.getProperty("Sensor" + id)) && Sensor.isHigh() && Type == 2) System.out.print("ruch! \n");

            System.out.print("\n" + Sensor.isHigh());

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //sendMSG(id);
        }
    }

    static private void sendMSG(int a){
        System.out.print("Wysylam sygnal: " + a + "\n");
        try {
            SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(9005);
            SSLSocket socket = (SSLSocket) server.accept();
            outputNetwork = new DataOutputStream(socket.getOutputStream());
            outputNetwork.writeInt(a);
            outputNetwork.flush();
            server.close();
        } catch (Exception e) {
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
