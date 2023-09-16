package oe.aloha.NetværkController;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.packet.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientModule implements Runnable {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientModule() {

    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", 8080);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            receiveMessage();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String messageText) throws IOException {
        Message message = new Message(messageText);
        output.writeObject(message);
    }

    public void receiveMessage() {
        try {
            while (true) {
                Packet response = (Packet) input.readObject();
                System.out.println("Response from someone: " + response);
                socket.close();
            }
        } catch (SocketException e) {
            System.out.println("Server is shutting down");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}




