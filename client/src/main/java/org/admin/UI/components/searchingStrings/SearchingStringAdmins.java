package org.admin.UI.components.searchingStrings;

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
import org.admin.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringAdmins extends Main {
    public static VBox build(List<User> startList, Consumer<User> func){
        VBox root = new VBox();

        root.setPrefSize(300, 600);
        root.setMaxSize(300, 600);

        TextField searchTextField = new TextField();

        ObservableList<User> observableList = FXCollections.observableArrayList(startList);
        ListView<User> listView = new ListView<>(observableList);

        MultipleSelectionModel<User> selectionModel = listView.getSelectionModel();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    selectionModel.clearSelection();
                    root.getChildren().remove(listView);
                    List<User> filterClients = filter(startList, newValue);

                    ObservableList<User> filteredListObservable = FXCollections.observableArrayList(filterClients);
                    listView.setItems(filteredListObservable);
                    if (!filterClients.isEmpty()) {
                        root.getChildren().add(listView);
                    }
                }
            });
        });

        selectionModel.selectedItemProperty().addListener(new ChangeListener<User>(){
            public void changed(ObservableValue<? extends User> changed, User oldValue, User newValue){
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

    private static List<User> filter(List<User> admins, String start){
        List<User> result = new ArrayList<>();
        for(User admin : admins){
            if(admin.getFio().startsWith(start) || admin.getPhone().contains(start)){
                result.add(admin);
            }
        }
        return result;
    }
}
