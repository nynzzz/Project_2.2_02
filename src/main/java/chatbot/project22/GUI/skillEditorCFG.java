package chatbot.project22.GUI;

import chatbot.project22.FaceDetection.FaceDetection;
import chatbot.project22.textFileBot.Bot;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.*;

public class skillEditorCFG extends Application {
    Stage stage;
    Scene scene0 ,scene1,scene2;
    Label label0 ;
    VBox layout0,layout1,layout2;
    Button ManageS,addS,back1, backStartScreen;
    Color color0;

    public skillEditorCFG(){
        stage = new Stage();
        stage.setResizable(false);
        this.stage = stage;

        layout0 = new VBox(20);
        layout1 = new VBox(20);
        layout2 = new VBox(20);

        scene0 = new Scene(layout0, 400, 600);
        scene1 = new Scene(layout1, 400, 600);
        scene2 = new Scene(layout2, 400, 600);

        label0 = new Label("Skill Editor");

        ManageS = new Button("Manage Existing Skills");
        addS = new Button("Add new Skills");
        back1 = new Button("Back");
        backStartScreen = new Button("");
        Image menuIcon = new Image(skillEditorTxt.class.getResource("home.jpeg").toExternalForm());
        ImageView menuView = new ImageView(menuIcon);
        menuView.setFitHeight(20);
        menuView.setFitWidth(20);
        menuView.setPreserveRatio(true);
        backStartScreen.setGraphic(menuView);

        ManageS.setPrefSize(180, 35);
        addS.setPrefSize(150, 35);
        ManageS.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        addS.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        back1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));
        backStartScreen.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));
        label0.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30));

        ManageS.setTranslateX(116);
        addS.setTranslateX(130);
        ManageS.setTranslateY(200);
        addS.setTranslateY(200);
        label0.setTranslateX(130);
        label0.setTranslateY(-15);
        backStartScreen.setTranslateX(330);
        backStartScreen.setTranslateY(-155);

        ManageS.setOnAction(e -> {
            
            ListView<String> fileList = new ListView<>();
            fileList.setPrefWidth(200);

            // Set an on-click event for the items in the ListView
            fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                }
            });

            VBox layout1 = new VBox(20);
            layout1.getChildren().addAll(fileList, back1);
            scene1 = new Scene(layout1, 400, 600);
            stage.setScene(scene1);
        });

        addS.setOnAction(e ->{
            // Create back button to return to the previous scene
            Button backButton = new Button("Back");
            backButton.setOnAction(event -> stage.setScene(scene0));

            // Create save button to save the input fields to separate text files
            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {
            });
        });

        back1.setOnAction(e ->{
            stage.setScene(scene0);
        });

        backStartScreen.setOnAction(e -> {
            stage.close();
            launchStartScreen();
        });

        layout0.getChildren().addAll(ManageS,addS,label0,backStartScreen);
        layout1.getChildren().addAll(back1);

        color0 =  Color.rgb(240,248,255);
        BackgroundFill backgroundFill = new BackgroundFill(color0, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        layout0.setBackground(background);
        layout1.setBackground(background);
        layout2.setBackground(background);

        FXMLLoader fxmlLoader = new FXMLLoader(skillEditorTxt.class.getResource("hello-view.fxml"));
        //    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Skill Editor");
        stage.setScene(scene0);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {
    }

    private void launchStartScreen() {
       new StartScreen();
    }
    public static void main(String[] args) {
        launch();
    }
}
