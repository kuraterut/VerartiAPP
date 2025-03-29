package org.admin.model;


import org.admin.utils.HelpFuncs;
import org.admin.utils.AppointmentStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.time.*;

public class Appointment extends Response {
	private Long id;
	private AppointmentStatus status;
	private LocalDate date;
	private LocalTime startTime;
	private List<Option> options;
	private Client client;
	private Master master;
	private String comment;

	public Long getId()						{return this.id;}
	public LocalDate getDate()				{return this.date;}
	public LocalTime getStartTime()			{return this.startTime;}
	public Client getClient()			{return this.client;}
	public Master getMaster()			{return this.master;}
	public List<Option> getOptions()	{return this.options;}
	public String getComment()				{return this.comment;}
	public AppointmentStatus getStatus()				{return this.status;}
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
	public void setOptions(List<Option> options)	{this.options = options;}
	public void setClient(Client client)			{this.client = client;}
	public void setMaster(Master master)			{this.master = master;}
	public void setComment(String comment)				{this.comment = comment;}
	public void setStatus(AppointmentStatus status)				{this.status = status;}
	public void addOption(Option option)			{this.options.add(option);}


	public JSONObject toJson(){
		JSONObject obj = new JSONObject();
		obj.put("date", HelpFuncs.localDateToString(this.date, "yyyy-MM-dd"));
		obj.put("start_time", HelpFuncs.localTimeToString(this.startTime, "HH:mm"));
		JSONArray optionIds = new JSONArray();
		for(Option option : this.options){
			optionIds.add(option.getId());
		}
		obj.put("option_ids", optionIds);
		obj.put("client_id", this.client.getId());
		obj.put("master_id", this.master.getId());
		obj.put("comment", this.comment);

		return obj;
	}

	public static Appointment fromJson(JSONObject obj){
		Appointment appointment = new Appointment();
		Long id = (Long)obj.get("id");
		AppointmentStatus status = AppointmentStatus.valueOf(((String)obj.get("status")).toUpperCase());

		//Parse Client
		Client client = new Client();
		JSONObject clientObj = (JSONObject)obj.get("client");
		Long clientId = (Long)clientObj.get("id");
		String clientName = (String)clientObj.get("name");
		String clientSurname = (String)clientObj.get("surname");
		String clientPatronymic = (String)clientObj.get("patronymic");
		String clientPhone = (String)clientObj.get("phone");
		String clientComment = (String)clientObj.get("comment");
		LocalDate clientBirthday = HelpFuncs.stringToLocalDate((String)clientObj.get("birthday"));

		client.setId(clientId);
		client.setName(clientName);
		client.setSurname(clientSurname);
		client.setPatronymic(clientPatronymic);
		client.setPhone(clientPhone);
		client.setComment(clientComment);
		client.setBirthday(clientBirthday);

		//Parse Master
		Master master = new Master();
		JSONObject masterObj = (JSONObject)obj.get("master");
		Long masterId = (Long)masterObj.get("id");
		String masterName = (String)masterObj.get("name");
		String masterSurname = (String)masterObj.get("surname");
		String masterPatronymic = (String)masterObj.get("patronymic");
		String masterPhone = (String)masterObj.get("phone");
		String masterBio = (String)masterObj.get("bio");

		master.setId(masterId);
		master.setName(masterName);
		master.setSurname(masterSurname);
		master.setPatronymic(masterPatronymic);
		master.setPhone(masterPhone);
		master.setBio(masterBio);

		//Parse Options
		List<Option> options = new ArrayList<>();
		JSONArray optionsArr = (JSONArray)obj.get("options");
		for(Object optionObj: optionsArr){
			JSONObject optionJson = (JSONObject)optionObj;
			Option option = new Option();
			Long optionId = (Long)optionJson.get("id");
			String optionName = (String)optionJson.get("name");
			Long optionPrice = (Long)optionJson.get("price");
			String optionDescription = (String)optionJson.get("description");
			LocalTime optionDuration = LocalTime.parse((String)optionJson.get("duration"));

			option.setId(optionId);
			option.setName(optionName);
			option.setPrice(optionPrice);
			option.setDescription(optionDescription);
			option.setDuration(optionDuration);

			options.add(option);
		}

		LocalDate date = HelpFuncs.stringToLocalDate((String)obj.get("date"));
		LocalTime startTime = LocalTime.parse((String)obj.get("start_time"));
		String comment = (String)obj.get("comment");

		appointment.setId(id);
		appointment.setStatus(status);
		appointment.setStartTime(startTime);
		appointment.setComment(comment);
		appointment.setMaster(master);
		appointment.setClient(client);
		appointment.setOptions(options);
		appointment.setDate(date);

		return appointment;
	}
}