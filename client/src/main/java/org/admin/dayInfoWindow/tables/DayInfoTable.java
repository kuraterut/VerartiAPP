package org.admin.dayInfoWindow.tables;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.Main;
import org.admin.connection.Connection;
import org.admin.connection.getRequests.GetMaster;
import org.admin.dayInfoWindow.dialog.AppointmentInfoDialog;
import org.admin.dayInfoWindow.dialog.CreateAppointmentDialog;
import org.admin.utils.Appointment;
import org.admin.utils.ClientInfo;
import org.admin.utils.MasterInfo;
import org.admin.utils.ServiceInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class DayInfoTable extends Main {
    private static Integer CELLS_IN_COLUMN_COUNT = 28;

    public static ScrollPane create(LocalDate date){
        int day = date.getDayOfMonth();
        int year = date.getYear();
        int month = date.getMonthValue();

        ScrollPane scrollPane = new ScrollPane();
        GridPane table = new GridPane();

        int countTableCell = 1;
        table.getRowConstraints().add(new RowConstraints(50));

        for(int i = 8; i < 23; i++){
            Label first = new Label(i+":00-"+i+":30");
            Label second = new Label(i+":30-"+(i+1)+":00");
            table.add(first, 0, countTableCell);
            countTableCell++;
            table.add(second, 0, countTableCell);
            countTableCell++;
            table.getRowConstraints().add(new RowConstraints(40));
            table.getRowConstraints().add(new RowConstraints(40));

            GridPane.setHalignment(first, HPos.CENTER);
            GridPane.setValignment(first, VPos.CENTER);
            GridPane.setHalignment(second, HPos.CENTER);
            GridPane.setValignment(second, VPos.CENTER);
        }
        scrollPane.setContent(table);

        table.getColumnConstraints().add(new ColumnConstraints(100));

//        Map<Long, List<Appointment>> dayInfo = Connection.getMastersSheduleByDate(token, date);
        List<MasterInfo> masters = GetMaster.getListByDate(token, date, true);

        Map<Long, List<Appointment>> dayInfo = new HashMap<>();
        for(MasterInfo master : masters){
            dayInfo.put(master.getId(), new ArrayList<>());
        }

        int countColumn = 0;
        for(Long masterId : dayInfo.keySet()){
            Set<Integer> usedCells = new HashSet<>();
            countColumn++;
            table.getColumnConstraints().add(new ColumnConstraints(200));
            MasterInfo master = GetMaster.getById(token, masterId);


            Label masterSurnameLbl = new Label(master.getSurname());

            table.add(masterSurnameLbl, countColumn, 0);
            GridPane.setHalignment(masterSurnameLbl, HPos.CENTER);
            GridPane.setValignment(masterSurnameLbl, VPos.CENTER);

            List<Appointment> appointments = dayInfo.get(masterId);
            Set<Integer> startCellsSet = new HashSet<>();
            for(Appointment appointment: appointments){
                Integer startCellForSet = calculateCellStart(appointment.getStartTime());
                startCellsSet.add(startCellForSet);
            }

            for(Appointment appointment: appointments){
                Long id = appointment.getId();
                ClientInfo client = appointment.getClient();
                List<ServiceInfo> services = appointment.getServices();
                ServiceInfo firstService = services.get(0);
                Integer cellStart = calculateCellStart(appointment.getStartTime());
                Integer cellNumber = calculateCellNumber(services);

                Rectangle rectStart = new Rectangle(150, 100, Color.AQUAMARINE);
                rectStart.setOnMouseClicked(event -> AppointmentInfoDialog.show(id));
                rectStart.setOnMouseEntered(event -> {
                    rectStart.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
                });
                rectStart.setOnMouseExited(event -> rectStart.setStyle("-fx-fill: aquamarine"));

                Label appointmentHeadLbl = new Label("Запись №"+id);
                Label clientLbl = new Label(client.getFio());
                Label serviceLbl = new Label();

                if(services.size() == 1){
                    serviceLbl.setText(firstService.getName());
                }
                else{
                    serviceLbl.setText(firstService.getName()+"...");
                }

                table.add(appointmentHeadLbl, countColumn, cellStart);
                table.add(clientLbl, countColumn, cellStart);
                table.add(serviceLbl, countColumn, cellStart);
                table.add(rectStart, countColumn, cellStart);

                usedCells.add(cellStart);

                for(int i = 1; i < cellNumber; i++){
                    if(startCellsSet.contains(cellStart+i)) break;
                    Rectangle rectFill = new Rectangle(200, 40, Color.AQUA);
                    table.add(rectFill, countColumn, cellStart+i);
                    usedCells.add(cellStart+i);
                }
            }
            for(int i = 1; i <= CELLS_IN_COLUMN_COUNT; i++){
                if(!usedCells.contains(i)){
                    Rectangle unusedRect = new Rectangle(200, 40, Color.TRANSPARENT);
                    Integer startCellToCreate = i;
                    unusedRect.setOnMouseClicked(event -> CreateAppointmentDialog.show(master, date, startCellToCreate, appointments));
                    table.add(unusedRect, countColumn, i);
                    unusedRect.setOnMouseEntered(event -> {
                        unusedRect.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
                    });
                    unusedRect.setOnMouseExited(event -> unusedRect.setStyle(""));
                }
            }
        }

        table.setGridLinesVisible(true);
        scrollPane.setPrefViewportHeight(800);
        scrollPane.setPrefViewportWidth(1150);

        return scrollPane;
    }

    public static LocalTime startCellToStartTime(Integer startCell){
        Integer hour = (startCell-1)/2 + 8;
        Integer minute = 30*((startCell+1)%2);
        return LocalTime.of(hour, minute);
    }

    public static Integer calculateCellStart(LocalTime startTime){
        int hour = startTime.getHour();
        int minute = startTime.getMinute();

        int ans = (hour-8)*2 + 1 + ((minute == 30)?1:0);
        return ans;
    }
    public static Integer calculateCellNumber(List<ServiceInfo> services){
        int totalCount = 0;
        for(ServiceInfo service : services){
            totalCount += service.getDuration().getHour()*2;
            totalCount += service.getDuration().getMinute()==30?1:0;
        }

        return totalCount;
    }
}
