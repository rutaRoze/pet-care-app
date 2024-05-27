package com.roze.petcare.service.impl;

import com.roze.petcare.exception.NoChangesMadeException;
import com.roze.petcare.mapper.AppointmentMapper;
import com.roze.petcare.model.request.AppointmentRequest;
import com.roze.petcare.model.response.AppointmentResponse;
import com.roze.petcare.persistance.AppointmentRepository;
import com.roze.petcare.persistance.model.AppointmentEntity;
import com.roze.petcare.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponse saveAppointment(AppointmentRequest appointmentRequest) {
        AppointmentEntity appointmentToSave = appointmentMapper.requestToEntity(appointmentRequest);
        AppointmentEntity savedAppointment = appointmentRepository.save(appointmentToSave);

        return appointmentMapper.entityToResponse(savedAppointment);
    }

    @Override
    public AppointmentResponse findAppointmentById(Long id) {

        AppointmentEntity appointmentEntity = getAppointmentByIdOrThrow(id);

        return appointmentMapper.entityToResponse(appointmentEntity);
    }

    @Override
    public AppointmentResponse updateAppointmentById(Long id, AppointmentRequest appointmentRequest) {

        AppointmentEntity existingAppointment = getAppointmentByIdOrThrow(id);

        if (isAppointmentEqual(existingAppointment, appointmentRequest)) {
            throw new NoChangesMadeException("Appointment object was not updated as no changes of object were made.");
        }

        AppointmentEntity appointmentToUpdate = appointmentMapper.requestToEntity(appointmentRequest);

        existingAppointment.setOwner(appointmentToUpdate.getOwner());
        existingAppointment.setVet(appointmentToUpdate.getVet());
        existingAppointment.setAppointmentDateTime(appointmentToUpdate.getAppointmentDateTime());
        existingAppointment.setReason(appointmentToUpdate.getReason());

        AppointmentEntity updatedAppointment = appointmentRepository.save(existingAppointment);

        return appointmentMapper.entityToResponse(updatedAppointment);
    }

    @Override
    public void deleteAppointmentById(Long id) {
        getAppointmentByIdOrThrow(id);
        appointmentRepository.deleteById(id);
    }

    private AppointmentEntity getAppointmentByIdOrThrow(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment object not found for the given ID: " + appointmentId));
    }

    private boolean isAppointmentEqual(AppointmentEntity existingAppointment, AppointmentRequest appointmentRequest) {

        return existingAppointment.getOwner().getId().equals(appointmentRequest.getOwnerId()) &&
                existingAppointment.getVet().getId().equals(appointmentRequest.getVetId()) &&
                existingAppointment.getAppointmentDateTime().equals(appointmentRequest.getAppointmentDateTime()) &&
                existingAppointment.getReason().equals(appointmentRequest.getReason());
    }
}
