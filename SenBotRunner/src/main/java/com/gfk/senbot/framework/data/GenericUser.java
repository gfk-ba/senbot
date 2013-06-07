package com.gfk.senbot.framework.data;

/**
 * 
 * This object manages user data
 * 
 * @author joostschouten
 * 
 */
public class GenericUser {

	private String id;
	private String userName;
	private String password;

	private String firstName;
	private String lastName;

	public GenericUser(String id, String userName, String password, String firstName, String lastName) {
		this(id, userName, password);
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public GenericUser(String id, String userName, String password) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
	}

	public GenericUser(String userName, String password) {
		super();
		this.id = null;
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}

}
