package org.kuraterut.verartitgbotformaster.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.verartitgbotformaster.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private LocalDate date;
    private LocalTime startTime;
    private String comment;
    private String clientName;
    private List<String> optionNames;

    public String buildInfo(){
        StringBuilder str = new StringBuilder();
        str.append("Время: ").append(DateUtils.localTimeToUserFormat(startTime)).append("\n");
        str.append("Клиент: ").append(clientName).append("\n");
        str.append("Услуги: ").append("\n");
        for (String optionName : optionNames){
            str.append(optionName).append(";\n");
        }
        str.append(comment).append("\n\n");
        return str.toString();
    }
}
