package org.admin.UI.window.productWindow.dialog;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.connection.getRequests.GetAdmin;
import org.admin.connection.postRequests.CreateTransaction;
import org.admin.controller.AdminController;
import org.admin.model.*;
import org.admin.utils.HelpFuncs;
import org.admin.utils.PaymentMethod;
import org.admin.utils.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductsPaymentDialog extends Main {
    public static void show(List<Product> chosenProducts, Admin admin, Client client, Node productWindow, Stage buyProductDialog) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Оплата услуг");


        Long totalPrice = 0L;
        List<Transaction> transactions = new ArrayList<>();
        for(Product product : chosenProducts) {
            Transaction transaction = new Transaction();

            transaction.setPurchaseAmount(product.getPrice());
            transaction.setTransactionType(TransactionType.PRODUCT);
            transaction.setCount(product.getCount());
            transaction.setAdminId(admin.getId());
            transaction.setClientId(client.getId());
            transaction.setUnitId(product.getId());

            totalPrice += product.getPrice()*product.getCount();
            transactions.add(transaction);
        }


        LocalDateTime dateTime = LocalDateTime.now();

        String dateTimeStr = HelpFuncs.localDateTimeToString(dateTime, "yyyy-MM-dd HH:mm");

        HBox paymentMethodBox = new HBox(10);

        Label dateTimeLbl = new Label(dateTimeStr);
        Label adminLbl = new Label("Админ: " + admin.getFio());
        Label clientLbl = new Label("Клиент: " + client.toString());
        Label totalPriceLbl = new Label("К оплате: " + totalPrice);
        Label paymentMethodLbl = new Label("Метод оплаты: ");
        Label servicesLbl = new Label("Товары:");
        Label messageLabel = new Label("");

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton paymentMethodCard = new RadioButton("Карта");
        RadioButton paymentMethodCash = new RadioButton("Наличные");
        paymentMethodCard.setToggleGroup(toggleGroup);
        paymentMethodCash.setToggleGroup(toggleGroup);
        paymentMethodCard.setSelected(true);

        paymentMethodBox.getChildren().addAll(paymentMethodLbl, paymentMethodCard, paymentMethodCash);
        paymentMethodBox.setAlignment(Pos.CENTER);


        ScrollPane optionsInfoScrollPane = new ScrollPane();
        optionsInfoScrollPane.setPrefViewportHeight(300);
        optionsInfoScrollPane.setPrefViewportWidth(400);
        optionsInfoScrollPane.setFitToHeight(true);
        optionsInfoScrollPane.setFitToWidth(true);
        buildProductsTable(optionsInfoScrollPane, chosenProducts);

        TextArea commentsArea = new TextArea();
        commentsArea.setWrapText(true);
        commentsArea.setScrollLeft(Double.MAX_VALUE);
        commentsArea.setMinWidth(400);
        commentsArea.setMinHeight(200);
        commentsArea.setMaxWidth(400);
        commentsArea.setMaxHeight(200);


        HBox bottomBtnsBox = new HBox();
        Button closeBtn = new Button("Отмена");
        Button paymentBtn = new Button("Оплата");

        closeBtn.setOnAction(event -> {
            dialog.close();
        });

        paymentBtn.setOnAction(event -> {
            RadioButton selectedPaymentMethod = (RadioButton) toggleGroup.getSelectedToggle();
            PaymentMethod paymentMethod;
            if(selectedPaymentMethod.getText().equals("Карта")) paymentMethod = PaymentMethod.CARD;
            else paymentMethod = PaymentMethod.CASH;
            for(Transaction transaction : transactions) {
                transaction.setPaymentMethod(paymentMethod);
            }
            Response response = CreateTransaction.post(token, transactions);
            if(response.getCode() == 200) {
                AdminController.loadProductsWindow(productWindow);
                buyProductDialog.close();
                dialog.close();
            }
            else messageLabel.setText(response.getMsg());
        });

        bottomBtnsBox.getChildren().addAll(closeBtn, paymentBtn);
        bottomBtnsBox.setSpacing(50);
        bottomBtnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15);
        root.setMaxWidth(600);

        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(dateTimeLbl, adminLbl);
        root.getChildren().addAll(clientLbl, totalPriceLbl, paymentMethodBox);
        root.getChildren().addAll(servicesLbl, optionsInfoScrollPane, commentsArea, messageLabel, bottomBtnsBox);

        Scene dialogScene = new Scene(root, 600, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static void buildProductsTable(ScrollPane scrollPane, List<Product> chosenProducts) {
        GridPane productsTable = new GridPane();
        Label productNameHeadLabel = new Label("Товар");
        Label productCountHeadLabel = new Label("Количество");
        Label productPriceHeadLabel = new Label("Прайс");

        productsTable.addRow(0, productNameHeadLabel, productCountHeadLabel, productPriceHeadLabel);
        productsTable.getColumnConstraints().add(new ColumnConstraints(200));
        productsTable.getColumnConstraints().add(new ColumnConstraints(100));
        productsTable.getColumnConstraints().add(new ColumnConstraints(100));

        GridPane.setValignment(productNameHeadLabel, VPos.CENTER);
        GridPane.setHalignment(productNameHeadLabel, HPos.CENTER);
        GridPane.setValignment(productCountHeadLabel, VPos.CENTER);
        GridPane.setHalignment(productCountHeadLabel, HPos.CENTER);
        GridPane.setValignment(productPriceHeadLabel, VPos.CENTER);
        GridPane.setHalignment(productPriceHeadLabel, HPos.CENTER);


        int productRowNum = 1;
        for(Product product : chosenProducts) {
            Label productNameLabel = new Label(product.getName());
            Label productCountLabel = new Label(product.getCount().toString());
            Label productPriceLabel = new Label(product.getPrice().toString());

            productsTable.addRow(productRowNum, productNameLabel, productCountLabel, productPriceLabel);

            GridPane.setValignment(productNameLabel, VPos.CENTER);
            GridPane.setHalignment(productNameLabel, HPos.CENTER);
            GridPane.setValignment(productCountLabel, VPos.CENTER);
            GridPane.setHalignment(productCountLabel, HPos.CENTER);
            GridPane.setValignment(productPriceLabel, VPos.CENTER);
            GridPane.setHalignment(productPriceLabel, HPos.CENTER);

            productRowNum++;
        }


        productsTable.setAlignment(Pos.CENTER);

        productsTable.setGridLinesVisible(true);

        scrollPane.setContent(productsTable);
    }
}
