module chatbot.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires opencv;
    requires easy.rules.core;
    opens chatbot.project22.GUI to javafx.fxml;
    exports chatbot.project22.GUI;
}