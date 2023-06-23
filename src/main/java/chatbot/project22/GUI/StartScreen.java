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

import java.io.IOException;


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

        // txtBot radio button
        RadioButton txtBotRadioButton = new RadioButton("txtBot");
        txtBotRadioButton.setStyle("-fx-font-size:20");
        txtBotRadioButton.setFont(new Font(20));
        txtBotRadioButton.setTranslateX(147);
        txtBotRadioButton.setTranslateY(100);

        // RDP algorithm button
        RadioButton cfg = new RadioButton("RDP Bot");
        cfg.setStyle("-fx-font-size:20");
        cfg.setFont(new Font(20));
        cfg.setTranslateX(147);
        cfg.setTranslateY(100);

        // Transformers button
        RadioButton transformers = new RadioButton("Transformers");
        transformers.setStyle("-fx-font-size:20");
        transformers.setFont(new Font(20));
        transformers.setTranslateX(147);
        transformers.setTranslateY(100);

        ToggleGroup botToggleGroup = new ToggleGroup();
        txtBotRadioButton.setToggleGroup(botToggleGroup);
        cfg.setToggleGroup(botToggleGroup);
        transformers.setToggleGroup(botToggleGroup);

        // Event handler for the Start button
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                if (txtBotRadioButton.isSelected()) {
                    ChatScreenTxt se = new ChatScreenTxt();
                    primaryStage.close();
                } else if (transformers.isSelected()) {
                    // Create a new stage for selecting transformer options
                    Stage transformerOptionsStage = new Stage();
                    transformerOptionsStage.setTitle("Transformer Options");

                    // Create a VBox to hold the options
                    VBox optionsBox = new VBox();
                    optionsBox.setSpacing(10);
                    optionsBox.setPadding(new Insets(10));
                    transformerOptionsStage.setWidth(300);

                    // Create the options buttons
                    RadioButton newsSummaryOption = new RadioButton("News Summary");
                    RadioButton qaFromContextOption = new RadioButton("QA from Context");
                    RadioButton multiTurnDialogOption = new RadioButton("Multi-turn Dialog");

                    // Add the options buttons to the VBox
                    optionsBox.getChildren().addAll(newsSummaryOption, qaFromContextOption, multiTurnDialogOption);

                    // Create a button to confirm the selection
                    Button confirmButton = new Button("Confirm");
                    confirmButton.setOnAction(event -> {
                        if (newsSummaryOption.isSelected()) {
                            try {
                                ChatScreenNewsSummary se = new ChatScreenNewsSummary();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            transformerOptionsStage.close();
                            primaryStage.close();
                        } else if (qaFromContextOption.isSelected()) {
                            try {
                                ChatScreenQaFromContext se = new ChatScreenQaFromContext();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            transformerOptionsStage.close();
                            primaryStage.close();
                        } else if (multiTurnDialogOption.isSelected()) {
                            try {
                                ChatScreenMultiTurnDialog se = new ChatScreenMultiTurnDialog();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            transformerOptionsStage.close();
                            primaryStage.close();
                        } else {
                            // Create a popup to display an error message if no option is selected
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Please select an option");
                            alert.showAndWait();
                        }
                    });

                    // Create a VBox to hold the options and confirm button
                    VBox root = new VBox();
                    root.setSpacing(10);
                    root.setPadding(new Insets(10));
                    root.getChildren().addAll(optionsBox, confirmButton);

                    // Create a scene and set it on the stage
                    Scene scene = new Scene(root);
                    transformerOptionsStage.setScene(scene);

                    // Show the transformer options stage
                    transformerOptionsStage.show();
                } else if (cfg.isSelected()){
                    try {
                        ChatScreenCFG se = new ChatScreenCFG();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    primaryStage.close();
                } else {
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

                }  else if (cfg.isSelected()) {
                    skillEditorCFG se = new skillEditorCFG();
                    primaryStage.close();
                } else {
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
        layout.getChildren().addAll(label0, b, skillEditor, botLabel, txtBotRadioButton, cfg, transformers);
        Scene s = new Scene(layout, 400, 600);

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
