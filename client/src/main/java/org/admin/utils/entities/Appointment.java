package org.admin.utils.entities;


import org.admin.utils.Response;

import java.util.*;
import java.time.*;

public class Appointment extends Response {
	private Long id;
	private String status;
	private LocalDate date;
	private LocalTime startTime;
	private List<Service> services;
	private Client client;
	private Master master;
	private String comment;

	public Long getId()						{return this.id;}
	public LocalDate getDate()				{return this.date;}
	public LocalTime getStartTime()			{return this.startTime;}
	public Client getClient()			{return this.client;}
	public Master getMaster()			{return this.master;}
	public List<Service> getServices()	{return this.services;}
	public String getComment()				{return this.comment;}
	public String getStatus()				{return this.status;}
	public String getDateTimeStr()			{
		String dateTime = date.getDayOfMonth()+".";
		dateTime += date.getMonthValue()+".";
		dateTime += date.getYear()+" ";
		dateTime += startTime.getHour();
		dateTime += startTime.getMinute();
		return dateTime;
	}

	
	public void setId(Long id)							{this.id = id;}
	public void setDate(LocalDate date)					{this.date = date;}
	public void setStartTime(LocalTime startTime)		{this.startTime = startTime;} 	
	public void setServices(List<Service> services)	{this.services = services;}
	public void setClient(Client client)			{this.client = client;}
	public void setMaster(Master master)			{this.master = master;}
	public void setComment(String comment)				{this.comment = comment;}
	public void setStatus(String status)				{this.status = status;}
	public void addService(Service service)			{this.services.add(service);}
}