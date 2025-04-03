package org.admin.UI.window.dayInfoWindow.tables;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.Main;
import org.admin.UI.window.dayInfoWindow.dialog.MasterInfoDialog;
import org.admin.connection.getRequests.GetAppointment;
import org.admin.UI.window.dayInfoWindow.dialog.AppointmentInfoDialog;
import org.admin.UI.window.dayInfoWindow.dialog.CreateAppointmentDialog;
import org.admin.connection.getRequests.GetUser;
import org.admin.model.*;
import org.admin.utils.AppointmentStatus;

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

        List<User> masters = GetUser.getMastersByDate(token, date, true);

        Map<Long, List<Appointment>> dayInfo = new HashMap<>();
        List<Appointment> dailyAppointments = GetAppointment.getListByDate(token, date);

        for(User master : masters){
            dayInfo.put(master.getId(), new ArrayList<>());
        }
        for(Appointment appointment : dailyAppointments){
            dayInfo.getOrDefault(appointment.getMaster().getId(), new ArrayList<>()).add(appointment);
        }

        int countColumn = 0;
        for(Long masterId : dayInfo.keySet()){
            Set<Integer> usedCells = new HashSet<>();
            countColumn++;
            table.getColumnConstraints().add(new ColumnConstraints(200));
            User master = GetUser.getById(token, masterId);

            Label masterSurnameLbl = new Label(master.getSurname());
            Rectangle masterClieckRect = new Rectangle(200, 50, Color.TRANSPARENT);
            masterClieckRect.setOnMouseClicked(event -> MasterInfoDialog.show(master.getId(), masterClieckRect, date));
            masterClieckRect.setOnMouseEntered(event -> {
                masterClieckRect.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
            });
            masterClieckRect.setOnMouseExited(event -> masterClieckRect.setStyle("-fx-fill: transparent;"));

            table.add(masterSurnameLbl, countColumn, 0);
            table.add(masterClieckRect, countColumn, 0);
            GridPane.setHalignment(masterSurnameLbl, HPos.CENTER);
            GridPane.setValignment(masterSurnameLbl, VPos.CENTER);
            GridPane.setHalignment(masterClieckRect, HPos.CENTER);
            GridPane.setValignment(masterClieckRect, VPos.CENTER);

            List<Appointment> appointments = dayInfo.get(masterId);
            Set<Integer> startCellsSet = new HashSet<>();
            for(Appointment appointment: appointments){
                Integer startCellForSet = calculateCellStart(appointment.getStartTime());
                startCellsSet.add(startCellForSet);
            }

            for(Appointment appointment: appointments){
                if(appointment.getOptions().isEmpty()) continue;
                Long id = appointment.getId();
                Client client = appointment.getClient();
                List<Option> options = appointment.getOptions();
                Option firstOption = options.get(0);
                Integer cellStart = calculateCellStart(appointment.getStartTime());
                Integer cellNumber = calculateCellNumber(options);
                if(CELLS_IN_COLUMN_COUNT - cellStart < cellNumber){cellNumber = CELLS_IN_COLUMN_COUNT - cellStart+1;}
                Rectangle rectStart = new Rectangle(200, 40, Color.AQUAMARINE);
                if(appointment.getStatus() == AppointmentStatus.COMPLETED) rectStart.setFill(Color.ORANGE);
                if(appointment.getStatus() == AppointmentStatus.CANCELLED) rectStart.setFill(Color.YELLOW);

                Rectangle clickRect = new Rectangle(200, 40, Color.TRANSPARENT);
                clickRect.setOnMouseClicked(event -> AppointmentInfoDialog.show(id, clickRect));
                clickRect.setOnMouseEntered(event -> {
                    clickRect.setStyle("-fx-cursor: hand; -fx-opacity: 0.2; -fx-fill: grey");
                });
                clickRect.setOnMouseExited(event -> clickRect.setStyle("-fx-fill: transparent;"));

                Label appointmentHeadLbl = new Label("Запись №"+id);

                Label clientLbl = new Label(client.getFio());
                Label serviceLbl = new Label();

                if(options.size() == 1){
                    serviceLbl.setText(firstOption.getName());
                }
                else{
                    serviceLbl.setText(firstOption.getName()+"...");
                }
                VBox appointmentBox = new VBox(appointmentHeadLbl, clientLbl);
                appointmentBox.setAlignment(Pos.CENTER);
                table.add(rectStart, countColumn, cellStart);
                table.add(appointmentBox, countColumn, cellStart);
                table.add(clickRect, countColumn, cellStart);

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
                    unusedRect.setOnMouseClicked(event -> CreateAppointmentDialog.show(master, date, startCellToCreate, appointments, unusedRect));
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
    public static Integer calculateCellNumber(List<Option> options){
        int totalCount = 0;
        for(Option option : options){
            totalCount += option.getDuration().getHour()*2;
            totalCount += option.getDuration().getMinute()==30?1:0;
        }

        return totalCount;
    }
}
