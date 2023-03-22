package chatbot.project22.textFileBot;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class TextFileEditor {

    public TextFileEditor() {}

    /**
     * This method is used to read a file
     * @param pathName is the name of the path the file is located at
     * @return a list that contains every sentence that is in that file
     */
    public ArrayList<String> readFile(String pathName) {
        ArrayList<String> output = new ArrayList<>();
        File file = new File(pathName);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine())
                output.add(scanner.nextLine());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    /**
     * This method is used to add 1 sentence to any text file
     * @param fileName is the name of the path where the file is located at
     * @param sentence is the sentence that will get added to the file
     */
    public void addLineToFile(String fileName, String sentence) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            String content = stringBuilder.toString();
            content += sentence;
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method overwrites the information in a file with new content
     * @param fileName is the path to the file the information will be stored in
     * @param content is the information that will get stored in the file
     */
    public void rewriteFile(String fileName, String content) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method creates a new skill file and adds the response line to it
     * @param path is the path of the file that will be made
     * @param answer is the sentence that will get stored in the file
     * @return true if the file was made, else false
     */
    public boolean createNewSkillFile(String path, String answer) {
        File newFile = new File(path);
        try {
            if (newFile.createNewFile()) {
                addLineToFile(path, answer);
                return true;
            }
            else {
                System.out.println("File already exists");
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
