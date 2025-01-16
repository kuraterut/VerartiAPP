package src;


import src.master.calendarWindow.CalendarWindow;
import src.master.helpWindow.HelpWindow;
import src.master.profileWindow.ProfileWindow;
import src.master.resourceWindow.ResourceWindow;
import src.master.sideMenu.SideMenu;
import src.AuthorizationWindow;

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

public class LoadInterface{
	public static int getDayOfWeekByStr(String date){
		int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        LocalDate ld = LocalDate.of(year, month, day);
        DayOfWeek dayOW = ld.getDayOfWeek();
		return dayOW.getValue();
	} 

	public static void loadMasterCalendarWindowFunc(Node node){
		node.getScene().setRoot(CalendarWindow.loadMasterCalendarWindow());	
	}

	public static void loadMasterResourcesListWindowFunc(Node node){
		node.getScene().setRoot(ResourceWindow.loadMasterResourcesListWindow());	
	}

	public static void loadMasterResourcesRequestsWindowFunc(Node node){
		node.getScene().setRoot(ResourceWindow.loadMasterResourcesRequestsWindow());	
	}

	public static void loadMasterProfileWindowFunc(Node node){
		node.getScene().setRoot(ProfileWindow.loadMasterProfileWindow());	
	}

	public static void loadMasterChangeProfileInfoWindowFunc(Node node){
		node.getScene().setRoot(ProfileWindow.loadChangeProfileWindow());	
	}

	public static void loadMasterChangeProfilePasswordWindowFunc(Node node){
		node.getScene().setRoot(ProfileWindow.loadChangeProfilePasswordWindow());	
	}

	public static void loadMasterHelpWindowFunc(Node node){
		node.getScene().setRoot(HelpWindow.loadMasterHelpWindow());	
	}
}