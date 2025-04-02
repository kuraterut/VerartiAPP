package org.admin.UI.window.productWindow;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.Main;

import org.admin.UI.window.dayInfoWindow.dialog.AppointmentInfoDialog;
import org.admin.UI.window.productWindow.dialog.BuyProductDialog;
import org.admin.UI.window.productWindow.dialog.ProductInfoDialog;
import org.admin.connection.getRequests.GetProduct;
import org.admin.UI.window.productWindow.dialog.CreateProductDialog;
import org.admin.UI.components.sideMenu.SideMenu;


import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import org.admin.model.Product;

import java.util.*;

public class ProductWindow extends Main{
    public static BorderPane loadProductsWindow(){
        BorderPane root             = new BorderPane();
        StackPane sideMenuStack     = SideMenu.buildSideMenu(1);
        
        VBox rightBox               = new VBox();
        VBox centerBox              = new VBox();

        Label title                 = new Label();

        javafx.scene.control.ScrollPane scrollTable = new javafx.scene.control.ScrollPane();

        scrollTable.setPrefViewportHeight(400);
        scrollTable.setPrefViewportWidth(200);
        scrollTable.setFitToWidth(true);
        scrollTable.setFitToHeight(true);

        buildAllProductsTable(scrollTable);

        title.setText("Список Товаров");
        
        rightBox.setPrefWidth(MENU_WIDTH);

        centerBox.setAlignment(Pos.TOP_CENTER);
        VBox.setMargin(title, new Insets(50, 10, 100, 10));
        centerBox.setSpacing(50);

        Button createNewProductButton = new Button("Создать товар");
        Button buyProduct = new Button("Покупка товара");

        HBox buttons = new HBox();
        buttons.getChildren().addAll(createNewProductButton, buyProduct);
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(50);

        createNewProductButton.setOnAction(event -> {
            CreateProductDialog.show(createNewProductButton);
        });
        buyProduct.setOnAction(event -> {
            BuyProductDialog.show(buyProduct);
        });

        centerBox.getChildren().addAll(title, scrollTable, buttons);
        root.setCenter(centerBox);
        root.setLeft(sideMenuStack);
        root.setRight(rightBox);

        return root;
    }

    public static void buildAllProductsTable(ScrollPane scrollPane){
        List<Product> allProducts = GetProduct.getAll(token);

        GridPane tableProductList = new GridPane();

        Label idHead = new Label("ID");
        Label nameHead = new Label("Название товара");
        Label priceHead = new Label("Прайс");
        Label countHead = new Label("Кол-во");

        tableProductList.addRow(0, idHead, nameHead, priceHead, countHead);

        GridPane.setHalignment(idHead, HPos.CENTER);
        GridPane.setValignment(idHead, VPos.CENTER);
        GridPane.setHalignment(nameHead, HPos.CENTER);
        GridPane.setValignment(nameHead, VPos.CENTER);
        GridPane.setHalignment(priceHead, HPos.CENTER);
        GridPane.setValignment(priceHead, VPos.CENTER);
        GridPane.setHalignment(countHead, HPos.CENTER);
        GridPane.setValignment(countHead, VPos.CENTER);

        tableProductList.getRowConstraints().add(new RowConstraints(30));

        for (int i = 0; i < allProducts.size(); i++){
            Product product = allProducts.get(i);

            Label id = new Label(product.getId()+"");
            Label name = new Label(product.getName());
            Label price = new Label(product.getPrice()+"");
            Label count = new Label(String.valueOf(product.getCount()));

            tableProductList.addRow(i+1, id, name, price, count);

            Rectangle productInfoRectangle = new Rectangle(300, 20, Color.TRANSPARENT);
            productInfoRectangle.setOnMouseClicked(event -> ProductInfoDialog.show(product, productInfoRectangle));
            productInfoRectangle.setOnMouseEntered(event -> {
                productInfoRectangle.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
            });
            productInfoRectangle.setOnMouseExited(event -> productInfoRectangle.setStyle("-fx-fill: transparent;"));
            tableProductList.add(productInfoRectangle, 1, i+1);

            tableProductList.getRowConstraints().add(new RowConstraints(20));

            GridPane.setHalignment(id, HPos.CENTER);
            GridPane.setValignment(id, VPos.CENTER);
            GridPane.setHalignment(name, HPos.CENTER);
            GridPane.setValignment(name, VPos.CENTER);
            GridPane.setHalignment(price, HPos.CENTER);
            GridPane.setValignment(price, VPos.CENTER);
            GridPane.setHalignment(count, HPos.CENTER);
            GridPane.setValignment(count, VPos.CENTER);
        }

        tableProductList.getColumnConstraints().add(new ColumnConstraints(50));
        tableProductList.getColumnConstraints().add(new ColumnConstraints(300));
        tableProductList.getColumnConstraints().add(new ColumnConstraints(70));
        tableProductList.getColumnConstraints().add(new ColumnConstraints(50));

        tableProductList.setAlignment(Pos.CENTER);
        tableProductList.setGridLinesVisible(true);

        scrollPane.setContent(tableProductList);

    }
}