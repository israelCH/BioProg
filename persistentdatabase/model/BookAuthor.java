package persistentdatabase.model;

import java.io.Serializable;

public class BookAuthor implements Serializable {
	public BookAuthor(String lastName, String firstName, String initials, String coll) {
		super();
		LastName = lastName;
		FirstName = firstName;
		Initials = initials;
		//DateAssocciatedWithName = dateAssocciatedWithName;
		//Role = role;
		CollectiveName = coll;
	}
	private String LastName;
	private String FirstName;
	private String Initials;
	//private String DateAssocciatedWithName;
	//private String Role;
	private String CollectiveName;
	
	public void setLastName(String name) { LastName = name; }
	public void setFirstName(String name) { FirstName = name; }
	public void setInitials(String name) { Initials = name; }
	//public void setDateAssocciatedWithName(String name) { DateAssocciatedWithName = name; }
	//public void setRole(String name) { Role = name; }
	public void setCollective(String col) { CollectiveName = col; }
	
	public String getLastName() { return LastName; }
	public String getFirstName() { return FirstName; }
	public String getInitials() { return Initials; }
	//public String getDateAssocciatedWithName() { return DateAssocciatedWithName; }
	//public String getRole() { return Role; }
	public String getCollective() { return CollectiveName; }
	
	@Override
	public String toString() {
		if (LastName != "")
			return Initials + " " + LastName + " " + FirstName;
		if (CollectiveName != "")
			return CollectiveName;
		return "Unknown Author";
	}
}
