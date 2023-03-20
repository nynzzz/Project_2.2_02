package chatbot.project22.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.*;

import javafx.application.Application;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;


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
    Button ManageS,addS,help,back1,back2,back3,back4,test,ask,save;
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

        ManageS.setPrefSize(150, 35);
        addS.setPrefSize(150, 35);
        test.setPrefSize(150, 35);
        ManageS.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        addS.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        help.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        test.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        ask.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        save.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));


        back1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        back2.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        back3.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        back4.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 12));
        label0.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        labelAnswer.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 16));


        ManageS.setTranslateX(130);
        addS.setTranslateX(130);
        test.setTranslateX(130);
        ManageS.setTranslateY(200);
        addS.setTranslateY(200);
        test.setTranslateY(200);
        ask.setTranslateX(130);



        label0.setTranslateX(130);
        label0.setTranslateY(-10);

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

        ManageS.setOnAction(e ->{
            stage.setScene(scene1);
        });
        test.setOnAction(e ->{
            stage.setScene(scene4);
        });
        addS.setOnAction(e ->{
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
        layout0.getChildren().addAll(ManageS,addS,test,label0);
        layout1.getChildren().addAll(back1);
        layout2.getChildren().addAll(back2,insertQuery,save);
        layout3.getChildren().addAll(back3);
        layout4.getChildren().addAll(back4,query,ask,labelAnswer);

        color0 =  Color.rgb(53,81,92);
        BackgroundFill backgroundFill = new BackgroundFill(color0, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        layout0.setBackground(background);
        layout1.setBackground(background);
        layout2.setBackground(background);
        layout3.setBackground(background);
        layout4.setBackground(background);

/*
        layout0.setBackground(Background.fill(color0));
        layout1.setBackground(Background.fill(color0));
        layout2.setBackground(Background.fill(color0));
        layout3.setBackground(Background.fill(color0));
        layout4.setBackground(Background.fill(color0));

*/
        FXMLLoader fxmlLoader = new FXMLLoader(skillEditorDemo.class.getResource("hello-view.fxml"));
        //    Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Skill Editor");
        stage.setScene(scene0);
        stage.show();
    }
    public void start(Stage stage) throws IOException {
    }
    private static void readSchedule(String path,String day, String time){
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

            if (data.contains(day)&& data.contains(time)){
                System.out.println("Your result: ");
                labelAnswer.setText("Your schedule: " +"\n"+ data);
                System.out.println(data);
            }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    private static void addSkill(String path,String str) {
        Path p = Paths.get(path);
        String s = System.lineSeparator() + str;

        try(PrintWriter output = new PrintWriter(new FileWriter(path,true)))
        {
            output.printf("%s\r\n", str);
        }
        catch (Exception e) {}

    }
     //   static String path = "/Users/zijiandong/Documents/GitHub/Project_2.2_Group02/GUI/demo11/src/main/resources/com/example/demo11/schedule.txt";

    public static void main(String[] args) {
     //   readSchedule(path,"Tuesday", "11");
        //addSkill();
        launch();

    }


}