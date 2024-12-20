package org.example.db_project;

public class Appointment {
    private String id;
    private String doctorName;
    private String patientName;
    private String appointmentDate;
    private String appointmentTime;
    private String status;

    public Appointment(String id, String doctorName, String patientName, String appointmentDate, String appointmentTime,String status) {
        this.id = id;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
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
