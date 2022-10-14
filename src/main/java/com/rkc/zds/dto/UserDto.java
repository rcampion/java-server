package com.rkc.zds.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

public class UserDto {
	private final String email;
	private final Set<Role> roles;
	private final LocalDate dateCreated;

	public UserDto(@JsonProperty("email") String email, @JsonProperty("roles") Set<Role> roles,
			@JsonProperty("dateCreated") LocalDate dateCreated) {
		super();
		this.email = email;
		this.roles = roles;
		this.dateCreated = dateCreated;
	}

	public String getEmail() {
		return email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public static enum Role {
		USER, ADMIN
	}

	private static final TypeReference<UserDto> typeRef = new TypeReference<UserDto>() {
	};

	public static TypeReference<UserDto> typeRef() {
		return typeRef;
	}

	private static final TypeReference<List<UserDto>> listTypeRef = new TypeReference<List<UserDto>>() {
	};

	public static TypeReference<List<UserDto>> listTypeRef() {
		return listTypeRef;
	}
}