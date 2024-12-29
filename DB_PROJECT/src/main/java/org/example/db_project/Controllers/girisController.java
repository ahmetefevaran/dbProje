package org.example.db_project.Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.db_project.HelloApplication;
import org.example.db_project.UserSession;

public class girisController {

    Connection connectToDatabase() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/proje";
        String user = "1";
        String password = "1";
        Connection con = DriverManager.getConnection(url, user, password);
        return con;
    }



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

        String target_tc = tc_field.getText();
        String target_password = password_field.getText();

        if (target_password.isBlank() || target_tc.isBlank()) {
            statusLabel.setText("TC ve Şifre boş bırakılamaz!");
        } else {
            String sql = "SELECT user_id, role FROM users " +
                    "WHERE tc_number = ? " +
                    "AND password = ?";

            try (Connection conn = connectToDatabase();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, target_tc);
                pstmt.setString(2, target_password);

                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    statusLabel.setText("TC veya Şifre hatalı!");
                } else {
                    String userId = rs.getString("user_id");
                    String userRole = rs.getString("role");

                    if (userRole.equals("doctor")) {

                        sql = "SELECT doctor_id FROM doctors WHERE user_id = ? ";

                        try (PreparedStatement pstmt_doc = conn.prepareStatement(sql)) {

                            pstmt_doc.setInt(1, Integer.parseInt(userId));

                            ResultSet rs_doc = pstmt_doc.executeQuery();

                            if (rs_doc.next()) {
                                String doctor_id = rs_doc.getString("doctor_id");

                                HelloApplication.userSession.setSesionUserId(userId);
                                HelloApplication.userSession.setSesionDoctorId(doctor_id);

                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/db_project/doktor-anasayfa.fxml"));
                                    Pane pane = loader.load();
                                    Scene scene = new Scene(pane);
                                    Stage stage = (Stage) giris_yap_button.getScene().getWindow();
                                    stage.setScene(scene);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                statusLabel.setText("Doktor bulunamadı.");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            statusLabel.setText("Bir hata oluştu!");
                        }

                    } else if (userRole.equals("patient")) {

                        sql = "SELECT patient_id FROM patients WHERE user_id = ? ";

                        try (PreparedStatement pstmt_pat = conn.prepareStatement(sql)) {

                            pstmt_pat.setInt(1, Integer.parseInt(userId));
                            ResultSet rs_pat = pstmt_pat.executeQuery();

                            if (rs_pat.next()) {
                                String patient_id = rs_pat.getString("patient_id");

                                // Session bilgilerini güncelleme
                                HelloApplication.userSession.setSesionUserId(userId);
                                HelloApplication.userSession.setSesionPatientId(patient_id);

                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/db_project/hasta-anasayfa.fxml"));
                                    Pane pane = loader.load();
                                    Scene scene = new Scene(pane);
                                    Stage stage = (Stage) giris_yap_button.getScene().getWindow();
                                    stage.setScene(scene);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else {
                                statusLabel.setText("Kullanıcı bulunamadı.");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            statusLabel.setText("Bir hata oluştu!");
                        }


                    } else if (userRole.equals("admin")) {

                    } else {
                        statusLabel.setText("Kullanıcı Bulunamadı!");
                    }


                    System.out.println("Kullanıcı ID: " + userId);
                    System.out.println("Kullanıcı Rolü: " + userRole);

                    statusLabel.setText("Giriş başarılı!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Bir hata oluştu!");
            }
        }


    }

    @FXML
    private void sifremi_unuttum_button_clickked() {
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with the main window
        popupStage.setTitle("Şifremi Unuttum");

        // Layout for the popup
        GridPane popupLayout = new GridPane();
        popupLayout.setPadding(new Insets(10));
        popupLayout.setHgap(10);
        popupLayout.setVgap(10);

        // Components for the popup
        Label TCLabel = new Label("Hasta TC:");
        TextField TCField = new TextField();
        TCField.setPromptText("Hasta TC");

        Label emailLabel = new Label("E-posta:");
        TextField emailField = new TextField();
        emailField.setPromptText("E-posta");

        Label yeniSifreLabel = new Label("Yeni Şifre:");
        PasswordField yeniSifreField = new PasswordField();
        yeniSifreField.setPromptText("Yeni Şifre");

        Label tekrarYeniSifreLabel = new Label("Yeni Şifre Tekrar:");
        PasswordField tekrarYeniSifreField = new PasswordField();
        tekrarYeniSifreField.setPromptText("Yeni Şifre Tekrar");

        Button kaydetButton = new Button("Kaydet");
        kaydetButton.setOnAction(event -> {
            // Retrieve input values
            String TC = TCField.getText();
            String email = emailField.getText();
            String yeniSifre = yeniSifreField.getText();
            String tekrarYeniSifre = tekrarYeniSifreField.getText();

            // Validate the inputs
            if (TC.isEmpty() || email.isEmpty() || yeniSifre.isEmpty() || tekrarYeniSifre.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Lütfen tüm alanları doldurun!");
                alert.show();
                return;
            }

            if (!yeniSifre.equals(tekrarYeniSifre)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Yeni şifreler eşleşmiyor!");
                alert.show();
                return;
            }

            // Fetch the user by TC and email
            String userId = getUserIdByTCAndEmail(TC, email);
            if (userId == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Kullanıcı bulunamadı!");
                alert.show();
                return;
            }

            // Update the user's password
            if (updateUserPassword(userId, yeniSifre)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Şifreniz başarıyla güncellendi!");
                alert.show();
                popupStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Şifre güncellenirken bir hata oluştu!");
                alert.show();
            }
        });

        popupLayout.add(TCLabel, 0, 0);
        popupLayout.add(TCField, 1, 0);
        popupLayout.add(emailLabel, 0, 1);
        popupLayout.add(emailField, 1, 1);
        popupLayout.add(yeniSifreLabel, 0, 2);
        popupLayout.add(yeniSifreField, 1, 2);
        popupLayout.add(tekrarYeniSifreLabel, 0, 3);
        popupLayout.add(tekrarYeniSifreField, 1, 3);
        popupLayout.add(kaydetButton, 1, 4);

        Scene popupScene = new Scene(popupLayout, 300, 250);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }


    @FXML
    void support_icon_clicked(MouseEvent event) {

    }


    // PostgreSQL bağlantısı ve veri ekleme
    @FXML
    void initialize() {
        assert giris_yap_button != null : "fx:id=\"giris_yap_button\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";
        assert password_field != null : "fx:id=\"password_field\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";
        assert sifremi_unuttum_button != null : "fx:id=\"sifremi_unuttum_button\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";
        assert statusLabel != null : "fx:id=\"statusLabel\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";
        assert statusLabel1 != null : "fx:id=\"statusLabel1\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";
        assert statusLabel2 != null : "fx:id=\"statusLabel2\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";
        assert support_icon != null : "fx:id=\"support_icon\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";
        assert tc_field != null : "fx:id=\"tc_field\" was not injected: check your FXML file 'giris-sayfasi.fxml'.";

    }

    String getUserIdByTCAndEmail(String TC, String email) {
        String userId = null;
        String sql = "SELECT user_id FROM users " +
                "WHERE tc_number = ? " +
                "AND email = ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, TC);
            pstmt.setString(2, email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getString("user_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userId;
    }

    boolean updateUserPassword(String userId, String yeniSifre) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection conn = connectToDatabase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, yeniSifre);
            pstmt.setInt(2, Integer.parseInt(userId));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Şifre başarıyla güncellendi.");
                return true;
            } else {
                System.out.println("Kullanıcı bulunamadı veya şifre güncellenemedi.");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }




}