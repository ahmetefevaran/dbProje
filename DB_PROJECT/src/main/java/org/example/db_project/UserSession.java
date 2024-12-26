package org.example.db_project;

public class UserSession {
    private String sesion_user_id;
    private String sesion_doctor_id;
    private String sesion_patient_id;

    // Constructor
    public UserSession(String sesion_user_id, String sesion_doctor_id, String sesion_patient_id) {
        this.sesion_user_id = sesion_user_id;
        this.sesion_doctor_id = sesion_doctor_id;
        this.sesion_patient_id = sesion_patient_id;
    }

    // Getter ve Setter metotları
    public String getSesionUserId() {
        return sesion_user_id;
    }

    public void setSesionUserId(String sesion_user_id) {
        this.sesion_user_id = sesion_user_id;
    }

    public String getSesionDoctorId() {
        return sesion_doctor_id;
    }

    public void setSesionDoctorId(String sesion_doctor_id) {
        this.sesion_doctor_id = sesion_doctor_id;
    }

    public String getSesionPatientId() {
        return sesion_patient_id;
    }

    public void setSesionPatientId(String sesion_patient_id) {
        this.sesion_patient_id = sesion_patient_id;
    }
}
