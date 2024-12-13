module org.example.db_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens org.example.db_project to javafx.fxml;
    exports org.example.db_project;
    exports org.example.db_project.Controllers;
    opens org.example.db_project.Controllers to javafx.fxml;
}