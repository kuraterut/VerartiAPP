package org.admin.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Admin extends User {
	protected Boolean isMaster;
	
	public Boolean getIsMaster()				{return isMaster;}
	public void setIsMaster(Boolean isMaster)	{this.isMaster = isMaster;}

	public static Admin fromJson(JSONObject obj){
		Admin admin = new Admin();
		Long id = (Long) obj.get("id");
		String name = (String) obj.get("name");
		String surname = (String) obj.get("surname");
		String patronymic = (String) obj.get("patronymic");
		String phone = (String) obj.get("phone");
		String bio = (String) obj.get("bio");
		String photoURL = (String) obj.get("photo_url");
		Boolean isMaster = false;
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

		return admin;
	}
}