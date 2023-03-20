package chatbot.project22.textFileBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Bot {

    // Path to the file that stores all the questions
    public static final String QUESTION_FILE_PATH = "src/main/resources/chatbot/project22/textFiles/Questions.txt";

    // Path to the file that stores all the statements
    public static final String STATEMENT_FILE_PATH = "src/main/resources/chatbot/project22/textFiles/Statements.txt";

    // Path to the directory in which all the skill Files will be stored
    public static final String SKILL_FILE_PATH = "src/main/resources/chatbot/project22/textFiles/skillFiles/";

    private final TextFileEditor textFileEditor;
    private UserCommand userCommand;

    private int stepCounter;

    /**
     * Constructor for the bot class
     */
    public Bot() {
        this.textFileEditor = new TextFileEditor();
        this.userCommand = new UserCommand();
        this.stepCounter = 0;
    }

    /**
     * This method will be called from the GUI the generate a response that is connected with the input from the user
     * @param input is the sentence the user wrote
     * @return the response the computer will give
     */
    public String generateResponse(String input) {
        if (userCommand.newSkill) {
            this.userCommand.newSkillInformation.add(input);
            return newSkillSteps();
        }
        String s = specialResponse(input);
        if (!Objects.equals(s, ""))
            return s;
        if (StringAttributes.isQuestion(input))
            return handleQuestion(input);
        else
            return handleStatement(input);
    }

    public String specialResponse(String input) {
        if (input.equalsIgnoreCase("hey") || input.equalsIgnoreCase("hello"))
            return "hello, ask a question";
        else if (input.equalsIgnoreCase("create a new skill")){
            this.stepCounter = 1;
            this.userCommand.newSkill = true;
            return "Follow these 4 steps to create a new skill\n1) What is the topic of your skill?";
        }
        return "";
    }

    public String newSkillSteps() {
        String s = "";
        if (this.stepCounter == 4) {
            // store new information
            String topic = this.userCommand.newSkillInformation.get(0);
            String question = topic + ": " + this.userCommand.newSkillInformation.get(1);
            String statement = topic + ": " + this.userCommand.newSkillInformation.get(3);
            textFileEditor.addLineToFile(QUESTION_FILE_PATH, question);
            textFileEditor.addLineToFile(STATEMENT_FILE_PATH, statement);
            textFileEditor.createNewSkillFile(SKILL_FILE_PATH + topic + ".txt", this.userCommand.newSkillInformation.get(2));
            this.userCommand.newSkill = false;
            return "new skill has been saved";
        }
        switch (this.stepCounter) {
            case 1 ->
                    s =  "2) Now please write a question related to this skill that I'll need to answer\nFor example: What do I eat on <DAY>?\nIn this example <DAY> is the changing variable";
            case 2 ->
                    s = "3) How do I need to respond? On the previous question, I will respond:\nOn <DAY> you will eat <*>\nThe <*> sign will be the answer to the question";
            case 3 ->
                    s = "4) How will you give new information about this topic? For example:\nOn <DAY> I will eat <*>, please make sure that the order of variables are the same in the question and the statement";
            default -> s = "";
        };
        this.stepCounter++;
        return s;
    }

    /**
     * This method is used to generate the answer if the input from the user was a question
     * @return the answer the computer will give
     */
    public String handleQuestion(String input){
        this.userCommand = new UserCommand(true, input);
        ArrayList<String> questions = this.textFileEditor.readFile(QUESTION_FILE_PATH);
        String name = "";
        String template = "";
        for (String question: questions) {
            ArrayList<String> questionSplit = StringAttributes.splitStringToWords(question, ":");
            if (StringAttributes.stringsEqual(this.userCommand, questionSplit.get(1).strip())) {
                name = questionSplit.get(0);
                template = questionSplit.get(1).strip();
                break;
            }
        }
        if (name.isEmpty()) {
            return "I dont know what you mean";
        }
        ArrayList<String> actions = textFileEditor.readFile(SKILL_FILE_PATH + name + ".txt");
        ArrayList<String> slotValues = this.userCommand.getSlotValues();
        ArrayList<String> slots = StringAttributes.getAllSlots(template);
        String answer = createAnswer(actions, slots, slotValues);
        return answer;
    }

    /**
     * This method is used to generate the answer if the input from the user was not a question
     * @return the answet the computer will give
     */
    public String handleStatement(String input) {
        if (!userCommand.changingAction) {
            this.userCommand = new UserCommand(false, input);
            ArrayList<String> statements = textFileEditor.readFile(STATEMENT_FILE_PATH);
            String name = "";
            String template = "";
            for (String statement : statements) {
                ArrayList<String> statementSplit = new ArrayList<>(StringAttributes.splitStringToWords(statement, ":"));
                if (StringAttributes.stringsEqual(this.userCommand, statementSplit.get(1).strip())) {
                    name = statementSplit.get(0);
                    template = statementSplit.get(1).strip();
                }
            }
            if (name.isEmpty()) {
                return "This command doesn't work";
            }
            ArrayList<String> slotValues = this.userCommand.getSlotValues();
            ArrayList<String> slots = StringAttributes.getAllSlots(template);
            String combined = combineSlotsAndValues(slots, slotValues);
            ArrayList<String> actions = textFileEditor.readFile(SKILL_FILE_PATH + name + ".txt");
            String action = createAction(slots, slotValues);
            for (int i = 0; i < actions.size(); i++) {
                if (actions.get(i).contains(combined)) {
                    userCommand.changingAction = true;
                    userCommand.actionIndex = i;
                    userCommand.tempSkillName = name;
                    userCommand.tempAction = action;
                    return ("You have already stored information with these values:\n" + actions.get(i) + "\nDo you want to overwrite this information? yes or no");
                }
            }
            textFileEditor.addLineToFile(SKILL_FILE_PATH + name + ".txt", action);
            return "Thank you for the information";
        }
        else {
            if (input.equalsIgnoreCase("yes")) {
                ArrayList<String> actions = textFileEditor.readFile(SKILL_FILE_PATH + userCommand.tempSkillName + ".txt");
                actions.remove(userCommand.actionIndex);
                actions.add(userCommand.tempAction);
                textFileEditor.rewriteFile(SKILL_FILE_PATH + userCommand.tempSkillName + ".txt", StringAttributes.listToString(actions));
                userCommand.changingAction = false;
                return "new information has been stored";
            }
            else if (input.equalsIgnoreCase("no")){
                userCommand.changingAction = false;
                return "okay";
            }
            else
                return "Please say yes or no";
        }
    }

    /**
     * This method is used to construct the answer the computer will give when all the information is known
     * @param actions is a list of al the sentences in the corresponding skill file
     * @param slots are the names of the placeholders in the skill sentence
     * @param slotValues are the values of the slots the user gave
     * @return the answer constructed
     */
    public String createAnswer(ArrayList<String> actions, ArrayList<String> slots, ArrayList<String> slotValues) {
        String response = actions.remove(0);
        String combined = "";
        for (int i = 0; i < slots.size(); i++){
            response = response.replace(slots.get(i), slotValues.get(i));
            combined += slots.get(i) + " " + slotValues.get(i) + " ";
        }
        combined = combined.strip();
        for (String action: actions) {
            ArrayList<String> split = new ArrayList<>(Arrays.stream(action.split(":")).toList());
            System.out.println(split.get(0) + "-" + combined);
            if (split.get(0).equalsIgnoreCase(combined)){
                return response.replace("<*>", split.get(1).strip());
            }
        }
        return "I dont know the answer to that question, please change the variables";
    }

    /**
     * This method is used to parse the information given by the user to a action sentence that will get stored in a skill file
     * @param slots are the names of the placeholders that appear in this action
     * @param slotValues are the values the user gave for these slots
     * @return the sentence that will get added to the skill file
     */
    public String createAction(ArrayList<String> slots, ArrayList<String> slotValues) {
        String output = combineSlotsAndValues(slots, slotValues);
        output += ": " + slotValues.get(slotValues.size() - 1);
        return output;
    }

    /**
     * This method is used to combine the slot names and the slot values
     * @param slots are the names of the slots
     * @param slotValues are the value of the slots
     * @return the combined string
     */
    public String combineSlotsAndValues(ArrayList<String> slots, ArrayList<String> slotValues) {
        String output = "";
        for (int i = 0; i < slots.size() - 1; i++) {
            output += slots.get(i) + " " + slotValues.get(i) + " ";
        }
        return output.strip();
    }

}