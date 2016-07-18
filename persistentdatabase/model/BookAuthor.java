package persistentdatabase.model;

public class BookAuthor {
	public BookAuthor(String lastName, String firstName, String initials, String dateAssocciatedWithName, String role) {
		super();
		LastName = lastName;
		FirstName = firstName;
		Initials = initials;
		DateAssocciatedWithName = dateAssocciatedWithName;
		Role = role;
	}
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
	
	@Override
	public String toString() {
		return LastName + " " + FirstName + "\n" + Initials
				+ "\n" + DateAssocciatedWithName + "\n" + Role;
	}
}
