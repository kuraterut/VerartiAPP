package org.kuraterut.verartitgbotformaster.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.verartitgbotformaster.utils.DateUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MasterInfo {
    String name;
    String surname;
    String patronymic;
    String phone;
    String bio;

    public String buildInfo(){
        StringBuilder str = new StringBuilder();
        str.append("Имя: ").append(name).append("\n");
        str.append("Фамилия: ").append(surname).append("\n");
        str.append("Отчество: ").append(patronymic).append("\n");
        str.append("Телефон: ").append(phone).append("\n");
        str.append("Биография: ").append(bio).append("\n");
        return str.toString();
    }
}
