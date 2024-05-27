package com.roze.petcare.service;

import com.roze.petcare.model.request.AppointmentRequest;
import com.roze.petcare.model.response.AppointmentResponse;
import org.springframework.stereotype.Service;

public interface AppointmentService {
    AppointmentResponse saveAppointment(AppointmentRequest appointmentRequest);

    AppointmentResponse findAppointmentById(Long id);

    AppointmentResponse updateAppointmentById(Long id, AppointmentRequest appointmentRequest);

    void deleteAppointmentById(Long id);
}
