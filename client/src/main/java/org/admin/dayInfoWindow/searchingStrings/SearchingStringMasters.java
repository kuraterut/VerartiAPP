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
import org.admin.utils.MasterInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringMasters extends Main {
    public static VBox build(List<MasterInfo> startList, Consumer<MasterInfo> func){
        VBox root = new VBox();

        root.setPrefSize(300, 150);
        root.setMaxSize(300, 150);
        TextField searchTextField = new TextField();
//        searchTextField.setPrefWidth(300);

        ObservableList<MasterInfo> observableList = FXCollections.observableArrayList(startList);
        ListView<MasterInfo> listView = new ListView<>(observableList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().remove(listView);
            List<MasterInfo> filterClients = filter(startList, newValue);

            ObservableList<MasterInfo> filteredListObservable = FXCollections.observableArrayList(filterClients);
            listView.setItems(filteredListObservable);
            if(!filterClients.isEmpty()){
                root.getChildren().add(listView);
            }
        });

        MultipleSelectionModel<MasterInfo> selectionModel = listView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<MasterInfo>(){
            public void changed(ObservableValue<? extends MasterInfo> changed, MasterInfo oldValue, MasterInfo newValue){
                func.accept(newValue);
            }
        });
        listView.setPrefSize(300, 250);
        searchTextField.setPrefSize(300, 30);

        root.getChildren().addAll(searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<MasterInfo> filter(List<MasterInfo> masters, String start){
        List<MasterInfo> result = new ArrayList<>();
        for(MasterInfo master : masters){
            if(master.getFio().startsWith(start) || master.getPhone().startsWith(start)){
                result.add(master);
            }
        }
        return result;
    }
}
