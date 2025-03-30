package org.master;


import org.master.calendarWindow.CalendarWindow;
import org.master.helpWindow.HelpWindow;
import org.master.profileWindow.ProfileWindow;
import org.master.resourceWindow.ResourceWindow;

import javafx.scene.*;

import java.time.*;

public class MasterInterface{
	public static int getDayOfWeekByStr(String date){
		int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        LocalDate ld = LocalDate.of(year, month, day);
        DayOfWeek dayOW = ld.getDayOfWeek();
		return dayOW.getValue();
	} 

	public static void loadCalendarWindow(Node node){
		node.getScene().setRoot(CalendarWindow.loadCalendarWindow());	
	}

	public static void loadResourcesListWindow(Node node){
		node.getScene().setRoot(ResourceWindow.loadResourcesListWindow());	
	}

	public static void loadResourcesRequestsWindow(Node node){
		node.getScene().setRoot(ResourceWindow.loadResourcesRequestsWindow());	
	}

	public static void loadProfileWindow(Node node){
		node.getScene().setRoot(ProfileWindow.loadProfileWindow());	
	}

	public static void loadChangeProfileInfoWindow(Node node){
		node.getScene().setRoot(ProfileWindow.loadChangeProfileWindow());	
	}

	public static void loadChangeProfilePasswordWindow(Node node){
		node.getScene().setRoot(ProfileWindow.loadChangeProfilePasswordWindow());	
	}

	public static void loadHelpWindow(Node node){
		node.getScene().setRoot(HelpWindow.loadHelpWindow());	
	}
}