package chatbot.project22.textFileBot;

import java.util.ArrayList;
import java.util.Arrays;

public class StringAttributes {

    /**
     * This method checks whether the input from the user and a template sentence are the same,
     * This is done by checking the patterns in between the slots
     * @param input is the sentence written by the user
     * @param template is the template sentence
     * @return true if they are the same
     */
    public static boolean stringsEqual(UserCommand input, String template){
        String question = input.command.toLowerCase();
        template = template.toLowerCase();
        while (template.contains("<")) {
            int beginBracketIndex = firstSlot(template);
            int endBracketIndex = endSlot(template);
            String pattern = template.substring(0, beginBracketIndex);
            template = template.substring(endBracketIndex + 1);
            if (question.contains(pattern)){
                question = question.replace(pattern, "*");
            }
            else {
                return false;
            }
        }
        question = question.substring(1);
        input.setSlotValues(new ArrayList<>(Arrays.stream(question.split("\\*")).toList()));
        return true;
    }

    /**
     * This method is used to find the index if the first '<' character in a sentence
     * @param template is the sentence where the character is supposed to be
     * @return the index of this character
     */
    public static int firstSlot(String template) {
        return template.indexOf('<');
    }

    /**
     * This method is used to find the index if the first '>' character in a sentence
     * @param template is the sentence where the character is supposed to be
     * @return the index of this character
     */
    public static int endSlot(String template) {
        return template.indexOf('>');
    }

    /**
     * This method is used to split a string to a list
     * @param input is the string that will get split
     * @param splitChar is the character that the string will be split around
     * @return all the parts after the split in a list
     */
    public static ArrayList<String> splitStringToWords(String input, String splitChar) {
        if (input.charAt(input.length() -1 ) == '?')
            input = input.substring(0, input.length() - 1);
        return new ArrayList<>(Arrays.stream(input.strip().split(splitChar)).toList());
    }

    /**
     * This method is used to retrieve al the slot names out of a string
     * @param string is the string where the slot names will get retrieved from
     * @return a list of slot names
     */
    public static ArrayList<String> getAllSlots(String string) {
        ArrayList<String> output = new ArrayList<>();
        while (string.contains(">")) {
            int begin = firstSlot(string);
            int end = endSlot(string) + 1;
            output.add(string.substring(begin, end));
            string = string.substring(end);
        }
        return output;
    }

    /**
     * This method checks if a string is a question
     * @param input is the string that will be checked
     * @return true if the string is a question
     */
    public static boolean isQuestion(String input) {
        return (input.charAt(input.length() - 1) == '?');
    }

    /**
     * This method transforms a list of strings to 1 string
     * it places enters between each item in the list
     * @param input is the arraylist that will be transformed
     * @return the newly created string
     */
    public static String listToString(ArrayList<String> input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            output.append(input.get(i));
            if (i != input.size() - 1)
                output.append('\n');
        }
        return output.toString();
    }
}
