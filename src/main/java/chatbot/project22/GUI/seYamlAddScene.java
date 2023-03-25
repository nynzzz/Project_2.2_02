package chatbot.project22.GUI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class seYamlAddScene extends Application{

    private Stage stage;
    private Button load = new Button("Load Rules");
    private Button add = new Button("Add Rules");
    private Button delete = new Button("Delete Rules");
    private Button generate = new Button("Generate Rules");
    private VBox button = new VBox();

    private TextArea mainArea = new TextArea("");
    private TextArea ruleArea = new TextArea(" ");
    private TextArea nameArea = new TextArea(" ");
    private TextArea descriptionArea = new TextArea(" ");
    private TextArea conditionArea = new TextArea(" ");
    private TextArea actionArea = new TextArea(" ");
    private VBox main;
    private VBox rule;


    private Label name = new Label("Name");
    private Label description = new Label("Description");
    private Label condition = new Label("Condition");
    private Label action = new Label("Action");

    public Scene skillEditorYamlAdd(){
//        ------------------Egor-------------Start----

        // Create the input fields for Question, Statement, and Skill
        Label SkillLabel = new Label("New Skill:");
        Label SkillExample = new Label("Example: \n " +
                "name: No Lecture on Saturday \n" +
                "description: this rule is triggered when is saturday and is type schedule \n" +
                "priority: 1 \n" +
                "condition: " + "DAY.equals(\"Saturday\")"  + "&& SCHEDULE !=null \n" +
                "System.out.println(\"No Lecture on Saturday\");");
        Label emptylabel = new Label("    ");
        Label emptylabel2 = new Label("    ");
        TextArea SkillInput = new TextArea();
        SkillInput.setPrefRowCount(5);
        VBox questionLayout = new VBox(SkillLabel, emptylabel, SkillExample, emptylabel2, SkillInput);

        // Create back button to return to the previous scene
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            skillEditorYAML.stage.setScene(skillEditorYAML.scene0);
        });

        // Create save button to save the input fields to separate text files
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {


            try {
                //TODO: add path to the yaml file with skills
                // Append the contents of the input fields to separate text files
                FileWriter questionWriter = new FileWriter("filepath", true);
                questionWriter.write("\n" + SkillInput.getText());
                questionWriter.close();

                // Create a popup to show that changes were saved
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Changes saved");
                alert.setHeaderText(null);
                alert.setContentText("New rule has been saved.");
                alert.showAndWait();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        // Create a layout to hold the input fields, buttons, and labels
        VBox layout2 = new VBox(20, questionLayout, backButton, saveButton);

        // -------------------------------Egor---------end------------------------





        stage = new Stage();
        stage.setTitle("Chat DACS 2023");
        main = new VBox(20);
        main.getChildren().addAll(mainArea,SkillExample);
        main.setAlignment(Pos.CENTER);

        mainArea.setTranslateY(50);
        SkillExample.setTranslateX(-55);
        SkillExample.setTranslateY(60);

        rule = new VBox(20);
        rule.getChildren().add(ruleArea);
        rule.setAlignment(Pos.TOP_LEFT);

        
        

        name.setFont(new Font("Times New Roman",20));
        description.setFont(new Font("Times New Roman",20));
        condition.setFont(new Font("Times New Roman",20));
        action.setFont(new Font("Times New Roman",20));

        //rule.setMaxWidth(150);
        //rule.setMinHeight(800);
        ruleArea.setPrefSize(150,500);
        

        nameArea.setMaxWidth(300);
        nameArea.setMaxHeight(15);

        descriptionArea.setMaxWidth(300);
        descriptionArea.setMaxHeight(15);

        conditionArea.setMaxWidth(300);
        conditionArea.setMaxHeight(15);

        actionArea.setMaxWidth(300);
        actionArea.setMaxHeight(15);

        /* 
        HBox a = new HBox();
        a.getChildren().addAll(name,nameArea);
        HBox b = new HBox();
        b.getChildren().addAll(description,descriptionArea);
        HBox c = new HBox();
        c.getChildren().addAll(condition,conditionArea);
        HBox d = new HBox();
        d.getChildren().addAll(action,actionArea);  
        
        VBox all = new VBox();
        all.getChildren().addAll(a,b,c,d);
        all.setAlignment(Pos.TOP_CENTER);   */

        rule.setTranslateX(0);  
        rule.setTranslateY(0); 

        name.setTranslateX(200); 
        nameArea.setTranslateX(300); 
        name.setTranslateY(0); 
        nameArea.setTranslateY(0); 

        description.setTranslateX(200);  
        descriptionArea.setTranslateX(300);
        description.setTranslateY(50); 
        descriptionArea.setTranslateY(50);

        condition.setTranslateX(200);  
        conditionArea.setTranslateX(300);
        condition.setTranslateY(100); 
        conditionArea.setTranslateY(100);

        action.setTranslateX(200);  
        actionArea.setTranslateX(300);
        action.setTranslateY(150); 
        actionArea.setTranslateY(150);

        main.setTranslateX(200);  
        main.setTranslateY(200);

    


        button.getChildren().addAll(load, add, delete, generate,backButton);
        button.setAlignment(Pos.TOP_RIGHT);

        load.setTranslateX(650); 
        add.setTranslateX(650); 
        delete.setTranslateX(650);   
        generate.setTranslateX(650);
        backButton.setTranslateX(650);
        //refresh.setTranslateX(610);
        //backStartScreen.setTranslateX(610);

        load.setOnAction(event -> {
        });

        add.setOnAction(event -> {
        });

        delete.setOnAction(event -> {
        });

        generate.setOnAction(event -> {
        });

        Group parent = new Group();
        
        parent.getChildren().add(main);
        parent.getChildren().add(rule);
        parent.getChildren().add(name);
        parent.getChildren().add(nameArea);
        parent.getChildren().add(description);
        parent.getChildren().add(descriptionArea);
        parent.getChildren().add(condition);
        parent.getChildren().add(conditionArea);
        parent.getChildren().add(action);
        parent.getChildren().add(actionArea);
        //parent.getChildren().add(all);
        parent.getChildren().add(button);
        Scene scene1 = new Scene(parent, 800, 600);
       // stage.setScene(scene1);

        //stage.show();
        return scene1;
    }

    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

  /*
    public static void main(String[] args){
        launch();
    }
   */
}
