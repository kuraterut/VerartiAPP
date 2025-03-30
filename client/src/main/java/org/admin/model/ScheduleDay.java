package org.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.admin.utils.HelpFuncs;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleDay extends Response{
    private LocalDate date;
    private List<Master> masters;
    private Admin admin;

    public ScheduleDay(int code, String message){
        this.setCode(code);
        this.setMsg(message);
    }

    public static ScheduleDay fromJson(JSONObject obj) {
        ScheduleDay scheduleDay = new ScheduleDay();

        LocalDate date = HelpFuncs.stringToLocalDate((String) obj.get("date"));

        List<Master> masters = new ArrayList<>();
        JSONArray mastersJsonArr = (JSONArray) obj.get("masters");
        for (Object masterObj : mastersJsonArr){
            JSONObject masterObjJson = (JSONObject) masterObj;
            Master master = Master.fromJson(masterObjJson);
            masters.add(master);
        }

        JSONObject adminJson = (JSONObject) obj.get("admin");
        Admin admin = null;
        if (adminJson != null){
            admin = Admin.fromJson(adminJson);
        }
        scheduleDay.setDate(date);
        scheduleDay.setMasters(masters);
        scheduleDay.setAdmin(admin);

        scheduleDay.setCode(200);
        return scheduleDay;
    }
}
