package com.hospital.mediconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    
    private String phone;
    
    private String role; // e.g., patient, doctor, admin

    @Column(name = "hospital_id")
    private Long hospitalId;

    private String specialization;
    
    @Column(name = "availability_status")
    private String availabilityStatus;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    private String gender;

    @Column(name = "blood_group")
    private String bloodGroup;

    private String address;

    @Column(name = "emergency_contact")
    private String emergencyContact;
}
