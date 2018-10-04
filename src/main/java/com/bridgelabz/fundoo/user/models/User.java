package com.bridgelabz.fundoo.user.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "Users")
//@Document(indexName="userindex", type="Users")
public class User {

	@Id
	private String id;

	private String name;

	private String email;

	private String contactNumber;

	private String password;

	private boolean activationStatus;
	
	private String profilePicture;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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

	public boolean isActivationStatus() {
		return activationStatus;
	}

	public void setActivationStatus(boolean activationStatus) {
		this.activationStatus = activationStatus;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", contactNumber=" + contactNumber
				+ ", password=" + password + ", activationStatus=" + activationStatus + "]";
	}

}