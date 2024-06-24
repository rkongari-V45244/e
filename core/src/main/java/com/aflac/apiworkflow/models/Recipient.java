package com.aflac.apiworkflow.models;

public class Recipient {

	private String role;
	private String email;
	
	public Recipient() {
		super();
	}
	
	public Recipient(String role, String email) {
		super();
		this.role = role;
		this.email = email;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
}
