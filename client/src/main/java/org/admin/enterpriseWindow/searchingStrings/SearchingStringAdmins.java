package org.admin.enterpriseWindow.searchingStrings;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.Main;
import org.admin.model.Admin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringAdmins extends Main {
    public static VBox build(List<Admin> startList, Consumer<Admin> func){
        VBox root = new VBox();

        root.setPrefSize(300, 600);
        root.setMaxSize(300, 600);

        TextField searchTextField = new TextField();

        ObservableList<Admin> observableList = FXCollections.observableArrayList(startList);
        ListView<Admin> listView = new ListView<>(observableList);

        MultipleSelectionModel<Admin> selectionModel = listView.getSelectionModel();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    selectionModel.clearSelection();
                    root.getChildren().remove(listView);
                    List<Admin> filterClients = filter(startList, newValue);

                    ObservableList<Admin> filteredListObservable = FXCollections.observableArrayList(filterClients);
                    listView.setItems(filteredListObservable);
                    if (!filterClients.isEmpty()) {
                        root.getChildren().add(listView);
                    }
                }
            });
        });

        selectionModel.selectedItemProperty().addListener(new ChangeListener<Admin>(){
            public void changed(ObservableValue<? extends Admin> changed, Admin oldValue, Admin newValue){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(newValue != null){
                            selectionModel.clearSelection();
                            func.accept(newValue);
                        }
                    }
                });
            }
        });
        listView.setPrefSize(300, 600);
        searchTextField.setPrefSize(300, 30);


        root.getChildren().addAll(searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<Admin> filter(List<Admin> admins, String start){
        List<Admin> result = new ArrayList<>();
        for(Admin admin : admins){
            if(admin.getFio().startsWith(start) || admin.getPhone().startsWith(start)){
                result.add(admin);
            }
        }
        return result;
    }
}
