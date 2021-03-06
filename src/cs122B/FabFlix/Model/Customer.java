package cs122B.FabFlix.Model;

/*
 * This User class only has the username field in this example.
 * 
 * However, in the real project, this User class can contain many more things,
 * for example, the user's shopping cart items.
 * 
 */
public class Customer {
	
	private final String email;
	
	public Customer(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}

}