package test;

import com.suai.controller.Data;
import org.junit.Assert;
import org.junit.Test;

public class DataTest {

  @Test (expected = Exception.class)
  public void DATATest1() throws Exception {
      byte[] d = {0, 0, 0};
      Data data = new Data(d);
  }

  @Test (expected = Exception.class)
  public void DATATest2() throws Exception {
    byte[] d = null;
    Data data = new Data(d);
  }

  @Test (expected = Exception.class)
  public void DATATest3() throws Exception {
    byte[] d = new byte[10];
    Data data = new Data(-1, d);
  }

  @Test
  public void getBlockTest() throws Exception{
    byte[] d = {0, 0, 0, 1, 0};
    Data data = new Data(d);
    Assert.assertEquals(1, data.getBlock());
  }

  @Test
  public void getOPCODETest() throws Exception {
    byte[] d = {0, 3, 0, 0, 0, 0};
    Data data = new Data(d);
    Assert.assertArrayEquals(new byte[]{0,3}, data.getOPCODE());
  }

  @Test (expected = Exception.class)
  public void getOPCODETest2() throws Exception{
    byte[] d = new byte[10];
    Data data = new Data(d);
    data.getOPCODE();
  }

}
