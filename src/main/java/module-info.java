module com.example.learningjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.mail;
    requires java.desktop;

    opens com.example.freshly to javafx.fxml;
    exports com.example.freshly;
}