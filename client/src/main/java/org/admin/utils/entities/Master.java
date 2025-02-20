package org.admin.utils.entities;

import org.admin.utils.Response;

import java.util.*;

public class Master extends User {
	private Long id;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String bio;
	private List<Service> services;
	private Boolean isAdmin;

	public Boolean equals(Master other){
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
	public List<Service> getServices()		{return this.services;}
	public Boolean getIsAdmin()				{return this.isAdmin;}

	public String getFio()					{return surname+" "+name+" "+patronymic;}
	public void setId(Long id)							{this.id = id;}
	public void setName(String name)					{this.name = name;}
	public void setSurname(String surname)				{this.surname = surname;}
	public void setPatronymic(String patronymic)		{this.patronymic = patronymic;}
	public void setBio(String bio)						{this.bio = bio;}
	public void setPhone(String phone)					{this.phone = phone;}
	public void setIsAdmin(Boolean isAdmin)				{this.isAdmin = isAdmin;}

	public void addService(Service service)	{this.services.add(service);}
	public void setServices(List<Service> services)	{this.services = services;}
}
