import javafx.application.Application;
import javafx.stage.Stage;

// import javax.mail.*;
// import java.text.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.util.*;

import javafx.scene.Scene;
import javafx.scene.Group;

import javafx.scene.control.Alert.AlertType;

import javafx.scene.Node;

import javafx.scene.input.MouseEvent;    

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import javafx.scene.shape.Circle;


import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;


import javafx.animation.TranslateTransition;
import javafx.animation.FadeTransition;


import javafx.util.Duration;

import java.sql.*;
import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.net.*;
import java.awt.*;


public class HelpFuncs extends Main{
	public static int getDayOfWeekByStr(String date){
		int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);
        LocalDate ld = LocalDate.of(year, month, day);
        DayOfWeek dayOW = ld.getDayOfWeek();
		return dayOW.getValue();
	} 

	public static void loadMasterCalendarWindowFunc(Node node, Main cur){
		node.getScene().setRoot(cur.loadMasterCalendarWindow());	
	}

	// public static void loadMasterResourceOrderDialogFunc(Node node, Main cur, String idStr){
	// 	node.getScene().setRoot(cur.loadMasterResourceOrderDialog(idStr));	
	// }

	public static void loadMasterResourcesListWindowFunc(Node node, Main cur){
		node.getScene().setRoot(cur.loadMasterResourcesListWindow());	
	}

	public static void loadMasterResourcesRequestsWindowFunc(Node node, Main cur){
		node.getScene().setRoot(cur.loadMasterResourcesRequestsWindow());	
	}

	public static void loadMasterProfileWindowFunc(Node node, Main cur){
		node.getScene().setRoot(cur.loadMasterProfileWindow());	
	}

	public static void loadMasterChangeProfileInfoWindowFunc(Node node, Main cur){
		node.getScene().setRoot(cur.loadChangeProfileWindow());	
	}

	public static void loadMasterChangeProfilePasswordWindowFunc(Node node, Main cur){
		node.getScene().setRoot(cur.loadChangeProfilePasswordWindow());	
	}

	// public static void loadMasterServicesWindowFunc(Node node, Main cur){
	// 	node.getScene().setRoot(cur.loadMasterServicesWindow());	
	// }

	public static void loadMasterHelpWindowFunc(Node node, Main cur){
		node.getScene().setRoot(cur.loadMasterHelpWindow());	
	}
}