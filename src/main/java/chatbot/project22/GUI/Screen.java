package chatbot.project22.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import javax.swing.event.ChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

public class Screen extends Application{

    private Stage stage;
    private Button load = new Button("Lood Rules");
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

    public Screen(){

        stage = new Stage();
        stage.setTitle("Chat DACS 2023");
        main = new VBox(20);
        main.getChildren().add(mainArea);
        main.setAlignment(Pos.CENTER);


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
        main.setTranslateY(300); 

    


        button.getChildren().addAll(load, add, delete, generate);
        button.setAlignment(Pos.TOP_RIGHT);

        load.setTranslateX(650); 
        add.setTranslateX(650); 
        delete.setTranslateX(650);   
        generate.setTranslateX(650);
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
        stage.setScene(scene1);

        stage.show();
    }



    @Override
    public void start(Stage arg0) throws Exception {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'start'");
    }

    
    public static void main(String[] args){
        launch();
    }
    
}
