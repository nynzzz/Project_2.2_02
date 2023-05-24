package chatbot.project22.GUI;

import java.io.BufferedReader;
import java.io.FileReader;
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

public class SignIn extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        
        Label user = new Label("Username: ");
        user.setStyle("-fx-font-size:20");
        user.setFont(new Font(20));
        user.setTranslateX(0);
        user.setTranslateY(50);

        TextField u = new TextField();
        u.setTranslateX(0);
        u.setTranslateY(75);
        u.setPromptText("Enter Username");


        Label password = new Label("Password: ");
        password.setStyle("-fx-font-size:20");
        password.setFont(new Font(20));
        password.setTranslateX(0);
        password.setTranslateY(75);

        TextField pass = new TextField();
        pass.setTranslateX(0);
        pass.setTranslateY(100);
        pass.setPromptText("Enter Password");
        
        Label l = new Label();

        FaceRecognitionSystem faceRecognitionSystem = new FaceRecognitionSystem();
        //final String n = faceRecognitionSystem.search();
        String n = faceRecognitionSystem.name;

        String n1 = ("["+"'"+u.getText()+"'"+"]");


        Button signin = new Button("Sign in");
        
        signin.setOnAction(event -> {

            // Check username and password            
            boolean same = same(u.getText());

            if (same) {
                if(n == n1){
                StartScreen s = new StartScreen();
                s.start(primaryStage); 
                primaryStage.close();
                }
            }
            else
            
            l.setText("Enter correct username");
            l.setStyle("-fx-font-size:20");
            l.setStyle("-fx-text-fill: red;");
            l.setFont(new Font(20));
            l.setTranslateX(0);
            l.setTranslateY(250);
      
        });


        signin.setTranslateX(147);
        signin.setTranslateY(200);
        signin.setPrefHeight(40);
        signin.setPrefWidth(120);


        VBox v = new VBox(20);
        v.setPadding(new Insets(10));
        v.getChildren().addAll(user,u,signin,l);

        Scene scene = new Scene(v, 400, 600);
        primaryStage.setTitle("Signin Screen");
        primaryStage.setScene(scene);
        primaryStage.show();

        
    }
    
    public boolean same(String u) {
        try (BufferedReader b = new BufferedReader(new FileReader("src/main/java/chatbot/project22/GUI/infor.txt"))) {
            String infor;
            while ((infor = b.readLine()) != null) {
                if (infor.startsWith("Username: ") && infor.substring(10).trim().equals(u)) {
                    infor = b.readLine();
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    

    public static void main(String[] args) {
        launch(args);
    }


}
