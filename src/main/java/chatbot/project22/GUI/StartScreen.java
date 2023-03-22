package chatbot.project22.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class StartScreen extends Application{
    Label label0 = new Label("Hi there!\n" +
            "Click here to start");

    public void start(Stage primaryStage) {

        }

    public static void main(String[] args){
        //  Start a = new Start();
        launch();
    }

    public StartScreen(){
        Stage primaryStage = new Stage();


        VBox layout = new VBox(20);
        Button b = new Button("Start");

        b.setStyle("-fx-font-size:20");
        b.setFont(new Font(20));

        b.setTranslateX(147);
        b.setTranslateY(200);
        b.setPrefHeight(40);
        b.setPrefWidth(120);

        // connect to skillEditor interface
        Button skillEditor = new Button("Skill Editor");
        skillEditor.setStyle("-fx-font-size:20");
        skillEditor.setFont(new Font(20));

        skillEditor.setTranslateX(147);
        skillEditor.setTranslateY(200);
        skillEditor.setPrefHeight(40);
        skillEditor.setPrefWidth(120);

        skillEditor.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent e) {
                skillEditorDemo se= new skillEditorDemo();
                primaryStage.close();

            }
        });

        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.close();
                ChatScreen chat= new ChatScreen();


            }
        });

        Color color0 =  Color.rgb(121,160,228);
        Color colorBack =  Color.rgb(240,248,255);
        BackgroundFill backgroundFill = new BackgroundFill(colorBack, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        label0.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 50));
        label0.setTextFill(color0);
        label0.setTranslateX(5);
        label0.setTranslateY(55);
        layout.setBackground(background);
        layout.getChildren().addAll(label0,b,skillEditor);
        Scene s = new Scene(layout, 400, 600);

        primaryStage.setTitle("Welcome!");
        primaryStage.setScene(s);


        primaryStage.show();

    }

}