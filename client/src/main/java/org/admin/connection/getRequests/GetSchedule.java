package org.admin.connection.getRequests;

import org.admin.connection.Connection;
import org.admin.model.ScheduleDay;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetSchedule extends Connection {
    public static List<ScheduleDay> getListByMonthAndYear(String token, int month, int year) {
        try{
            getConnection("http://localhost:8000/api/admin/schedule?month=" + month + "&year=" + year);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);


            List<ScheduleDay> scheduleDays = new ArrayList<>();

            JSONObject data = getJson();
            JSONArray jsonArr = (JSONArray) data.get("schedule");

            for(Object elemObj : jsonArr) {
                JSONObject elem = (JSONObject)elemObj;
                ScheduleDay scheduleDay = ScheduleDay.fromJson(elem);
                scheduleDays.add(scheduleDay);
            }

            return scheduleDays;
        }
        catch(Exception ex){
            System.out.println("class: GetSchedule, method: getListByMonthAndYear, exception: " + ex.getMessage());
            return new ArrayList<>();
        }
    }
}
