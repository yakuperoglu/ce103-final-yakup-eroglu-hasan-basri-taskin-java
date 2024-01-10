package com.hasan.yakup.librarysystem;
import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String password;
    
    private User(String email, String password) {
		this.email = email;
		this.password = password;

	}
}
