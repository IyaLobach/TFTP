package com.suai.controller;

import com.suai.model.TFTPServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Sender { // отправка файла клиенту

  private int dataSize = 512;
  protected static boolean sender;
  private FileWriter fileReport;

  // sender = true ServerSendToClient
  // sender = false ClientSendToServer

  public Sender(String filename, DatagramSocket socket, int port, InetAddress ipAddress,
      boolean s) throws Exception {
      sender = s;
      if (filename == null)
        throw new Exception("Incorrect file in Sender constructor");
      sendFile(filename, socket, port, ipAddress);
  }

  public void sendFile(String filename, DatagramSocket socket, int port, InetAddress ipAddress)
      throws Exception {
    if (!sender) {
      byte[] a = new byte[516];
      DatagramPacket waitAck = new DatagramPacket(a, a.length);
      socket.receive(waitAck);
      Ack ack = new Ack(a);
      ack.receiveAckReport();
      port = waitAck.getPort();
      ipAddress = waitAck.getAddress();
    }
    File file = new File(filename);
    if (!checkFile(filename)) {
      Error error = new Error("File not found. ", (byte) 1);
      error.sendError(socket, ipAddress, port);
      if (sender) {
        error.writeSendError();
      } else {
        System.out.println(error.getMessage());
      }
      return;
    }
    FileInputStream fileToLoad = null;
    try {
      fileToLoad = new FileInputStream(file);
      int i = 0;
      ArrayList tmpData = new ArrayList();
      while ((i = fileToLoad.read()) != -1) {
        tmpData.add((byte) (i));
      }
      int blochNumber = tmpData.size() / 512;
      int block = 1;
      for (int index = 0; index < blochNumber; index++) {
        byte[] data = new byte[dataSize];
        for (int j = 0; j < dataSize; j++) {
          data[j] = (byte) tmpData.get(j + dataSize * index);
        }
        Data d = new Data(block, data);
        d.sendData(socket, ipAddress, port);
        d.sendDataReport();
        byte[] tmpAck = new byte[516];
        DatagramPacket waitAck = new DatagramPacket(tmpAck, tmpAck.length);
        socket.receive(waitAck);
        if (tmpAck[1] == 4) {
          Ack ack = new Ack(tmpAck);
          if (ack.getBlock() == block) {
            ack.receiveAckReport();
          }
        }
        block++;
      }
      int lastSize = tmpData.size() % 512;
      if (lastSize != 0) {
        byte[] data = new byte[lastSize];
        for (int index = 0; index < lastSize; index++) {
          data[index] = (byte) tmpData.get(index + 512 * blochNumber);
        }
        Data d = new Data(block, data);
        d.sendData(socket, ipAddress, port);
        d.sendDataReport();
        byte[] tmpAck = new byte[516];
        DatagramPacket waitACK = new DatagramPacket(tmpAck, tmpAck.length);
        socket.receive(waitACK);
        if (tmpAck[1] == 4) {
          Ack ack = new Ack(tmpAck);
          if (ack.getBlock() == block) {
            ack.receiveAckReport();
          }
        }
      } else // в случае, если размер кратен 512
      {
        byte[] zero = new byte[1];
        Data d = new Data(block, zero);
        d.sendData(socket, ipAddress, port);
        d.sendDataReport();
        byte[] tmpAck = new byte[516];
        DatagramPacket waitAck = new DatagramPacket(tmpAck, tmpAck.length, ipAddress, port);
        socket.receive(waitAck);
        if (tmpAck[1] == 4) {
          Ack ack = new Ack(tmpAck);
          if (ack.getBlock() == block) {
            ack.receiveAckReport();
          }
        }
      }
      writeSendReport();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  public void writeSendReport() throws Exception {
    if (sender) {
      TFTPServer.fileReport.write(TFTPServer.clientName + "downloaded the file successfully." + TFTPServer.getTime());
    }
  }

  public static boolean checkFile(String filename) {
    File file = new File(filename);
    return file.exists();
  }
}