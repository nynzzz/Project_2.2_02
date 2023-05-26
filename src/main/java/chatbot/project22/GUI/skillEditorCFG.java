package chatbot.project22.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class skillEditorCFG {
    Stage stage;
    Scene scene;
    VBox layout;

    public skillEditorCFG() {
        stage = new Stage();
        stage.setResizable(false);
        layout = new VBox(20);

        File selectedFile = new File("src/main/resources/chatbot/project22/CFG/CFG_rules.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String content = "";
            String line = reader.readLine();
            while (line != null) {
                content += line + "\n";
                line = reader.readLine();
            }
            reader.close();

            TextArea fileContent = new TextArea(content);
            fileContent.setPrefRowCount(45);
            Button saveButton = new Button("Save");

            saveButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        FileWriter writer = new FileWriter(selectedFile);
                        writer.write(fileContent.getText());
                        writer.close();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Changes saved");
                        alert.setHeaderText(null);
                        alert.setContentText("Your changes have been saved.");
                        alert.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button backButton = new Button("Back");
            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                    new StartScreen();
                }
            });

            layout.getChildren().addAll(fileContent, saveButton, backButton);
            scene = new Scene(layout, 400, 600);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
