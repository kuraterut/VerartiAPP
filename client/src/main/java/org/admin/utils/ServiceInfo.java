package org.admin.utils;

import java.util.*;
import java.time.*;
import java.time.format.*;

public class ServiceInfo extends Response{
	private Long id;
	private String name;
	private String description;
	private LocalTime duration;
	private Double price;

	public Boolean equals(ServiceInfo other){
		return this.id == other.getId();
	}

	public Long getId()					{return this.id;}
	public String getName()				{return this.name;}
	public String getDescription()		{return this.description;}
	public Double getPrice()			{return this.price;}
	public LocalTime getDuration()		{return this.duration;}
	public String getDurationString()	{
		return duration.format(DateTimeFormatter.ofPattern("hh:mm", Locale.ENGLISH));
	}

	public void setId(Long id)						{this.id = id;}
	public void setName(String name)				{this.name = name;}
	public void setDuration(LocalTime duration)		{this.duration = duration;}
	public void setPrice(Double price)				{this.price = price;}
	public void setDescription(String description)	{this.description = description;}
}