package chatbot.project22.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class StartScreen extends Application {
    Label label0 = new Label("Hi there!\n" + "Click start to chat");

    public void start(Stage primaryStage) {

    }

    public static void main(String[] args) {
        launch();
    }

    public StartScreen() {
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);

        VBox layout = new VBox(20);
        Button b = new Button("Start");
        b.setStyle("-fx-font-size:20");
        b.setFont(new Font(20));
        b.setTranslateX(147);
        b.setTranslateY(75);
        b.setPrefHeight(40);
        b.setPrefWidth(120);

        // connect to skillEditor interface
        Button skillEditor = new Button("Skill Editor");
        skillEditor.setStyle("-fx-font-size:20");
        skillEditor.setFont(new Font(20));
        skillEditor.setTranslateX(147);
        skillEditor.setTranslateY(75);
        skillEditor.setPrefHeight(40);
        skillEditor.setPrefWidth(120);

        // Choose the bot label
        Label botLabel = new Label("Choose the bot:");
        botLabel.setStyle("-fx-font-size:20");
        botLabel.setFont(new Font(20));
        botLabel.setTranslateX(147);
        botLabel.setTranslateY(100);
        skillEditor.setPrefHeight(40);
        skillEditor.setPrefWidth(120);

        // txtBot radio button
        RadioButton txtBotRadioButton = new RadioButton("txtBot");
        txtBotRadioButton.setStyle("-fx-font-size:20");
        txtBotRadioButton.setFont(new Font(20));
        txtBotRadioButton.setTranslateX(147);
        txtBotRadioButton.setTranslateY(100);
        skillEditor.setPrefHeight(40);
        skillEditor.setPrefWidth(120);

        // RDP algorithm button
        RadioButton cfg = new RadioButton("RDP Bot");
        cfg.setStyle("-fx-font-size:20");
        cfg.setFont(new Font(20));
        cfg.setTranslateX(147);
        cfg.setTranslateY(100);
        skillEditor.setPrefHeight(40);
        skillEditor.setPrefWidth(120);

        // cyk radio button
        RadioButton cykButton = new RadioButton("CYK Bot");
        cykButton.setStyle("-fx-font-size:20");
        cykButton.setFont(new Font(20));
        cykButton.setTranslateX(147);
        cykButton.setTranslateY(100);
        skillEditor.setPrefHeight(40);
        skillEditor.setPrefWidth(120);


        ToggleGroup botToggleGroup = new ToggleGroup();
        txtBotRadioButton.setToggleGroup(botToggleGroup);
        cfg.setToggleGroup(botToggleGroup);
        cykButton.setToggleGroup(botToggleGroup);

        // Event handler for the Start button
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                if (txtBotRadioButton.isSelected()) {
                    ChatScreenTxt se = new ChatScreenTxt();
                    primaryStage.close();
                } else if (cykButton.isSelected()) {
                   //TODO: chat screen for cyk
                    ChatScreencyk cs = new ChatScreencyk();
                    primaryStage.close();
                } 

                else if (cfg.isSelected()){
                    ChatScreenCFG se = new ChatScreenCFG();
                    primaryStage.close();
                }

                else{
                    // Create a popup to show that a bot needs to be chosen first
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("!!!");
                    alert.setHeaderText(null);
                    alert.setContentText("Please choose a bot first!");
                    alert.showAndWait();
                }
            }
        });

        // Event handler for the Skill Editor button
        skillEditor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (txtBotRadioButton.isSelected()) {
                    skillEditorTxt se = new skillEditorTxt();
                    primaryStage.close();

                } else if (cykButton.isSelected()) {
                    skillEditorYAML se = new skillEditorYAML();
                    primaryStage.close();

                }

                else if (cfg.isSelected()) {
                    skillEditorCFG se = new skillEditorCFG();
                    primaryStage.close();
                }

                else{
                    // Create a popup to show that a bot needs to be chosen first
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Changes saved");
                    alert.setHeaderText(null);
                    alert.setContentText("Please choose a bot first!");
                    alert.showAndWait();
                }
            }
        });

        Color color0 = Color.rgb(121, 160, 228);
        Color colorBack = Color.rgb(240, 248, 255);
        BackgroundFill backgroundFill = new BackgroundFill(colorBack, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        label0.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 50));
        label0.setTextFill(color0);
        label0.setTranslateX(5);
        label0.setTranslateY(55);
        layout.setBackground(background);
        layout.getChildren().addAll(label0, b, skillEditor, botLabel, txtBotRadioButton,  cfg,cykButton);
        Scene s = new Scene(layout, 400, 600);

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
