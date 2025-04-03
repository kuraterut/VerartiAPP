package org.admin.UI.window.dayInfoWindow.dialog;

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
import lombok.Getter;
import org.Main;
import org.admin.connection.deleteRequests.DeleteClient;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.connection.getRequests.GetClient;
import org.admin.connection.getRequests.GetTransaction;
import org.admin.connection.putRequests.UpdateClient;
import org.admin.controller.AdminController;
import org.admin.model.*;
import org.admin.utils.PaymentMethod;
import org.admin.utils.TransactionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionsInfoDialog extends Main {
    public static void show(LocalDate date){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Информация о Транзакциях");

        List<Transaction> totalTransactions = GetTransaction.getListByDate(token, date);
        List<Transaction> appointmentTransactions = GetTransaction.getListByDate(token, date, TransactionType.APPOINTMENT);
        List<Transaction> productTransactions = GetTransaction.getListByDate(token, date, TransactionType.PRODUCT);
        List<Transaction> cardTransactions = GetTransaction.getListByDate(token, date, PaymentMethod.CARD);
        List<Transaction> cashTransactions = GetTransaction.getListByDate(token, date, PaymentMethod.CASH);

        Label totalAmountLabel = new Label("Всего: " + calculateTotalAmount(totalTransactions));
        Label optionAmountLabel = new Label("Услуги: " + calculateTotalAmount(appointmentTransactions));
        Label productAmountLabel = new Label("Товары: " + calculateTotalAmount(productTransactions));
        Label cardAmountLabel = new Label("Картой: " + calculateTotalAmount(cardTransactions));
        Label cashAmountLabel = new Label("Наличными: " + calculateTotalAmount(cashTransactions));

        VBox amountsBox = new VBox(30);
        amountsBox.setAlignment(Pos.CENTER);

        amountsBox.getChildren().addAll(totalAmountLabel, optionAmountLabel, productAmountLabel, cardAmountLabel, cashAmountLabel);
        Scene dialogScene = new Scene(amountsBox, 500, 300);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public static Long calculateTotalAmount(List<Transaction> transactions){
        Long totalAmount = 0L;
        for(Transaction transaction : transactions){
            totalAmount += transaction.getPurchaseAmount()*transaction.getCount();
        }
        return totalAmount;
    }
}
