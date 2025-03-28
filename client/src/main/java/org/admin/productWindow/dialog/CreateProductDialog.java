package org.admin.productWindow.dialog;

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
import org.admin.AdminInterface;
import org.admin.connection.postRequests.CreateAdmin;
import org.admin.connection.postRequests.CreateProduct;
import org.admin.utils.Response;
import org.admin.utils.entities.Admin;
import org.admin.utils.entities.Product;

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
        Label descriptionHeadLbl = new Label("Описание: ");

        TextField nameTextField = new TextField();
        TextField priceTextField = new TextField();
        TextField countTextField = new TextField();
        TextArea descriptionTextArea = new TextArea();

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

        table.addColumn(0, nameHeadLbl, priceHeadLbl, countHeadLbl, descriptionHeadLbl);
        table.addColumn(1, nameTextField, priceTextField, countTextField, descriptionTextArea);

        GridPane.setHalignment(nameHeadLbl, HPos.CENTER);
        GridPane.setValignment(nameHeadLbl, VPos.CENTER);
        GridPane.setHalignment(priceHeadLbl, HPos.CENTER);
        GridPane.setValignment(priceHeadLbl, VPos.CENTER);
        GridPane.setHalignment(countHeadLbl, HPos.CENTER);
        GridPane.setValignment(countHeadLbl, VPos.CENTER);
        GridPane.setHalignment(descriptionHeadLbl, HPos.CENTER);
        GridPane.setValignment(descriptionHeadLbl, VPos.CENTER);

        GridPane.setHalignment(nameTextField, HPos.CENTER);
        GridPane.setValignment(nameTextField, VPos.CENTER);
        GridPane.setHalignment(priceTextField, HPos.CENTER);
        GridPane.setValignment(priceTextField, VPos.CENTER);
        GridPane.setHalignment(countTextField, HPos.CENTER);
        GridPane.setValignment(countTextField, VPos.CENTER);
        GridPane.setHalignment(descriptionTextArea, HPos.CENTER);
        GridPane.setValignment(descriptionTextArea, VPos.CENTER);

        table.getColumnConstraints().add(new ColumnConstraints(150));
        table.getColumnConstraints().add(new ColumnConstraints(250));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(50));
        table.getRowConstraints().add(new RowConstraints(150));

        cancelBtn.setOnAction(event -> dialog.close());
        addBtn.setOnAction(event -> {
            String name = nameTextField.getText();
            String description = descriptionTextArea.getText();

            Long price = 0L;
            try{price = Long.parseLong(priceTextField.getText());}
            catch (NumberFormatException e){errorMsg.setText("Неправильный формат прайса, должно быть целое число");}

            Integer count = 0;
            try{count = Integer.parseInt(countTextField.getText());}
            catch (NumberFormatException e){errorMsg.setText("Неправильный формат количества, должно быть целое число");}


            Product product = new Product();

            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setCount(count);

            Response response = CreateProduct.post(token, product);
            if(response.getCode() == 200) {
                AdminInterface.loadProductsWindow(node);
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