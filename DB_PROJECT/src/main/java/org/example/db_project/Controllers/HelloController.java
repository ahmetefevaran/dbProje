package org.example.db_project.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.db_project.Database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class HelloController {




    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button giris_yap_button;

    @FXML
    private TextField password_field;

    @FXML
    private Button sifremi_unuttum_button;

    @FXML
    private Label statusLabel;

    @FXML
    private Label statusLabel1;

    @FXML
    private Label statusLabel2;

    @FXML
    private ImageView support_icon;

    @FXML
    private TextField tc_field;

    @FXML
    void giris_yap_button_clicked(ActionEvent event) {


        // KULLANICI EKLEME OLARAK AYARLI

        statusLabel.setText("Selam");


        String name = tc_field.getText();
        String email = password_field.getText();

        if (name.isEmpty() || email.isEmpty()) {
            statusLabel.setText("Ad ve e-posta gereklidir!");
            return;
        }

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                statusLabel.setText("Kullanıcı başarıyla eklendi!");
                tc_field.clear();
                password_field.clear();
            } else {
                statusLabel.setText("Bir hata oluştu.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void sifremi_unuttum_button_clıckked(ActionEvent event) {

    }

    @FXML
    void support_icon_clicked(MouseEvent event) {

    }


    // PostgreSQL bağlantısı ve veri ekleme
    @FXML
    void initialize() {
        assert giris_yap_button != null : "fx:id=\"giris_yap_button\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert password_field != null : "fx:id=\"password_field\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert sifremi_unuttum_button != null : "fx:id=\"sifremi_unuttum_button\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert statusLabel1 != null : "fx:id=\"statusLabel1\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert statusLabel2 != null : "fx:id=\"statusLabel2\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert support_icon != null : "fx:id=\"support_icon\" was not injected: check your FXML file 'hello-view.fxml'.";
        assert tc_field != null : "fx:id=\"tc_field\" was not injected: check your FXML file 'hello-view.fxml'.";

    }

}