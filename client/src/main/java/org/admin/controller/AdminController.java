package org.admin.controller;

import org.admin.UI.window.ScheduleWindow.CalendarOfEmployeesWindow;
import org.admin.UI.window.enterpriseWindow.EnterpriseWindow;
import org.admin.UI.window.helpWindow.HelpWindow;
import org.admin.UI.window.profileWindow.ProfileWindow;
import org.admin.UI.window.productWindow.ProductWindow;
import org.admin.UI.window.dayInfoWindow.DayInfoWindow;

import javafx.scene.*;

import java.time.*;

public class AdminController {
	public static int getDayOfWeekByStr(String date){
		int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        LocalDate ld = LocalDate.of(year, month, day);
        DayOfWeek dayOW = ld.getDayOfWeek();
		return dayOW.getValue();
	}

	public static void loadProductsWindow(Node node){
		node.getScene().setRoot(ProductWindow.loadProductsWindow());
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


	public static void loadEnterpriseWindow(Node node){
		node.getScene().setRoot(EnterpriseWindow.loadEnterpriseWindow());
	}

	public static void loadDayInfoWindow(Node node, LocalDate date){
		node.getScene().setRoot(DayInfoWindow.loadWindow(date));	
	}

	public static void loadCalendarOfEmployeesWindow(Node node){
		node.getScene().setRoot(CalendarOfEmployeesWindow.loadCalendarWindow());	
	}
}