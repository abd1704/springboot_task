package com.example.demo.controller;

import com.example.demo.exception.DoctorNotFoundException;
import com.example.demo.model.Doctor;
import com.example.demo.model.Patient;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/doctors")
    List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @GetMapping("/doctors/{id}")
    Doctor getDoctorById(@PathVariable Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    @PostMapping("/doctors")
    Doctor createDoctor(@RequestBody Doctor newDoctor) {
        // Save the doctor first to generate the doctor's ID
        Doctor savedDoctor = doctorRepository.save(newDoctor);

        // Set the doctor for each patient and save them
        List<Patient> patients = newDoctor.getPatients();
        if (patients != null && !patients.isEmpty()) {
            patients.forEach(patient -> {
                patient.setDoctor(savedDoctor);
                // Assuming patientRepository is your repository for the Patient entity
                patientRepository.save(patient);
            });
        }

        return savedDoctor;
    }


    @PutMapping("/doctors/{id}")
    Doctor updateDoctor(@RequestBody Doctor newDoctor, @PathVariable Long id) {
        return doctorRepository.findById(id)
                .map(doctor -> {
                    doctor.setName(newDoctor.getName());
                    doctor.setQualification(newDoctor.getQualification());
                    // Update any other fields as needed
                    return doctorRepository.save(doctor);
                }).orElseThrow(() -> new DoctorNotFoundException(id));
    }

    @DeleteMapping("/doctors/{id}")
    String deleteDoctor(@PathVariable Long id) {
        if (!doctorRepository.existsById(id)) {
            throw new DoctorNotFoundException(id);
        }
        doctorRepository.deleteById(id);
        return "Doctor with id " + id + " has been deleted";
    }
}