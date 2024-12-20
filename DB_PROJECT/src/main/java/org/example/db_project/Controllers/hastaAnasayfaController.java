package org.example.db_project.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.example.db_project.Appointment;
import org.example.db_project.Doctors;

import java.sql.*;
import java.time.LocalDate;

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
    @FXML private TableView<?> randevu_sorgu_table;


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
    @FXML private ComboBox<String> polikinlik_combobox;
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


    @FXML
    void initialize() {

        // ----- Yan Panel -----
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
        polikinlik_combobox.setItems(poliklinikler);

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
    void baslangic_date_onaction(ActionEvent event) {

    }

    @FXML
    void bitis_date_onaction(ActionEvent event) {

    }

    @FXML
    void doktor_combobox_on_action(ActionEvent event) {

    }

    @FXML
    void polikinlik_combobox_on_action(ActionEvent event) {

        String selectedPoliklinik = polikinlik_combobox.getValue();
        System.out.println("Seçilen Poliklinik: " + selectedPoliklinik);


        String sql = "SELECT * FROM doctors WHERE specialization ILIKE ? ";
        try (Connection conn =connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             pstmt.setString(1, selectedPoliklinik);




            ResultSet rs = pstmt.executeQuery();

            ObservableList<Doctors> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                String id = rs.getString("doctor_id");
                String user_id = rs.getString("user_id");


                liste.add(id);
            }


            doktor_combobox.setItems(liste);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void randevu_ara_button_onaction(ActionEvent event) {

    }

    @FXML
    void randevu_sorgu_table_clicked(MouseEvent event) {

    }

    @FXML
    void sifirla_button_action(ActionEvent event) {

    }

}
