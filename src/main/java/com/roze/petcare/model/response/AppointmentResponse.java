package com.roze.petcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private Long appointmentId;
    private String ownerName;
    private String ownerSurname;
    private String vetName;
    private String vetSurname;
    private LocalDateTime appointmentDateTime;
    private String reason;
}
