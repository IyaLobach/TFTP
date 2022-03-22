package com.suai.controller;

import com.suai.model.TFTPServer;
import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Receiver { // получение файла

  private final int packetSize = 516; // максимальный размер пакета
  protected static boolean receiver;


  public Receiver(InetAddress ip, int p, DatagramSocket d, String f, boolean r) throws Exception{
      receiver = r;
      if (f == null)
        throw new Exception("Incorrect filename in Receiver constructor");
      receiveFile(d, ip, p, f);
  }

  // receiver = true ServerReceiveFromClient
  // receiver = false ClientReceiveFromServer

  public void receiveFile(DatagramSocket datagramSocket, InetAddress ipAddress, int port,
      String filename) throws Exception { // получение файла
    if (receiver){
      Ack ack = new Ack((int)0);
      ack.sendAck(datagramSocket, ipAddress, port);
      ack.sendAckReport();
    }
    ArrayList tmpData = new ArrayList();
    int block = 1;
    byte[] bufferByteArray = null;
    DatagramPacket inPacket = null;
    do {
      bufferByteArray = new byte[packetSize];
      inPacket = new DatagramPacket(bufferByteArray, bufferByteArray.length);
      datagramSocket.receive(inPacket);
      byte[] opCode = {bufferByteArray[0], bufferByteArray[1]};
      if (opCode[1] == 5) { // код ошибки
        Error error = new Error(inPacket.getData());
        if (!receiver) {
          System.out.println(error.getMessage());
        } else {
          error.writeReceiveError();
        }
        return;
      } else if (opCode[1] == 3) { // признак пакета с данными
        Data d = new Data(inPacket.getData());
        d.receiveDataReport();
        for (int index = 4; index < inPacket.getLength(); index++) {
          tmpData.add(bufferByteArray[index]);
        }
        Ack ack = new Ack(d.getBlock()); // отправка получения блока
        ack.sendAck(datagramSocket, inPacket.getAddress(), inPacket.getPort());
      }
      block++;
    } while (!isLastPacket(inPacket));
    writeFile(tmpData, filename);
    writeReceiveReport();
  }

  // запись файла в файл назначения
  private void writeFile(ArrayList tmpData, String fileName) throws Exception {
    File file = new File(fileName);
    FileOutputStream receiveFile = new FileOutputStream(file);
    for (int i = 0; i < tmpData.size(); i++) {
      receiveFile.write((byte) tmpData.get(i));
    }
    receiveFile.close();
  }

  private boolean isLastPacket(DatagramPacket datagramPacket) {
    if (datagramPacket.getLength() < 512) {
      return true;
    } else {
      return false;
    }
  }

  public void writeReceiveReport() throws Exception {
    if (receiver) {
      TFTPServer.fileReport.write(TFTPServer.clientName + "successfully uploaded the file." + TFTPServer.getTime());
    }
  }
}

