package org.admin.UI.window.productWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.deleteRequests.DeleteProduct;
import org.admin.connection.putRequests.UpdateProduct;
import org.admin.controller.AdminController;
import org.admin.model.Product;
import org.admin.model.Response;
import org.admin.utils.validation.CountValidation;
import org.admin.utils.validation.PriceValidation;
import org.admin.utils.validation.Validation;

import java.util.Optional;

public class ProductInfoDialog extends Main {
    public static void show(Product product, Node node) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о Товаре");

        VBox root = new VBox();
        Label messageLabel = new Label("");

        Button cancelButton = new Button("Назад");
        Button deleteButton = new Button("Удалить товар");
        Button saveButton = new Button("Сохранить изменения");

        HBox btns = new HBox();
        btns.setAlignment(Pos.CENTER);
        btns.setSpacing(20);

        GridPane infoTable = new GridPane();
        Label idLabel           = new Label("ID: ");
        Label nameLabel         = new Label("Название: ");
        Label priceLabel        = new Label("Прайс: ");
        Label countLabel        = new Label("Кол-во на складе: ");

        Label productIdLabel                   = new Label(product.getId().toString());
        TextField nameTextField         = new TextField(product.getName());
        TextField priceTextField = new TextField(String.valueOf(product.getPrice()));
        TextField countTextField = new TextField(String.valueOf(product.getCount()));

        infoTable.getColumnConstraints().add(new ColumnConstraints(150));
        infoTable.getColumnConstraints().add(new ColumnConstraints(200));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.getRowConstraints().add(new RowConstraints(50));
        infoTable.addColumn(0,
                idLabel, nameLabel, priceLabel, countLabel);

        infoTable.addColumn(1, productIdLabel, nameTextField, priceTextField, countTextField);

        GridPane.setHalignment(idLabel, HPos.CENTER);
        GridPane.setValignment(idLabel, VPos.CENTER);
        GridPane.setHalignment(nameLabel, HPos.CENTER);
        GridPane.setValignment(nameLabel, VPos.CENTER);
        GridPane.setHalignment(priceLabel, HPos.CENTER);
        GridPane.setValignment(priceLabel, VPos.CENTER);
        GridPane.setHalignment(countLabel, HPos.CENTER);
        GridPane.setValignment(countLabel, VPos.CENTER);

        GridPane.setHalignment(productIdLabel, HPos.CENTER);
        GridPane.setValignment(productIdLabel, VPos.CENTER);
        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(priceTextField, HPos.CENTER);
        GridPane.setValignment(priceTextField, VPos.CENTER);
        GridPane.setHalignment(countTextField, HPos.CENTER);
        GridPane.setValignment(countTextField, VPos.CENTER);

        infoTable.setAlignment(Pos.CENTER);

        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        btns.getChildren().addAll(cancelButton, deleteButton, saveButton);
        root.getChildren().addAll(infoTable, messageLabel, btns);

        cancelButton.setOnAction(event -> dialog.close());

        deleteButton.setOnAction(event -> {
            if(!showDeleteProductConfirmation()) return;
            Response response = DeleteProduct.deleteById(token, product.getId());
            if(response.getCode() == 200){
                dialog.close();
                AdminController.loadProductsWindow(node);
            }
            else{messageLabel.setText(response.getMsg());}
        });

        saveButton.setOnAction(event -> {
            String name = nameTextField.getText();
            String priceStr = priceTextField.getText();
            String countStr = countTextField.getText();

            Validation priceValidation = new PriceValidation(priceStr);
            Validation countValidation = new CountValidation(countStr);
            if (name.isEmpty()) {messageLabel.setText("Название не может быть пустым"); return;}
            if(!priceValidation.validate()) {messageLabel.setText("Прайс должен быть числом"); return;}
            if(!countValidation.validate()) {messageLabel.setText("Количество должно быть числом"); return;}

            Long price = Long.parseLong(priceStr);
            Integer count = Integer.parseInt(countStr);;

            Product newProduct = new Product();
            newProduct.setId(product.getId());
            newProduct.setName(name);
            newProduct.setPrice(price);
            newProduct.setCount(count);

            Response response = UpdateProduct.updateInfo(token, newProduct);
            if(response.getCode() != 200){messageLabel.setText(response.getMsg()); return;}
            dialog.close();
            AdminController.loadProductsWindow(node);
        });


        Scene dialogScene = new Scene(root, 1200, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static boolean showDeleteProductConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Удаление продукта");
        alert.setContentText("Вы уверены, что хотите безвозвратно удалить продукт?");

        // Настраиваем кнопки
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);

        // Ждём выбора пользователя
        Optional<ButtonType> result = alert.showAndWait();

        // Возвращаем true, если нажата OK
        return result.isPresent() && result.get() == buttonTypeOk;
    }
}
