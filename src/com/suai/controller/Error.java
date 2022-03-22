package com.suai.controller;

import com.suai.model.TFTPServer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class Error {

  private final byte OPCODE = 5;
  private byte[] error;
  private String message;
  private final byte ERRCODE;


  public Error(String m, byte errcode) throws Exception {
    if (m == null | errcode > 8)
      throw new Exception("Incorrect ERROR constructor");
    message = m;
    error = new byte[2 + 2 + message.length() + 1];
    byte zero = 0;
    error[0] = zero;
    error[1] = OPCODE;
    error[2] = 0;
    ERRCODE = errcode;
    error[3] = errcode;
    int i = 0;
    for (; i < message.length(); i++) {
      error[i + 4] = (byte) message.charAt(i);
    }
    error[i + 4] = zero;
  }

  public Error(byte[] e) throws Exception {
    if (e.length < 6 | e[1] !=5 )
      throw new Exception("Incorreect byte[] in ERROR constructor");
    error = e;
    ERRCODE = error[3];
    int i = 4;
    byte[] m = new byte[e.length - 2 - 2 - 1];
    while (error[i] != 0) {
      m[i - 4] = e[i];
      i++;
    }
    message = new String(m, StandardCharsets.UTF_8);
  }

  public void sendError(DatagramSocket socket, InetAddress ipAddress, int port) throws Exception {
    DatagramPacket sendError = new DatagramPacket(error, error.length, ipAddress, port);
    socket.send(sendError);
  }

  public String getMessage() {
    if (ERRCODE == 2){
      return "Incorrect format. ";
    }
    if (ERRCODE  == 0){
      return "Unknown operation. ";
    }
    if (ERRCODE  == 1){
      return "File not found. ";
    }

    return message;
  }

  public void writeSendError() throws Exception {
      if (Sender.sender) {
        TFTPServer.fileReport
            .write(TFTPServer.serverName + this.getMessage() + "The connection was interrupted." + TFTPServer.getTime());
      }
  }

  public void writeReceiveError() throws Exception {
      if (Receiver.receiver) {
        TFTPServer.fileReport.write(TFTPServer.serverName + this.getMessage() + "The connection was interrupted." + TFTPServer.getTime());
      }
  }


}
