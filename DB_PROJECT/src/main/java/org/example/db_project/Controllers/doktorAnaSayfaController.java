package org.example.db_project.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.db_project.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import org.example.db_project.HelloApplication;
import org.example.db_project.Medications;
import org.example.db_project.TestResults;

public class doktorAnaSayfaController {

    public TableColumn recetelerimPatientColumn;
    @FXML private TableView<Appointment> randevuYonetTable;
    @FXML private TableColumn<Appointment, String> idRow;
    @FXML private TableColumn<Appointment, String> hastaAdiRow;
    @FXML private TableColumn<Appointment, String> tarihColumn;
    @FXML private TableColumn<Appointment, Void> durumColumn;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<?, ?> DozColumn;

    @FXML
    private TableColumn<?, ?> buttonColumn;

    @FXML
    private TableColumn<?, ?> dateColumn;

    @FXML
    private TextField dozField;


    @FXML
    private TableColumn<?, ?> hastaAdiColumn;

    @FXML
    private TextField hastaAdiField;


    @FXML
    private ComboBox<String> ilacAdiComboBox;

    @FXML
    private TableColumn<?, ?> ilacColumn;

    @FXML
    private Button kaydetBtn;

    @FXML
    private TableColumn<?, ?> patientColumn;


    @FXML
    private Button randevuYonetAraButton;

    @FXML
    private TextField randevuYonetHastaAdInput;

    @FXML
    private DatePicker randevuYonetbaslangicTarihInput;

    @FXML
    private Button randevularAraButton1;

    @FXML
    private DatePicker randevularBaslangicTarihInput;

    @FXML
    private TableColumn<?, ?> randevularDurumColumn;

    @FXML
    private TextField randevularDurumInput;

    @FXML
    private TextField randevularHastaAdInput;

    @FXML
    private TableColumn<?, ?> randevularIdColumn;

    @FXML
    private TableView<Appointment> randevularTable;

    @FXML
    private Button recetelerimAraButton;

    @FXML
    private TextField recetelerimHastaAdInput;


    @FXML
    private TableView<Medications> recetelerimTable;

    @FXML
    private DatePicker recetelerimbaslangicTarihInput;

    @FXML
    private TableColumn<?, ?> sonucColumn;

    @FXML
    private TableColumn<?, ?> startDateColumn;

    @FXML
    private Button tahlilAraButton;

    @FXML
    private DatePicker tahlilBaslangicTarihInput;

    @FXML
    private TableColumn<?, ?> tahlilDateColumn;

    @FXML
    private TextField tahlilAdInput;
    

    @FXML
    private TextField tahlilPoliklinikInput;

    @FXML
    private TableView<TestResults> tahlilSonucTable;


    @FXML
    private TableColumn<?, ?> timeColumn;

    @FXML
    private TableColumn<?, ?> tabloTahlilAdi;

    @FXML
    private ComboBox<String> randevularTamamlanmaDurumuInput;

    @FXML
    private TextField hastaAdiInput;

    @FXML
    private Label receteLabel;


    @FXML
    private Button newPrescription;

    @FXML
    private Button panel_cikis_button;

    @FXML
    private Label panel_cinsiyet;

    @FXML
    private Label panel_dogum_tarihi;

    @FXML
    private Label panel_doktor_ad_label;

    @FXML
    private Label panel_doktor_soyad_label;

    @FXML
    private Label panel_doktor_tc_label;

    @FXML
    private Button panel_duzenle_button;

    @FXML
    private Label panel_email;

    @FXML
    private ImageView panel_hasta_image;

    @FXML
    private Label panel_tel_no;

    @FXML
    private Label panel_poliklinik;

    @FXML
    private VBox panel_yaklasan_randevular_vbox;

    @FXML private TableColumn<Medications, Void> recetelerimDurumColumn;

    final String sesion_user_id = HelloApplication.userSession.getSesionUserId();
    final String sesion_doctor_id = HelloApplication.userSession.getSesionDoctorId();

    //Functions
    private String getPatientIdByTC(String tcNumber) {
        String sql = "SELECT p.patient_id FROM patients p " +
                "JOIN users u ON u.user_id = p.user_id " +
                "WHERE u.tc_number = ?";
        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tcNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("patient_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no patient found
    }

    private boolean insertPrescription(String patientId, String medicationName, String dosage) {
        String sql = "INSERT INTO prescriptions (patient_id, doctor_id, medication_name, dosage, prescribed_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(patientId));
            pstmt.setInt(2, Integer.parseInt(sesion_doctor_id)); // Replace with the correct doctor ID
            pstmt.setString(3, medicationName);
            pstmt.setInt(4, Integer.parseInt(dosage));
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    void setYaklasanRandevular() {
        String sql = "SELECT * FROM doctorAppointments " +
                "WHERE doctor_id = ? " +
                "AND status = 'planned' " +
                "AND appointment_date BETWEEN CURRENT_DATE AND CURRENT_DATE + 2";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(sesion_doctor_id)); // Patient ID
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String doctor_name = rs.getString("doctor_name");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentTime = rs.getString("appointment_time");
                String specialization = rs.getString("specialization");

                // Tarih, doktor ve klinik bilgilerini 3 ayrı label olarak ayırıyoruz
                String time = appointmentTime.substring(0, 5);
                String dateText = appointmentDate + " " + time;
                String doctorText = doctor_name;
                String specializationText = specialization;

                // HBox içinde her label'ı yan yana hizalayacağız
                HBox hbox = new HBox();
                hbox.setSpacing(10); // Aralarına boşluk ekler
                hbox.setAlignment(Pos.CENTER_LEFT); // Sağ üst hizalama

                // HBox'a border ekliyoruz
                hbox.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-background-color: #f5f5f5; -fx-border-radius: 5px;");

                // Date Label
                Label dateLabel = new Label(dateText);
                dateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

                // Doctor Label
                Label doctorLabel = new Label(doctorText);
                doctorLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

                // Specialization Label
                Label specializationLabel = new Label(specializationText);
                specializationLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

                // HBox'a Label'ları ekle
                hbox.getChildren().addAll(dateLabel, doctorLabel, specializationLabel);

                // VBox'a HBox'ı ekle
                panel_yaklasan_randevular_vbox.getChildren().add(hbox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void panel_cikis_button_onaction(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/db_project/giris-sayfasi.fxml"));
            Pane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = (Stage) panel_cikis_button.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPopup() {
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window
        popupStage.setTitle("Yeni Reçete Ekle");

        // Layout for the popup
        GridPane popupLayout = new GridPane();
        popupLayout.setPadding(new Insets(10));
        popupLayout.setHgap(10);
        popupLayout.setVgap(10);

        // Components for the popup
        Label hastaTCLabel = new Label("Hasta TC:");
        TextField hastaTCField = new TextField();
        hastaTCField.setPromptText("Hasta TC");

        Label ilacAdiLabel = new Label("İlaç Adı:");
        ComboBox<String> ilacAdiComboBox = new ComboBox<>();
        ilacAdiComboBox.getItems().addAll("Paracetamol", "Ibuprofen", "Amoxicillin", "Ciprofloxacin", "Metformin");
        ilacAdiComboBox.setPromptText("İlaç Adı");

        Label dozLabel = new Label("Doz:");
        TextField dozField = new TextField();
        dozField.setPromptText("Doz");

        Button kaydetButton = new Button("Kaydet");
        kaydetButton.setOnAction(event -> {
            // Retrieve input values
            String hastaTC = hastaTCField.getText();
            System.out.println(hastaTC);
            String ilacAdi = ilacAdiComboBox.getValue();
            String doz = dozField.getText();

            if (hastaTC.isEmpty() || ilacAdi == null || doz.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Lütfen tüm alanları doldurun!");
                alert.show();
                return;
            }

            // Fetch the patient_id using hastaTC
            String patientId = getPatientIdByTC(hastaTC);
            if (patientId == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Hasta bulunamadı!");
                alert.show();
                return;
            }

            // Insert the prescription
            if (insertPrescription(patientId, ilacAdi, doz)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Reçete başarıyla kaydedildi!");
                alert.show();
                popupStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Reçete kaydedilirken bir hata oluştu!");
                alert.show();
            }

            updateReceteler();
        });

        // Add components to the popup layout
        popupLayout.add(hastaTCLabel, 0, 0);
        popupLayout.add(hastaTCField, 1, 0);
        popupLayout.add(ilacAdiLabel, 0, 1);
        popupLayout.add(ilacAdiComboBox, 1, 1);
        popupLayout.add(dozLabel, 0, 2);
        popupLayout.add(dozField, 1, 2);
        popupLayout.add(kaydetButton, 1, 3);

        // Set the scene and show the popup
        Scene popupScene = new Scene(popupLayout, 300, 200);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }






    Connection connectToDatabase() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proje";
        String user = "doctor";
        String password = "doctor";
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }

    @FXML
    void randevuYonetAraButtonAction(ActionEvent event) {

        String hastaAdi = randevuYonetHastaAdInput.getText();
        LocalDate selectedDate = randevuYonetbaslangicTarihInput.getValue();

        String sql = "SELECT * FROM doctorAppointments WHERE doctor_id = ? AND patient_name ILIKE ? AND appointment_date >= ? AND status = 'planned'";
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (selectedDate == null) selectedDate = LocalDate.now();

            pstmt.setInt(1,Integer.valueOf(sesion_doctor_id));
            pstmt.setString(2,"%"+hastaAdi+"%");
            pstmt.setTimestamp(3, Timestamp.valueOf(selectedDate.atStartOfDay()));
            ResultSet rs = pstmt.executeQuery();
            ObservableList<Appointment> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                String id = rs.getString("appointment_id");
                String patientName = rs.getString("patient_name");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentTime = rs.getString("appointment_time");
                String status = rs.getString("status");

                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Appointment(id,null,patientName, appointmentDate, appointmentTime,null,status, sesion_user_id));
            }
            randevuYonetTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevularAraButtonAction() {

        String hastaAdi = randevularHastaAdInput.getText();
        LocalDate selectedDate = randevularBaslangicTarihInput.getValue();
        String statusInput = (String) randevularTamamlanmaDurumuInput.getValue();

        String sql = "SELECT * FROM doctorAppointments WHERE doctor_id = ? AND patient_name ILIKE ? "+ (statusInput != null && !statusInput.equals("all") ?  "AND status = ?" : "") +
                (selectedDate != null ? " AND appointment_date >= ?" : "");
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,Integer.valueOf(sesion_doctor_id));
            pstmt.setString(2,"%"+hastaAdi+"%");
            if (statusInput !=null && !statusInput.equals("all")) pstmt.setString(3,statusInput);
            if (selectedDate !=null)
            {
                if (statusInput !=null && !statusInput.equals("all")) pstmt.setTimestamp(4, Timestamp.valueOf(selectedDate.atStartOfDay()));
                else pstmt.setTimestamp(3, Timestamp.valueOf(selectedDate.atStartOfDay()));
            }

            ResultSet rs = pstmt.executeQuery();
            ObservableList<Appointment> liste = FXCollections.observableArrayList();

            while (rs.next()) {
                String id = rs.getString("appointment_id");
                String patientName = rs.getString("patient_name");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentTime = rs.getString("appointment_time");
                String status = rs.getString("status");


                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Appointment(id,null,patientName, appointmentDate, appointmentTime,null,status,sesion_user_id));
            }
            randevularTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void recetelerimAraButtonAction(ActionEvent event) {
        String hastaAdi = recetelerimHastaAdInput.getText();
        LocalDate selectedDate = recetelerimbaslangicTarihInput.getValue();


        String sql = "SELECT pr.medication_name, pr.dosage, pr.prescribed_date, pr.doctor_id, pr.patient_id, u.name, pr.prescription_id " +
                "FROM prescriptions pr " +
                "JOIN patients p ON pr.patient_id = p.patient_id " +
                "JOIN users u ON p.user_id = u.user_id " +
                "WHERE u.name ILIKE ? " +
                (selectedDate != null ? "AND pr.prescribed_date >= ?" : "");

        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {


            pstmt.setString(1,"%"+hastaAdi+"%");
            if (selectedDate != null)  pstmt.setTimestamp(2, Timestamp.valueOf(selectedDate.atStartOfDay()));

            ResultSet rs = pstmt.executeQuery();

            ObservableList<Medications> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                String ilacAdi = rs.getString("medication_name");
                String patient_id = rs.getString("patient_id");
                String doctor_id = rs.getString("doctor_id");
                String prescribed_at = rs.getString("prescribed_date");
                String dosage = rs.getString("dosage");
                String name = rs.getString("name");
                String prescriptionId = rs.getString("prescription_id");

                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Medications(prescriptionId, doctor_id, patient_id, ilacAdi, dosage, prescribed_at,name,null));
            }
            recetelerimTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tahlilAraButtonAction(ActionEvent event) {
        updateTahlil();
    }

    void updateRandevular()
    {
        randevularAraButtonAction();
    }

    void updateRandevuYonet()
    {
        randevuYonetAraButtonAction(null);
    }

    void updateReceteler()
    {
        recetelerimAraButtonAction(null);

    }


    void updateTahlil() {
        String tahlilAdi = tahlilAdInput.getText();
        LocalDate baslangic_date = tahlilBaslangicTarihInput.getValue();


        String sql = "SELECT t.name AS analysis_name, t.date, t.result, u.name AS patient_name " +
                "FROM analysis t " +
                "JOIN patients p ON p.patient_id = t.patient_id " +
                "JOIN doctors d ON t.doctor_id = d.doctor_id " +
                "JOIN users u ON u.user_id = p.user_id " +
                "WHERE d.doctor_id = ? ";

        if (tahlilAdi != null && !tahlilAdi.isEmpty()) {
            sql += "AND t.name ILIKE ? ";
        }
        if (baslangic_date != null) {
            sql += "AND t.date >= ? ";
        }
        sql += "ORDER BY t.date DESC";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(sesion_doctor_id));

            int index = 2;
            if (tahlilAdi != null && !tahlilAdi.isEmpty()) {
                pstmt.setString(index++, "%" + tahlilAdi + "%");
            }
            if (baslangic_date != null) {
                pstmt.setDate(index++, Date.valueOf(baslangic_date));
            }

            ResultSet rs = pstmt.executeQuery();
            ObservableList<TestResults> liste = FXCollections.observableArrayList();

            while (rs.next()) {
                String testName = rs.getString("analysis_name");
                String testDate = rs.getString("date");
                String testResult = rs.getString("result");
                String patientName = rs.getString("patient_name");

                liste.add(new TestResults(testName, testDate, testResult,null, patientName));
            }

            tahlilDateColumn.setCellValueFactory(new PropertyValueFactory<>("testDate"));
            sonucColumn.setCellValueFactory(new PropertyValueFactory<>("testResult"));
            tabloTahlilAdi.setCellValueFactory(new PropertyValueFactory<>("testName"));
            hastaAdiColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

            tahlilSonucTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void panel_duzenle_button_onaction(ActionEvent event) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Profil Düzenle");

        GridPane popupLayout = new GridPane();
        popupLayout.setPadding(new Insets(10));
        popupLayout.setHgap(10);
        popupLayout.setVgap(10);


        Label telefonLabel = new Label("Telefon No:");
        TextField telefonField = new TextField();
        telefonField.setPromptText("Telefon No (10 Haneli)");

        Label emailLabel = new Label("E-posta:");
        TextField emailField = new TextField();
        emailField.setPromptText("E-posta");

        Button kaydetButton = new Button("Kaydet");
        kaydetButton.setOnAction(e -> {

            String telefon = telefonField.getText();
            String email = emailField.getText();

            boolean isUpdated = false;


            if (!telefon.isEmpty()) {
                if (!telefon.matches("\\d{10}") || telefon.startsWith("0")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Telefon numarası 10 haneli olmalı ve başında '0' olmamalıdır!");
                    alert.show();
                    return;
                }
                System.out.println("Telefon güncelleniyor: " + telefon);
                isUpdated = updateTelefon(telefon);

            }

            if (!email.isEmpty()) {
                System.out.println("E-posta güncelleniyor: " + email);
                isUpdated = updateEmail(email);

            }

            // Feedback to user
            if (isUpdated) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Profil başarıyla güncellendi!");
                alert.show();
                update_yan_panel();
                popupStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Herhangi bir değişiklik yapılmadı.");
                alert.show();
            }
        });

        popupLayout.add(telefonLabel, 0, 1);
        popupLayout.add(telefonField, 1, 1);
        popupLayout.add(emailLabel, 0, 2);
        popupLayout.add(emailField, 1, 2);
        popupLayout.add(kaydetButton, 1, 3);

        Scene popupScene = new Scene(popupLayout, 400, 250);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private boolean updateAddress(String address) {

        String sql = "UPDATE doctors SET address = ? WHERE user_id = ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, address);
            pstmt.setInt(2, Integer.parseInt(sesion_user_id));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Adres başarıyla güncellendi.");
                return true;
            } else {
                System.out.println("Kullanıcı bulunamadı veya adres güncellenemedi.");
                return false;
            }

        } catch (Exception er) {
            er.printStackTrace();
            return false;
        }
    }

    private boolean updateTelefon(String telefon) {

        String sql = "UPDATE doctors SET phone = ? WHERE user_id = ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, telefon);
            pstmt.setInt(2, Integer.parseInt(sesion_user_id));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Telefon başarıyla güncellendi.");
                return true;
            } else {
                System.out.println("Kullanıcı bulunamadı veya telefon güncellenemedi.");
                return false;
            }

        } catch (Exception er) {
            er.printStackTrace();
            return false;
        }
    }

    private boolean updateEmail(String email) {

        String sql = "UPDATE users SET email = ? WHERE user_id = ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setInt(2, Integer.parseInt(sesion_user_id));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("E-posta başarıyla güncellendi.");
                return true;
            } else {
                System.out.println("Kullanıcı bulunamadı veya E-posta güncellenemedi.");
                return false;
            }

        } catch (Exception er) {
            er.printStackTrace();
            return false;
        }
    }

    void update_yan_panel() {
        try (Connection conn = connectToDatabase()) {
            String sql = "SELECT * FROM doctorprofile WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                int userId = Integer.parseInt(sesion_user_id);
                pstmt.setInt(1, userId);

                ResultSet rs = pstmt.executeQuery();

                // Eğer sonuç varsa bilgileri atama yap
                if (rs.next()) {
                    String fullName = rs.getString("name");
                    String[] nameParts = fullName.split(" ");

                    String firstName = nameParts[0];
                    panel_doktor_ad_label.setText(firstName);

                    String lastName = nameParts[nameParts.length - 1];
                    panel_doktor_soyad_label.setText(lastName);

                    panel_email.setText(rs.getString("email"));
                    panel_tel_no.setText(rs.getString("phone"));
                    panel_cinsiyet.setText(rs.getString("gender"));
                    panel_doktor_tc_label.setText(rs.getString("tc_number"));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                } else {
                    System.out.println("Hasta bilgisi bulunamadı.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setYaklasanRandevular();
    }


    @FXML
    void initialize() {
        assert newPrescription != null : "fx:id=\"newPrescription\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert receteLabel != null : "fx:id=\"receteLabel\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert hastaAdiInput != null : "fx:id=\"hastaAdiInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularTamamlanmaDurumuInput != null : "fx:id=\"randevularTamamlanmaDurumuInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert buttonColumn != null : "fx:id=\"buttonColumn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert dateColumn != null : "fx:id=\"dateColumn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert dozField != null : "fx:id=\"dozField\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert durumColumn != null : "fx:id=\"durumColumn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert hastaAdiField != null : "fx:id=\"hastaAdiField\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert hastaAdiRow != null : "fx:id=\"hastaAdiRow\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert idRow != null : "fx:id=\"idRow\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert ilacAdiComboBox != null : "fx:id=\"ilacAdiComboBox\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert kaydetBtn != null : "fx:id=\"kaydetBtn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert patientColumn != null : "fx:id=\"patientColumn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetAraButton != null : "fx:id=\"randevuYonetAraButton\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetHastaAdInput != null : "fx:id=\"randevuYonetHastaAdInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetTable != null : "fx:id=\"randevuYonetTable\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetbaslangicTarihInput != null : "fx:id=\"randevuYonetbaslangicTarihInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularAraButton1 != null : "fx:id=\"randevularAraButton1\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularBaslangicTarihInput != null : "fx:id=\"randevularBaslangicTarihInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularHastaAdInput != null : "fx:id=\"randevularHastaAdInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularTable != null : "fx:id=\"randevularTable\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert recetelerimAraButton != null : "fx:id=\"recetelerimAraButton\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert recetelerimHastaAdInput != null : "fx:id=\"recetelerimHastaAdInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert recetelerimTable != null : "fx:id=\"recetelerimTable\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert recetelerimbaslangicTarihInput != null : "fx:id=\"recetelerimbaslangicTarihInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert tahlilAraButton != null : "fx:id=\"tahlilAraButton\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert tahlilBaslangicTarihInput != null : "fx:id=\"tahlilBaslangicTarihInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert tahlilAdInput != null : "fx:id=\"tahlilAdInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert tahlilPoliklinikInput != null : "fx:id=\"tahlilPoliklinikInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert tahlilSonucTable != null : "fx:id=\"tahlilSonucTable\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert tarihColumn != null : "fx:id=\"tarihColumn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert timeColumn != null : "fx:id=\"timeColumn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";

        ObservableList<String> options = FXCollections.observableArrayList(
                "planned",
                "completed",
                "cancelled",
                "all"
        );
        randevularTamamlanmaDurumuInput.setItems(options);

        try (Connection conn = connectToDatabase()) {
            // SQL sorgusu: `doctorprofile` görünümünden kullanıcı bilgilerini al
            String sql = "SELECT * FROM doctorprofile WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                int userId = Integer.parseInt(sesion_user_id);
                pstmt.setInt(1, userId);

                ResultSet rs = pstmt.executeQuery();

                // Eğer sonuç varsa bilgileri atama yap
                if (rs.next()) {
                    String fullName = rs.getString("name");
                    String[] nameParts = fullName.split(" ");

                    String firstName = nameParts[0];
                    panel_doktor_ad_label.setText(firstName);

                    String lastName = nameParts[nameParts.length - 1];
                    panel_doktor_soyad_label.setText(lastName);

                    panel_email.setText(rs.getString("email"));
                    panel_tel_no.setText(rs.getString("phone"));
                    panel_cinsiyet.setText(rs.getString("gender"));
                    panel_poliklinik.setText(rs.getString("specialization"));
                    panel_doktor_tc_label.setText(rs.getString("tc_number"));

                } else {
                    System.out.println("Doktor bilgisi bulunamadı.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setYaklasanRandevular();


        // randevu yönet icin veri bağlama
        idRow.setCellValueFactory(new PropertyValueFactory<>("id"));
        hastaAdiRow.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // randevular icin veri bağlama
        randevularIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        randevularDurumColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // reçetelerim icin veri bağlama
        ilacColumn.setCellValueFactory(new PropertyValueFactory<>("medication_name"));
        DozColumn.setCellValueFactory(new PropertyValueFactory<>("dosage_instructions"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("prescribed_at"));
        recetelerimPatientColumn.setCellValueFactory(new PropertyValueFactory<>("patient_name"));;


        idRow.setCellValueFactory(new PropertyValueFactory<>("id"));
        hastaAdiRow.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // Durum sütunu için özel buton tanımlaması
        durumColumn.setCellFactory(new Callback<>() {
            //Sil butonuna tıklanınca burası çalışıyor
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Randevuyu İptal Et");

                    {
                        button.setOnAction(event -> {
                            Appointment data = getTableView().getItems().get(getIndex());

                            String updateSql = "UPDATE appointments " +
                                    "SET status ='cancelled' " +
                                    "WHERE appointment_id = ?";

                            try (Connection conn =connectToDatabase();
                                 PreparedStatement pstmt = conn.prepareStatement(updateSql)) {

                                pstmt.setInt(1,Integer.valueOf(data.getId()));

                                pstmt.executeQuery();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            updateRandevuYonet();
                            updateRandevular();

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        });

        recetelerimDurumColumn.setCellFactory(new Callback<>() {
            //Sil butonuna tıklanınca burası çalışıyor
            @Override
            public TableCell<Medications, Void> call(final TableColumn<Medications, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Reçeteyi sil");

                    {
                        button.setOnAction(event -> {

                            String updateSql = "DELETE FROM prescriptions " +
                                    "WHERE prescription_id = ?";

                            try (Connection conn =connectToDatabase();
                                 PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                                Medications data = getTableView().getItems().get(getIndex());

                                pstmt.setInt(1,Integer.valueOf(data.getPrescription_id()));
                                pstmt.executeQuery();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            updateReceteler();
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        });


        updateRandevular();
        updateRandevuYonet();
        updateReceteler();
        updateTahlil();

    }
}
