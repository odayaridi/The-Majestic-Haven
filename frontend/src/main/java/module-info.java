module org.example.the_majestic_haven_midterm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jfr;
    requires java.desktop;
    requires javafx.media;
    requires mysql.connector.j;
    requires java.net.http;
    requires org.json;
    requires java.sql;
    requires com.google.protobuf;


    exports org.example.the_majestic_haven_midterm.Models;
    opens org.example.the_majestic_haven_midterm.Models to javafx.fxml;
    exports org.example.the_majestic_haven_midterm.Controllers;
    opens org.example.the_majestic_haven_midterm.Controllers to javafx.fxml;
    exports org.example.the_majestic_haven_midterm.Singleton;
    opens org.example.the_majestic_haven_midterm.Singleton to javafx.fxml;
    exports org.example.the_majestic_haven_midterm.Launchers;
    opens org.example.the_majestic_haven_midterm.Launchers to javafx.fxml;

}