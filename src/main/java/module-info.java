module com.alvarogalia.imageclassifier {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.alvarogalia.imageclassifier to javafx.fxml;
    exports com.alvarogalia.imageclassifier;
}