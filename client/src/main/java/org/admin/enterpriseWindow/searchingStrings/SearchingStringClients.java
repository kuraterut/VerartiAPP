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
import org.admin.connection.getRequests.GetClient;
import org.admin.utils.ClientInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringClients extends Main {
    public static VBox build(List<ClientInfo> clients, Consumer<ClientInfo> func){
        VBox root = new VBox();


        root.setPrefSize(300, 600);
        root.setMaxSize(300, 600);

        TextField searchTextField = new TextField();

        ObservableList<ClientInfo> clientsObservable = FXCollections.observableArrayList(clients);
        ListView<ClientInfo> clientsListView = new ListView<>(clientsObservable);

        MultipleSelectionModel<ClientInfo> selectionModel = clientsListView.getSelectionModel();

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    selectionModel.clearSelection();
                    root.getChildren().remove(clientsListView);
                    List<ClientInfo> filterClients = filter(clients, newValue);

                    ObservableList<ClientInfo> filterClientsObservable = FXCollections.observableArrayList(filterClients);
                    clientsListView.setItems(filterClientsObservable);
                    if (!filterClients.isEmpty() && !newValue.isEmpty()) {
                        root.getChildren().add(clientsListView);
                    }
                }
            });
        });

        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<ClientInfo>(){
            public void changed(ObservableValue<? extends ClientInfo> changed, ClientInfo oldValue, ClientInfo newValue){
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

    private static List<ClientInfo> filter(List<ClientInfo> clients, String start){
        List<ClientInfo> result = new ArrayList<>();
        for(ClientInfo client : clients){
            if(client.getFio().startsWith(start) || client.getPhone().startsWith(start)){
                result.add(client);
            }
        }
        return result;
    }
}
