package com.example.demo.controller;

import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.exception.PrescriptionNotFoundException;
import com.example.demo.model.Prescription;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PrescriptionController {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping("/patients/{patientId}/prescriptions")
    Prescription newPrescriptionForPatient(@PathVariable Long patientId, @RequestBody Prescription newPrescription) {
        return patientRepository.findById(patientId)
                .map(patient -> {
                    newPrescription.setPatient(patient);
                    return prescriptionRepository.save(newPrescription);
                })
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    @GetMapping("/prescriptions")
    List<Prescription> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    @GetMapping("/prescriptions/{id}")
    Prescription getPrescriptionById(@PathVariable Long id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(id));
    }

    @PutMapping("/prescriptions/{id}")
    Prescription updatePrescription(@RequestBody Prescription newPrescription, @PathVariable Long id) {
        return prescriptionRepository.findById(id)
                .map(prescription -> {
                    prescription.setDrugs(newPrescription.getDrugs());
                    prescription.setDate(newPrescription.getDate());
                    // Update any other fields as needed
                    return prescriptionRepository.save(prescription);
                }).orElseThrow(() -> new PrescriptionNotFoundException(id));
    }

    @DeleteMapping("/prescriptions/{id}")
    String deletePrescription(@PathVariable Long id) {
        if (!prescriptionRepository.existsById(id)) {
            throw new PrescriptionNotFoundException(id);
        }
        prescriptionRepository.deleteById(id);
        return "Prescription with id " + id + " has been deleted";
    }
}