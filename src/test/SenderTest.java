package test;
import com.suai.controller.Sender;
import org.junit.Test;

public class SenderTest {

  @Test (expected = Exception.class)
  public void SenderTest() throws Exception {
    Sender sender = new Sender(" ", null, 0, null, true);
  }

}
