package chatbot.project22.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;

public  class skillEditorYAML extends Application {
    static Stage stage;
    static Scene scene0 ,scene1,scene2;
    Label label0 ;
    VBox layout0,layout1,layout2;
    Button ManageS,addS,back1, backStartScreen;
    Color color0;


    public skillEditorYAML(){
        stage = new Stage();
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
        backStartScreen = new Button("Restart");

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
            // Create a ListView to display the names of the txt files and folders in the directory
            ListView<String> fileList = new ListView<>();
            fileList.setPrefWidth(200);

            // Create a File object representing the directory that contains the txt files and folders
//            TODO: add directory path
            File directory = new File("directory to files");

            // Get a list of the txt files and folders in the directory
            File[] files = directory.listFiles();

            // Iterate through the list of files and add their names to the ListView
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".yaml")) {
                    // Add the txt files to the ListView
                    fileList.getItems().add(file.getName());
                } else if (file.isDirectory()) {
                    // Add the folders to the ListView
                    fileList.getItems().add(file.getName() + "/");
                }
            }

            // Set an on-click event for the items in the ListView
            fileList.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Get the selected file or folder name from the ListView
                    String fileName = fileList.getSelectionModel().getSelectedItem();

                    if (fileName.endsWith("/")) {
                        // If the selected item is a folder, update the ListView to show its contents
                        File selectedFolder = new File(directory.getPath() + "/" + fileName.substring(0, fileName.length() - 1));
                        File[] folderContents = selectedFolder.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.toLowerCase().endsWith(".yaml");
                            }
                        });

                        fileList.getItems().clear();
                        for (File file : folderContents) {
                            fileList.getItems().add(file.getName());
                        }
                    }
                    else if(fileName.endsWith("Questions.txt") || fileName.endsWith("Statements.txt")){
                        File selectedFile = new File(directory.getPath() + "/" + fileName);

                        try {
                            // Read the contents of the selected file
                            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                            String content = "";
                            String line = reader.readLine();
                            while (line != null) {
                                content += line + "\n";
                                line = reader.readLine();
                            }
                            reader.close();

                            // Create a new scene to display the contents of the selected file
                            TextArea fileContent = new TextArea(content);
                            Button saveButton = new Button("Save");

                            // Set a save button in the new scene that writes the contents of the TextArea to the selected txt file
                            saveButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        FileWriter writer = new FileWriter(selectedFile);
                                        writer.write(fileContent.getText());
                                        writer.close();

                                        // Create a popup to show that changes were saved
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


                            // Create a back button that returns to the main screen
                            Button backButton = new Button("Back");
                            backButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    stage.setScene(scene1);
                                }
                            });

                            VBox newLayout = new VBox();
                            newLayout.getChildren().addAll(fileContent, saveButton, backButton);
                            Scene newScene = new Scene(newLayout, 400, 600);

                            stage.setScene(newScene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    else {
                        // If the selected item is a txt file, open it in a new scene
                        File selectedFile = new File(directory.getPath() + "/skillFiles/" + fileName);

                        try {
                            // Read the contents of the selected file
                            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                            String content = "";
                            String line = reader.readLine();
                            while (line != null) {
                                content += line + "\n";
                                line = reader.readLine();
                            }
                            reader.close();

                            // Create a new scene to display the contents of the selected file
                            TextArea fileContent = new TextArea(content);
                            Button saveButton = new Button("Save");

                            // Set a save button in the new scene that writes the contents of the TextArea to the selected txt file
                            saveButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    try {
                                        FileWriter writer = new FileWriter(selectedFile);
                                        writer.write(fileContent.getText());
                                        writer.close();

                                        // Create a popup to show that changes were saved
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


                            // Create a back button that returns to the main screen
                            Button backButton = new Button("Back");
                            backButton.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    stage.setScene(scene1);
                                }
                            });

                            VBox newLayout = new VBox();
                            newLayout.getChildren().addAll(fileContent, saveButton, backButton);
                            Scene newScene = new Scene(newLayout, 400, 600);

                            stage.setScene(newScene);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            VBox layout1 = new VBox(20);
            layout1.getChildren().addAll(fileList, back1);
            scene1 = new Scene(layout1, 400, 600);
            stage.setScene(scene1);
        });

        addS.setOnAction(e ->{
        //    scene2 = new Scene(layout2, 400, 600);

            // Set the new scene
         //   stage.setScene(scene2);

            Scene addSkillScene = new seYamlAddScene().skillEditorYamlAdd();
            stage.setScene(addSkillScene);
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

        FXMLLoader fxmlLoader = new FXMLLoader(skillEditorYAML.class.getResource("hello-view.fxml"));
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
     //   readSchedule(path,"Tuesday", "11");
        //addSkill();
        launch();
    }
}