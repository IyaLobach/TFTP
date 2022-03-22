package com.suai.controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

// 2 byte (OPCODE) + String (filename) + 1 byte (0) + String (Mode) + 1 byte (0)
public class Request {

  private byte[] request;
  private final String mode = "octet";


  public Request(byte[] r) throws Exception {
    if (r.length < 6)
      throw new Exception("Incorrect byte[] in REQUEST constructor");
    request = r;
  }

  public Request(byte OPCODE, String filename) throws Exception {
    if (OPCODE != 2 && OPCODE != 6 && OPCODE != 1)
      throw new Exception("Incorrect OPCODE REQUEST");
    if (filename == null)
      throw new Exception("Incorrect filename REQUEST");
    byte zero = 0;
    int requestLength = 2 + filename.length() + 1 + mode.length() + 1;
    request = new byte[requestLength];
    int pos = 0;
    request[pos] = zero;
    pos++;
    request[pos] = OPCODE;
    pos++;
    for (int i = 0; i < filename.length(); i++) {
      request[pos] = (byte) filename.charAt(i);
      pos++;
    }
    request[pos] = zero;
    pos++;
    for (int i = 0; i < mode.length(); i++) {
      request[pos] = (byte) mode.charAt(i);
      pos++;
    }
    request[pos] = zero;
  }

  public byte getOpcode(){
    return request[1];
  }

  public String getFilename()
  {
    int i = 2;
    for (; i < request.length; i++) {
      if(request[i] == 0)
        break;
    }
    return new String(request,2, i-2);
  }

  public void sendRequest(DatagramSocket socket, InetAddress ipAddress, int port) {
    try {
      DatagramPacket sendREQ = new DatagramPacket(request, request.length, ipAddress, port);
      socket.send(sendREQ);
    }
    catch(Exception e){
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }

  public byte[] getRequest() {
    return request;
  }


}
