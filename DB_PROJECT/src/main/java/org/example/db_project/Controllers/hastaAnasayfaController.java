package org.example.db_project.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.db_project.Appointment;
import org.example.db_project.Doctors;
import org.example.db_project.HelloApplication;
import org.example.db_project.Medications;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;

public class hastaAnasayfaController {
    Connection connectToDatabase() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proje";
        String user = "1";
        String password = "1";
        Connection con = DriverManager.getConnection(url, user, password);
        return con;
    }


    @FXML private TableColumn<?, ?> randevu_sorgu_doktorColumn;
    @FXML private TableColumn<?, ?> randevu_sorgu_poliklinkColumn;
    @FXML private TableColumn<?, ?> randevu_sorgu_saatColumn;
    @FXML private TableColumn<?, ?> randevu_sorgu_tarihColumn;
    @FXML private TableView<Appointment> randevu_sorgu_table;


    @FXML private TableColumn<Appointment, ?> randevu_doktorColumn;
    @FXML private TableColumn<Appointment, ?> randevu_poliklinikColumn;
    @FXML private TableColumn<Appointment, ?> randevu_durumColumn;
    @FXML private TableColumn<Appointment, Void> randevu_islemColumn;
    @FXML private TableColumn<Appointment, ?> randevu_tarihColumn;
    @FXML private TableView<Appointment> randevu_tableView;


    @FXML private TableColumn<?, ?> tahlil_tarihColumn;
    @FXML private TableColumn<?, ?> tahlil_doktorColumn;
    @FXML private TableColumn<?, ?> tahlil_notColumn;
    @FXML private TableColumn<?, ?> tahlil_poliklinkColumn;
    @FXML private TableColumn<?, ?> tahlil_sonucColumn;
    @FXML private TableView<?> tahlil_tableView;


    @FXML private TableColumn<?, ?> recetelerim_tarihColumn;
    @FXML private TableColumn<?, ?> recetelerim_doktorColumn;
    @FXML private TableColumn<?, ?> recetelerim_dozColumn;
    @FXML private TableColumn<?, ?> recetelerim_ilacColumn;
    @FXML private TableView<Medications> recetelerim_tableView;


    @FXML private Button panel_cikis_button;
    @FXML private Label panel_hasta_ad_label;
    @FXML private ImageView panel_hasta_image;
    @FXML private Label panel_hasta_soyad_label;
    @FXML private Label panel_hasta_tc_label;
    @FXML private Button panel_duzenle_button;
    @FXML private VBox panel_yaklasan_randevular_vbox;
    @FXML private VBox panel_yeni_recete_vbox;
    @FXML private Label panel_email;
    @FXML private Label panel_adres;
    @FXML private Label panel_cinsiyet;
    @FXML private Label panel_dogum_tarihi;
    @FXML private Label panel_kan_grubu;
    @FXML private Label panel_tel_no;
    @FXML private SplitPane yan_panel;


    @FXML private ComboBox<Doctors> doktor_combobox;
    @FXML private Label format_hatasi_label;
    @FXML private ComboBox<String> poliklinik_combobox;
    @FXML private Label poliklinik_zorunludur_label;
    @FXML private Button randevu_ara_button;
    @FXML private DatePicker randevu_sorgu_bitis_date;
    @FXML private DatePicker randevu_sorgu_baslangic_date;
    @FXML private Button sifirla_button;

    @FXML private TextField randevular_doktor_adi;
    @FXML private Button randevu_yonet_ara_button;
    @FXML private DatePicker randevu_basla_tarih;
    @FXML private DatePicker randevu_bitis_tarih;


    @FXML private Button ara_button1;
    @FXML private DatePicker tahlil_basla_tarih;
    @FXML private DatePicker tahlil_bitis_tarih;


    @FXML private Button ara_button2;
    @FXML private TextField recetelerim_arama_kismi;
    @FXML private DatePicker recetelerim_basla_tarih;
    @FXML private DatePicker recetelerim_bitis_tarih;


    final String sesion_user_id = HelloApplication.userSession.getSesionUserId();
    final String sesion_patient_id = HelloApplication.userSession.getSesionPatientId();



    @FXML
    void initialize() {


        // ----- Yan Panel -----




        update_yan_panel();
        assert yan_panel != null : "fx:id=\"yan_panel\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_adres != null : "fx:id=\"panel_adres\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_cikis_button != null : "fx:id=\"panel_cikis_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_cinsiyet != null : "fx:id=\"panel_cinsiyet\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_dogum_tarihi != null : "fx:id=\"panel_dogum_tarihi\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_duzenle_button != null : "fx:id=\"panel_duzenle_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_email != null : "fx:id=\"panel_email\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_hasta_ad_label != null : "fx:id=\"panel_hasta_ad_label\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_hasta_image != null : "fx:id=\"panel_hasta_image\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_hasta_soyad_label != null : "fx:id=\"panel_hasta_soyad_label\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_hasta_tc_label != null : "fx:id=\"panel_hasta_tc_label\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_kan_grubu != null : "fx:id=\"panel_kan_grubu\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_tel_no != null : "fx:id=\"panel_tel_no\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_yaklasan_randevular_vbox != null : "fx:id=\"panel_yaklasan_randevular_vbox\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert panel_yeni_recete_vbox != null : "fx:id=\"panel_yeni_recete_vbox\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";


        // ----- Randevu Sorgu -----
        ObservableList<String> poliklinikler = FXCollections.observableArrayList(
                "Kardiyoloji",
                "Ortopedi",
                "Dahiliye",
                "Nöroloji",
                "Üroloji",
                "Göz Hastalıkları",
                "Kulak Burun Boğaz",
                "Dermatoloji"
        );
        poliklinik_combobox.setItems(poliklinikler);

        randevu_sorgu_table.setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setCursor(Cursor.HAND));
            row.setOnMouseExited(event -> row.setCursor(Cursor.DEFAULT));
            return row;
        });
        randevu_sorgu_table.setRowFactory(tv -> {
            TableRow<Appointment> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    Appointment clickedAppointment = row.getItem();
                    System.out.println("Tıklanan Satır: " + clickedAppointment);
                }
            });
            return row;
        });

        assert sifirla_button != null : "fx:id=\"sifirla_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert format_hatasi_label != null : "fx:id=\"format_hatasi_label\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_ara_button != null : "fx:id=\"randevu_ara_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert doktor_combobox != null : "fx:id=\"doktor_combobox\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert poliklinik_zorunludur_label != null : "fx:id=\"poliklinik_zorunludur_label\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_baslangic_date != null : "fx:id=\"randevu_sorgu_baslangic_date\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_bitis_date != null : "fx:id=\"randevu_sorgu_bitis_date\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_doktorColumn != null : "fx:id=\"randevu_sorgu_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_poliklinkColumn != null : "fx:id=\"randevu_sorgu_poliklinkColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_saatColumn != null : "fx:id=\"randevu_sorgu_saatColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_table != null : "fx:id=\"randevu_sorgu_table\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_tarihColumn != null : "fx:id=\"randevu_sorgu_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";


        // ----- Randevu Yönet -----

        randevu_islemColumn.setCellFactory(new Callback<>() {
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
                            }

                            updateRandevulariYonet();
                            setYaklasanRandevular();
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
        updateRandevulariYonet();

        assert randevu_yonet_ara_button != null : "fx:id=\"ara_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_basla_tarih != null : "fx:id=\"randevu_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_bitis_tarih != null : "fx:id=\"randevu_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_doktorColumn != null : "fx:id=\"randevu_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_islemColumn != null : "fx:id=\"randevu_iptalButton\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_poliklinikColumn != null : "fx:id=\"randevu_poliklinikColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_durumColumn != null : "fx:id=\"randevu_saatColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_tableView != null : "fx:id=\"randevu_tableView\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_tarihColumn != null : "fx:id=\"randevu_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";


        // ----- Tahlil -----
        assert ara_button1 != null : "fx:id=\"ara_button2\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_basla_tarih != null : "fx:id=\"tahlil_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_bitis_tarih != null : "fx:id=\"tahlil_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_doktorColumn != null : "fx:id=\"tahlil_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_notColumn != null : "fx:id=\"tahlil_notColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_poliklinkColumn != null : "fx:id=\"tahlil_poliklinkColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_sonucColumn != null : "fx:id=\"tahlil_sonucColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_tableView != null : "fx:id=\"tahlil_tableView\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_tarihColumn != null : "fx:id=\"tahlil_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";


        // ----- Reçeteler -----

        updateReceteler();
        assert ara_button2 != null : "fx:id=\"ara_button2\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_arama_kismi != null : "fx:id=\"recetelerim_arama_kismi\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_basla_tarih != null : "fx:id=\"recetelerim_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_bitis_tarih != null : "fx:id=\"recetelerim_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_doktorColumn != null : "fx:id=\"recetelerim_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_dozColumn != null : "fx:id=\"recetelerim_dozColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_ilacColumn != null : "fx:id=\"recetelerim_ilacColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_tableView != null : "fx:id=\"recetelerim_tableView\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_tarihColumn != null : "fx:id=\"recetelerim_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";

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
    void poliklinik_combobox_on_action(ActionEvent event) {

        String selectedPoliklinik = poliklinik_combobox.getValue();
        System.out.println("Seçilen Poliklinik: " + selectedPoliklinik);

        String sql = "SELECT d.doctor_id, d.user_id, u.name " +
                "FROM doctors d " +
                "JOIN users u ON d.user_id = u.user_id " +
                "WHERE d.specialization ILIKE ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, selectedPoliklinik);

            ResultSet rs = pstmt.executeQuery();

            // Sadece doctor_id ve user_id bilgilerini tutacak liste
            ObservableList<Doctors> liste = FXCollections.observableArrayList();

            while (rs.next()) {
                String doctorId = rs.getString("doctor_id");
                String userId = rs.getString("user_id");
                String userName = rs.getString("name");

                // Doctor nesnesi oluşturuyoruz
                Doctors doktor = new Doctors(doctorId, userId, userName);

                // Listeye ekliyoruz
                liste.add(doktor);
            }

            // Listeyi doktor_combobox'a ayarlıyoruz
            doktor_combobox.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevu_ara_button_onaction(ActionEvent event) {
        Doctors selectedDoctor = doktor_combobox.getValue();
        ObservableList<Appointment> randevuListesi = FXCollections.observableArrayList();

        if (selectedDoctor != null) {
            randevu_sorgu_doktor_list(selectedDoctor, randevuListesi);
        } else {
            ObservableList<Doctors> doctorList = doktor_combobox.getItems();

            if (doctorList != null && !doctorList.isEmpty()) {
                for (Doctors doctor : doctorList) {
                    randevu_sorgu_doktor_list(doctor, randevuListesi);
                }
            } else {
                System.out.println("Doktor listesi boş. Lütfen bir doktor ekleyin.");
            }
        }

        // Tablonun kolonlarını modele bağla
        randevu_sorgu_doktorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        randevu_sorgu_tarihColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        randevu_sorgu_saatColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        randevu_sorgu_poliklinkColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

        // Listeyi tabloya ekle
        randevu_sorgu_table.setItems(randevuListesi);

        randevu_sorgu_table.setOpacity(1);
    }

    void randevu_sorgu_doktor_list(Doctors selectedDoctor, ObservableList<Appointment> randevuListesi) {
        String selectedPoliklinik = poliklinik_combobox.getValue();
        String selectedUserId = selectedDoctor.getUserId();
        String selectedDoctorId = selectedDoctor.getDoctorId();
        System.out.println("Seçilen Doktorun User ID: " + selectedUserId);
        System.out.println("Seçilen Doktorun ID: " + selectedDoctorId);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String startDateStr = startDate.format(formatter);
        String endDateStr = endDate.format(formatter);

        if (randevu_sorgu_baslangic_date.getValue() != null) {
            startDate = randevu_sorgu_baslangic_date.getValue();
            startDateStr = startDate.toString();
        }

        if (randevu_sorgu_bitis_date.getValue() != null) {
            endDate = randevu_sorgu_bitis_date.getValue();
            endDateStr = endDate.toString();
        } else if (randevu_sorgu_baslangic_date.getValue() != null) {
            endDate = startDate.plusWeeks(1);
            endDateStr = endDate.format(formatter);
        }

        String sql = "SELECT * FROM  get_doctor_available_times(?, ?, ?)";
        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setArray(1, conn.createArrayOf("INTEGER", new Integer[]{Integer.parseInt(selectedDoctorId)}));
            pstmt.setDate(2, Date.valueOf(startDateStr));
            pstmt.setDate(3, Date.valueOf(endDateStr));

            ResultSet rs = pstmt.executeQuery();

            int id_sayac = 0;

            while (rs.next()) {
                id_sayac++;
                String idSayacStr = String.valueOf(id_sayac);

                String doctorName = rs.getString("doctor_name");
                String availableTime = rs.getTimestamp("available_time").toLocalDateTime().toLocalTime().toString();
                String availableDate = rs.getTimestamp("available_time").toLocalDateTime().toLocalDate().toString();
                randevuListesi.add(new Appointment(idSayacStr, doctorName, doctorName, availableDate, availableTime, selectedPoliklinik,null, selectedDoctorId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevu_sorgu_table_clicked(MouseEvent event) {
        // Seçili randevuyu al
        Appointment selectedAppointment = randevu_sorgu_table.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            // Popup oluştur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Randevu Ayrıntıları");
            alert.setHeaderText("Randevunun Detayları:");
            alert.setContentText(
                    "Doktor: " + selectedAppointment.getDoctorName() + "\n" +
                            "Tarih: " + selectedAppointment.getAppointmentDate() + "\n" +
                            "Saat: " + selectedAppointment.getAppointmentTime() + "\n" +
                            "Poliklinik: " + selectedAppointment.getSpecialization()  + "\n\n" +
                            "Bu randevuyu almak istediğinize emin misiniz ?"
            );

            // Buton seçeneklerini ekle
            ButtonType yesButton = new ButtonType("Evet", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButton = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesButton, cancelButton);

            // Kullanıcının seçimini al
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == yesButton) {
                System.out.println("Evet seçildi. İşlem onaylandı.");


                if (sesion_patient_id != null) {
                    // Appointment eklemek için SQL
                    String sqlInsertAppointment = "INSERT INTO public.appointments (" +
                            "doctor_id, patient_id, appointment_date, appointment_time, status, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";

                    try (Connection con = connectToDatabase();
                         PreparedStatement pstmt = con.prepareStatement(sqlInsertAppointment)) {


                        String appointmentDateStr = selectedAppointment.getAppointmentDate();
                        String appointmentTimeStr = selectedAppointment.getAppointmentTime() + ":00";

                        Date appointmentDate = Date.valueOf(appointmentDateStr);
                        Time appointmentTime = Time.valueOf(appointmentTimeStr);

                        pstmt.setInt(1, Integer.parseInt(selectedAppointment.getDoctor_id())); // doctor_id
                        pstmt.setInt(2, Integer.parseInt(sesion_patient_id)); // patient_id
                        pstmt.setDate(3, appointmentDate);  // appointment_date
                        pstmt.setTime(4, appointmentTime);  // appointment_time
                        pstmt.setString(5, "planned"); // status (default planned)
                        pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now())); // created_at

                        pstmt.executeUpdate();

                        Alert info_alert = new Alert(Alert.AlertType.INFORMATION);
                        info_alert.setTitle("Başarı");
                        info_alert.setHeaderText(null);
                        info_alert.setContentText("Randevu başarıyla oluşturuldu.");
                        yesButton = new ButtonType("Devam Et", ButtonBar.ButtonData.OK_DONE);
                        info_alert.getButtonTypes().setAll(yesButton);
                        info_alert.showAndWait();


                        sifirla();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Hasta bilgisi bulunamadı.");
                }
            } else {
                System.out.println("İptal edildi.");
            }
        } else {
            System.out.println("Seçili bir randevu yok.");
        }
    }

    @FXML
    void sifirla_button_action(ActionEvent event) {
        sifirla();
    }

    void sifirla() {

        randevu_sorgu_baslangic_date.setValue(null);
        randevu_sorgu_baslangic_date.setPromptText("-FARK ETMEZ-");

        randevu_sorgu_bitis_date.setValue(null);
        randevu_sorgu_bitis_date.setPromptText("-FARK ETMEZ-");

        doktor_combobox.setValue(null);

        poliklinik_combobox.setValue("Kardiyoloji");

        ObservableList<?> currentData = randevu_sorgu_table.getItems();
        currentData.clear();
        randevu_sorgu_table.setOpacity(0);
    }

    @FXML
    void randevu_yonet_ara_button_onaction(ActionEvent event) {
        updateRandevulariYonet();
    }

    void updateRandevulariYonet() {
        String doktorAdi = randevular_doktor_adi.getText();
        LocalDate baslangic_date = randevu_basla_tarih.getValue();
        LocalDate bitis_date = randevu_bitis_tarih.getValue();

        String sql = "SELECT * FROM patientAppointments WHERE patient_id = ? AND status = 'planned'";

        if (doktorAdi != null && !doktorAdi.isEmpty()) {
            sql += "AND doctor_name LIKE ? ";
        }
        if (baslangic_date != null && bitis_date != null) {
            sql += "AND appointment_date BETWEEN ? AND ? ";
        } else if (baslangic_date != null) {
            sql += "AND appointment_date >= ? ";
        } else if (bitis_date != null) {
            sql += "AND appointment_date <= ? ";
        }
        sql += "ORDER BY appointment_date DESC";


        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(sesion_patient_id)); // Patient ID

            int index = 2; // Start from the 3rd parameter
            if (doktorAdi != null && !doktorAdi.isEmpty()) {
                pstmt.setString(index++, "%" + doktorAdi + "%"); // Doctor name (LIKE)
            }
            if (baslangic_date != null && bitis_date != null) {
                pstmt.setDate(index++, Date.valueOf(baslangic_date)); // Start date
                pstmt.setDate(index++, Date.valueOf(bitis_date)); // End date
            } else if (baslangic_date != null) {
                pstmt.setDate(index++, Date.valueOf(baslangic_date)); // Start date
            } else if (bitis_date != null) {
                pstmt.setDate(index++, Date.valueOf(bitis_date)); // End date
            }

            ResultSet rs = pstmt.executeQuery();
            ObservableList<Appointment> liste = FXCollections.observableArrayList();

            while (rs.next()) {
                String doctor_name = rs.getString("doctor_name");
                String appointmentDate = rs.getString("appointment_date");
                String appointmentTime = rs.getString("appointment_time");
                String appointmentId = rs.getString("appointment_id");
                String specialization = rs.getString("specialization");
                String status = rs.getString("status");

                String format_date =  appointmentDate + "    " + appointmentTime.substring(0, 5);

                liste.add(new Appointment(appointmentId, doctor_name, status, appointmentDate, format_date, specialization,null, sesion_user_id));
            }

            randevu_tarihColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
            randevu_poliklinikColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));
            randevu_doktorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            randevu_durumColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

            randevu_tableView.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void updateReceteler()
    {

        String sql = "SELECT * " +
                "FROM prescriptions pr " +
                "JOIN patients p ON p.patient_id = pr.patient_id " +
                "JOIN doctors d ON pr.doctor_id = d.doctor_id " +
                "JOIN users u ON u.user_id = d.user_id " +
                "WHERE p.patient_id = ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, Integer.parseInt(sesion_patient_id));

            ResultSet rs = pstmt.executeQuery();

            ObservableList<Medications> liste = FXCollections.observableArrayList();
            while (rs.next()) {


                String ilacAdi = rs.getString("medication_name");
                String prescribed_at = rs.getString("prescribed_date");
                String dosage = rs.getString("dosage");
                String name = rs.getString("name");

                liste.add(new Medications("1", null, null, ilacAdi, dosage, prescribed_at,null, name));
            }

            recetelerim_tarihColumn.setCellValueFactory(new PropertyValueFactory<>("prescribed_at"));
            recetelerim_doktorColumn.setCellValueFactory(new PropertyValueFactory<>("doctor_name"));
            recetelerim_ilacColumn.setCellValueFactory(new PropertyValueFactory<>("medication_name"));
            recetelerim_dozColumn.setCellValueFactory(new PropertyValueFactory<>("dosage_instructions"));


            recetelerim_tableView.setItems(liste);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    void setYaklasanRandevular() {
        String sql = "SELECT * FROM patientAppointments " +
                "WHERE patient_id = ? " +
                "AND status = 'planned' " +
                "AND appointment_date BETWEEN CURRENT_DATE AND CURRENT_DATE + 2";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(sesion_patient_id));
            ResultSet rs = pstmt.executeQuery();

            panel_yaklasan_randevular_vbox.getChildren().clear(); // İlk önce VBox'ı boşaltıyoruz

            if (!rs.next()) {
                Label noAppointmentsLabel = new Label("Yaklaşan randevunuz bulunmamaktadır.");
                noAppointmentsLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10;");
                panel_yaklasan_randevular_vbox.getChildren().add(noAppointmentsLabel);
            } else {
                do {
                    String doctor_name = rs.getString("doctor_name");
                    String appointmentDate = rs.getString("appointment_date");
                    String appointmentTime = rs.getString("appointment_time");
                    String specialization = rs.getString("specialization");

                    String time = appointmentTime.substring(0, 5);
                    String dateText = appointmentDate + " " + time;
                    String doctorText = doctor_name;
                    String specializationText = specialization;

                    HBox hbox = new HBox();
                    hbox.setSpacing(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);

                    hbox.setStyle("-fx-border-color: gray; -fx-border-width: 1px; -fx-background-color: #f5f5f5; -fx-border-radius: 5px;");

                    Label dateLabel = new Label(dateText);
                    dateLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

                    Label doctorLabel = new Label(doctorText);
                    doctorLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

                    Label specializationLabel = new Label(specializationText);
                    specializationLabel.setStyle("-fx-font-size: 14px; -fx-padding: 10;");

                    hbox.getChildren().addAll(dateLabel, doctorLabel, specializationLabel);
                    panel_yaklasan_randevular_vbox.getChildren().add(hbox);
                } while (rs.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    void panel_duzenle_button_onaction(ActionEvent event) {
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window
        popupStage.setTitle("Profil Düzenle");

        // Layout for the popup
        GridPane popupLayout = new GridPane();
        popupLayout.setPadding(new Insets(10));
        popupLayout.setHgap(10);
        popupLayout.setVgap(10);

        // Fields for user input
        Label addressLabel = new Label("Adres:");
        TextField addressField = new TextField();
        addressField.setPromptText("Adres");

        Label telefonLabel = new Label("Telefon No:");
        TextField telefonField = new TextField();
        telefonField.setPromptText("Telefon No (10 Haneli)");

        Label emailLabel = new Label("E-posta:");
        TextField emailField = new TextField();
        emailField.setPromptText("E-posta");

        // Save button
        Button kaydetButton = new Button("Kaydet");
        kaydetButton.setOnAction(e -> {
            // Retrieve input values
            String address = addressField.getText();
            String telefon = telefonField.getText();
            String email = emailField.getText();

            boolean isUpdated = false;

            // Address update (if provided)
            if (!address.isEmpty()) {
                System.out.println("Adres güncelleniyor: " + address);
                isUpdated = updateAddress(address);

            }

            // Telefon numarası update (if provided and valid)
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

            // Email update (if provided)
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

        // Add components to the layout
        popupLayout.add(addressLabel, 0, 0);
        popupLayout.add(addressField, 1, 0);
        popupLayout.add(telefonLabel, 0, 1);
        popupLayout.add(telefonField, 1, 1);
        popupLayout.add(emailLabel, 0, 2);
        popupLayout.add(emailField, 1, 2);
        popupLayout.add(kaydetButton, 1, 3);

        // Set the scene and show the popup
        Scene popupScene = new Scene(popupLayout, 400, 250);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private boolean updateAddress(String address) {

        String sql = "UPDATE patients SET address = ? WHERE user_id = ?";

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

        String sql = "UPDATE patients SET phone = ? WHERE user_id = ?";

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
            String sql = "SELECT * FROM patientprofile WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                int userId = Integer.parseInt(sesion_user_id);
                pstmt.setInt(1, userId);

                ResultSet rs = pstmt.executeQuery();

                // Eğer sonuç varsa bilgileri atama yap
                if (rs.next()) {
                    String fullName = rs.getString("name");
                    String[] nameParts = fullName.split(" ");

                    String firstName = nameParts[0];
                    panel_hasta_ad_label.setText(firstName);

                    String lastName = nameParts[nameParts.length - 1];
                    panel_hasta_soyad_label.setText(lastName);

                    panel_email.setText(rs.getString("email"));
                    panel_tel_no.setText(rs.getString("phone"));
                    panel_cinsiyet.setText(rs.getString("gender"));
                    panel_kan_grubu.setText(rs.getString("bloodtype"));
                    panel_adres.setText(rs.getString("address"));
                    panel_hasta_tc_label.setText(rs.getString("tc_number"));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    panel_dogum_tarihi.setText(sdf.format(rs.getDate("date_of_birth")));

                } else {
                    System.out.println("Hasta bilgisi bulunamadı.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setYaklasanRandevular();
    }

}
