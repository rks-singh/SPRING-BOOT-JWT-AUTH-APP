package com.ravi.entity;

import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Data
@Entity
public class User {

	@Id
	@GeneratedValue
	private Integer userId;
	private String userName;
	private String userPassword;
	private String userEmail;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "userRole", joinColumns = @JoinColumn(name ="user_id"))
	private Set<String> userRole;
}
