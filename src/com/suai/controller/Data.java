package com.suai.controller;

import com.suai.model.TFTPServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Data {

  // 2 byte (OPCODE) + 2 byte (#Block) + max 512 byte (Data)

  private final byte OPCODE = 3;
  private byte[] data;

  public Data(int block, byte[] d) throws Exception {
    if (block < 0 || d == null)
      throw new Exception("Incorecrt block or byte[] in DATA constructor");
    data = new byte[d.length + 4];
    byte zero = 0;
    data[0] = zero;
    data[1] = (byte) OPCODE;
    byte[] b = ByteBuffer.allocate(4).putInt(block).array();
    data[2] = b[2];
    data[3] = b[3];
    for (int i = 0; i < d.length; i++) {
      data[i + 4] = d[i];
    }
  }

  public Data(byte[] d) throws Exception {
      if (d == null || d.length < 4) {
        throw new Exception("Incorrect byte[] in constructor DATA");
      }
      data = d;
  }

  public byte[] getOPCODE() throws Exception {
    byte[] opcode = {data[0], data[1]};
    if (opcode[1] != 3) {
      throw new Exception();
    }
    return opcode;
  }


  public int getBlock() {
    byte[] block = {0, 0, data[2], data[3]};
    return ByteBuffer.wrap(block).getInt();
  }


  public void sendData(DatagramSocket socket, InetAddress ipAddress, int port) throws Exception {
      DatagramPacket sendData = new DatagramPacket(data, data.length, ipAddress, port);
      socket.send(sendData);
  }


  public void sendDataReport() {
    try {
      if (Sender.sender) {
        TFTPServer.fileReport
            .write(TFTPServer.serverName + "sent a block of data " + getBlock() + "." +  TFTPServer.getTime());
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }


  public void receiveDataReport() {
    try {
      if (Receiver.receiver) {
        TFTPServer.fileReport
            .write(TFTPServer.serverName + "received data block " + getBlock() + "." +  TFTPServer.getTime());
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
}
