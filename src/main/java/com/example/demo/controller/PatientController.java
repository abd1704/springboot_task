package com.example.demo.controller;

import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/patients")
    List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/patients/{id}")
    Patient getPatientById(@PathVariable Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
    }

    @PostMapping("/patients")
    Patient createPatient(@RequestBody Patient newPatient) {
        return patientRepository.save(newPatient);
    }

    @PutMapping("/patients/{id}")
    Patient updatePatient(@RequestBody Patient newPatient, @PathVariable Long id) {
        return patientRepository.findById(id)
                .map(patient -> {
                    patient.setName(newPatient.getName());
                    patient.setAge(newPatient.getAge());
                    patient.setAllergies(newPatient.getAllergies());
                    // Update any other fields as needed
                    return patientRepository.save(patient);
                }).orElseThrow(() -> new PatientNotFoundException(id));
    }

    @DeleteMapping("/patients/{id}")
    String deletePatient(@PathVariable Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException(id);
        }
        patientRepository.deleteById(id);
        return "Patient with id " + id + " has been deleted";
    }
}