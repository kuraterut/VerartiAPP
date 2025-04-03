package org.admin.model;

import lombok.*;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDay extends Response{
    private LocalDate date;
    private List<User> masters;
    private User admin;

    public ScheduleDay(int code, String message){
        this.setCode(code);
        this.setMsg(message);
    }

    public static ScheduleDay fromJson(JSONObject obj) {
        ScheduleDay scheduleDay = new ScheduleDay();

        LocalDate date = HelpFuncs.stringToLocalDate((String) obj.get("date"));

        List<User> masters = new ArrayList<>();
        JSONArray mastersJsonArr = (JSONArray) obj.get("masters");
        for (Object masterObj : mastersJsonArr){
            JSONObject masterObjJson = (JSONObject) masterObj;
            User master = User.fromJson(masterObjJson);
            masters.add(master);
        }

        JSONObject adminJson = (JSONObject) obj.get("admin");
        User admin = null;
        if (adminJson != null){
            admin = User.fromJson(adminJson);
        }
        else{
            admin = new User(404, "Не назначен");
        }
        scheduleDay.setDate(date);
        scheduleDay.setMasters(masters);
        scheduleDay.setAdmin(admin);

        scheduleDay.setCode(200);
        return scheduleDay;
    }
}
