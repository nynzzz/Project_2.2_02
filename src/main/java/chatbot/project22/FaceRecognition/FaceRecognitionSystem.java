package chatbot.project22.FaceRecognition;
import chatbot.project22.GUI.RecognitionSystems;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class FaceRecognitionSystem {

    public static final String[] systemCommands = new String[]{"virtualPy/Scripts/python", "virtualPy/bin/python"};


    public static void create_data(String user_name) throws IOException, InterruptedException {

        int systemNumber = operatingSystem();

        String[] command = {
                systemCommands[systemNumber],
                "src/main/java/chatbot/project22/FaceRecognition/Data/Create_Data.py",
                "create_data",
                user_name
        };
        String[] landmarkCommand = {
                systemCommands[systemNumber],
                "src/main/java/chatbot/project22/FaceRecognition/Landmarks.py",
                "create_data"
        };
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output;
        while ((output = reader.readLine()) != null) {
            System.out.println(output);
        }


        // Read error output
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String error;
        while ((error = stdError.readLine()) != null) {
            System.err.println(error);
        }

        // Wait for the process to complete
        try {
            int exitCode = process.waitFor();
            System.out.println("Process finished with exit code " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Process process2 = Runtime.getRuntime().exec(landmarkCommand);

    }

    public static String search(RecognitionSystems recognitionTechnique) throws IOException {

        int systemNumber = operatingSystem();

        String[] command = {};
        switch (recognitionTechnique) {
            case LBP ->
                command = new String[]{ systemCommands[systemNumber],
                                        "src/main/java/chatbot/project22/FaceRecognition/Landmarks.py",
                                        "LBP"
                                    };
            case EIGEN_FACES ->
                command = new String[]{ systemCommands[systemNumber],
                                        "src/main/java/chatbot/project22/FaceRecognition/Data/Create_Data.py",
                                        "search"
                                        };
            case LANDMARKS_ANN ->
                command = new String[]{ systemCommands[systemNumber],
                                        "src/main/java/chatbot/project22/FaceRecognition/Landmarks.py",
                                        "ANN"
                                        };
            case ANGLE_LANDMARKS ->
                command = new String[]{ systemCommands[systemNumber],
                                        "src/main/java/chatbot/project22/FaceRecognition/Landmarks.py",
                                        "ANGLES"
                                        };
            case LBP_AND_ANGLES ->
                command = new String[]{ systemCommands[systemNumber],
                                        "src/main/java/chatbot/project22/FaceRecognition/Landmarks.py",
                                        "LBP_ANGLES"
                                        };
            default ->
                System.out.println("invalid technique");
        }

        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output;
        String found = "";
        while ((output = reader.readLine()) != null) {
            found = output;
            System.out.println("name = " + output);
        }


        // Read error output
        BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String error;
        while ((error = stdError.readLine()) != null) {
            System.err.println(error);
        }

        // Wait for the process to complete
        try {
            int exitCode = process.waitFor();
            System.out.println("Process finished with exit code " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return found;
    }


    public static int operatingSystem(){
        String osSystem = System.getProperty("os.name").toLowerCase();
        if (osSystem.contains("win"))
            return 0;
        else if (osSystem.contains("mac") || osSystem.contains("nix") || osSystem.contains("nux") || osSystem.contains("aix"))
                return 1;
        else return 0;
    }


    public static void main(String[] args) throws IOException {
        System.out.println("1");
        FaceRecognitionSystem faceRecognitionSystem = new FaceRecognitionSystem();
//        faceRecognitionSystem.create_data("keren_better");
//        System.out.println("2");
        String name=faceRecognitionSystem.search(RecognitionSystems.LBP);
        System.out.println("i found that tha name foind isssssssss");
        System.out.println(name);
//        System.out.println("3");
    }
}
