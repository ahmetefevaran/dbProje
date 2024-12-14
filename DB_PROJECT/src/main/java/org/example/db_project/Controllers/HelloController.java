package org.example.db_project.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.db_project.Database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HelloController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private Button addButton;
    @FXML private Label statusLabel;

    // PostgreSQL bağlantısı ve veri ekleme
    @FXML
    private void initialize() {
        addButton.setOnAction(e -> addUser());
    }

    // Veritabanına kullanıcı ekleme metodu
    private void addUser() {
        String name = nameField.getText();
        String email = emailField.getText();

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
                nameField.clear();
                emailField.clear();
            } else {
                statusLabel.setText("Bir hata oluştu.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Veritabanı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
}