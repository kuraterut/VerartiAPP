package org.admin.model;


import lombok.*;
import org.admin.utils.HelpFuncs;
import org.admin.utils.AppointmentStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.time.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment extends Response {
	private Long id;
	private AppointmentStatus status;
	private LocalDate date;
	private LocalTime startTime;
	private List<Option> options;
	private Client client;
	private User master;
	private String comment;

	public Appointment(int code, String message){
		this.setCode(code);
		this.setMsg(message);
	}

	public void addOption(Option option){this.options.add(option);}

	public String getDateTimeStr(){
		LocalDateTime dateTime = LocalDateTime.of(date, startTime);
		return HelpFuncs.localDateTimeToString(dateTime, "yyyy-MM-dd HH:mm");
	}

	public JSONObject toJson(){
		JSONObject obj = new JSONObject();
		List<Long> optionIds = new ArrayList<>();
		for(Option option : this.options){
			optionIds.add(option.getId());
		}

		obj.put("date", HelpFuncs.localDateToString(this.date, "yyyy-MM-dd"));
		obj.put("start_time", HelpFuncs.localTimeToString(this.startTime, "HH:mm"));
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
		JSONObject clientObj = (JSONObject)obj.get("client");
		Client client = Client.fromJson(clientObj);

		//Parse Master
		JSONObject masterObj = (JSONObject)obj.get("master");
		User master = User.fromJson(masterObj);

		//Parse Options
		List<Option> options = new ArrayList<>();
		JSONArray optionsArr = (JSONArray)obj.get("options");
		if(optionsArr != null){
			for(Object optionObj: optionsArr){
				JSONObject optionJson = (JSONObject)optionObj;
				Option option = Option.fromJson(optionJson);
				options.add(option);
			}
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

		appointment.setCode(200);
		return appointment;
	}
}