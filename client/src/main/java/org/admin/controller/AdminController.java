package org.admin.controller;

import org.admin.UI.window.scheduleWindow.ScheduleWindow;
import org.admin.UI.window.enterpriseWindow.EnterpriseWindow;
import org.admin.UI.window.profileWindow.ProfileWindow;
import org.admin.UI.window.productWindow.ProductWindow;
import org.admin.UI.window.dayInfoWindow.DayInfoWindow;

import javafx.scene.*;

import java.time.*;

public class AdminController {
	public static void loadProductsWindow(Node node){
		node.getScene().setRoot(ProductWindow.loadProductsWindow());
	}

	public static void loadProfileWindow(Node node){
		node.getScene().setRoot(ProfileWindow.loadProfileWindow());
	}

	public static void loadEnterpriseWindow(Node node){
		node.getScene().setRoot(EnterpriseWindow.loadEnterpriseWindow());
	}

	public static void loadDayInfoWindow(Node node, LocalDate date){
		node.getScene().setRoot(DayInfoWindow.loadWindow(date));	
	}

	public static void loadCalendarOfEmployeesWindow(Node node){
		node.getScene().setRoot(ScheduleWindow.loadCalendarWindow());
	}
}