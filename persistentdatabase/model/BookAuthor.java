package persistentdatabase.model;

public class BookAuthor {
	private String LastName;
	private String FirstName;
	private String Initials;
	private String DateAssocciatedWithName;
	private String Role;
	
	public void setLastName(String name) { LastName = name; }
	public void setFirstName(String name) { FirstName = name; }
	public void setInitials(String name) { Initials = name; }
	public void setDateAssocciatedWithName(String name) { DateAssocciatedWithName = name; }
	public void setRole(String name) { Role = name; }
	
	public String getLastName() { return LastName; }
	public String getFirstName() { return FirstName; }
	public String getInitials() { return Initials; }
	public String getDateAssocciatedWithName() { return DateAssocciatedWithName; }
	public String getRole() { return Role; }
}
