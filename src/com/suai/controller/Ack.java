package com.suai.controller;

import com.suai.model.TFTPServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class Ack {

  // 2 bytes (OPCODE) + 2 bytes (#BLOCK)

  private final byte OPCODE = 4;
  private byte[] Ack = new byte[4];


  public Ack(int block) throws Exception {
    if (block < 0) {
      throw new Exception("Incorrect block in ACK constructor");
    }
    Ack[0] = 0;
    Ack[1] = OPCODE;
    byte[] b = ByteBuffer.allocate(4).putInt(block).array();
    Ack[2] = b[2];
    Ack[3] = b[3];
  }

  public Ack(byte[] bytes) throws Exception {
    if (bytes.length < 4) {
      throw new Exception("Incorrect byte[] in ACK constructor");
    }
    Ack[0] = 0;
    Ack[1] = bytes[1];
    Ack[2] = bytes[2];
    Ack[3] = bytes[3];
  }

  public int getBlock() throws Exception {
    byte[] block = {0, 0, Ack[2], Ack[3]};
    if (ByteBuffer.wrap(block).getInt() < 0) {
      throw new Exception("Incorrect block in Data getBlock()");
    }
    return ByteBuffer.wrap(block).getInt();
  }


  public void sendAck(DatagramSocket socket, InetAddress ipAddress, int port) throws Exception {
    if (Ack != null & Ack.length != 4)
      throw new Exception("Incorrect Ack to send");
    DatagramPacket sendAck = new DatagramPacket(Ack, Ack.length, ipAddress, port);
    socket.send(sendAck);
  }

  public void receiveAckReport() throws Exception {
    if (Sender.sender) {
      TFTPServer.fileReport.write(
          TFTPServer.serverName + "received a response about the successful receipt of the block " + getBlock()
              + "." + TFTPServer.getTime());
    }
  }

  public void sendAckReport() throws Exception {
    if (Receiver.receiver) {
      TFTPServer.fileReport.write(
          TFTPServer.serverName + "sent a response about the successful receipt of the block " + getBlock()
              + "." + TFTPServer.getTime());
    }
  }
}
