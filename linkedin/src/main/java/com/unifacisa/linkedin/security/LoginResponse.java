package com.unifacisa.linkedin.security;

import lombok.Getter;

@Getter

public class LoginResponse {
	private String token;
	private Long id;
	private String userName;

	public LoginResponse(String token, Long id, String userName) {
		this.token = token;
		this.id = id;
		this.userName = userName;
	}

	public LoginResponse() {
	}


}
