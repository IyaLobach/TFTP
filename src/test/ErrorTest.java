package test;

import com.suai.controller.Error;
import org.junit.Assert;
import org.junit.Test;

public class ErrorTest {

  @Test
  public void gerMessageTest() throws Exception {
    Error e1 = new Error(" ", (byte) 0);
    Assert.assertEquals("Unknown operation. ", e1.getMessage());
    Error e2 = new Error(" ", (byte) 1);
    Assert.assertEquals("File not found. ", e2.getMessage());
    Error e3 = new Error(" ", (byte) 2);
    Assert.assertEquals("Incorrect format. ", e3.getMessage());
  }

  @Test(expected = Exception.class)
  public void ERRORTest() throws Exception {
    Error error = new Error(new byte[]{0, 0, 0});
  }

  @Test(expected = Exception.class)
  public void ERRORTest2() throws Exception {
    Error error = new Error(" ", (byte) 9);
  }

  @Test(expected = Exception.class)
  public void ERRORTest3() throws Exception {
    Error error = new Error(new byte[]{0, 2, 0, 0, 0, 0, 0});
  }

  @Test(expected = Exception.class)
  public void sendERRORTest() throws Exception {
    Error error = new Error("Error", (byte) 0);
    error.sendError(null, null, 0);
  }

  @Test (expected =  Exception.class)
  public void sendERRRORTest() throws Exception {
    Error e = new Error(" ", (byte)0);
    e.sendError(null, null, -1);
  }


}
