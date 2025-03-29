package org.admin.model;

import org.admin.utils.HelpFuncs;
import org.json.simple.JSONObject;

import java.time.*;

public class Client extends Response {
	private Long id;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String comment;
	private LocalDate birthday;

	public Boolean equals(Client other){
		return this.id == other.getId();
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

    public Response checkInfo(){
    	if (this.name.equals("")){
    		return new Response(-1, "Введите имя");
    	}
    	if (this.surname.equals("")){
    		return new Response(-1, "Введите фамилию");
    	}
    	//+79092762462
    	if (this.phone.length() != 12 || !this.phone.startsWith("+7")){
    		for(int i = 1; i < 12; i++){
    			if(!Character.isDigit(this.phone.charAt(i))){
    				return new Response(-1, "Неверный формат номера телефона(+7...)");		
    			}
    		}
    		
    	}

    	return new Response(200, "");
    }

	public Long getId()				{return this.id;}
	public String getName()			{return this.name;}
	public String getSurname()		{return this.surname;}
	public String getPatronymic()	{return this.patronymic;}
	public String getPhone()		{return this.phone;}
	public String getComment()		{return this.comment;}
	public LocalDate getBirthday()	{return this.birthday;}
	public String getFio(){return surname+" "+name+" "+patronymic;}

	public void setId(Long id)					{this.id = id;}
	public void setName(String name)			{this.name = name;}
	public void setSurname(String surname)		{this.surname = surname;}
	public void setPatronymic(String patronymic){this.patronymic = patronymic;}
	public void setPhone(String phone)			{this.phone = phone;}
	public void setComment(String comment)		{this.comment = comment;}
	public void setBirthday(LocalDate birthday)	{this.birthday = birthday;}


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

		return client;
	}

}