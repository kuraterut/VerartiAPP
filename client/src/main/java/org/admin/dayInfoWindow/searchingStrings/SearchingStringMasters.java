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
import org.admin.utils.entities.Master;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringMasters extends Main {
    public static VBox build(List<Master> startList, Consumer<Master> func){
        VBox root = new VBox();

        root.setPrefSize(300, 150);
        root.setMaxSize(300, 150);
        TextField searchTextField = new TextField();
//        searchTextField.setPrefWidth(300);

        ObservableList<Master> observableList = FXCollections.observableArrayList(startList);
        ListView<Master> listView = new ListView<>(observableList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().remove(listView);
            List<Master> filterClients = filter(startList, newValue);

            ObservableList<Master> filteredListObservable = FXCollections.observableArrayList(filterClients);
            listView.setItems(filteredListObservable);
            if(!filterClients.isEmpty()){
                root.getChildren().add(listView);
            }
        });

        MultipleSelectionModel<Master> selectionModel = listView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Master>(){
            public void changed(ObservableValue<? extends Master> changed, Master oldValue, Master newValue){
                func.accept(newValue);
            }
        });
        listView.setPrefSize(300, 250);
        searchTextField.setPrefSize(300, 30);

        root.getChildren().addAll(searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<Master> filter(List<Master> masters, String start){
        List<Master> result = new ArrayList<>();
        for(Master master : masters){
            if(master.getFio().startsWith(start) || master.getPhone().startsWith(start)){
                result.add(master);
            }
        }
        return result;
    }
}
