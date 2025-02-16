package org.admin.dayInfoWindow.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.Main;
import org.admin.connection.Connection;
import org.admin.connection.getRequests.GetMaster;
import org.admin.connection.postRequests.PutMasterOnDate;
import org.admin.utils.MasterInfo;
import org.admin.utils.Response;
import org.admin.utils.SearchingStringListenerMasters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class PutMasterOnDateDialog extends Main {
    public static void show(LocalDate date) {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM yyyy", Locale.ENGLISH);
        String dateStr = dtf.format(date);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Назначить мастера");

        Label errorMsg = new Label("");
        Label dateLbl = new Label(dateStr);



        List<MasterInfo> allMasters = GetMaster.getAll(token);

        ComboBox<MasterInfo> choosingMaster = new ComboBox<MasterInfo>();

        choosingMaster.setOnKeyReleased(new SearchingStringListenerMasters(choosingMaster, allMasters));

        choosingMaster.setEditable(true);
        ObservableList<MasterInfo> comboBoxList = FXCollections.observableArrayList();
        assert allMasters != null;
        comboBoxList.addAll(allMasters);
        choosingMaster.setItems(comboBoxList);

        Button cancelBtn = new Button("Отмена");
        Button createMasterBtn = new Button("Создать нового мастера");
        HBox btnsBox = new HBox();

        btnsBox.setSpacing(50);
        btnsBox.setAlignment(Pos.CENTER);

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);


        choosingMaster.setCellFactory(new Callback<ListView<MasterInfo>, ListCell<MasterInfo>>() {
            @Override
            public ListCell<MasterInfo> call(ListView<MasterInfo> param) {
                return new ListCell<MasterInfo>() {
                    @Override
                    protected void updateItem(MasterInfo user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user == null || empty) {
                            setText(null);
                        } else {
                            setText(user.toString()); // Используем toString() для отображения
                        }
                    }
                };
            }
        });


        // Настраиваем отображение выбранного элемента
        choosingMaster.setButtonCell(new ListCell<MasterInfo>() {
            @Override
            protected void updateItem(MasterInfo user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                    setText(null);
                } else {
                    setText(user.toString()); // Используем toString() для отображения
                }
            }
        });

        // Обработка выбора элемента
        choosingMaster.setOnAction(event -> {
            try {
                MasterInfo master = choosingMaster.getValue();
                if (master != null) { // Проверка на null
                    Long masterId = master.getId();
                    Response response = PutMasterOnDate.post(token, masterId, date);

                    if (response.getCode() == 200) {
                        choosingMaster.setOnAction(null); // Отключаем обработчик
                        choosingMaster.setOnKeyReleased(null);
                        choosingMaster.getItems().clear(); // Очистите список элементов
                        choosingMaster.setValue(null);
                        dialog.close();
                    } else {
                        errorMsg.setText(response.getCode() + " " + response.getMsg());
                    }
                } else {
                    errorMsg.setText("Мастер не выбран");
                }
            } catch (Exception e) {
                errorMsg.setText("Неверный формат");
//                e.printStackTrace(); // Добавьте логирование для отладки
            }
        });

        btnsBox.getChildren().addAll(cancelBtn, createMasterBtn);
        root.getChildren().addAll(dateLbl, choosingMaster, errorMsg, btnsBox);

        cancelBtn.setOnAction(event -> dialog.close());
        createMasterBtn.setOnAction(event -> {
            dialog.close();
            CreateMasterDialog.show();
        });




        Scene dialogScene = new Scene(root, 500, 500);
        dialog.setOnHidden(event -> {
            choosingMaster.setOnAction(null); // Отключаем обработчик
            choosingMaster.setOnKeyReleased(null);
            choosingMaster.getItems().clear(); // Очистите список элементов
            choosingMaster.setValue(null);
        });
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }
}
