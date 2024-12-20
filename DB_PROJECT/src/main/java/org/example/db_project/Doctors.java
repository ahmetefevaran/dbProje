package org.example.db_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Doctors {
    private String doctor_name;
    private String doctor_id;
    private String user_id;
    private String specialization;
    private String phone;

    // Parametreli Constructor
    public Doctors(String doctor_id, String user_id, String doctor_name) {
        this.doctor_id = doctor_id;
        this.user_id = user_id;
        this.doctor_name = doctor_name;
    }

    // Getter Metotları
    public String getDoctorId() {
        return doctor_id;
    }

    public String getDoctorName() {
        return doctor_name;
    }

    public String getUserId() {
        return user_id;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return doctor_name; // ComboBox'ta kullanıcıya görünen metin
    }
}
