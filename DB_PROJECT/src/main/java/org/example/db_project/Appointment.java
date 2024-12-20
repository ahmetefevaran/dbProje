package org.example.db_project;

public class Appointment {
    private String id;
    private String doctorName;
    private String patientName;
    private String appointmentDate;
    private String appointmentTime;
    private String specialization;

    public Appointment(String id, String doctorName, String patientName, String appointmentDate, String appointmentTime, String specialization) {
        this.id = id;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.specialization = specialization;
    }

    public String getId() {
        return id;
    }

    public String getSpecialization() { return specialization; }

    public String getDoctorName() {
        return doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }
}
