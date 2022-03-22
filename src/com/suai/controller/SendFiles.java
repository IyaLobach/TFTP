package com.suai.controller;


import com.suai.model.TFTPServer;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class SendFiles {

  private int dataSize = 512;


  public SendFiles(DatagramSocket socket, InetAddress ipAddressClient, int portClient,
      String serverDirectory) throws Exception {
    Sender.sender = true;
    byte[] names = filesList(serverDirectory);
    int blockNumber = names.length / dataSize;
    int block = 1;
    for (int index = 0; index < blockNumber; index++) {
      byte[] data = new byte[dataSize];
      for (int j = 0; j < dataSize; j++) {
        data[j] = names[j + dataSize * index];
      }
      Data d = new Data(block, data);
      d.sendData(socket, ipAddressClient, portClient);
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
      block++;
    }
    int lastSize = names.length % 512;
    if (lastSize != 0) {
      byte[] data = new byte[lastSize];
      for (int index = 0; index < lastSize; index++) {
        data[index] = (byte) names[index + 512 * blockNumber];
      }
      Data d = new Data(block, data);
      d.sendData(socket, ipAddressClient, portClient);
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
      d.sendData(socket, ipAddressClient, portClient);
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
    }
    writeSendReport();
    Sender.sender = false;
  }

  public byte[] filesList(String serverDirectory) {
    StringBuilder names = new StringBuilder();
    File dir = new File(serverDirectory);
    for (File file : dir.listFiles()) {
      if (file.isFile()) {
        names.append(file.getName()).append("\n");
      }
    }
    return names.toString().getBytes(StandardCharsets.UTF_8);
  }

  public void writeSendReport() throws Exception {
    TFTPServer.fileReport.write(TFTPServer.clientName + "successfully got the list of files." + TFTPServer.getTime());
  }
}
