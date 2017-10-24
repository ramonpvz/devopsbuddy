package com.devopsbuddy.backend.persistence.domain.backend;

import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
	
	private final String authority;
	
	public Authority(String authority) {
		this.authority = authority;
	}

	/** The Serial Version UID for Serializable classes. **/
	private static final long serialVersionUID = 1L;

	@Override
	public String getAuthority() {
		return authority;
	}

}
