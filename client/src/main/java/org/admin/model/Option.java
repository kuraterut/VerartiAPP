package org.admin.model;

import org.admin.utils.HelpFuncs;
import org.json.simple.JSONObject;

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

	public JSONObject toJson(){
		JSONObject obj = new JSONObject();
		obj.put("name", this.name);
		obj.put("description", this.description);
		obj.put("duration", HelpFuncs.localTimeToString(this.duration, "HH:mm"));
		obj.put("price", this.price);

		return obj;
	}

	public static Option fromJson(JSONObject obj){
		Option option = new Option();

		Long id = (Long) obj.get("id");
		String name = (String) obj.get("name");
		String description = (String) obj.get("description");
		LocalTime duration = LocalTime.parse((String) obj.get("duration"));
		Long price = (Long) obj.get("price");

		option.setId(id);
		option.setName(name);
		option.setDescription(description);
		option.setDuration(duration);
		option.setPrice(price);

		return option;
	}
}