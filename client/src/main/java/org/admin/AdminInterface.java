package org.admin;

import org.admin.calendarOfEmployeesWindow.CalendarOfEmployeesWindow;
import org.admin.enterpriseWindow.EnterpriseWindow;
import org.admin.helpWindow.HelpWindow;
import org.admin.profileWindow.ProfileWindow;
import org.admin.resourceWindow.ResourceWindow;
import org.admin.sideMenu.SideMenu;
import org.admin.dayInfoWindow.DayInfoWindow;
import org.AuthorizationWindow;

import javafx.application.*;
import javafx.stage.*;

import javafx.scene.*;

import javafx.scene.control.Alert.*;
import javafx.scene.input.*;    
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.animation.*;
import javafx.collections.*;
import javafx.util.*;

import java.time.*;

public class AdminInterface{
	public static int getDayOfWeekByStr(String date){
		int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        LocalDate ld = LocalDate.of(year, month, day);
        DayOfWeek dayOW = ld.getDayOfWeek();
		return dayOW.getValue();
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