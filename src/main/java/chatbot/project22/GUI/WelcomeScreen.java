package chatbot.project22.GUI;

import javafx.application.Application;
import javafx.collections.FXCollections;
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
    Label label0 = new Label("Welcome!!!" + "\nClick to Signin\n" + "or Signup!");
    Label Labelchoose = new Label("Choose face recognition:");
    ChoiceBox chooseImageFun;

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
        signin.setTranslateX(144);
        signin.setTranslateY(80);
        signin.setPrefHeight(40);
        signin.setPrefWidth(120);

        Button Signup = new Button("Sign up");
        Signup.setStyle("-fx-font-size:20");
        Signup.setFont(new Font(20));
        Signup.setTranslateX(144);
        Signup.setTranslateY(85);
        Signup.setPrefHeight(40);
        Signup.setPrefWidth(120);




         chooseImageFun = new ChoiceBox(FXCollections.observableArrayList(
                RecognitionSystems.values())
        );
        chooseImageFun.setValue(RecognitionSystems.EIGEN_FACES);

        chooseImageFun.setTranslateX(144);
        chooseImageFun.setTranslateY(95);
        chooseImageFun.setPrefHeight(20);
        chooseImageFun.setPrefWidth(120);


        signin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                RecognitionSystems choice = (RecognitionSystems) chooseImageFun.getValue();
                SignIn l = new SignIn(choice);
                try {
                    l.start(primaryStage);
                StartScreen  s = new StartScreen();
                primaryStage.close();

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
        label0.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 40));
        label0.setTextFill(color0);
        label0.setTranslateX(5);
        label0.setTranslateY(35);

        Labelchoose.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 22));
        Labelchoose.setTextFill(color0);
        Labelchoose.setTranslateX(5);
        Labelchoose.setTranslateY(100);


        layout.setBackground(background);
        layout.getChildren().addAll(label0, signin, Signup,Labelchoose,chooseImageFun);
        Scene s = new Scene(layout, 400, 600);

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(s);
        primaryStage.show();
    }
}
