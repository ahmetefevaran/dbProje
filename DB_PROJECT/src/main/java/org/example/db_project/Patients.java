package org.example.db_project;

public class Patients {
    private String patient_id;
    private String user_id;
    private String date_of_birth;
    private String gender;
    private String address;
    private String phone;
    private String bloodtype;

    // Parametreli Constructor
    public Patients(String patient_id, String user_id, String date_of_birth, String gender, String address, String phone, String bloodtype) {
        this.patient_id = patient_id;
        this.user_id = user_id;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.bloodtype = bloodtype;
    }

    // Getter ve Setter Metotları
    public String getPatientId() {
        return patient_id;
    }

    public void setPatientId(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodType() {
        return bloodtype;
    }

    public void setBloodType(String bloodtype) {
        this.bloodtype = bloodtype;
    }

}
