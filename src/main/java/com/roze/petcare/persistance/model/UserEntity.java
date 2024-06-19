package com.roze.petcare.persistance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<AppointmentEntity> ownerAppointments = new HashSet<>();

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL)
    private Set<AppointmentEntity> vetAppointments = new HashSet<>();

    public void addRole(RoleEntity role) {
        if (roles == null) {
            roles = new HashSet<>();
        }
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(RoleEntity role) {
        if (roles != null) {
            roles.remove(role);
            role.getUsers().remove(this);
        }
    }

    public void addAppointmentToOwner(AppointmentEntity appointment) {
        if (ownerAppointments == null) {
            ownerAppointments = new HashSet<>();
        }
        appointment.setOwner(this);
        ownerAppointments.add(appointment);
    }

    public void removeAppointmentFromOwner(AppointmentEntity appointment) {
        if (ownerAppointments != null) {
            ownerAppointments.remove(appointment);
            appointment.setOwner(null);
        }
    }

    public void addAppointmentToVet(AppointmentEntity appointment) {
        if (vetAppointments == null) {
            vetAppointments = new HashSet<>();
        }
        appointment.setVet(this);
        vetAppointments.add(appointment);
    }

    public void removeAppointmentFromVet(AppointmentEntity appointment) {
        if (vetAppointments != null) {
            vetAppointments.remove(appointment);
            appointment.setVet(null);
        }
    }
}
