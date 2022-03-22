package com.suai.model;


import com.suai.controller.Error;
import com.suai.controller.Receiver;
import com.suai.controller.Request;
import com.suai.controller.SendFiles;
import com.suai.controller.Sender;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.ZonedDateTime;
import java.util.Date;

public class TFTPServer {

  private DatagramSocket serverSocket;
  private InetAddress ipAddressClient = null;
  private int portClient = 0;
  private int portServer = 69;
  private String serverDirectory = "C:/Users/iyush/IdeaProjects/TFTP/Server/";
  public static FileWriter fileReport;
  public static String clientName;
  public static String serverName = "Server: ";


  public void startServer() {
    byte[] clientMessageByte = new byte[100];
    DatagramPacket receiveClientMessagePacket = new DatagramPacket(clientMessageByte,
        clientMessageByte.length);
    try {
      serverSocket = new DatagramSocket(portServer);
      System.out.println("Server started working");
      while (true) {
        fileReport = new FileWriter("C:/Users/iyush/IdeaProjects/TFTP/Server.log", true);
        serverSocket.receive(receiveClientMessagePacket); // получение запроса
        ipAddressClient = receiveClientMessagePacket.getAddress();
        portClient = receiveClientMessagePacket.getPort();
        clientName = "Client" + portClient + ": ";
        fileReport.write("\n" + clientName + "join." + TFTPServer.getTime());
        fileReport.flush();
        Request request = new Request(receiveClientMessagePacket.getData());
        byte OPCODE = request.getOpcode();
        if (OPCODE == 1) {
          String filename = request.getFilename();
          fileReport
              .write(clientName + "wants to download file " + filename + TFTPServer.getTime());
          Sender sender = new Sender(serverDirectory + filename, serverSocket, portClient,
              ipAddressClient, true);
          fileReport.flush();
        }
        if (OPCODE == 2) {
          String filename = request.getFilename();
          fileReport.write(clientName + "wants to load file  " + filename + TFTPServer.getTime());
          Receiver receiver = new Receiver(ipAddressClient, portClient, serverSocket,
              serverDirectory + filename, true);
          fileReport.flush();
        }
        if (OPCODE == 6) {
          fileReport.write(clientName + "wants to show." + TFTPServer.getTime());
          SendFiles senderFiles = new SendFiles(serverSocket, ipAddressClient, portClient,
              serverDirectory);
          fileReport.flush();
        }
        if (OPCODE == 5) {
          Error error = new Error(receiveClientMessagePacket.getData());
          fileReport.write(
              clientName + error.getMessage() + "The connection was interrupted." + TFTPServer
                  .getTime());
          fileReport.flush();
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        fileReport.close();
        serverSocket.close();
      } catch (Exception e) {
        System.out.println(e.getMessage());
        e.printStackTrace();
      }
    }
  }

  public static String getTime() {
    StringBuilder str = new StringBuilder();
    Date d = new Date();
    int year = d.getYear() + 1900;
    str.append(" ").append(year).append(" ").append(d.getDate()).append("/")
        .append(d.getMonth()).append(" ").append(d.getHours())
        .append(":").append(d.getMinutes()).append(":").append(d.getSeconds()).append("\n");
    return str.toString();
  }

}
