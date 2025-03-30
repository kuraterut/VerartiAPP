package org.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Admin extends User {
	protected Boolean isMaster;

	public Admin(int code, String message){
		this.setCode(code);
		this.setMsg(message);
	}

	public static Admin fromJson(JSONObject obj){
		Admin admin = new Admin();
		Long id = (Long) obj.get("id");
		String name = (String) obj.get("name");
		String surname = (String) obj.get("surname");
		String patronymic = (String) obj.get("patronymic");
		String phone = (String) obj.get("phone");
		String bio = (String) obj.get("bio");
		String photoURL = (String) obj.get("photo");
		boolean isMaster = false;
		ArrayList<String> roles = new ArrayList<>((JSONArray) obj.get("roles"));
		if(roles.contains("MASTER")) isMaster = true;

		admin.setId(id);
		admin.setName(name);
		admin.setSurname(surname);
		admin.setPatronymic(patronymic);
		admin.setPhone(phone);
		admin.setBio(bio);
		admin.setPhotoURL(photoURL);
		admin.setIsMaster(isMaster);

		admin.setCode(200);
		return admin;
	}
}