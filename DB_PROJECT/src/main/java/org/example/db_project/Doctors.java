package org.example.db_project;

public class Doctors {
    private String doctor_id;
    private String user_id;
    private String specialization;
    private String phone;

    // Parametreli Constructor
    public Doctors(String doctor_id, String user_id, String specialization, String phone) {
        this.doctor_id = doctor_id;
        this.user_id = user_id;
        this.specialization = specialization;
        this.phone = phone;
    }

    // Getter ve Setter Metotları
    public String getDoctorId() {
        return doctor_id;
    }

    public void setDoctorId(String doctor_id) {
        this.doctor_id = doctor_id;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}