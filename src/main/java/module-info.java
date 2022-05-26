module com.milesacq.mbtajava.mbtajava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires json.simple;

    opens com.milesacq.mbtajava to javafx.fxml;
    exports com.milesacq.mbtajava;
}