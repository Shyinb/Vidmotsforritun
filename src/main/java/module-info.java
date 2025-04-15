module com.example.floskur {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires okhttp3;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires org.json;


    opens vidmot to javafx.fxml;
    exports vidmot;
}