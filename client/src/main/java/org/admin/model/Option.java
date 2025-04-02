package org.admin.model;

import lombok.*;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONObject;

import java.util.*;
import java.time.*;
import java.time.format.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option extends Response {
	private Long id;
	private String name;
	private String description;
	private LocalTime duration;
	private Long price;

	public Option(int code, String message) {
		this.setCode(code);
		this.setMsg(message);
	}

	@Override
	public boolean equals(Object other){
		if(other == null) return false;
		if(other == this) return true;
		if(!(other instanceof Option otherOption)) return false;
        return Objects.equals(this.id, otherOption.getId());
	}

	@Override
	public String toString() {
		return id + " " + name;
	}

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

		option.setCode(200);
		return option;
	}
}