package chatbot.project22.FaceRecognition;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FaceRecognitionSystem {

    public static void create_data(String user_name) throws IOException, InterruptedException {
        String[] command = {
                "virtualPy/Scripts/python",
                "src/main/java/chatbot/project22/FaceRecognition/Data/Create_Data.py",
                "create_data",
                user_name
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
    }

    public static String search() throws IOException {
        String[] command = {
                "virtualPy/Scripts/python",
                "src/main/java/chatbot/project22/FaceRecognition/Data/Create_Data.py",
                "search"
        };
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String output;
        String found = "";
        while ((output = reader.readLine()) != null) {
            found = output;
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
        return found;
    }




    public static void main(String[] args) throws IOException {
        System.out.println("1");
        FaceRecognitionSystem faceRecognitionSystem = new FaceRecognitionSystem();
//        faceRecognitionSystem.create_data("keren_better");
//        System.out.println("2");
        String name=faceRecognitionSystem.search();
        System.out.println("i found that tha name foind isssssssss");
        System.out.println(name);
//        System.out.println("3");
    }
}
