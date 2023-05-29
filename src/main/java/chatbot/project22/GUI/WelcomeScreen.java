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


public class WelcomeScreen extends Application {
    Label label0 = new Label("Welcome!!!\n" + "\nClick to Signin\n" + "or Signup!");

    public void start(Stage primaryStage) {

    }

    public static void main(String[] args) {
        launch();
    }

    public WelcomeScreen() {
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);

        VBox layout = new VBox(20);
        Button signin = new Button("Sign in");
        signin.setStyle("-fx-font-size:20");
        signin.setFont(new Font(20));
        signin.setTranslateX(147);
        signin.setTranslateY(100);
        signin.setPrefHeight(40);
        signin.setPrefWidth(120);

        Button Signup = new Button("Sign up");
        Signup.setStyle("-fx-font-size:20");
        Signup.setFont(new Font(20));
        Signup.setTranslateX(147);
        Signup.setTranslateY(125);
        Signup.setPrefHeight(40);
        Signup.setPrefWidth(120);

       
        signin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                SignIn l = new SignIn();
                try {
                    l.start(primaryStage);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        Signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                
                SignUp c = new SignUp();
                c.start(primaryStage);

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
        layout.getChildren().addAll(label0, signin, Signup);
        Scene s = new Scene(layout, 400, 600);

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
