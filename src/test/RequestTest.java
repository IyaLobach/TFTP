package test;
import com.suai.controller.Request;
import org.junit.Assert;
import org.junit.Test;

public class RequestTest {

  @Test (expected = Exception.class)
  public void REQUESTTest() throws Exception {
    Request r = new Request(new byte[]{0,0,0});
  }

  @Test (expected = Exception.class)
  public void REQUESTTest2() throws Exception {
    Request r = new Request((byte) 4, " ");
  }

  @Test
  public void getOPCODE() throws Exception {
    Request r1 = new Request((byte)1, "Hello.txt");
    Assert.assertEquals(1, r1.getOpcode());
    Request r2 = new Request((byte)2, "Hello.txt");
    Assert.assertEquals(2, r2.getOpcode());
  }
  @Test
  public void getFilename() throws Exception {
    Request r1 = new Request((byte)1, "Hello.txt");
    Assert.assertEquals("Hello.txt", r1.getFilename());
    Request r2 = new Request((byte)2, "Hi.bmp");
    Assert.assertEquals("Hi.bmp", r2.getFilename());
  }
}
