package SFRest;

public class Lead {
	
	public String getFirstName() { return firstName; }
	public String getLastName() { return lastName; }
	public String getCompanyName() { return companyName; }
	public String getId() { return id; }
	
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public void setCompanyName(String companyName) { this.companyName = companyName; }
	public void setId(String id) { this.id = id; }
	
	private String firstName;
	private String lastName;
	private String companyName;
	private String id;
}
