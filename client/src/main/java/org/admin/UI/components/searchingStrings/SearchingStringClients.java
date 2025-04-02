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
import org.admin.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringClients extends Main {
    public static VBox build(List<Client> clients, Consumer<Client> func){
        VBox root = new VBox();


        root.setPrefSize(300, 600);
        root.setMaxSize(300, 600);

        TextField searchTextField = new TextField();

        ObservableList<Client> clientsObservable = FXCollections.observableArrayList(clients);
        ListView<Client> clientsListView = new ListView<>(clientsObservable);

        MultipleSelectionModel<Client> selectionModel = clientsListView.getSelectionModel();

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    selectionModel.clearSelection();
                    root.getChildren().remove(clientsListView);
                    List<Client> filterClients = filter(clients, newValue);

                    ObservableList<Client> filterClientsObservable = FXCollections.observableArrayList(filterClients);
                    clientsListView.setItems(filterClientsObservable);
                    if (!filterClients.isEmpty()) {
                        root.getChildren().add(clientsListView);
                    }
                }
            });
        });

        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Client>(){
            public void changed(ObservableValue<? extends Client> changed, Client oldValue, Client newValue){
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
        clientsListView.setPrefSize(300, 600);
        searchTextField.setPrefSize(300, 30);

        root.getChildren().addAll(searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<Client> filter(List<Client> clients, String start){
        List<Client> result = new ArrayList<>();
        for(Client client : clients){
            if(client.getFio().startsWith(start) || client.getPhone().contains(start)){
                result.add(client);
            }
        }
        return result;
    }
}
