package chatbot.project22.GUI;

import chatbot.project22.textFileBot.Bot;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import javax.swing.event.ChangeListener;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatScreen extends Application{

    private final Stage stage;
    private final VBox box = new VBox();
    private VBox messageArea;
    private String messageFont = "Arial";
    private int messageFontSize = 14;

    private Label count1 = new Label(); 
    private static Label count2 = new Label();
    private Button start = new Button("Start");
    private Button stop = new Button("Stop");
    private Button refresh = new Button("Refresh");
    private Button backStartScreen = new Button("Restart");


    private Bot textFileBot;

    public ChatScreen() {
        this.textFileBot = new Bot();

        box.getStyleClass().add("chatbox");

        stage = new Stage();
        stage.setTitle("Chat DACS 2023");
        TextField txt = new TextField();
        txt.setTranslateY(370);
        txt.setTranslateX(0);
        txt.setPrefWidth(610);

        messageArea = new VBox();
        // messageArea.setFillWidth(true);
        messageArea.setSpacing(10);
        ScrollPane scrollPane = new ScrollPane(messageArea);
        scrollPane.setPrefSize(610, 360);
        scrollPane.vvalueProperty().bind(messageArea.heightProperty());
        txt.setPromptText("Type a message...");



        Button send = new Button();
        send.setText("SEND");
        send.setTranslateX(614);
        send.setTranslateY(370);
        send.setPrefHeight(20);
        send.setPrefWidth(60);

        send.setOnAction(actionEvent -> {
            String messageText = txt.getText().trim();
            if (!messageText.isEmpty()) {
                Text message = new Text(messageText);
                message.setFont(Font.font(messageFont, messageFontSize));
                message.setTextAlignment(TextAlignment.CENTER);
                message.setWrappingWidth(450);

                HBox chatBubble = new HBox();
                chatBubble.getChildren().add(message);
                chatBubble.setPadding(new Insets(10));
                chatBubble.getStyleClass().add("chat_bubble");
                chatBubble.setAlignment(Pos.BASELINE_RIGHT); 
        
                StackPane messageContainer = new StackPane(chatBubble);
                messageContainer.setAlignment(Pos.BASELINE_RIGHT);
    
        
                HBox senderBox = new HBox();
                senderBox.setAlignment(Pos.BASELINE_RIGHT);
                senderBox.setPadding(new Insets(0, 10, 0, 0));
        
                VBox messageBox = new VBox(senderBox, messageContainer);
                messageBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                messageBox.setPadding(new Insets(5, 5, 5, 130));
        
                String responseText = textFileBot.generateResponse(messageText);
                Text responseMessage = new Text(responseText);
                responseMessage.setFont(Font.font(messageFont, messageFontSize));
                responseMessage.setTextAlignment(TextAlignment.CENTER);
                responseMessage.setWrappingWidth(450);
        
                HBox responseBubble = new HBox();
                responseBubble.getChildren().add(responseMessage);
                responseBubble.setPadding(new Insets(10));
                responseBubble.getStyleClass().add("chat_bubble2");
        
                StackPane responseContainer = new StackPane(responseBubble);
                responseContainer.setAlignment(Pos.BASELINE_LEFT);
        
                
        
                HBox receiverBox = new HBox();
                receiverBox.setPadding(new Insets(0, 0, 0, 10));
        
                VBox responseBox = new VBox(receiverBox, responseContainer);
                responseBox.setAlignment(Pos.BASELINE_LEFT);
                responseBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                responseBox.setPadding(new Insets(5, 50, 5, 5));
        
                messageArea.getChildren().addAll(messageBox, responseBox);
                messageArea.layout();
                txt.clear();
            }
        });



        HBox messageInputBox = new HBox(txt, send);
        messageInputBox.setSpacing(10);
        messageInputBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(scrollPane, messageInputBox);
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(send,backStartScreen);

        backStartScreen.setOnAction(e->{
            stage.close();
            StartScreen ss = new StartScreen();
        });

        // Time count

        //HBox h = new HBox();
        VBox v = new VBox();
        v.setSpacing(10);
        v.setPadding(new Insets(10, 10, 10, 10));

        count1.setFont(new Font("Times New Roman",20));
        count2.setFont(new Font("Times New Roman",20));

        v.getChildren().addAll(count1,count2,start,stop,refresh,backStartScreen);
        v.setAlignment(Pos.TOP_RIGHT);

        AtomicInteger time = new AtomicInteger(-1);
        //Scene scene = new Scene(h,500,400);
        Time t = new Time();

        start.setOnAction(event-> {

            String message = t.getMessage();

            if (message == null || message.length()< 1){
                count2.setText("second");
                if (time.get() == -1)time.set(0);
                t.setStartNumber(time.get());
            }else{
                count2.setText("second");
                t.setStartNumber(Integer.parseInt(message));
            }

            t.restart();
        });

        stop.setOnAction(event -> {
            if (time.get() == -1)count2.setText("task not started");
            else {
                String message = t.getMessage();
                t.cancel();
                time.set(Integer.parseInt(message));
            }
        });

        refresh.setOnAction(event -> {
            stage.close();
            ChatScreen cs = new ChatScreen();
        });

        txt.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String messageText = txt.getText().trim();
                    if (!messageText.isEmpty()) {
                        Text message = new Text(messageText);
                        message.setFont(Font.font(messageFont, messageFontSize));
                        message.setTextAlignment(TextAlignment.LEFT);
                        message.setWrappingWidth(450);

                        HBox chatBubble = new HBox();
                        chatBubble.getChildren().add(message);
                        chatBubble.setPadding(new Insets(10));
                        chatBubble.getStyleClass().add("chat_bubble");
                        chatBubble.setAlignment(Pos.BASELINE_RIGHT);

                        StackPane messageContainer = new StackPane(chatBubble);
                        messageContainer.setAlignment(Pos.BASELINE_RIGHT);


                        HBox senderBox = new HBox();
                        senderBox.setAlignment(Pos.BASELINE_RIGHT);
                        senderBox.setPadding(new Insets(0, 10, 0, 0));

                        VBox messageBox = new VBox(senderBox, messageContainer);
                        messageBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        messageBox.setPadding(new Insets(5, 5, 5, 130));

                        String responseText = textFileBot.generateResponse(messageText);
                        Text responseMessage = new Text(responseText);
                        responseMessage.setFont(Font.font(messageFont, messageFontSize));
                        responseMessage.setTextAlignment(TextAlignment.LEFT);
                        responseMessage.setWrappingWidth(450);

                        HBox responseBubble = new HBox();
                        responseBubble.getChildren().add(responseMessage);
                        responseBubble.setPadding(new Insets(10));
                        responseBubble.getStyleClass().add("chat_bubble2");

                        StackPane responseContainer = new StackPane(responseBubble);
                        responseContainer.setAlignment(Pos.BASELINE_LEFT);



                        HBox receiverBox = new HBox();
                        receiverBox.setPadding(new Insets(0, 0, 0, 10));

                        VBox responseBox = new VBox(receiverBox, responseContainer);
                        responseBox.setAlignment(Pos.BASELINE_LEFT);
                        responseBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
                        responseBox.setPadding(new Insets(5, 50, 5, 5));

                        messageArea.getChildren().addAll(messageBox, responseBox);
                        messageArea.layout();
                        txt.clear();
                    }}

            }
        });

        count1.textProperty().bind(t.messageProperty());

        count1.setTranslateX(610); 
        count2.setTranslateX(610); 
        start.setTranslateX(610);   
        stop.setTranslateX(610);
        refresh.setTranslateX(610);
        backStartScreen.setTranslateX(610);

        //Background color
        Color colorBack =  Color.rgb(240,248,255);
        BackgroundFill backgroundFill = new BackgroundFill(colorBack, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);


        Group parent = new Group();

        parent.getChildren().add(messageArea);
        parent.getChildren().add(scrollPane);

        parent.getChildren().add(txt);
        parent.getChildren().add(send);

        parent.getChildren().add(v);
        Scene scene1 = new Scene(parent, 700, 400);
        scene1.setFill(colorBack);
        scene1.getStylesheets().add(ChatScreen.class.getResource("chat.css").toExternalForm());
        stage.setScene(scene1);

        stage.show();
    }

    static class Time extends Service<Void>{

        private int startNumber = -1;

        public void setStartNumber(int startNumber) {
            this.startNumber = startNumber;
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    if (startNumber == -1)startNumber = 0;
                    if (startNumber == 600)startNumber = 600;
                    for (int i = startNumber; i <=600 ; i++) {
                        updateMessage(Integer.toString(i));
                        Thread.sleep(1000);
                    }
                    Platform.runLater(()-> count2.setText("Over"));
                    return null;
                }
            };
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
}
