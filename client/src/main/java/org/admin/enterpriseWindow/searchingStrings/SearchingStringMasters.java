package org.admin.enterpriseWindow.searchingStrings;

import javafx.application.Platform;
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
import org.admin.utils.MasterInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringMasters extends Main {
    public static VBox build(List<MasterInfo> startList, Consumer<MasterInfo> func){
        VBox root = new VBox();

        root.setPrefSize(300, 600);
        root.setMaxSize(300, 600);
        TextField searchTextField = new TextField();
//        searchTextField.setPrefWidth(300);

        ObservableList<MasterInfo> observableList = FXCollections.observableArrayList(startList);
        ListView<MasterInfo> listView = new ListView<>(observableList);

        MultipleSelectionModel<MasterInfo> selectionModel = listView.getSelectionModel();
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    selectionModel.clearSelection();
                    root.getChildren().remove(listView);
                    List<MasterInfo> filterClients = filter(startList, newValue);

                    ObservableList<MasterInfo> filteredListObservable = FXCollections.observableArrayList(filterClients);
                    listView.setItems(filteredListObservable);
                    if(!filterClients.isEmpty()){
                        root.getChildren().add(listView);
                    }
                }
            });
        });

        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<MasterInfo>(){
            public void changed(ObservableValue<? extends MasterInfo> changed, MasterInfo oldValue, MasterInfo newValue){
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
        searchTextField.setPrefSize(300, 30);
        listView.setPrefSize(300, 600);

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
