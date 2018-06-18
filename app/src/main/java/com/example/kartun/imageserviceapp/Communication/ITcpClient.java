package com.example.kartun.imageserviceapp.Communication;

public interface ITcpClient {

    /**
     * Connect to server. Fail return false. Success return true.
     * @return true/false depends on connect status.
     */
    Boolean connect();

    /**
     * Disconnect from server. Close the socket.
     */
    void disconnect();

    /**
     * Write to server int number and after that array of bytes.
     * @param num number to write
     * @param bytes bytes to write
     */
    void write(int num, byte[] bytes);
}
