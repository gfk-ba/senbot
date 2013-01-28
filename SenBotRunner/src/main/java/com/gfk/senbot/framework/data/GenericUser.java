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

}
