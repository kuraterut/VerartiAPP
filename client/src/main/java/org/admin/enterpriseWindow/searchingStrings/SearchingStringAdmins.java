package org.admin.enterpriseWindow.searchingStrings;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.Main;
import org.admin.utils.AdminInfo;
import org.admin.utils.MasterInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringAdmins extends Main {
    public static VBox build(List<AdminInfo> startList, Consumer<AdminInfo> func){
        VBox root = new VBox();

        Label headLabel = new Label("Админы");
        headLabel.setPadding(new Insets(0, 0, 10, 0));
        root.setPrefSize(300, 600);
        root.setMaxSize(300, 600);

        TextField searchTextField = new TextField();

        ObservableList<AdminInfo> observableList = FXCollections.observableArrayList(startList);
        ListView<AdminInfo> listView = new ListView<>(observableList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().remove(listView);
            List<AdminInfo> filterClients = filter(startList, newValue);

            ObservableList<AdminInfo> filteredListObservable = FXCollections.observableArrayList(filterClients);
            listView.setItems(filteredListObservable);
            if(!filterClients.isEmpty() && !newValue.isEmpty()){
                root.getChildren().add(listView);
            }
        });

        MultipleSelectionModel<AdminInfo> selectionModel = listView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<AdminInfo>(){
            public void changed(ObservableValue<? extends AdminInfo> changed, AdminInfo oldValue, AdminInfo newValue){
                func.accept(newValue);
            }
        });
        listView.setPrefSize(300, 600);
        searchTextField.setPrefSize(300, 30);


        root.getChildren().addAll(headLabel, searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<AdminInfo> filter(List<AdminInfo> admins, String start){
        List<AdminInfo> result = new ArrayList<>();
        for(AdminInfo admin : admins){
            if(admin.getFio().startsWith(start) || admin.getPhone().startsWith(start)){
                result.add(admin);
            }
        }
        return result;
    }
}
