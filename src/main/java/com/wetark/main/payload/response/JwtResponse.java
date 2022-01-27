package com.wetark.main.payload.response;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String id;
	private String phoneNo;
	private String email;
	private List<String> roles;

	public JwtResponse(String accessToken, String id, String phoneNo, String email, List<String> roles) {
		this.token = accessToken;
		this.id = id;
		this.phoneNo = phoneNo;
		this.email = email;
		this.roles = roles;
	}

	public String getAccessToken() {
		return type + " " + token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public List<String> getRoles() {
		return roles;
	}
}
