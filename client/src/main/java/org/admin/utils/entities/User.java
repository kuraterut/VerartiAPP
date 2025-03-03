package org.admin.utils.entities;

import org.admin.utils.Response;

import java.util.ArrayList;

public class User extends Response {
    private String name;
    private String surname;
    private String patronymic;
    private String email;
    private String phone;
    private String password;


    public String getName()             {return name;}
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
}
