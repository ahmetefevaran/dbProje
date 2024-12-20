package org.example.db_project.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.db_project.Appointment;
import org.example.db_project.Doctors;
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
    @FXML private TableColumn<Appointment, ?> randevu_islemColumn;
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
    @FXML private TableView<?> recetelerim_tableView;


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


    @FXML private Button ara_button;
    @FXML private DatePicker randevu_basla_tarih;
    @FXML private DatePicker randevu_bitis_tarih;
    @FXML private TextArea randevu_detaylar_area;


    @FXML private Button ara_button1;
    @FXML private DatePicker tahlil_basla_tarih;
    @FXML private DatePicker tahlil_bitis_tarih;
    @FXML private TextArea tahlil_detay_area;


    @FXML private Button ara_button2;
    @FXML private TextArea recete_detay_area;
    @FXML private TextField recetelerim_arama_kismi;
    @FXML private DatePicker recetelerim_basla_tarih;
    @FXML private DatePicker recetelerim_bitis_tarih;

    final String sesion_user_id = "221";

    @FXML
    void initialize() {


        // ----- Yan Panel -----


        try (Connection conn = connectToDatabase()) {
            // SQL sorgusu: `patientprofile` görünümünden kullanıcı bilgilerini al
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

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    panel_dogum_tarihi.setText(sdf.format(rs.getDate("date_of_birth")));

                } else {
                    System.out.println("Hasta bilgisi bulunamadı.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        assert ara_button != null : "fx:id=\"ara_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_basla_tarih != null : "fx:id=\"randevu_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_bitis_tarih != null : "fx:id=\"randevu_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_detaylar_area != null : "fx:id=\"randevu_detaylar_area\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_doktorColumn != null : "fx:id=\"randevu_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_islemColumn != null : "fx:id=\"randevu_iptalButton\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_poliklinikColumn != null : "fx:id=\"randevu_poliklinikColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_durumColumn != null : "fx:id=\"randevu_saatColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_tableView != null : "fx:id=\"randevu_tableView\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_tarihColumn != null : "fx:id=\"randevu_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recete_detay_area != null : "fx:id=\"recete_detay_area\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";


        // ----- Tahlil -----
        assert ara_button1 != null : "fx:id=\"ara_button2\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_basla_tarih != null : "fx:id=\"tahlil_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_bitis_tarih != null : "fx:id=\"tahlil_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_detay_area != null : "fx:id=\"tahlil_detay_area\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_doktorColumn != null : "fx:id=\"tahlil_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_notColumn != null : "fx:id=\"tahlil_notColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_poliklinkColumn != null : "fx:id=\"tahlil_poliklinkColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_sonucColumn != null : "fx:id=\"tahlil_sonucColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_tableView != null : "fx:id=\"tahlil_tableView\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_tarihColumn != null : "fx:id=\"tahlil_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";


        // ----- Reçeteler -----
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/db_project/hello-view.fxml"));
            Pane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = (Stage) panel_cikis_button.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void baslangic_date_onaction(ActionEvent event) {

    }

    @FXML
    void bitis_date_onaction(ActionEvent event) {

    }

    @FXML
    void doktor_combobox_on_action(ActionEvent event) {

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
                randevuListesi.add(new Appointment(idSayacStr, doctorName, doctorName, availableDate, availableTime, selectedPoliklinik));
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

            } else {
                System.out.println("İptal edildi.");
            }
        } else {
            System.out.println("Seçili bir randevu yok.");
        }
    }



    @FXML
    void sifirla_button_action(ActionEvent event) {

    }
}
