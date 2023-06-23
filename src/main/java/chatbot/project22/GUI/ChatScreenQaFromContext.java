package chatbot.project22.GUI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import chatbot.project22.GUI.ChatScreenCFG;
import chatbot.project22.GUI.StartScreen;
import chatbot.project22.LMs.ModelCalls;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class ChatScreenQaFromContext extends Application {
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
    private Button backStartScreen = new Button("");

    static String mydialogpPath = "src/main/resources/chatbot/project22/LMs/QA_dialogHistory.txt";
    private String context = ""; // Variable to store the context

    public ChatScreenQaFromContext() throws IOException {
        emptyChatHistory();

        box.getStyleClass().add("chatbox");

        stage = new Stage();
        stage.setTitle("Chat DACS 2023");
        TextField txt = new TextField();
        txt.setTranslateY(370);
        txt.setTranslateX(0);
        txt.setPrefWidth(610);
        stage.setResizable(false);

        messageArea = new VBox();
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

        // Start the chat with a bot message
        String initialBotMessage = "Please provide a context about the topic you want. Add a \"Context:\" prefix to the text. If you want to change the context, just provide a new one.";
        Text initialBotMessageText = new Text(initialBotMessage);
        initialBotMessageText.setFont(Font.font(messageFont, messageFontSize));
        initialBotMessageText.setTextAlignment(TextAlignment.LEFT);
        initialBotMessageText.setWrappingWidth(450);

        HBox initialBotBubble = new HBox();
        initialBotBubble.getChildren().add(initialBotMessageText);
        initialBotBubble.setPadding(new Insets(10));
        initialBotBubble.getStyleClass().add("chat_bubble2");

        StackPane initialBotContainer = new StackPane(initialBotBubble);
        initialBotContainer.setAlignment(Pos.BASELINE_LEFT);

        HBox initialBotReceiverBox = new HBox();
        initialBotReceiverBox.setPadding(new Insets(0, 0, 0, 10));

        VBox initialBotResponseBox = new VBox(initialBotReceiverBox, initialBotContainer);
        initialBotResponseBox.setAlignment(Pos.BASELINE_LEFT);
        initialBotResponseBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        initialBotResponseBox.setPadding(new Insets(5, 50, 5, 5));

        messageArea.getChildren().add(initialBotResponseBox);

        send.setOnAction(actionEvent -> {
            String messageText = txt.getText().trim();

            String saveText = '\n' + "User input: " + txt.getText().trim();
            try {
                Files.write(Paths.get(mydialogpPath), saveText.getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!messageText.isEmpty()) {
                Text message = new Text(messageText);
                message.setFont(Font.font(messageFont, messageFontSize));
                message.setTextAlignment(TextAlignment.RIGHT);
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

                // Response
                String responseText;
                try {
                    if (messageText.startsWith("Context:")) {
                        // Update the context with the new value
                        context = messageText.substring(8);
                        responseText = "Thank you for providing a context. You can now ask your question.";
                    } else {
                        responseText = ModelCalls.roberta_pred(messageText, context);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                    responseText = "An error occurred while generating the response.";
                }
                System.out.println("RESPONSE: " + responseText);

                Text responseMessage = new Text(responseText);
                responseMessage.setFont(Font.font(messageFont, messageFontSize));
                responseMessage.setTextAlignment(TextAlignment.LEFT);
                responseMessage.setWrappingWidth(450);

                String saveResponseText = '\n' + "Bot response: " + responseMessage.getText().trim();
                try {
                    Files.write(Paths.get(mydialogpPath), saveResponseText.getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

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
        root.getChildren().addAll(send, backStartScreen);

        Image menuIcon = new Image(ChatScreenCFG.class.getResource("home.jpeg").toExternalForm());
        ImageView menuView = new ImageView(menuIcon);
        menuView.setFitHeight(20);
        menuView.setFitWidth(20);
        menuView.setPreserveRatio(true);
        backStartScreen.setGraphic(menuView);

        backStartScreen.setOnAction(e -> {
            stage.close();
            StartScreen ss = new StartScreen();
        });

        VBox v = new VBox();
        v.setSpacing(10);
        v.setPadding(new Insets(10, 10, 10, 10));

        count1.setFont(new Font("Times New Roman", 20));
        count2.setFont(new Font("Times New Roman", 20));

        v.getChildren().addAll(count1, count2, start, stop, refresh, backStartScreen);
        v.setAlignment(Pos.TOP_RIGHT);

        AtomicInteger time = new AtomicInteger(-1);

        count1.setTranslateX(610);
        count2.setTranslateX(610);
        start.setTranslateX(610);
        stop.setTranslateX(610);
        refresh.setTranslateX(610);
        backStartScreen.setTranslateX(610);
        backStartScreen.setTranslateY(-180);
        //Background color
        Color colorBack = Color.rgb(240, 248, 255);
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
        scene1.getStylesheets().add(ChatScreenCFG.class.getResource("chat.css").toExternalForm());
        stage.setScene(scene1);

        stage.show();
    }

    @Override
    public void start(Stage stage) throws Exception {

    }

    public void emptyChatHistory() throws IOException {
        FileWriter fwriter = new FileWriter(mydialogpPath, false);
        BufferedWriter write = new BufferedWriter(fwriter);
        write.write(""); // write empty string to delete everything
        write.close();
    }

    private static String getChatHistory() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(mydialogpPath));
        int numMessages = Math.min((lines.size() - 1) / 2, 4); // Retrieve the last 4 user inputs and bot responses

        StringBuilder chatHistory = new StringBuilder();
        int startIndex = lines.size() - (numMessages * 2) - 1;

        for (int i = startIndex; i < lines.size() - 1; i += 2) {
            String userMessage = lines.get(i).replace("Context: ", "");
            chatHistory.append(userMessage).append(". ");
        }

        return chatHistory.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
