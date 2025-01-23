package src.admin.utils;

import java.util.*;
import java.time.*;

public class ClientInfo extends Response{
	private Long id;
	private String name;
	private String surname;
	private String patronymic;
	private String phone;
	private String comment;
	private LocalDate birthday;

	public Boolean equals(ClientInfo other){
		return this.id == other.getId();
	}

	@Override
    public String toString() {
        String view = surname + " ";
        view += name + " ";
        view += patronymic + " ";
        view += "(" + phone + ")";
        return view;
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


}