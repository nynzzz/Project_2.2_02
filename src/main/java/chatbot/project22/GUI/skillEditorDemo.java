package chatbot.project22.GUI;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.application.Application;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;

/*
JavaFx structure: Stage-Scene-Layout-components such as button, Label, etc
Stage is the most basic one
U can change the Scene of the Stage
U can change the Layout of the Scene(There're different layout such as VBox)
U can add different buttons, text fields,labels to the Layout
 */


public class skillEditorDemo extends Application {
    Stage stage;
    Scene scene0 ,scene1,scene2,scene3, scene4;
    Label label0 ,labelTodo;
    public static  Label labelAnswer;
    VBox layout0,layout1,layout2,layout3, layout4;
    Button ManageS,addS,help,back1,back2,back3,back4,test,ask,save, backStartScreen;
    Color color0;
    TextArea query,insertQuery;

    //@Override

    public skillEditorDemo(){
        stage = new Stage();
        query = new TextArea("What is the course on Tuesday 11 ?");
        query.setPrefSize(88,18);

        insertQuery = new TextArea("Insert your new Query here");
        insertQuery.setPrefSize(88,18);
        this.stage = stage;
        layout0 = new VBox(20);
        layout1 = new VBox(20);
        layout2 = new VBox(20);
        layout3 = new VBox(20);
        layout4 = new VBox(20);
        scene0 = new Scene(layout0, 400, 600);
        scene1 = new Scene(layout1, 400, 600);
        scene2 = new Scene(layout2, 400, 600);
        scene3 = new Scene(layout3, 400, 600);
        scene4 = new Scene(layout4, 400, 600);
        label0 = new Label("Skill Editor");
        labelTodo = new Label("Needs to be done");
        labelAnswer = new Label("");

        ManageS = new Button("Manage Existing Skills");
        addS = new Button("Add new Skills");
        help = new Button("Help");
        test = new Button("Test");
        ask = new Button("ask");
        save = new Button("save");

        back1 = new Button("Back");
        back2 = new Button("Back");
        back3 = new Button("Back");
        back4 = new Button("Back");
        backStartScreen = new Button("Restart");

        ManageS.setPrefSize(180, 35);
        addS.setPrefSize(150, 35);
        test.setPrefSize(150, 35);
        ManageS.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        addS.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        help.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        test.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 15));
        ask.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));
        save.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));


        back1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));
        back2.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));
        back3.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));
        back4.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));
        backStartScreen.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 12));

        label0.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30));
        labelAnswer.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 16));


        ManageS.setTranslateX(116);
        addS.setTranslateX(130);
        test.setTranslateX(130);
        ManageS.setTranslateY(200);
        addS.setTranslateY(200);
        test.setTranslateY(200);
        ask.setTranslateX(130);



        label0.setTranslateX(130);
        label0.setTranslateY(-50);

        backStartScreen.setTranslateX(330);
        backStartScreen.setTranslateY(-210);

        save.setOnAction(e ->{
            //addSkill(path,insertQuery.getText());
        });

        ask.setOnAction(e ->{
            String str = query.getText();
            String strNum = str.replaceAll("[^0-9]", "");
            System.out.println(strNum);
            System.out.println(str);
            String day = "";
            String time = "";
            String[] words = str.split("[\\s']");
            for(int i=0; i< words.length;i++){
                if(words[i].equals("Monday")||words[i].equals("Tuesday")||words[i].equals("Wednesday")||words[i].equals("Thursday")||words[i].equals("Friday")||words[i].equals("Saturday")||words[i].equals("Sumday")){
                    day = words[i];
                }
            }
            String[] wordsNum = strNum.split("[\\s']");
            //      for(int j=0; j< wordsNum.length;j++) {
            time = wordsNum[0];


            //      }

       //     readSchedule(path,day,time);
        });

        ManageS.setOnAction(e -> {
            // Create a ListView to display the names of the txt files and folders in the directory
            ListView<String> fileList = new ListView<>();
            fileList.setPrefWidth(200);

            // Create a File object representing the directory that contains the txt files and folders
            File directory = new File("src/main/resources/chatbot/project22/textFiles");

            // Get a list of the txt files and folders in the directory
            File[] files = directory.listFiles();

            // Iterate through the list of files and add their names to the ListView
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
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
                                return name.toLowerCase().endsWith(".txt");
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


        test.setOnAction(e ->{
            stage.setScene(scene4);
        });


        addS.setOnAction(e ->{
            // Create the input fields for Question, Statement, and Skill
            Label questionLabel = new Label("Question:");
            TextArea questionInput = new TextArea();
            questionInput.setPrefRowCount(2);
            VBox questionLayout = new VBox(questionLabel, questionInput);

            Label statementLabel = new Label("Statement:");
            TextArea statementInput = new TextArea();
            statementInput.setPrefRowCount(2);
            VBox statementLayout = new VBox(statementLabel, statementInput);

            Label skillLabel = new Label("Skill:");
            TextArea skillInput = new TextArea();
            skillInput.setPrefRowCount(2);
            VBox skillLayout = new VBox(skillLabel, skillInput);

            TextField skillName = new TextField();
            Label skillNameLabel = new Label("Skill name:");
            HBox skillNameLayout = new HBox(10, skillNameLabel, skillName);

            // Create back button to return to the previous scene
            Button backButton = new Button("Back");
            backButton.setOnAction(event -> stage.setScene(scene0));

            // Create save button to save the input fields to separate text files
            Button saveButton = new Button("Save");
            saveButton.setOnAction(event -> {

                String skill = skillName.getText();

                try {
                    // Append the contents of the input fields to separate text files
                    FileWriter questionWriter = new FileWriter("src/main/resources/chatbot/project22/textFiles/Questions.txt", true);
                    questionWriter.write("\n" + questionInput.getText());
                    questionWriter.close();

                    FileWriter statementWriter = new FileWriter("src/main/resources/chatbot/project22/textFiles/Statements.txt", true);
                    statementWriter.write("\n" + statementInput.getText());
                    statementWriter.close();

                    // Create a file object for the skill file
                    File skillFile = new File("src/main/resources/chatbot/project22/textFiles/skillFiles/" + skill + ".txt");

                    try {
                        // Write the skill data to the file
                        FileWriter writer = new FileWriter(skillFile);
                        writer.write(skillInput.getText());
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            // Create a layout to hold the input fields, buttons, and labels
            VBox layout2 = new VBox(20, questionLayout, statementLayout, skillLayout, skillNameLayout, backButton, saveButton);
            scene2 = new Scene(layout2, 400, 600);

            // Set the new scene
            stage.setScene(scene2);
        });



        help.setOnAction(e ->{

        });

        back1.setOnAction(e ->{
            stage.setScene(scene0);
        });
        back2.setOnAction(e ->{
            stage.setScene(scene0);
        });
        back3.setOnAction(e ->{
            stage.setScene(scene0);
        });
        back4.setOnAction(e ->{
            stage.setScene(scene0);
        });

        backStartScreen.setOnAction(e -> {
            stage.close();
            launchStartScreen();
        });





        layout0.getChildren().addAll(ManageS,addS,test,label0,backStartScreen);
        layout1.getChildren().addAll(back1);
        layout2.getChildren().addAll(back2,insertQuery,save);
        layout3.getChildren().addAll(back3);
        layout4.getChildren().addAll(back4,query,ask,labelAnswer);

        color0 =  Color.rgb(240,248,255);
        BackgroundFill backgroundFill = new BackgroundFill(color0, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        layout0.setBackground(background);
        layout1.setBackground(background);
        layout2.setBackground(background);
        layout3.setBackground(background);
        layout4.setBackground(background);


        FXMLLoader fxmlLoader = new FXMLLoader(skillEditorDemo.class.getResource("hello-view.fxml"));
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