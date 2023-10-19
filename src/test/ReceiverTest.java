package test;

import com.suai.controller.Receiver;
import org.junit.Test;

public class ReceiverTest {

  @Test(expected = Exception.class)
  public void ReceiverTest() throws Exception {
    Receiver receiver = new Receiver(null, 0, null, " ", true);
  }


}
