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
import org.admin.connection.getRequests.GetClient;
import org.admin.utils.entities.Client;

import java.util.ArrayList;
import java.util.List;

public class SearchingStringClients extends Main {
    public static VBox build(){
        VBox root = new VBox();

        List<Client> clients = GetClient.getAll(token);
        TextField searchTextField = new TextField();

        ObservableList<Client> clientsObservable = FXCollections.observableArrayList(clients);
        ListView<Client> clientsListView = new ListView<>(clientsObservable);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().remove(clientsListView);
            List<Client> filterClients = filter(clients, newValue);

            ObservableList<Client> filterClientsObservable = FXCollections.observableArrayList(filterClients);
            clientsListView.setItems(filterClientsObservable);
            if(!filterClients.isEmpty() && !newValue.isEmpty()){
                root.getChildren().add(clientsListView);
            }
        });

        MultipleSelectionModel<Client> clientsSelectionModel = clientsListView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        clientsSelectionModel.selectedItemProperty().addListener(new ChangeListener<Client>(){
            public void changed(ObservableValue<? extends Client> changed, Client oldValue, Client newValue){
                System.out.println(newValue);
            }
        });
        clientsListView.setPrefSize(300, 250);
        searchTextField.setPrefSize(300, 30);

        root.getChildren().addAll(searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<Client> filter(List<Client> clients, String start){
        List<Client> result = new ArrayList<>();
        for(Client client : clients){
            if(client.getFio().startsWith(start) || client.getPhone().startsWith(start)){
                result.add(client);
            }
        }
        return result;
    }
}
