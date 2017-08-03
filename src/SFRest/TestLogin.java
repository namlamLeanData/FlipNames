package SFRest;

import org.testng.annotations.Test;
import org.testng.Assert;


public class TestLogin {
  @Test
  public void testLogin() {
	  Assert.assertEquals(fn.doLogin(), 200, "baseUri: https://continuous-integration.my.salesforce.com/services/apexrest/QATestLeads");
  }
  
  FlipNames fn = new FlipNames();
}
