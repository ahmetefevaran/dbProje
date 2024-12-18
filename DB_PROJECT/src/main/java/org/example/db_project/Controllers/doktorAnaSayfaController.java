package org.example.db_project.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.util.Callback;
import org.example.db_project.Appointment;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class doktorAnaSayfaController {

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
    private TextField hastaAdiField;

    @FXML
    private TableColumn<?, ?> hastaAdiColumn;

    @FXML
    private ComboBox<?> ilacAdiComboBox;

    @FXML
    private TableColumn<?, ?> ilacColumn;

    @FXML
    private Button kaydetBtn;

    @FXML
    private TableColumn<?, ?> patientColumn;

    @FXML
    private TableColumn<?, ?> policlinicCloumn;

    @FXML
    private TableColumn<?, ?> policlinicColumn;

    @FXML
    private Button randevuYonetAraButton;

    @FXML
    private TextField randevuYonetHastaAdInput;

    @FXML
    private DatePicker randevuYonetbaslangicTarihInput11;

    @FXML
    private Button randevularAraButton1;

    @FXML
    private DatePicker randevularBaslangicTarihInput1;

    @FXML
    private TextField randevularHastaAdInput1;

    @FXML
    private TableView<?> randevularTable;

    @FXML
    private Button recetelerimAraButton;

    @FXML
    private TextField recetelerimHastaAdInput;

    @FXML
    private TableView<?> recetelerimTable;

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
    private TextField randevuYonetDurumInput;


    Connection connectToDatabase() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proje";
        String user = "1";
        String password = "1";
        Connection conn = DriverManager.getConnection(url, user, password);
        return conn;
    }
    @FXML
    void kaydetButtonAction(ActionEvent event) {

        String sql = "INSERT INTO medications (patient_id,doctor_id,medication_name,dosage_instructions,prescribed_at) VALUES(?,?,?,?,?)";

        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,2);
            pstmt.setInt(2,1);
            pstmt.setString(3,"asd");
            pstmt.setInt(4,5);
            Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
            pstmt.setTimestamp(5, currentTimestamp);
            int insertedRows = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void randevuYonetAraButtonAction(ActionEvent event) {
        int doctorId = 1;
        String hastaAdi = randevuYonetHastaAdInput.getText();

        String sql = "SELECT * FROM doctorAppointments WHERE doctor_id = ? AND patient_name LIKE ? ";
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,doctorId);
            pstmt.setString(2,"%"+hastaAdi+"%");
            ResultSet rs = pstmt.executeQuery();
            ObservableList<Appointment> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                String id = rs.getString("appointment_id");
                String patientName = rs.getString("patient_name");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentTime = rs.getString("appointment_time");
                String status = rs.getString("status");

                // Appointment nesnesi oluştur ve listeye ekle
                liste.add(new Appointment(id,"1",patientName, appointmentDate, appointmentTime));
            }
            randevuYonetTable.setItems(liste);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevularAraButtonAction(ActionEvent event) {

    }

    @FXML
    void recetelerimAraButtonAction(ActionEvent event) {

    }

    @FXML
    void tahlilAraButtonAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
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
        assert policlinicColumn != null : "fx:id=\"policlinicColumn\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetAraButton != null : "fx:id=\"randevuYonetAraButton\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetHastaAdInput != null : "fx:id=\"randevuYonetHastaAdInput\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetTable != null : "fx:id=\"randevuYonetTable\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevuYonetbaslangicTarihInput11 != null : "fx:id=\"randevuYonetbaslangicTarihInput11\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularAraButton1 != null : "fx:id=\"randevularAraButton1\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularBaslangicTarihInput1 != null : "fx:id=\"randevularBaslangicTarihInput1\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
        assert randevularHastaAdInput1 != null : "fx:id=\"randevularHastaAdInput1\" was not injected: check your FXML file 'doktor-anasayfa.fxml'.";
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

        // Sütunların veri bağlaması
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
                            System.out.println("Butona basıldı. ID: " + data.getId());
                            // Özel işlem yapılabilir, örneğin durum değiştirilebilir
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

        // Örnek veri ekleme
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(
                new Appointment("1", "Dr. Ahmet Yılmaz", "Ali Can", "15.12.2024", "10:30"),
                new Appointment("2", "Dr. Fatma Kaya", "Ayşe Yıldız", "16.12.2024", "11:00"),
                new Appointment("3", "Dr. Mehmet Demir", "Hasan Çelik", "17.12.2024", "14:00")
        );

        // Tabloya veri ekleme
        randevuYonetTable.setItems(appointments);
    }
}
