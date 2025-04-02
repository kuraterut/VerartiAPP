package org.kuraterut.verartitgbotformaster.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.kuraterut.verartitgbotformaster.model.entity.Appointment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponse {
    private List<Appointment> appointments;
}