package org.admin.utils.entities;

import org.admin.utils.Response;

import java.util.*;
import java.time.*;
import java.time.format.*;

public class Option extends Response {
	private Long id;
	private String name;
	private String description;
	private LocalTime duration;
	private Long price;

	public Boolean equals(Option other){
		return this.id == other.getId();
	}

	public Long getId()					{return this.id;}
	public String getName()				{return this.name;}
	public String getDescription()		{return this.description;}
	public Long getPrice()				{return this.price;}
	public LocalTime getDuration()		{return this.duration;}
	public String getDurationString()	{
		return duration.format(DateTimeFormatter.ofPattern("hh:mm", Locale.ENGLISH));
	}

	@Override
	public String toString() {
		return id + " " + name;

	}

	public void setId(Long id)						{this.id = id;}
	public void setName(String name)				{this.name = name;}
	public void setDuration(LocalTime duration)		{this.duration = duration;}
	public void setPrice(Long price)				{this.price = price;}
	public void setDescription(String description)	{this.description = description;}
}