package org.example.db_project.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.example.db_project.Appointment;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import org.example.db_project.Medications;

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
    private TableColumn<?, ?> policlinicCloumn;

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
    private TextField tahlilHastaAdInput;

    @FXML
    private TextField tahlilPoliklinikInput;

    @FXML
    private TableView<?> tahlilSonucTable;


    @FXML
    private TableColumn<?, ?> timeColumn;

    @FXML
    private ComboBox<String> randevularTamamlanmaDurumuInput;

    @FXML
    private TextField hastaAdiInput;

    @FXML
    private Label receteLabel;

    final String sesion_user_id = "1";

    Connection connectToDatabase() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proje";
        String user = "1";
        String password = "1";
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
    @FXML
    void kaydetButtonAction(ActionEvent event) {
        receteLabel.setVisible(false);

        String sql = "INSERT INTO prescriptions (patient_id,doctor_id,medication_name,dosage,prescribed_date) VALUES(?,?,?,?,?)";
        String patientId = hastaAdiInput.getText();
        String ilacAdi = (String) ilacAdiComboBox.getValue();
        String doz =dozField.getText();
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,Integer.parseInt(patientId));
            pstmt.setInt(2,1);
            pstmt.setString(3,ilacAdi);
            pstmt.setInt(4, Integer.parseInt(doz));
            Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
            pstmt.setTimestamp(5, currentTimestamp);
            try {
                int insertedRows = pstmt.executeUpdate();
            }catch (SQLException e){
                receteLabel.setVisible(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void randevuYonetAraButtonAction(ActionEvent event) {
        int doctorId = 1;
        String hastaAdi = randevuYonetHastaAdInput.getText();
        LocalDate selectedDate = randevuYonetbaslangicTarihInput.getValue();

        String sql = "SELECT * FROM doctorAppointments WHERE doctor_id = ? AND patient_name LIKE ? AND appointment_date >= ? AND status = 'planned'";
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (selectedDate == null) selectedDate = LocalDate.now();

            pstmt.setInt(1,doctorId);
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
                liste.add(new Appointment(id,"1",patientName, appointmentDate, appointmentTime,status, sesion_user_id));
            }
            randevuYonetTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevularAraButtonAction(ActionEvent event) {
        int doctorId = 1;
        String hastaAdi = randevularHastaAdInput.getText();
        LocalDate selectedDate = randevularBaslangicTarihInput.getValue();
        String statusInput = (String) randevularTamamlanmaDurumuInput.getValue();

        String sql = "SELECT * FROM doctorAppointments WHERE doctor_id = ? AND patient_name LIKE ? "+ (statusInput != null && !statusInput.equals("all") ?  "AND status = ?" : "") +
                (selectedDate != null ? " AND appointment_date >= ?" : "");
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,doctorId);
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
                liste.add(new Appointment(id,"1",patientName, appointmentDate, appointmentTime,status,sesion_user_id));
            }
            randevularTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void recetelerimAraButtonAction(ActionEvent event) {
        int doctorId = 1;
        String hastaAdi = recetelerimHastaAdInput.getText();
        LocalDate selectedDate = recetelerimbaslangicTarihInput.getValue();


        String sql = "SELECT pr.medication_name, pr.dosage, pr.prescribed_date, pr.doctor_id, pr.patient_id, u.name " +
                "FROM prescriptions pr " +
                "JOIN patients p ON pr.patient_id = p.patient_id " +
                "JOIN users u ON p.user_id = u.user_id " +
                "WHERE u.name LIKE ? " +
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

                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Medications("1", doctor_id, patient_id, ilacAdi, dosage, prescribed_at,name));
            }
            recetelerimTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tahlilAraButtonAction(ActionEvent event) {

    }

    void updateRandevular()
    {
        String sql = "SELECT * FROM doctorappointments";
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            ObservableList<Appointment> liste = FXCollections.observableArrayList();

            while (rs.next()) {
                String id = rs.getString("appointment_id");
                String patientName = rs.getString("patient_name");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentTime = rs.getString("appointment_time");
                String status = rs.getString("status");


                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Appointment(id,"1",patientName, appointmentDate, appointmentTime,status,sesion_user_id));
            }
            randevularTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateRandevuYonet()
    {
        String sql = "SELECT * FROM doctorAppointments WHERE status = 'planned'";
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            ObservableList<Appointment> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                String id = rs.getString("appointment_id");
                String patientName = rs.getString("patient_name");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentTime = rs.getString("appointment_time");
                String status = rs.getString("status");

                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Appointment(id,"1",patientName, appointmentDate, appointmentTime,status,sesion_user_id));
            }
            randevuYonetTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateReceteler()
    {
        String sql = "SELECT pr.medication_name, pr.dosage, pr.prescribed_date, pr.doctor_id, pr.patient_id, u.name " +
                "FROM prescriptions pr " +
                "JOIN patients p ON pr.patient_id = p.patient_id " +
                "JOIN users u ON p.user_id = u.user_id ";

        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            ResultSet rs = pstmt.executeQuery();

            ObservableList<Medications> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                String ilacAdi = rs.getString("medication_name");
                String patient_id = rs.getString("patient_id");
                String doctor_id = rs.getString("doctor_id");
                String prescribed_at = rs.getString("prescribed_date");
                String dosage = rs.getString("dosage");
                String name = rs.getString("name");

                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Medications("1", doctor_id, patient_id, ilacAdi, dosage, prescribed_at,name));
            }
            recetelerimTable.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
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
        assert tahlilHastaAdInput != null : "fx:id=\"tahlilHastaAdInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
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

        hastaAdiInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Sadece rakamlara izin ver
                hastaAdiInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // Doz girişi için sadece sayıya izin ver
        dozField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Sadece rakamlara izin ver
                dozField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        ObservableList<String> ilaclar = FXCollections.observableArrayList(
                "Paracetamol",
                "Ibuprofen",
                "Amoxicillin",
                "Ciprofloxacin",
                "Metformin"
        );
        ilacAdiComboBox.setItems(ilaclar);

        // randevu yönet icin veri bağlama
        idRow.setCellValueFactory(new PropertyValueFactory<>("id"));
        hastaAdiRow.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // randevular icin veri bağlama
        randevularIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
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

        updateRandevular();
        updateRandevuYonet();
        updateReceteler();



    }
}
