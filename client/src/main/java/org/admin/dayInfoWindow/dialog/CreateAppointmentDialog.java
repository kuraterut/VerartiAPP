package org.admin.dayInfoWindow.dialog;

import javafx.stage.Modality;
import javafx.stage.Stage;
import org.Main;
import org.admin.dayInfoWindow.tables.DayInfoTable;
import org.admin.utils.Appointment;
import org.admin.utils.MasterInfo;
import org.admin.utils.ServiceInfo;

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
    }
}
