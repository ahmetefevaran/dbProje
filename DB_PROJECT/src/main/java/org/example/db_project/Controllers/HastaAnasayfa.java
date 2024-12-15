package org.example.db_project.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class HastaAnasayfa {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<?> doktor_combobox;

    @FXML
    private Label format_hatasi_label;

    @FXML
    private ImageView hastane_logo;

    @FXML
    private ComboBox<?> polikinlik_combobox;

    @FXML
    private Label poliklinik_zorunludur_label;

    @FXML
    private Button randevu_ara_button;

    @FXML
    private DatePicker randevu_basla_tarih;

    @FXML
    private DatePicker randevu_bitis_tarih;

    @FXML
    private TextArea randevu_detaylar_area;

    @FXML
    private TableColumn<?, ?> randevu_doktorColumn;

    @FXML
    private Button randevu_iptalButton;

    @FXML
    private TableColumn<?, ?> randevu_poliklinikColumn;

    @FXML
    private TableColumn<?, ?> randevu_saatColumn;

    @FXML
    private DatePicker randevu_sorgu_baslangıc_date;

    @FXML
    private DatePicker randevu_sorgu_bitis_date;

    @FXML
    private TableColumn<?, ?> randevu_sorgu_doktorColumn;

    @FXML
    private TableColumn<?, ?> randevu_sorgu_poliklinkColumn;

    @FXML
    private TableColumn<?, ?> randevu_sorgu_saatColumn;

    @FXML
    private TableView<?> randevu_sorgu_table;

    @FXML
    private TableColumn<?, ?> randevu_sorgu_tarihColumn;

    @FXML
    private TableColumn<?, ?> randevu_tarihColumn;

    @FXML
    private TextArea recete_detay_area;

    @FXML
    private TextField recetelerim_arama_kismi;

    @FXML
    private DatePicker recetelerim_basla_tarih;

    @FXML
    private DatePicker recetelerim_bitis_tarih;

    @FXML
    private TableColumn<?, ?> recetelerim_doktorColumn;

    @FXML
    private TableColumn<?, ?> recetelerim_dozColumn;

    @FXML
    private TableColumn<?, ?> recetelerim_ilacColumn;

    @FXML
    private TableColumn<?, ?> recetelerim_tarihColumn;

    @FXML
    private Button sifirla_button;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableView<?> tahlilTableView;

    @FXML
    private DatePicker tahlil_basla_tarih;

    @FXML
    private DatePicker tahlil_bitis_tarih;

    @FXML
    private TextArea tahlil_detay_area;

    @FXML
    private TableColumn<?, ?> tahlil_doktorColumn;

    @FXML
    private TableColumn<?, ?> tahlil_notColumn;

    @FXML
    private TableColumn<?, ?> tahlil_poliklinkColumn;

    @FXML
    private TableColumn<?, ?> tahlil_sonucColumn;

    @FXML
    private TableColumn<?, ?> tahlil_tarihColumn;

    @FXML
    void baslangıc_date_onaction(ActionEvent event) {

    }

    @FXML
    void bitis_date_onaction(ActionEvent event) {

    }

    @FXML
    void doktor_combobox_on_action(ActionEvent event) {

    }

    @FXML
    void hastane_logo_clicked(MouseEvent event) {

    }

    @FXML
    void polikinlik_combobox_on_action(ActionEvent event) {

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

    @FXML
    void initialize() {
        assert doktor_combobox != null : "fx:id=\"doktor_combobox\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert format_hatasi_label != null : "fx:id=\"format_hatasi_label\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert hastane_logo != null : "fx:id=\"hastane_logo\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert polikinlik_combobox != null : "fx:id=\"polikinlik_combobox\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert poliklinik_zorunludur_label != null : "fx:id=\"poliklinik_zorunludur_label\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_ara_button != null : "fx:id=\"randevu_ara_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_basla_tarih != null : "fx:id=\"randevu_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_bitis_tarih != null : "fx:id=\"randevu_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_detaylar_area != null : "fx:id=\"randevu_detaylar_area\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_doktorColumn != null : "fx:id=\"randevu_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_iptalButton != null : "fx:id=\"randevu_iptalButton\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_poliklinikColumn != null : "fx:id=\"randevu_poliklinikColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_saatColumn != null : "fx:id=\"randevu_saatColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_baslangıc_date != null : "fx:id=\"randevu_sorgu_baslangıc_date\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_bitis_date != null : "fx:id=\"randevu_sorgu_bitis_date\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_doktorColumn != null : "fx:id=\"randevu_sorgu_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_poliklinkColumn != null : "fx:id=\"randevu_sorgu_poliklinkColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_saatColumn != null : "fx:id=\"randevu_sorgu_saatColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_table != null : "fx:id=\"randevu_sorgu_table\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_sorgu_tarihColumn != null : "fx:id=\"randevu_sorgu_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert randevu_tarihColumn != null : "fx:id=\"randevu_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recete_detay_area != null : "fx:id=\"recete_detay_area\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_arama_kismi != null : "fx:id=\"recetelerim_arama_kismi\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_basla_tarih != null : "fx:id=\"recetelerim_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_bitis_tarih != null : "fx:id=\"recetelerim_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_doktorColumn != null : "fx:id=\"recetelerim_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_dozColumn != null : "fx:id=\"recetelerim_dozColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_ilacColumn != null : "fx:id=\"recetelerim_ilacColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert recetelerim_tarihColumn != null : "fx:id=\"recetelerim_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert sifirla_button != null : "fx:id=\"sifirla_button\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tableView != null : "fx:id=\"tableView\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlilTableView != null : "fx:id=\"tahlilTableView\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_basla_tarih != null : "fx:id=\"tahlil_basla_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_bitis_tarih != null : "fx:id=\"tahlil_bitis_tarih\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_detay_area != null : "fx:id=\"tahlil_detay_area\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_doktorColumn != null : "fx:id=\"tahlil_doktorColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_notColumn != null : "fx:id=\"tahlil_notColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_poliklinkColumn != null : "fx:id=\"tahlil_poliklinkColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_sonucColumn != null : "fx:id=\"tahlil_sonucColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";
        assert tahlil_tarihColumn != null : "fx:id=\"tahlil_tarihColumn\" was not injected: check your FXML file 'hasta-anasayfa.fxml'.";

    }

}
