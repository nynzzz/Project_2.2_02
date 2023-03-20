package chatbot.project22.textFileBot;

import java.util.ArrayList;

public class UserCommand {

    public String command;

    public String original;

    public boolean isQuestion;

    public ArrayList<String> commandWords;

    private ArrayList<String> slotValues;

    public boolean changingAction;

    public String tempSkillName;

    public int actionIndex;

    public String tempAction;

    /**
     * empty constructor for the UserCommand class
     */
    public UserCommand(){
        this.changingAction = false;
    }

    /**
     * Second constructor for the UserCommand class
     * @param isQuestion is true is the input is a question, else false
     * @param input is the sentence typed by the user
     */
    public UserCommand(boolean isQuestion, String input) {
        this.isQuestion = isQuestion;
        this.original = input;
        this.command = input;
        if (isQuestion)
            this.command = input.substring(0, input.length()- 1);
        this.changingAction = false;
    }


    /**
     * This method saves the values of the slots from the sentence of the user
     * @param values are the slot values
     */
    public void setSlotValues(ArrayList<String> values) {
        this.slotValues = values;
    }

    /**
     * @return the slot values
     */
    public ArrayList<String> getSlotValues() {return this.slotValues;}

}
