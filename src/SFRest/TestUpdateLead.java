package SFRest;

import java.util.List;

import org.testng.annotations.Test;
import org.testng.Assert;

public class TestUpdateLead {
	
	@Test(priority = 0)
	public void setup() {
		fn.doLogin();
		leads = fn.getLeads();
	}
	
	// Number of leads returned from Salesforce should equal number of leads created prior
	@Test(priority = 1)
	public void testGetLeads() {
		Assert.assertEquals(leads.size(), FlipNames.count);
	}
	
	@Test
	public void testSwap1() {
		Lead lead = leads.get(index++);
		Assert.assertEquals(fn.putLead(lead), 200);
		Lead swapped = fn.getLeadById(lead.getId());
		
		Assert.assertEquals(lead.getFirstName(), swapped.getLastName());
		Assert.assertEquals(lead.getLastName(), swapped.getFirstName());
		Assert.assertEquals(lead.getId(), swapped.getId());
	}
	
	@Test
	public void testSwap2() {
		Lead lead = leads.get(index++);
		Assert.assertEquals(fn.putLead(lead), 200);
		Lead swapped = fn.getLeadById(lead.getId());
		
		Assert.assertEquals(lead.getFirstName(), swapped.getLastName());
		Assert.assertEquals(lead.getLastName(), swapped.getFirstName());
		Assert.assertEquals(lead.getId(), swapped.getId());
	}
	
	@Test
	public void testSwap3() {
		Lead lead = leads.get(index++);
		Assert.assertEquals(fn.putLead(lead), 200);
		Lead swapped = fn.getLeadById(lead.getId());
		
		Assert.assertEquals(lead.getFirstName(), swapped.getLastName());
		Assert.assertEquals(lead.getLastName(), swapped.getFirstName());
		Assert.assertEquals(lead.getId(), swapped.getId());
	}
	
	@Test
	public void testSwap4() {
		Lead lead = leads.get(index++);
		Assert.assertEquals(fn.putLead(lead), 200);
		Lead swapped = fn.getLeadById(lead.getId());
		
		Assert.assertEquals(lead.getFirstName(), swapped.getLastName());
		Assert.assertEquals(lead.getLastName(), swapped.getFirstName());
		Assert.assertEquals(lead.getId(), swapped.getId());
	}
	
	@Test
	public void testSwap5() {
		Lead lead = leads.get(index++);
		Assert.assertEquals(fn.putLead(lead), 200);
		Lead swapped = fn.getLeadById(lead.getId());
		
		Assert.assertEquals(lead.getFirstName(), swapped.getLastName());
		Assert.assertEquals(lead.getLastName(), swapped.getFirstName());
		Assert.assertEquals(lead.getId(), swapped.getId());
	}
	
	FlipNames fn = new FlipNames();
	List<Lead> leads;
	private int index = 0;
}
