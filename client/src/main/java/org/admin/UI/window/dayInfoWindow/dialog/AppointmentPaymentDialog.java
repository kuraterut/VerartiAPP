package org.admin.UI.window.dayInfoWindow.dialog;

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
import org.admin.connection.getRequests.GetUser;
import org.admin.connection.postRequests.CreateTransaction;
import org.admin.controller.AdminController;
import org.admin.model.*;
import org.admin.utils.HelpFuncs;
import org.admin.utils.PaymentMethod;
import org.admin.utils.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentPaymentDialog extends Main {
    public static void show(Appointment appointment, Node dayInfoNode, Stage appointmentInfoDialog) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Оплата услуг");

        User admin = GetUser.getAdminByDate(token, appointment.getDate());
        if(admin.getCode() != 200) admin.setId(-1L);

        Long totalPrice = 0L;
        List<Transaction> transactions = new ArrayList<>();
        for(Option option : appointment.getOptions()) {
            Transaction transaction = new Transaction();

            transaction.setPurchaseAmount(option.getPrice());
            transaction.setTransactionType(TransactionType.OPTION);
            transaction.setCount(1);
            transaction.setAdminId(admin.getId());
            transaction.setClientId(appointment.getClient().getId());
            transaction.setUnitId(option.getId());

            totalPrice += option.getPrice();
            transactions.add(transaction);
        }


        User master = appointment.getMaster();
        Client client = appointment.getClient();
        LocalDateTime dateTime = LocalDateTime.of(appointment.getDate(), appointment.getStartTime());

        String dateTimeStr = HelpFuncs.localDateTimeToString(dateTime, "yyyy-MM-dd HH:mm");

        HBox paymentMethodBox = new HBox(10);

        Label dateTimeLbl = new Label(dateTimeStr);
        Label appointmentHeadLbl = new Label("Запись №"+appointment.getId());
        Label masterLbl = new Label("Мастер: " + master.getFio());
        Label clientLbl = new Label("Клиент: " + client.toString());
        Label totalPriceLbl = new Label("К оплате: " + totalPrice);
        Label paymentMethodLbl = new Label("Метод оплаты: ");
        Label servicesLbl = new Label("Услуги:");
        Label messageLabel = new Label("");

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton paymentMethodCard = new RadioButton("Карта");
        RadioButton paymentMethodCash = new RadioButton("Наличные");
        paymentMethodCard.setToggleGroup(toggleGroup);
        paymentMethodCash.setToggleGroup(toggleGroup);
        paymentMethodCard.setSelected(true);

        paymentMethodBox.getChildren().addAll(paymentMethodLbl, paymentMethodCard, paymentMethodCash);
        paymentMethodBox.setAlignment(Pos.CENTER);

        if(appointment.getCode() != 200) messageLabel.setText(appointment.getCode()+" "+appointment.getMsg());

        ScrollPane optionsInfoScrollPane = new ScrollPane();
        optionsInfoScrollPane.setPrefViewportHeight(300);
        optionsInfoScrollPane.setPrefViewportWidth(400);
        optionsInfoScrollPane.setFitToHeight(true);
        optionsInfoScrollPane.setFitToWidth(true);
        buildOptionsTable(optionsInfoScrollPane, appointment);

        TextArea commentsArea = new TextArea();
        commentsArea.setWrapText(true);
        commentsArea.setScrollLeft(Double.MAX_VALUE);
        commentsArea.setText(appointment.getComment());
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
                AdminController.loadDayInfoWindow(dayInfoNode, appointment.getDate());
                appointmentInfoDialog.close();
                dialog.close();
            }
            else messageLabel.setText(response.getMsg());
        });

        bottomBtnsBox.getChildren().addAll(closeBtn, paymentBtn);
        bottomBtnsBox.setSpacing(50);
        bottomBtnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15);
        root.setMaxWidth(600);

        dateTimeLbl.setAlignment(Pos.TOP_RIGHT);
        appointmentHeadLbl.setAlignment(Pos.TOP_CENTER);
        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(dateTimeLbl, appointmentHeadLbl);
        root.getChildren().addAll(masterLbl, clientLbl, totalPriceLbl, paymentMethodBox);
        root.getChildren().addAll(servicesLbl, optionsInfoScrollPane, commentsArea, messageLabel, bottomBtnsBox);

        Scene dialogScene = new Scene(root, 600, 600);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static void buildOptionsTable(ScrollPane scrollPane, Appointment appointment) {
        //Options table
        GridPane optionsTable = new GridPane();
        Label optionNameHeadLabel = new Label("Услуга");
        Label optionTimeHeadLabel = new Label("Длительность");
        Label optionPriceHeadLabel = new Label("Прайс");

        optionsTable.addRow(0, optionNameHeadLabel, optionTimeHeadLabel, optionPriceHeadLabel);
        optionsTable.getColumnConstraints().add(new ColumnConstraints(200));
        optionsTable.getColumnConstraints().add(new ColumnConstraints(100));
        optionsTable.getColumnConstraints().add(new ColumnConstraints(100));

        GridPane.setValignment(optionNameHeadLabel, VPos.CENTER);
        GridPane.setHalignment(optionNameHeadLabel, HPos.CENTER);
        GridPane.setValignment(optionTimeHeadLabel, VPos.CENTER);
        GridPane.setHalignment(optionTimeHeadLabel, HPos.CENTER);
        GridPane.setValignment(optionPriceHeadLabel, VPos.CENTER);
        GridPane.setHalignment(optionPriceHeadLabel, HPos.CENTER);

        List<Option> options = appointment.getOptions();

        int optionsRowNum = 1;
        for(Option option : options) {
            Label optionNameLabel = new Label(option.getName());
            Label optionDurationLabel = new Label(HelpFuncs.localTimeToString(option.getDuration(), "HH:mm"));
            Label optionPriceLabel = new Label(Long.toString(option.getPrice()));

            optionsTable.addRow(optionsRowNum, optionNameLabel, optionDurationLabel, optionPriceLabel);

            GridPane.setValignment(optionNameLabel, VPos.CENTER);
            GridPane.setHalignment(optionNameLabel, HPos.CENTER);
            GridPane.setValignment(optionDurationLabel, VPos.CENTER);
            GridPane.setHalignment(optionDurationLabel, HPos.CENTER);
            GridPane.setValignment(optionPriceLabel, VPos.CENTER);
            GridPane.setHalignment(optionPriceLabel, HPos.CENTER);

            optionsRowNum++;
        }


        optionsTable.setAlignment(Pos.CENTER);

        optionsTable.setGridLinesVisible(true);

        scrollPane.setContent(optionsTable);
    }
}
