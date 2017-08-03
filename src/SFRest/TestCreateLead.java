package SFRest;

import org.testng.annotations.Test;
import org.testng.Assert;

public class TestCreateLead {
	@Test(priority = 0)
	public void setup() {
		fn.doLogin();
	}
	
	@Test
	public void testCreateLead1() {
		Assert.assertEquals(fn.postLead(first+FlipNames.count, last+FlipNames.count, company+FlipNames.count++), 200);
	}
  
	@Test
	public void testCreateLead2() {
		Assert.assertEquals(fn.postLead(first+FlipNames.count, last+FlipNames.count, company+FlipNames.count++), 200);
	}
  
	@Test
	public void testCreateLead3() {
		Assert.assertEquals(fn.postLead(first+FlipNames.count, last+FlipNames.count, company+FlipNames.count++), 200);
	}
  
	@Test
	public void testCreateLead4() {
		Assert.assertEquals(fn.postLead(first+FlipNames.count, last+FlipNames.count, company+FlipNames.count++), 200);
	}
  
	@Test
	public void testCreateLead5() {
		Assert.assertEquals(fn.postLead(first+FlipNames.count, last+FlipNames.count, company+FlipNames.count++), 200);
	}
	
	// Should fail due to empty strings
	@Test
	public void testCreateLead7() {
		Assert.assertEquals(fn.postLead("",  "",  ""), 500);
	}
	
	// Should fail due to no characters
	@Test
	public void testCreateLead() {
		Assert.assertEquals(fn.postLead(" ", " ", " "), 500);
	}
	
	// Should fail due to null value in required field
	@Test
	public void testCreateLead8() {
		Assert.assertEquals(fn.postLead(first+FlipNames.count,  null,  company+FlipNames.count), 500, "Insertion unsuccessful. Status code: 500");
	}

	// Should fail due to null value in required field
	@Test
	public void testCreateLead9() {
		Assert.assertEquals(fn.postLead(first+FlipNames.count,  last+FlipNames.count,  null), 500, "Insertion unsuccessful. Status code: 500");
	}
	
	// Should fail due to special character in FirstName
	@Test
	public void testCreateLead10() {
		Assert.assertEquals(fn.postLead("Beyoncé", "Knowles", "Record Label"), 400);
	}
  
	FlipNames fn = new FlipNames();
	private final String first   = "First";
	private final String last    = "Last";
	private final String company = "Company";
}
