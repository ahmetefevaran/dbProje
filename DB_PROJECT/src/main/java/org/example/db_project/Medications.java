package org.example.db_project;

public class Medications {
    private String prescription_id;
    private String doctor_id;
    private String patient_id;
    private String medication_name;
    private String dosage_instructions;
    private String prescribed_at;
    private String patient_name;


    public Medications(String prescription_id, String doctor_id, String patient_id, String medication_name, String dosage_instructions, String prescribed_at, String patient_name) {
        this.prescription_id = prescription_id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.medication_name = medication_name;
        this.dosage_instructions = dosage_instructions;
        this.prescribed_at = prescribed_at;
        this.patient_name =patient_name;
    }

    public String getPrescription_id() {
        return prescription_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getprescription_id() {
        return prescription_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getMedication_name() {
        return medication_name;
    }

    public String getDosage_instructions() {
        return dosage_instructions;
    }

    public String getPrescribed_at() {
        return prescribed_at;
    }
}
