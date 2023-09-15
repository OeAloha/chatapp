package oe.aloha.NetværkController;

import oe.aloha.Entities.Packet;
import oe.aloha.Entities.packet.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientModule implements Runnable {
    Socket socket;
    ObjectInputStream input;
    ObjectOutputStream output;
    public ClientModule() throws IOException {
         socket = new Socket("localhost",8080);
         input = new ObjectInputStream(socket.getInputStream());
         output = new ObjectOutputStream(socket.getOutputStream());
    }


    public void sendMessage(String messageText) throws IOException {
        Message message = new Message(messageText);
        output.writeObject(message);
    }

    public void recieveMessage(){
        new Thread(() -> {
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
        }).start();

    }

    @Override
    public void run() {

    }
}









