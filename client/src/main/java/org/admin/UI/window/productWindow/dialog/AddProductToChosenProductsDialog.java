package org.admin.UI.window.productWindow.dialog;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.admin.UI.components.searchingStrings.SearchingStringOptions;
import org.admin.UI.components.searchingStrings.SearchingStringProducts;
import org.admin.model.Appointment;
import org.admin.model.Option;
import org.admin.model.Product;

import java.util.List;

public class AddProductToChosenProductsDialog {
    public static void show(List<Product> chosenProducts, List<Product> notChosenProducts) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Добавить товар");

        VBox root = new VBox();

        Label headerLabel = new Label("Добавьте товар");
        VBox searchingStringBox = SearchingStringProducts.build(notChosenProducts, product->{
            if(product != null){
                product.setCount(1);
                chosenProducts.add(product);
                dialog.close();
            }
        });
        Button cancelButton = new Button("Отмена");

        cancelButton.setOnAction(event -> dialog.close());
        root.getChildren().addAll(headerLabel, searchingStringBox, cancelButton);
        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);


        Scene dialogScene = new Scene(root, 1200, 600);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
