package org.admin.UI.window.productWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.controller.AdminController;
import org.admin.connection.postRequests.CreateProduct;
import org.admin.model.Response;
import org.admin.model.Product;
import org.admin.utils.validation.CountValidation;
import org.admin.utils.validation.PriceValidation;
import org.admin.utils.validation.Validation;

public class CreateProductDialog extends Main {
    public static void show(Node node){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать Продукт");

        GridPane table = new GridPane();

        VBox root = new VBox();
        HBox btnsBox = new HBox();

        Button cancelBtn = new Button();
        Button addBtn = new Button();

        Label errorMsg = new Label();
        Label nameHeadLbl = new Label("Название: ");
        Label priceHeadLbl = new Label("Прайс: ");
        Label countHeadLbl = new Label("Количество: ");

        TextField nameTextField = new TextField();
        TextField priceTextField = new TextField();
        TextField countTextField = new TextField();

        errorMsg.setText("");

        table.setAlignment(Pos.CENTER);

        addBtn.setText("Создать");
        addBtn.setWrapText(true);
        addBtn.setTextAlignment(TextAlignment.CENTER);

        cancelBtn.setText("Отмена");
        cancelBtn.setWrapText(true);
        cancelBtn.setTextAlignment(TextAlignment.CENTER);

        btnsBox.setSpacing(200);
        btnsBox.setAlignment(Pos.CENTER);

        root.setSpacing(50);
        root.setAlignment(Pos.CENTER);

        table.addColumn(0, nameHeadLbl, priceHeadLbl, countHeadLbl);
        table.addColumn(1, nameTextField, priceTextField, countTextField);

        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(priceHeadLbl, HPos.CENTER);
        GridPane.setValignment(priceHeadLbl, VPos.CENTER);
        GridPane.setHalignment(countHeadLbl, HPos.CENTER);
        GridPane.setValignment(countHeadLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(priceTextField, HPos.CENTER);
        GridPane.setValignment(priceTextField, VPos.CENTER);
        GridPane.setHalignment(countTextField, HPos.CENTER);
        GridPane.setValignment(countTextField, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(250));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));

        cancelBtn.setOnAction(event -> dialog.close());
        addBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String priceStr = priceTextField.getText();
            String countStr = countTextField.getText();

            Validation priceValidation = new PriceValidation(priceStr);
            Validation countValidation = new CountValidation(countStr);
            if(!priceValidation.validate()) {errorMsg.setText("Неправильный формат прайса, должно быть целое число"); return;}
            if(!countValidation.validate()) {errorMsg.setText("Неправильный формат количества, должно быть целое число"); return;}

            Long price = Long.parseLong(priceStr);
            Integer count = Integer.parseInt(countStr);

            Product product = new Product();

            product.setName(name);
            product.setPrice(price);
            product.setCount(count);

            Response response = CreateProduct.post(token, product);
            if(response.getCode() == 200) {
                AdminController.loadProductsWindow(node);
                dialog.close();
            }
            else{errorMsg.setText(response.getMsg());}
        });
        btnsBox.getChildren().addAll(cancelBtn, addBtn);
        root.getChildren().addAll(table, errorMsg, btnsBox);

        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}