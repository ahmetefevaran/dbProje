package org.example.db_project;

public class Appointment {
    private String id;
    private String doctorName;
    private String patientName;
    private String appointmentDate;
    private String appointmentTime;

    public Appointment(String id, String doctorName, String patientName, String appointmentDate, String appointmentTime) {
        this.id = id;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public String getId() {
        return id;
    }

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
