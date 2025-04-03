package org.kuraterut.verartitgbotformaster.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("start_time")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonProperty("comment")
    private String comment;
    @JsonProperty("client_name")
    private String clientName;
    @JsonProperty("option_names")
    private List<String> optionNames;

    public String buildInfo(){
        StringBuilder str = new StringBuilder();
        str.append("Время: ").append(startTime.toString()).append("\n");

        str.append("Клиент: ").append(clientName).append("\n");
        str.append("Услуги: ").append("\n");
        for (String optionName : optionNames){
            str.append(optionName).append(";\n");
        }
        str.append("\n");
        str.append("Комментарий: ").append(comment).append("\n\n");
        return str.toString();
    }
}
