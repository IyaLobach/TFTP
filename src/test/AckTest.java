package test;

import com.suai.controller.Ack;
import org.junit.Assert;
import org.junit.Test;


public class AckTest {

  @Test (expected = Exception.class)
  public void AckTest1() throws Exception {
    int block = -1;
    Ack ack = new Ack(block);
  }

  @Test (expected = Exception.class)
  public void ACKTest2() throws Exception {
    byte[] a = {0,0,0};
    Ack ack = new Ack(a);
  }

  @Test
  public void ACKTest3() throws Exception {
    byte[] a = {0,0,0,0};
    Ack ack = new Ack(a);
  }

  @Test
  public void getBlockTest() throws Exception {
   Ack ack1  = new Ack(new byte[]{ 0, 4, 0, 1});
   Assert.assertEquals(1, ack1.getBlock());
   Ack ack2  = new Ack(new byte[]{ 0, 4, 0, 2});
   Assert.assertEquals(2, ack2.getBlock());
  }

  @Test (expected =  Exception.class)
  public void sendASKTest() throws Exception {
    Ack ack = new Ack(0);
    ack.sendAck(null, null, 0);
  }

}
