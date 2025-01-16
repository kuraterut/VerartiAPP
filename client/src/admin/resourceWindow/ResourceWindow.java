package src.admin.resourceWindow;

import src.Main;
import src.admin.AdminInterface;
import src.admin.connection.Connection;
import src.admin.sideMenu.SideMenu;

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

import java.util.*;

public class ResourceWindow extends Main{
    public static BorderPane loadResourcesListWindow(){
        ArrayList<String[]> resourceList = Connection.getResourcesList(token);
        
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title                 = new Label();

        javafx.scene.control.ScrollPane scrollTable = new javafx.scene.control.ScrollPane();
        GridPane tableResourceList = new GridPane();
        
        Label idHead = new Label("ID ресурса");
        Label nameHead = new Label("Название ресурса");
        Label descriptionHead = new Label("Описание ресурса");

        tableResourceList.addRow(0, idHead, nameHead, descriptionHead);
        GridPane.setHalignment(idHead, HPos.CENTER);
        GridPane.setValignment(idHead, VPos.CENTER);
        GridPane.setHalignment(nameHead, HPos.CENTER);
        GridPane.setValignment(nameHead, VPos.CENTER);
        GridPane.setHalignment(descriptionHead, HPos.CENTER);
        GridPane.setValignment(descriptionHead, VPos.CENTER);

        if(resourceList == null){
            String[] errorStrArr = new String[]{"Ошибка", "Ошибка", "Ошибка"};
            resourceList = new ArrayList<>();
            resourceList.add(errorStrArr);
        }

        for (int i = 0; i < resourceList.size(); i++){
            Label id = new Label(resourceList.get(i)[0]);
            Label name = new Label(resourceList.get(i)[1]);
            Label description = new Label(resourceList.get(i)[2]);
            Button order = new Button("Заказать");
            String idStr = resourceList.get(i)[0];
            order.setOnAction(event -> loadResourceOrderDialog(idStr));

            if(resourceList.get(i)[0].equals("Ошибка")){
                tableResourceList.addRow(i+1, id, name, description);    
            }
            else{
                tableResourceList.addRow(i+1, id, name, description, order);
            }
            GridPane.setHalignment(id, HPos.CENTER);
            GridPane.setValignment(id, VPos.CENTER);
            GridPane.setHalignment(name, HPos.CENTER);
            GridPane.setValignment(name, VPos.CENTER);
            GridPane.setHalignment(description, HPos.CENTER);
            GridPane.setValignment(description, VPos.CENTER);
            GridPane.setHalignment(order, HPos.CENTER);
            GridPane.setValignment(order, VPos.CENTER);
        }

        tableResourceList.getColumnConstraints().add(new ColumnConstraints(500));
        tableResourceList.getColumnConstraints().add(new ColumnConstraints(500));

        tableResourceList.setAlignment(Pos.CENTER);
        tableResourceList.setGridLinesVisible(true);

        scrollTable.setPrefViewportHeight(200);
        scrollTable.setPrefViewportWidth(100);
        scrollTable.setFitToWidth(true);
        scrollTable.setFitToHeight(true);
        scrollTable.setContent(tableResourceList);
        

        title.setText("Список Ресурсов");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title, scrollTable);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }


    public static void loadResourceOrderDialog(String idStr) {
        int id = Integer.parseInt(idStr);
        
        String[] resourceInfo = Connection.getResourceInfoByID(token, id);
        if(resourceInfo == null){
            resourceInfo = new String[2];
            resourceInfo[0] = "Ошибка";
            resourceInfo[1] = "Ошибка";
        }
        String resourceName = resourceInfo[0];
        String resourceDescription = resourceInfo[1];

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Заказать ресурс");
        
        Label idLbl = new Label(idStr);
        Label nameLbl = new Label(resourceName);
        Label descriptionLbl = new Label(resourceDescription);
        Label countLbl = new Label("Укажите количество:");
        TextField countField = new TextField();
        Label error = new Label("");

        Button button1 = new Button("Назад");
        button1.setOnAction(e -> {
            dialog.close();
        });

        Button button2 = new Button("Заказать");
        button2.setOnAction(e -> {
            String countStr = countField.getText();
            try{
                int count = Integer.parseInt(countStr);
                int status = Connection.createResourceRequest(token, id, count);
                if(status == 200){dialog.close();}
                else{error.setText("Ошибка отправки запроса");}
            }
            catch(Exception ex){
                error.setText("Введите число");
            }
        });

        VBox dialogLayout = new VBox(100);
        dialogLayout.setAlignment(Pos.CENTER);
        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        btns.setSpacing(50);
        btns.getChildren().addAll(button1, button2);
        dialogLayout.getChildren().addAll(idLbl, nameLbl, descriptionLbl, countLbl, btns);
        Scene dialogScene = new Scene(dialogLayout, 500, 500);
        
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static BorderPane loadResourcesRequestsWindow(){
        ArrayList<String[]> resourceRequestsList = Connection.getResourcesRequestsList(token);
        
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title                 = new Label();

        javafx.scene.control.ScrollPane scrollTable = new javafx.scene.control.ScrollPane();
        GridPane tableResourceRequestList = new GridPane();
        
        Label idHead = new Label("ID ресурса");
        Label nameHead = new Label("Название ресурса");
        Label countHead = new Label("Количество");
        Label statusHead = new Label("Статус");

        tableResourceRequestList.addRow(0, idHead, nameHead, countHead, statusHead);
        GridPane.setHalignment(idHead, HPos.CENTER);
        GridPane.setValignment(idHead, VPos.CENTER);
        GridPane.setHalignment(nameHead, HPos.CENTER);
        GridPane.setValignment(nameHead, VPos.CENTER);
        GridPane.setHalignment(countHead, HPos.CENTER);
        GridPane.setValignment(countHead, VPos.CENTER);
        GridPane.setHalignment(statusHead, HPos.CENTER);
        GridPane.setValignment(statusHead, VPos.CENTER);

        if(resourceRequestsList == null){
            String[] errorStrArr = new String[]{"Ошибка", "Ошибка", "Ошибка", "Ошибка"};
            resourceRequestsList = new ArrayList<>();
            resourceRequestsList.add(errorStrArr);
        }

        for (int i = 0; i < resourceRequestsList.size(); i++){
            HBox statusBox = new HBox();
            statusBox.setSpacing(20);
            String statusStr = resourceRequestsList.get(i)[3];

            Label id = new Label(resourceRequestsList.get(i)[0]);
            Label name = new Label(resourceRequestsList.get(i)[1]);
            Label count = new Label(resourceRequestsList.get(i)[2]);
            Label status = new Label(resourceRequestsList.get(i)[3]);

            statusBox.getChildren().add(status);
            if(statusStr.equals("Готово к получению")){
                Button statusBtn = new Button("Получено");
                statusBox.getChildren().add(statusBtn);
            }
            
            tableResourceRequestList.addRow(i+1, id, name, count, statusBox);
            GridPane.setHalignment(id, HPos.CENTER);
            GridPane.setValignment(id, VPos.CENTER);
            GridPane.setHalignment(name, HPos.CENTER);
            GridPane.setValignment(name, VPos.CENTER);
            GridPane.setHalignment(count, HPos.CENTER);
            GridPane.setValignment(count, VPos.CENTER);
            GridPane.setHalignment(statusBox, HPos.CENTER);
            GridPane.setValignment(statusBox, VPos.CENTER);
        }

        tableResourceRequestList.getColumnConstraints().add(new ColumnConstraints(500));
        tableResourceRequestList.getColumnConstraints().add(new ColumnConstraints(500));

        tableResourceRequestList.setAlignment(Pos.CENTER);
        tableResourceRequestList.setGridLinesVisible(true);

        scrollTable.setPrefViewportHeight(200);
        scrollTable.setPrefViewportWidth(100);
        scrollTable.setFitToWidth(true);
        scrollTable.setFitToHeight(true);
        scrollTable.setContent(tableResourceRequestList);
        

        title.setText("Запросы ресурсов");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        centerBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        centerBox.getChildren().addAll(title, scrollTable);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }
}