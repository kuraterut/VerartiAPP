package org.admin.utils.entities;

import org.admin.utils.HelpFuncs;
import org.admin.utils.Response;

public class User extends Response {
    protected Long id;
    protected String name;
    protected String surname;
    protected String patronymic;
    protected String email;
    protected String phone;
    protected String bio;
    protected String photoURL;
    protected String password;

    public Long getId()                 {return id;}
    public String getName()             {return name;}
    public String getSurname()          {return surname;}
    public String getPatronymic()       {return patronymic;}
    public String getEmail()            {return email;}
    public String getPhone()            {return phone;}
    public String getBio()              {return bio;}
    public String getPhotoURL()         {return photoURL;}
    public String getPassword()         {return password;}

    public void setId(Long id)                      {this.id = id;}
    public void setName(String name)                {this.name = name;}
    public void setSurname(String surname)          {this.surname = surname;}
    public void setPatronymic(String patronymic)    {this.patronymic = patronymic;}
    public void setEmail(String email)              {this.email = email;}
    public void setPhone(String phone)              {this.phone = phone;}
    public void setBio(String bio)                  {this.bio = bio;}
    public void setPhotoURL(String photoURL)        {this.photoURL = photoURL;}
    public void setPassword(String password)        {this.password = password;}

    public Response checkInfo(){
        if (this.name.equals("")) return new Response(-1, "Введите имя");
        if (this.surname.equals("")) return new Response(-1, "Введите фамилию");
        if (!HelpFuncs.checkPhone(this.phone)) return new Response(-1, "Неверный формат телефона (+7...)");
        if (!HelpFuncs.checkEmail(this.email)) return new Response(-1, "Неверный формат Email (Должен содержать @)");
        return new Response(200, "OK");
    }

    @Override
    public String toString() {
        String view = id + " " + surname + " ";
        view += name + " ";
        view += patronymic + " ";
        view += "(" + phone + ")";
        return view;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if(!(other instanceof User)) return false;
        if(other == this) return true;
        return this.id == ((User)other).getId();
    }

    public String getFio(){
        return surname + " " + name + " " + patronymic;
    }
}
