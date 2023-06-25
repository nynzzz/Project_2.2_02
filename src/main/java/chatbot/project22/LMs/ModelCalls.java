package chatbot.project22.LMs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ModelCalls {

    public static String roberta_pred(String question, String context) throws IOException, InterruptedException {
        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"roberta_pred\", \"params\": {\"question\": \"" + question + "\", \"context\": \"" + context + "\"}}"
        };
        return callPythonMethod(command);
    }


    public static String bart_pred(String question, String context) throws IOException, InterruptedException {
        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"bart_pred\", \"params\": {\"question\": \"" + question + "\", \"context\": \"" + context + "\"}}"
        };
        return callPythonMethod(command);
    }

    public static String bart_large_cnn_sum(String text) throws IOException, InterruptedException {
        // Escape special characters in the text
        String escapedText = text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"bart_large_cnn_sum\", \"params\": {\"text\": \"" + escapedText + "\"}}"
        };
        return callPythonMethod(command);
    }


    public static String t5_base_cnn_sum(String text) throws IOException, InterruptedException {
        // Escape special characters in the text
        String escapedText = text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");

        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"bart_large_cnn_sum\", \"params\": {\"text\": \"" + escapedText + "\"}}"
        };
        return callPythonMethod(command);
    }

    public static String dialogpt_medium_pred(String user_input, String chat_history) throws IOException, InterruptedException {
        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"dialogpt_medium\", \"params\": {\"user_input\": \"" + user_input + "\", \"chat_history\": \"" + chat_history + "\"}}"
        };
        return callPythonMethod(command);
    }



    private static String callPythonMethod(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }

        // Read and print error messages
        StringBuilder errorMessage = new StringBuilder();
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            errorMessage.append(errorLine).append("\n");
        }
        if (errorMessage.length() > 0) {
            System.err.println("Error executing Python method: " + command[2]);
            System.err.println(errorMessage.toString());
        }

        // Wait for the process to complete
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            System.err.println("Error executing Python method: " + command[2]);
        }

        return output.toString().trim();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        String question = "What is treatment for Fungal infection?";
        String context = "In humans, fungal infections occur when an invading fungus takes over an area of the body and is too much for the immune system to handle. Fungi can live in the air, soil, water, and plants. There are also some fungi that live naturally in the human body. Like many microbes, there are helpful fungi and harmful fungi.The name of disease is Fungal infection is an illness when you have itching  skin_rash  nodal_skin_eruptions  dischromic _patches . You must bath twice use detol or neem in bathing water keep infected area dry use clean cloths";
        String text = "summarize: Donald Trump, the 45th President of the United States, is a polarizing\n" +
                "    figure who has left an indelible mark on American politics. Known for his\n" +
                "    larger-than-life personality, Trump's presidency was characterized by\n" +
                "    controversial policies, fiery rhetoric, and a penchant for unconventional\n" +
                "    communication through social media.During his time in office, Trump pursued\n" +
                "    an \\\"America First\\\" agenda, aiming to prioritize the interests of the United\n" +
                "    States in areas such as trade, immigration, and foreign policy. His\n" +
                "    administration implemented significant tax cuts, deregulation measures, and\n" +
                "    pursued a more assertive stance on international trade agreements.Trump's\n" +
                "    approach to governance often drew both fervent support and vehement opposition.\n" +
                "    Supporters praised his efforts to revitalize the economy, prioritize national\n" +
                "    security, and challenge traditional political norms. Critics, on the other hand,\n" +
                "    raised concerns about his handling of sensitive issues, including immigration,\n" +
                "    climate change, and racial tensions.Beyond policy, Trump's leadership style\n" +
                "    and unfiltered communication drew considerable attention. His prolific use of\n" +
                "    Twitter became a defining characteristic of his presidency, allowing him to\n" +
                "    directly communicate with his base and express his thoughts, often generating\n" +
                "    controversy and media frenzy.Trump's presidency was not without challenges\n" +
                "    and controversies, including investigations into Russian interference in the\n" +
                "    2016 election and subsequent impeachment proceedings. These events further\n" +
                "    deepened the divisions within the country and fueled passionate debates about\n" +
                "    the state of democracy and the role of the presidency.Love him or hate him,\n" +
                "    Donald Trump's impact on American politics and public discourse cannot be denied.\n" +
                "    His presidency left a lasting impression on the nation and continues to shape\n" +
                "    the political landscape as the United States moves forward";

//        try {
//            String user_input = "Hello, how are you doing today?";
//            String chat_history = "";
//            String dialogptResult = dialogpt_medium_pred(user_input, chat_history);
//            System.out.println("DialoGPT Medium Result: " + dialogptResult);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            String robertaResult = roberta_pred(question, context);
//            System.out.println("Roberta Result: " + robertaResult);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        try {
//            String bartResult = bart_pred(question, context);
//            System.out.println("BART Result: " + bartResult);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
        try {
            String bartLargeResult = bart_large_cnn_sum(text);
            System.out.println("BART Large Result: " + bartLargeResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String t5BaseResult = t5_base_cnn_sum(text);
            System.out.println("T5 Base Result: " + t5BaseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
