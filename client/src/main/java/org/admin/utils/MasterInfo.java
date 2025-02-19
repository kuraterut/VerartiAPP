package org.admin.utils;

import java.util.*;

public class MasterInfo extends Response{
	private Long id;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String bio;
	private List<ServiceInfo> services;

	public Boolean equals(MasterInfo other){
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
	public List<ServiceInfo> getServices()	{return this.services;}
	public String getFio()					{return surname+" "+name+" "+patronymic;}
	public void setId(Long id)							{this.id = id;}

	public void setName(String name)					{this.name = name;}
	public void setSurname(String surname)				{this.surname = surname;}
	public void setPatronymic(String patronymic)		{this.patronymic = patronymic;}
	public void setBio(String bio)						{this.bio = bio;}
	public void setPhone(String phone)					{this.phone = phone;}

	public void addService(ServiceInfo service)	{this.services.add(service);}
	public void setServices(List<ServiceInfo> services)	{this.services = services;}
}
