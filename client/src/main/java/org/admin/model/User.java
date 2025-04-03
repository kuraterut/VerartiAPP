package org.admin.model;

import lombok.*;
import org.admin.utils.HelpFuncs;
import org.admin.utils.UserRole;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends Response {
    protected Long id;
    protected String name;
    protected String surname;
    protected String patronymic;
    protected String phone;
    protected String bio;
    protected String photoURL;
    protected String password;
    protected Set<UserRole> roles;

    public User(int code, String message){
        super(code,message);
    }
    //TODO Сделать все на Builder'ах

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
        return Objects.equals(this.id, ((User) other).getId());
    }

    public String getFio(){
        return surname + " " + name + " " + patronymic;
    }
    public String getSmallFio() {return surname + " " + name.charAt(0) + ".";}

    public boolean isAdmin(){return roles.contains(UserRole.ADMIN);}
    public boolean isMaster(){return roles.contains(UserRole.MASTER);}

    public void addRole(UserRole role){roles.add(role);}


    public JSONObject toJson(){
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("surname", surname);
        obj.put("patronymic", patronymic);
        obj.put("phone", phone);
        obj.put("bio", bio);
        obj.put("password", password);

        JSONArray roles = new JSONArray();
        for(UserRole role : this.roles){
            roles.add(role.toString());
        }
        obj.put("roles", roles);

        return obj;
    }

    public static User fromJson(JSONObject obj){
        User user = new User();
        user.setRoles(new HashSet<>());

        Long id = (Long) obj.get("id");
        String name = (String) obj.get("name");
        String surname = (String) obj.get("surname");
        String patronymic = (String) obj.get("patronymic");
        String phone = (String) obj.get("phone");
        String bio = (String) obj.get("bio");
        String photoURL = (String) obj.get("photo");
        ArrayList<String> roles = new ArrayList<>((JSONArray) obj.get("roles"));
        for(String role : roles){
            user.addRole(UserRole.valueOf(role));
        }

        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        user.setPhone(phone);
        user.setBio(bio);
        user.setPhotoURL(photoURL);

        user.setCode(200);
        return user;
    }
}
