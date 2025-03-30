package org.admin.UI.components.searchingStrings;

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
import org.admin.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SearchingStringProducts extends Main {
    public static VBox build(List<Product> startList, Consumer<Product> func){
        VBox root = new VBox();

        root.setPrefSize(300, 150);
        root.setMaxSize(300, 150);
        TextField searchTextField = new TextField();

        ObservableList<Product> observableList = FXCollections.observableArrayList(startList);
        ListView<Product> listView = new ListView<>(observableList);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            root.getChildren().remove(listView);
            List<Product> filterClients = filter(startList, newValue);

            ObservableList<Product> filteredListObservable = FXCollections.observableArrayList(filterClients);
            listView.setItems(filteredListObservable);
            if(!filterClients.isEmpty() && !newValue.isEmpty()){
                root.getChildren().add(listView);
            }
        });

        MultipleSelectionModel<Product> selectionModel = listView.getSelectionModel();
        // устанавливаем слушатель для отслеживания изменений
        selectionModel.selectedItemProperty().addListener(new ChangeListener<Product>(){
            public void changed(ObservableValue<? extends Product> changed, Product oldValue, Product newValue){
                func.accept(newValue);
            }
        });
        listView.setPrefSize(300, 250);
        searchTextField.setPrefSize(300, 30);

        root.getChildren().addAll(searchTextField);
        root.setAlignment(Pos.TOP_CENTER);
        return root;
    }

    private static List<Product> filter(List<Product> products, String start){
        List<Product> result = new ArrayList<>();
        for(Product product : products){
            if(product.getName().startsWith(start)){
                result.add(product);
            }
        }
        return result;
    }
}
