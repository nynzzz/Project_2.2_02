module chatbot.project22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires jiprolog;
    requires java.desktop;

    opens chatbot.project22.GUI to javafx.fxml;
    exports chatbot.project22.GUI;
}