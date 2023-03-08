package chatbot.project22.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class StartScreen extends Application{

    public void start(Stage primaryStage) {
        

        VBox layout = new VBox(20);
        Button b = new Button("Start");

        b.setStyle("-fx-font-size:30");
        b.setFont(new Font(30));

        b.setTranslateX(200);
        b.setTranslateY(200);
        b.setPrefHeight(60);
        b.setPrefWidth(150);

        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.close();
                ChatScreen chat= new ChatScreen();


            }
        });


        Scene s = new Scene(layout, 500, 800);
        InputStream stream = null;
        try {
            stream = new FileInputStream(ChatScreen.class.getResource("welcom.jpeg").getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(stream);
        ImageView imageView = new ImageView(image);


        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        //Creating a Group object
        Group root = new Group(imageView);
        root.getChildren().add(b);

        //Creating a scene object
        Scene scene=new Scene(root);

        //Setting title to the Stage

        primaryStage.setTitle("Welcome!");

        primaryStage.setScene(scene);


        primaryStage.show();

       
    }

    public static void main(String[] args){
        //  Start a = new Start();
        launch();
    }


}