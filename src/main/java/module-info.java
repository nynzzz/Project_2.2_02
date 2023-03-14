module chatbot.project22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    opens chatbot.project22.GUI to javafx.fxml;
    exports chatbot.project22.GUI;
}