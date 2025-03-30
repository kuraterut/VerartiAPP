package org.admin.UI.window.productWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.deleteRequests.DeleteClient;
import org.admin.connection.getRequests.GetAdmin;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.connection.getRequests.GetClient;
import org.admin.connection.getRequests.GetProduct;
import org.admin.connection.putRequests.UpdateClient;
import org.admin.controller.AdminController;
import org.admin.model.*;
import org.admin.utils.validation.PhoneNumberValidation;
import org.admin.utils.validation.Validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BuyProductDialog extends Main {
    public static void show(Node node){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Купить товары");

        Label headLabel = new Label("Выберите товары");
        Label messageLabel = new Label("");

        HBox clientPhoneBox = new HBox(20);
        Label clientPhoneLabel = new Label("Номер телефона покупателя: ");
        TextField clientPhoneTextField = new TextField();
        clientPhoneBox.getChildren().addAll(clientPhoneLabel, clientPhoneTextField);
        clientPhoneBox.setAlignment(Pos.CENTER);

        Label adminPhoneLabel = new Label("Номер телефона администратора: " + Main.login);

        List<Product> chosenProducts = new ArrayList<>();
        List<Product> allProducts = GetProduct.getAll(token);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        buildProductsTable(scrollPane, chosenProducts, allProducts);

        Button closeDialogBtn = new Button("Закрыть");
        Button addChosenProductBtn = new Button("Добавить товар");
        Button paymentBtn = new Button("Оплата");

        closeDialogBtn.setOnAction(event -> dialog.close());
        addChosenProductBtn.setOnAction(event -> {
            List<Product> notChosenProducts = allProducts.stream().filter(product -> !chosenProducts.contains(product)).toList();
            AddProductToChosenProductsDialog.show(chosenProducts, notChosenProducts);
            buildProductsTable(scrollPane, chosenProducts, allProducts);
        });
        paymentBtn.setOnAction(event -> {
            Validation clientPhoneValidation = new PhoneNumberValidation(clientPhoneTextField.getText());
            if(!clientPhoneValidation.validate()) {
                messageLabel.setText("Некорректный номер клиента");
                return;
            }
            Client client = GetClient.getByPhone(token, clientPhoneTextField.getText());
            if(client.getCode() != 200) {
                messageLabel.setText(client.getMsg());
                return;
            }
            Admin admin = GetAdmin.getByPhone(token, Main.login);
            if(admin.getCode() != 200) {
                messageLabel.setText(admin.getMsg());
                return;
            }

            ProductsPaymentDialog.show(chosenProducts, admin, client, node, dialog);
        });

        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(closeDialogBtn, addChosenProductBtn, paymentBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(headLabel, clientPhoneBox, adminPhoneLabel, scrollPane, messageLabel, buttons);

        Scene dialogScene = new Scene(root, 1200, 700);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static void buildProductsTable(ScrollPane scrollPane, List<Product> chosenProducts, List<Product> allProducts){
        GridPane table = new GridPane();

        Label productIdHeadLabel = new Label("ID");
        Label productNameHeadLabel = new Label("Товар");
        Label productPriceHeadLabel = new Label("Прайс");
        Label productCountHeadLabel = new Label("Кол-во");

        table.getColumnConstraints().add(new ColumnConstraints(50));
        table.getColumnConstraints().add(new ColumnConstraints(200));
        table.getColumnConstraints().add(new ColumnConstraints(50));
        table.getColumnConstraints().add(new ColumnConstraints(50));
        table.getColumnConstraints().add(new ColumnConstraints(60));
        table.getColumnConstraints().add(new ColumnConstraints(30));
        table.getColumnConstraints().add(new ColumnConstraints(30));

        GridPane.setValignment(productIdHeadLabel, VPos.CENTER);
        GridPane.setHalignment(productIdHeadLabel, HPos.CENTER);
        GridPane.setValignment(productNameHeadLabel, VPos.CENTER);
        GridPane.setHalignment(productNameHeadLabel, HPos.CENTER);
        GridPane.setValignment(productPriceHeadLabel, VPos.CENTER);
        GridPane.setHalignment(productPriceHeadLabel, HPos.CENTER);
        GridPane.setValignment(productCountHeadLabel, VPos.CENTER);
        GridPane.setHalignment(productCountHeadLabel, HPos.CENTER);

        table.addRow(0, productIdHeadLabel, productNameHeadLabel, productPriceHeadLabel, productCountHeadLabel);

        int numRow = 1;
        for(Product product : chosenProducts){
            Label productIdLabel = new Label(String.valueOf(product.getId()));
            Label productNameLabel = new Label(product.getName());
            Label productPriceLabel = new Label(String.valueOf(product.getPrice()));
            Label productCountLabel = new Label(String.valueOf(product.getCount()));
            Button deleteProductButton = new Button("Удалить");
            Button addProductButton = new Button("+");
            Button removeProductButton = new Button("-");

            table.addRow(numRow, productIdLabel, productNameLabel, productPriceLabel, productCountLabel, deleteProductButton);
            if(product.getCount() < getMaxCountProduct(allProducts, product)) table.add(addProductButton, 5, numRow);
            if(product.getCount() > 1) table.add(removeProductButton, 6, numRow);

            final int productNum = numRow-1;
            deleteProductButton.setOnAction(event -> {
                chosenProducts.remove(productNum);
                buildProductsTable(scrollPane, chosenProducts, allProducts);
            });
            addProductButton.setOnAction(event -> {
                product.setCount(product.getCount()+1);
                buildProductsTable(scrollPane, chosenProducts, allProducts);
            });
            removeProductButton.setOnAction(event -> {
                product.setCount(product.getCount()-1);
                buildProductsTable(scrollPane, chosenProducts, allProducts);
            });


            GridPane.setValignment(productIdLabel, VPos.CENTER);
            GridPane.setHalignment(productIdLabel, HPos.CENTER);
            GridPane.setValignment(productNameLabel, VPos.CENTER);
            GridPane.setHalignment(productNameLabel, HPos.CENTER);
            GridPane.setValignment(productPriceLabel, VPos.CENTER);
            GridPane.setHalignment(productPriceLabel, HPos.CENTER);
            GridPane.setValignment(productCountLabel, VPos.CENTER);
            GridPane.setHalignment(productCountLabel, HPos.CENTER);
            GridPane.setValignment(deleteProductButton, VPos.CENTER);
            GridPane.setHalignment(deleteProductButton, HPos.CENTER);
            GridPane.setValignment(addProductButton, VPos.CENTER);
            GridPane.setHalignment(addProductButton, HPos.CENTER);
            GridPane.setValignment(removeProductButton, VPos.CENTER);
            GridPane.setHalignment(removeProductButton, HPos.CENTER);

            numRow++;
        }

        table.setAlignment(Pos.CENTER);
        table.setGridLinesVisible(true);
        scrollPane.setContent(table);
    }

    public static Integer getMaxCountProduct(List<Product> allProducts, Product product){
        for(Product p : allProducts){
            if(Objects.equals(product.getId(), p.getId())){
                return p.getCount();
            }
        }
        return 0;
    }
}
