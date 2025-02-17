package org.admin.dayInfoWindow.dialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.Main;
import org.admin.dayInfoWindow.tables.DayInfoTable;
import org.admin.utils.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CreateAppointmentDialog extends Main {
    public static void show(MasterInfo master, LocalDate date, Integer startCell, List<Appointment> appointments){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Создать запись");

        LocalTime startTime = DayInfoTable.startCellToStartTime(startCell);
        List<ServiceInfo> services = master.getServices();

        Label masterFioLabel = new Label(master.getFio());

        ComboBox<ServiceInfo> servicesComboBox = new ComboBox<>();
        servicesComboBox.setOnKeyReleased(new SearchingStringListenerServices(servicesComboBox, services));
        servicesComboBox.setEditable(true);
        ObservableList<ServiceInfo> servicesObservable = FXCollections.observableArrayList(services);
        servicesComboBox.setItems(servicesObservable);


        servicesComboBox.setCellFactory(new Callback<ListView<ServiceInfo>, ListCell<ServiceInfo>>() {
            @Override
            public ListCell<ServiceInfo> call(ListView<ServiceInfo> param) {
                return new ListCell<ServiceInfo>() {
                    @Override
                    protected void updateItem(ServiceInfo user, boolean empty) {
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

        servicesComboBox.setButtonCell(new ListCell<ServiceInfo>() {
            @Override
            protected void updateItem(ServiceInfo user, boolean empty) {
                super.updateItem(user, empty);
                if (user == null || empty) {
                    setText(null);
                } else {
                    setText(user.toString()); // Используем toString() для отображения
                }
            }
        });

    }
}
