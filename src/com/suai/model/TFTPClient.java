package com.suai.model;


import com.suai.controller.Error;
import com.suai.controller.ReceiveFiles;
import com.suai.controller.Request;
import com.suai.controller.Receiver;
import com.suai.controller.Sender;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class TFTPClient {

  private DatagramSocket clientSocket;
  private InetAddress ipAddressServer;
  private InetAddress ipAddressClient;
  private int portServer;
  private int portClient;
  private String clientDirectory = "C:/Users/iyush/IdeaProjects/TFTP/Client/";


  public void startClient() {
    try {
      while (true) {
        clientSocket = new DatagramSocket();
        portClient = clientSocket.getLocalPort();
        ipAddressClient = clientSocket.getLocalAddress();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Type ipAddress: ");
        if (scanner.hasNext()) {
          ipAddressServer = InetAddress.getByName(scanner.next());
          System.out.print("Type port: ");
          if (scanner.hasNextInt()) {
            portServer = scanner.nextInt();
          }
        }
        System.out.println("@load or @download or @show");
        scanner = new Scanner(System.in);
        String inputMessage = scanner.nextLine();
        if (inputMessage.contains("@download")) { // ввести проверки на корректность формата и файла
          if (inputMessage.length() <= 9) {
            Error error = new Error("Incorrect format. ", (byte) 2);
            error.sendError(clientSocket, ipAddressServer, portServer);
            System.out.println(error.getMessage());
            clientSocket.close();
            continue;
          }
          String filename = inputMessage.substring(10);
          Request request = new Request((byte) 1, filename);
          DatagramPacket requestDownloadFile = new DatagramPacket(request.getRequest(),
              request.getRequest().length, ipAddressServer, portServer);
          clientSocket.send(requestDownloadFile);
          Receiver receiver = new Receiver(ipAddressServer, portServer, clientSocket,
              clientDirectory + filename, false);
          System.out.println("File downloaded successfully.");
          clientSocket.close();
          continue;
        }
        if (inputMessage.contains("@load")) { // ввести проверки на корректность формата и файла
          if (inputMessage.length() <= 5) {
            Error error = new Error("Incorrect format. ", (byte) 2);
            error.sendError(clientSocket, ipAddressServer, portServer);
            System.out.println(error.getMessage());
            clientSocket.close();
            continue;
          }
          String filename = inputMessage.substring(6);
          Request request = new Request((byte) 2, filename);
          request.sendRequest(clientSocket, ipAddressServer, portServer);
          Sender sender = new Sender(clientDirectory + filename, clientSocket, portServer,
                        ipAddressServer, false);
          System.out.println("File uploaded successfully.");
          clientSocket.close();
          continue;
        }
        if (inputMessage.contains("@show")) {
          Request request = new Request((byte) 6, " ");
          request.sendRequest(clientSocket, ipAddressServer, portServer);
          ReceiveFiles receiverFiles = new ReceiveFiles(ipAddressServer, portServer, clientSocket);
          clientSocket.close();
          continue;
        }

        Error error = new Error("Unknown operation. ", (byte) 0);
        error.sendError(clientSocket, ipAddressServer, portServer);
        System.out.println(error.getMessage());
        clientSocket.close();

      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    } finally {
      clientSocket.close();
    }
  }
}


