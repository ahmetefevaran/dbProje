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

public class doktorAnaSayfaController {

    @FXML private TableView<Appointment> randevuYonetTable;
    @FXML private TableColumn<Appointment, String> idRow;
    @FXML private TableColumn<Appointment, String> hastaAdiRow;
    @FXML private TableColumn<Appointment, String> tarihColumn;
    @FXML private TableColumn<Appointment, Void> durumColumn;

    @FXML
    void initialize() {
        // Sütunların veri bağlaması
        idRow.setCellValueFactory(new PropertyValueFactory<>("id"));
        hastaAdiRow.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        tarihColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // Durum sütunu için özel buton tanımlaması
        durumColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Appointment, Void> call(final TableColumn<Appointment, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Sil");

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
