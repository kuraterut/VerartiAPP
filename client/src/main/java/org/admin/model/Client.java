package org.admin.model;

import lombok.*;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONObject;

import java.time.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client extends Response {
	private Long id;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String comment;
	private LocalDate birthday;

	public Client(int code, String message){
		this.setCode(code);
		this.setMsg(message);
	}

	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof Client otherClient)) return false;
        return Objects.equals(this.id, otherClient.getId());
	}

	@Override
    public String toString() {
        String view = id + " ";
        view += surname + " ";
        view += name + " ";
        view += patronymic + " ";
        view += "(" + phone + ")";
        return view;
    }

	public String getFio(){return surname+" "+name+" "+patronymic;}

	public JSONObject toJson(){
		JSONObject obj = new JSONObject();
		obj.put("name", this.name);
		obj.put("surname", this.surname);
		obj.put("patronymic", this.patronymic);
		obj.put("phone", this.phone);
		obj.put("comment", this.comment);
		obj.put("birthday", HelpFuncs.localDateToString(this.birthday, "yyyy-MM-dd"));

		return obj;
	}

	public static Client fromJson(JSONObject obj){
		Client client = new Client();

		Long id = (Long) obj.get("id");
		String name = (String) obj.get("name");
		String surname = (String) obj.get("surname");
		String patronymic = (String) obj.get("patronymic");
		String phone = (String) obj.get("phone");
		String comment = (String) obj.get("comment");
		LocalDate birthday = HelpFuncs.stringToLocalDate((String) obj.get("birthday"));

		client.setId(id);
		client.setName(name);
		client.setSurname(surname);
		client.setPatronymic(patronymic);
		client.setPhone(phone);
		client.setComment(comment);
		client.setBirthday(birthday);

		client.setCode(200);
		return client;
	}

}