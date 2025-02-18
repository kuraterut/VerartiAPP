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
import org.admin.utils.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringServices extends Main {
    public static VBox build(List<ServiceInfo> startList, Consumer<ServiceInfo> func){
        VBox root = new VBox();

        Label headLabel = new Label("Услуги");
        headLabel.setPadding(new Insets(0, 0, 10, 0));

        root.setPrefSize(300, 600);
        root.setMaxSize(300, 600);
        TextField searchTextField = new TextField();

        ObservableList<ServiceInfo> observableList = FXCollections.observableArrayList(startList);
        ListView<ServiceInfo> listView = new ListView<>(observableList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().remove(listView);
            List<ServiceInfo> filterClients = filter(startList, newValue);

            ObservableList<ServiceInfo> filteredListObservable = FXCollections.observableArrayList(filterClients);
            listView.setItems(filteredListObservable);
            if(!filterClients.isEmpty() && !newValue.isEmpty()){
                root.getChildren().add(listView);
            }
        });

        MultipleSelectionModel<ServiceInfo> selectionModel = listView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<ServiceInfo>(){
            public void changed(ObservableValue<? extends ServiceInfo> changed, ServiceInfo oldValue, ServiceInfo newValue){
                func.accept(newValue);
            }
        });
        listView.setPrefSize(300, 600);
        searchTextField.setPrefSize(300, 30);

        root.getChildren().addAll(headLabel, searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<ServiceInfo> filter(List<ServiceInfo> services, String start){
        List<ServiceInfo> result = new ArrayList<>();
        for(ServiceInfo service : services){
            if(service.getName().startsWith(start)){
                result.add(service);
            }
        }
        return result;
    }
}
