package chatbot.project22.GUI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import chatbot.project22.FaceDetection.FaceDetection;
import chatbot.project22.FaceRecognition.FaceRecognitionSystem;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.scene.control.*;
import javafx.scene.control.TextField;

public class SignUp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        Label user = new Label("Enter your name: ");
        user.setStyle("-fx-font-size:20");
        user.setFont(new Font(20));
        user.setTranslateX(0);
        user.setTranslateY(50);

        TextField u = new TextField();
        u.setTranslateX(0);
        u.setTranslateY(75);


        Label password = new Label("Password: ");
        password.setStyle("-fx-font-size:20");
        password.setFont(new Font(20));
        password.setTranslateX(0);
        password.setTranslateY(75);

        TextField pass = new TextField();
        pass.setTranslateX(0);
        pass.setTranslateY(100);

        Button signup = new Button("Sign up");
        
        signup.setOnAction(event -> {

            //Save the entered username and password
            save(u.getText());
            FaceRecognitionSystem faceRecognitionSystem = new FaceRecognitionSystem();
            try {
                faceRecognitionSystem.create_data(u.getText());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            StartScreen s = new StartScreen();
            s.start(primaryStage);
            primaryStage.close();            
            
        });


        signup.setTranslateX(147);
        signup.setTranslateY(250);
        signup.setPrefHeight(40);
        signup.setPrefWidth(120);

        VBox v = new VBox(10);
        v.setPadding(new Insets(10));
        v.getChildren().addAll(user,u,signup);

        Scene scene = new Scene(v, 400, 600);
        primaryStage.setTitle("Signup Screen");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void save(String u) {
        try (BufferedWriter b = new BufferedWriter(new FileWriter("src/main/java/chatbot/project22/GUI/infor.txt", true))) {
            b.write("Username: " + u);
            b.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   

    public static void main(String[] args) {
        launch(args);
    }


}
