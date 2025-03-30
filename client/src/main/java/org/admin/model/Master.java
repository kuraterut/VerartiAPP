package org.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Master extends User {
	private Boolean isAdmin;

	public Master(int code, String message){
		this.setCode(code);
		this.setMsg(message);
	}

	public static Master fromJson(JSONObject obj){
		Master master = new Master();
		Long id = (Long) obj.get("id");
		String name = (String) obj.get("name");
		String surname = (String) obj.get("surname");
		String patronymic = (String) obj.get("patronymic");
		String phone = (String) obj.get("phone");
		String bio = (String) obj.get("bio");
		String photoURL = (String) obj.get("photo");

		boolean isAdmin = false;
		ArrayList<String> roles = new ArrayList<>((JSONArray) obj.get("roles"));
		if(roles.contains("ADMIN")) isAdmin = true;

		master.setId(id);
		master.setName(name);
		master.setSurname(surname);
		master.setPatronymic(patronymic);
		master.setPhone(phone);
		master.setBio(bio);
		master.setPhotoURL(photoURL);
		master.setIsAdmin(isAdmin);

		master.setCode(200);
		return master;
	}
}
