package org.admin.utils.entities;

import org.admin.utils.Response;

public class Admin extends User {
	private Long id;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String email;
	private String bio;
	private String photoURL;
	private Boolean isMaster;
	
	public Boolean equals(Admin other){
		return this.id == other.getId();
	}

	@Override
	public String toString() {
		String view = id+" "+surname + " ";
		view += name + " ";
		view += patronymic + " ";
		view += "(" + phone + ")";
		return view;
	}

	public Long getId()						{return this.id;}
	public String getName()					{return this.name;}
	public String getSurname()				{return this.surname;}
	public String getPatronymic()			{return this.patronymic;}
	public String getBio()					{return this.bio;}
	public String getPhone()				{return this.phone;}
	public String getEmail()				{return this.email;}
	public String getPhotoURL()				{return this.photoURL;}
	public String getFio()					{return surname+" "+name+" "+patronymic;}
	public Boolean getIsMaster()			{return isMaster;}

	public void setId(Long id)							{this.id = id;}
	public void setName(String name)					{this.name = name;}
	public void setSurname(String surname)				{this.surname = surname;}
	public void setPatronymic(String patronymic)		{this.patronymic = patronymic;}
	public void setBio(String bio)						{this.bio = bio;}
	public void setEmail(String email)					{this.email = email;}
	public void setPhone(String phone)					{this.phone = phone;}
	public void setPhotoURL(String photoURL)			{this.photoURL = photoURL;}
	public void setIsMaster(Boolean isMaster)			{this.isMaster = isMaster;}
}