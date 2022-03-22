package com.suai.controller;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ReceiveFiles {

  private int packetSize = 516;

  public ReceiveFiles(InetAddress ipAddress, int port, DatagramSocket datagramSocket) throws Exception
  {
    ArrayList tmpData = new ArrayList();
    int block = 1;
    byte [] bufferByteArray = null;
    DatagramPacket inPacket = null;
    do {
      block++;
      bufferByteArray = new byte[packetSize];
      inPacket = new DatagramPacket(bufferByteArray, bufferByteArray.length, ipAddress, port); // надо любой порт
      datagramSocket.receive(inPacket);
      byte[] opCode = { bufferByteArray[0], bufferByteArray[1]};
      if (opCode[1] == 5) { // код ошибки
        System.out.println("ReceiveErorr");
      } else if (opCode[1] == 3) { // признак пакета с данными
        Data d = new Data(inPacket.getData());
        byte[] names = new byte[inPacket.getLength() - 4];
        for (int index = 4; index < inPacket.getLength(); index++)
          names[index - 4] = bufferByteArray[index];
        System.out.println(new String(names, StandardCharsets.UTF_8));
        Ack ack = new Ack(d.getBlock());
        ack.sendAck(datagramSocket, ipAddress, port);
      }
    } while (!isLastPacket(inPacket));
  }

  private boolean isLastPacket(DatagramPacket datagramPacket) {
    if (datagramPacket.getLength() < 512)
      return true;
    else
      return false;
  }
}
