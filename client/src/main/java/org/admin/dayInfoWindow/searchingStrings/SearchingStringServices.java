package org.admin.dayInfoWindow.searchingStrings;

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
import org.admin.utils.entities.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringServices extends Main {
    public static VBox build(List<Service> startList, Consumer<Service> func){
        VBox root = new VBox();

        root.setPrefSize(300, 150);
        root.setMaxSize(300, 150);
        TextField searchTextField = new TextField();

        ObservableList<Service> observableList = FXCollections.observableArrayList(startList);
        ListView<Service> listView = new ListView<>(observableList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().remove(listView);
            List<Service> filterClients = filter(startList, newValue);

            ObservableList<Service> filteredListObservable = FXCollections.observableArrayList(filterClients);
            listView.setItems(filteredListObservable);
            if(!filterClients.isEmpty() && !newValue.isEmpty()){
                root.getChildren().add(listView);
            }
        });

        MultipleSelectionModel<Service> selectionModel = listView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Service>(){
            public void changed(ObservableValue<? extends Service> changed, Service oldValue, Service newValue){
                func.accept(newValue);
            }
        });
        listView.setPrefSize(300, 250);
        searchTextField.setPrefSize(300, 30);

        root.getChildren().addAll(searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<Service> filter(List<Service> services, String start){
        List<Service> result = new ArrayList<>();
        for(Service service : services){
            if(service.getName().startsWith(start)){
                result.add(service);
            }
        }
        return result;
    }
}
