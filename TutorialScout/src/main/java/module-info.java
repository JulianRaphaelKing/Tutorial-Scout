module com.raphael.tutorialscout {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires java.net.http;
    requires com.google.gson;
    requires okhttp3;
    requires org.json;

    exports com.raphael.tutorialscout.ui;
    exports com.raphael.tutorialscout.backend.api;
    exports com.raphael.tutorialscout.backend;
}
