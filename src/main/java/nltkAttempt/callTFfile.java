package nltkAttempt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class callTFfile {
    public static void main(String[] args) {
        try {
            // Build the command to execute the Python script
            String question = "How is the weather in Maastricht? ";
            ProcessBuilder processBuilder = new ProcessBuilder("python", "src/main/java/nltkAttempt/TF1.py", question);

            // Start the process
            Process process = processBuilder.start();

            // Get the output from the Python script
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            // Read the response from the standard output
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }

            // Wait for the process to finish
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            // Print the response
            System.out.println("Response from Python script:");
            System.out.println(response.toString());


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
