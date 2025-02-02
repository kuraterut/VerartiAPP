package org.director.utils;

import java.time.*;

class ClientInfo{
	Long id;
	String name;
	String surname;
	String patronymic;
	String email;
	String phone;
	String comment;
	LocalDate birthday;


	public Boolean equals(ClientInfo other){
		return this.id == other.getId();
	}

	public Long getId(){return this.id;}
	public String getName(){return this.name;}
	public String getSurname(){return this.surname;}
	public String getPatronymic(){return this.patronymic;}
	public String getEmail(){return this.email;}
	public String getPhone(){return this.phone;}
	public String getComment(){return this.comment;}
	public LocalDate getBirthday(){return this.birthday;}

}

class ServiceInfo{
	Long id;
	String name;
	String description;
	LocalTime time;
	Double price;


	public Boolean equals(ServiceInfo other){
		return this.id == other.getId();
	}

	public Long getId(){return this.id;}
	public String getName(){return this.name;}
	public LocalTime getTime(){return this.time;}
	public Double getPatronymic(){return this.price;}
}


class Appointment{
	Long id;
	LocalDate date;
	LocalTime startTime;
	ServiceInfo service;
	ClientInfo client;
	MasterInfo master;

	public Long getId(){return this.id;}
	public LocalDate getDate(){return this.date;}
	public LocalTime getStartTime(){return this.startTime;}
	public ServiceInfo getService(){return this.service;}
	public ClientInfo getClient(){return this.client;}
	public MasterInfo getMaster(){return this.master;}
}

class MasterInfo{
	Long id;
	String name;
	String surname;
	String patronymic;


	public Boolean equals(MasterInfo other){
		return this.id == other.getId();
	}

	public Long getId(){return this.id;}
	public String getName(){return this.name;}
	public String getSurname(){return this.surname;}
	public String getPatronymic(){return this.patronymic;}
}

