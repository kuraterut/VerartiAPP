package src.director;



import src.director.calendarWindow.CalendarWindow;
import src.director.helpWindow.HelpWindow;
import src.director.profileWindow.ProfileWindow;
import src.director.resourceWindow.ResourceWindow;
import src.director.sideMenu.SideMenu;
import src.director.mainInfoWindow.MainInfoWindow;
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

public class DirectorInterface{
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