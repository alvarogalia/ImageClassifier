module com.alvarogalia.imageclassifier {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.io;
    requires java.desktop;
    requires javafx.swing;


    opens com.alvarogalia.imageclassifier to javafx.fxml;
    exports com.alvarogalia.imageclassifier;
}