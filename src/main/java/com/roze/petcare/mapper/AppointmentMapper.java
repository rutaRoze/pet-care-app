package com.roze.petcare.mapper;

import com.roze.petcare.model.request.AppointmentRequest;
import com.roze.petcare.model.response.AppointmentResponse;
import com.roze.petcare.persistance.UserRepository;
import com.roze.petcare.persistance.model.AppointmentEntity;
import com.roze.petcare.persistance.model.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    @Autowired
    private UserRepository userRepository;

    public AppointmentEntity requestToEntity(AppointmentRequest appointmentRequest) {

        UserEntity owner =  userRepository.findById(appointmentRequest.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + appointmentRequest.getOwnerId()));

        UserEntity vet =  userRepository.findById(appointmentRequest.getVetId())
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + appointmentRequest.getVetId()));

        return AppointmentEntity.builder()
                .owner(owner)
                .vet(vet)
                .appointmentDateTime(appointmentRequest.getAppointmentDateTime())
                .reason(appointmentRequest.getReason())
                .build();
    }

    public AppointmentResponse entityToResponse(AppointmentEntity appointmentEntity) {

        return AppointmentResponse.builder()
                .appointmentId(appointmentEntity.getAppointmentId())
                .ownerName(appointmentEntity.getOwner().getName())
                .ownerSurname(appointmentEntity.getOwner().getSurname())
                .vetName(appointmentEntity.getVet().getName())
                .vetSurname(appointmentEntity.getVet().getSurname())
                .appointmentDateTime(appointmentEntity.getAppointmentDateTime())
                .reason(appointmentEntity.getReason())
                .build();
    }
}
