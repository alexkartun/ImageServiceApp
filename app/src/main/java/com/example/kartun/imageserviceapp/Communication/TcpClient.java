package com.example.kartun.imageserviceapp.Communication;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Tcp client channel that responsible to connect to server and send data.
 */
public class TcpClient implements  ITcpClient {

    // Ip. For cellphone use your computer id. F.e. ipv4-address: 10.0.0.5
    private static String serverIp = "10.0.2.2";
    // Port
    private static int serverPort = 8001;

    private Socket socket;
    private DataOutputStream dataOutputStream;

    public TcpClient() {

        this.socket = null;
        this.dataOutputStream = null;
    }

    @Override
    public Boolean connect() {
        try {

            this.socket = new Socket(InetAddress.getByName(serverIp), serverPort);
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

            return true;
        } catch (IOException e) {

            Log.e("TCP", "C: Error", e);

        }
        return false;
    }

    @Override
    public void disconnect() {
        try {

            this.socket.close();

        } catch (IOException e) {

            Log.e("TCP", "C: Error", e);

        }
    }

    @Override
    public void write(int num, byte[] bytes) {
        try {

            this.dataOutputStream.writeInt(num);
            this.dataOutputStream.write(bytes);
            this.dataOutputStream.flush();

        } catch (IOException e) {

            Log.e("TCP", "C: Error", e);

        }
    }
}
