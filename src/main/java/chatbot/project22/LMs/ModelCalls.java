package chatbot.project22.LMs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

public class ModelCalls {

    public static String roberta_pred(String question, String context) throws IOException, InterruptedException {
        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"roberta_pred\", \"params\": {\"question\": \"" + question + "\", \"context\": \""
                        + context + "\"}}"
        };
        return callPythonMethod(command);
    }

    public static String bart_pred(String question, String context) throws IOException, InterruptedException {
        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"bart_pred\", \"params\": {\"question\": \"" + question + "\", \"context\": \"" + context
                        + "\"}}"
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

    public static String dialogpt_medium_pred(String user_input, String chat_history)
            throws IOException, InterruptedException {
        String[] command = {
                "virtualPy/bin/python",
                "src/main/java/chatbot/project22/LMs/GetPredictions.py",
                "{\"method\": \"dialogpt_medium\", \"params\": {\"user_input\": \"" + user_input
                        + "\", \"chat_history\": \"" + chat_history + "\"}}"
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
    public static String getQuestion(int index) {
    switch (index) {
        case 1:
            return "What is your favorite color?";
        case 2:
            return "Have you ever traveled to another country?";
        case 3:
            return "Can you tell me about your hobbies?";
        case 4:
            return "What kind of books do you enjoy reading?";
        case 5:
            return "Do you have any pets?";
        case 6:
            return "Have you ever participated in a sports competition?";
        case 7:
            return "What is your favorite type of cuisine?";
        case 8:
            return "Can you tell me about your dream vacation destination?";
        case 9:
            return "Do you enjoy watching movies? If so, what genre do you prefer?";
        case 10:
            return "Have you ever learned to play a musical instrument?";
        case 11:
            return "What is your favorite season and why?";
        case 12:
            return "Have you ever attended a live concert or music festival?";
        case 13:
            return "Can you describe your typical weekend routine?";
        case 14:
            return "What is your favorite form of exercise or physical activity?";
        case 15:
            return "Have you ever volunteered for a charitable organization?";
        case 16:
            return "Do you enjoy cooking or baking? What is your signature dish?";
        case 17:
            return "Can you tell me about a memorable trip you've taken in the past?";
        case 18:
            return "What is your favorite board game or card game?";
        case 19:
            return "Have you ever tried any extreme sports or adventurous activities?";
        case 20:
            return "Can you share a funny or embarrassing childhood memory?";
        case 21:
            return "What is your favorite type of art or artistic expression?";
        case 22:
            return "Have you ever been involved in a creative project, like writing or painting?";
        case 23:
            return "Do you have any hidden talents or skills?";
        case 24:
            return "Can you describe your dream job or career?";
        case 25:
            return "What is your favorite type of music or favorite band/artist?";
        case 26:
            return "Have you ever attended a theater play or musical?";
        case 27:
            return "Can you share a memorable experience from your school days?";
        case 28:
            return "What is your favorite outdoor activity or sport?";
        case 29:
            return "Have you ever met someone famous or influential?";
        case 30:
            return "Can you tell me about a recent accomplishment or achievement you're proud of?";
        case 31:
            return "Do you enjoy gardening or taking care of plants?";
        case 32:
            return "What is your favorite type of dance or dance style?";
        case 33:
            return "Have you ever participated in a community event or festival?";
        case 34:
            return "Can you describe your perfect day off or relaxation routine?";
        case 35:
            return "What is your favorite type of dessert or sweet treat?";
        case 36:
            return "Have you ever taken part in a charity run or marathon?";
        case 37:
            return "Can you share a memorable experience from a family gathering?";
        case 38:
            return "What is your favorite type of movie or TV show genre?";
        case 39:
            return "Have you ever been involved in a team sport or group activity?";
        case 40:
            return "Can you describe a challenging experience you've faced and how you overcame it?";
        case 41:
            return "What is your favorite type of animal and why?";
        case 42:
            return "Have you ever attended a themed party or costume event?";
        case 43:
            return "Can you share a memorable experience from a concert or music festival?";
        case 44:
            return "What is your favorite type of weather or climate?";
        case 45:
            return "Do you enjoy any form of artistic expression, such as painting or sculpting?";
        case 46:
            return "Have you ever participated in a science fair or exhibition?";
        case 47:
            return "Can you describe a memorable experience from a camping or outdoor trip?";
        case 48:
            return "What is your favorite type of cuisine to cook at home?";
        case 49:
            return "Have you ever taken part in a fundraising event or charity auction?";
        case 50:
            return "Can you share a memorable experience from a road trip or long journey?";
        default:
            return "Unknown question";
    }
}
public static String getText(int index) {
    switch (index) {
        case 1:
            return "The human foot is a strong and complex mechanical structure containing 26 bones, 33 joints (20 of which are actively articulated), and more than a hundred muscles, tendons, and ligaments.[2] The joints of the foot are the ankle and subtalar joint and the interphalangeal joints of the foot. An anthropometric study of 1197 North American adult Caucasian males (mean age 35.5 years) found that a man's foot length was 26.3 cm with a standard deviation of 1.2 cm.[3]\n" + //
                "\n" + //
                "The foot can be subdivided into the hindfoot, the midfoot, and the forefoot:\n" + //
                "\n" + //
                "The hindfoot is composed of the talus (or ankle bone) and the calcaneus (or heel bone). The two long bones of the lower leg, the tibia and fibula, are connected to the top of the talus to form the ankle. Connected to the talus at the subtalar joint, the calcaneus, the largest bone of the foot, is cushioned underneath by a layer of fat.[2]\n" + //
                "\n" + //
                "The five irregular bones of the midfoot, the cuboid, navicular, and three cuneiform bones, form the arches of the foot which serves as a shock absorber. The midfoot is connected to the hind- and fore-foot by muscles and the plantar fascia.[2]\n" + //
                "\n" + //
                "The forefoot is composed of five toes and the corresponding five proximal long bones forming the metatarsus. Similar to the fingers of the hand, the bones of the toes are called phalanges and the big toe has two phalanges while the other four toes have three phalanges each. The joints between the phalanges are called interphalangeal and those between the metatarsus and phalanges are called metatarsophalangeal (MTP).[2]\n" + //
                "\n" + //
                "Both the midfoot and forefoot constitute the dorsum (the area facing upward while standing) and the planum (the area facing downward while standing).\n" + //
                "\n" + //
                "The instep is the arched part of the top of the foot between the toes and the ankle.";
        case 2:
            return "Extensor group: the tibialis anterior originates on the proximal half of the tibia and the interosseous membrane and is inserted near the tarsometatarsal joint of the first digit. In the non-weight-bearing leg, the tibialis anterior dorsiflexes the foot and lift its medial edge (supination). In the weight-bearing leg, it brings the leg toward the back of the foot, like in rapid walking. The extensor digitorum longus arises on the lateral tibial condyle and along the fibula, and is inserted on the second to fifth digits and proximally on the fifth metatarsal. The extensor digitorum longus acts similar to the tibialis anterior except that it also dorsiflexes the digits. The extensor hallucis longus originates medially on the fibula and is inserted on the first digit. It dorsiflexes the big toe and also acts on the ankle in the unstressed leg. In the weight-bearing leg, it acts similarly to the tibialis anterior.[7]\n" + //
                    "\n" + //
                    "Peroneal group: the peroneus longus arises on the proximal aspect of the fibula and peroneus brevis below it. Together, their tendons pass behind the lateral malleolus. Distally, the peroneus longus crosses the plantar side of the foot to reach its insertion on the first tarsometatarsal joint, while the peroneus brevis reaches the proximal part of the fifth metatarsal. These two muscles are the strongest pronators and aid in plantar flexion. The peroneus longus also acts like a bowstring that braces the transverse arch of the foot.[8]";
        case 3:
            return "Existing large-scale summarization datasets\n" + //
                    "consist of relatively short documents. For example, articles in the CNN/Daily Mail dataset (Hermann et al., 2015) are on average about 600 words\n" + //
                    "long. Similarly, existing neural summarization\n" + //
                    "models have focused on summarizing sentences\n" + //
                    "and short documents. In this work, we propose a\n" + //
                    "model for effective abstractive summarization of\n" + //
                    "longer documents. Scientific papers are an example of documents that are significantly longer\n" + //
                    "than news articles (see Table 1). They also follow a standard discourse structure describing the\n" + //
                    "problem, methodology, experiments/results, and\n" + //
                    "finally conclusions (Suppe, 1998).\n" + //
                    "Most summarization works in the literature\n" + //
                    "focus on extractive summarization. Examples\n" + //
                    "of prominent approaches include frequency-based\n" + //
                    "methods (Vanderwende et al., 2007), graph-based\n" + //
                    "methods (Erkan and Radev, 2004), topic modeling (Steinberger and Jezek, 2004), and neural\n" + //
                    "models (Nallapati et al., 2017). Abstractive summarization is an alternative approach where the\n" + //
                    "generated summary may contain novel words and\n" + //
                    "phrases and is more similar to how humans summarize documents (Jing, 2002). Recently, neural methods have led to encouraging results in\n" + //
                    "abstractive summarization (Nallapati et al., 2016;\n" + //
                    "See et al., 2017; Paulus et al., 2017; Li et al.,\n" + //
                    "2017). These approaches employ a general framework of sequence-to-sequence (seq2seq) models\n" + //
                    "(Sutskever et al., 2014) where the document is\n" + //
                    "fed to an encoder network and another (recurrent)\n" + //
                    "network learns to decode the summary. While\n" + //
                    "promising, these methods focus on summarizing\n" + //
                    "news articles which are relatively short. Many\n" + //
                    "other document types, however, are longer and\n" + //
                    "structured. Seq2seq models tend to struggle with\n" + //
                    "longer sequences because at each decoding step,\n" + //
                    "the decoder needs to learn to construct a context\n" + //
                    "vector capturing relevant information from all the\n" + //
                    "tokens in the source sequence (Shao et al., 2017).\n" + //
                    "Our main contribution is an abstractive model\n" + //
                    "for summarizing scientific papers which are an\n" + //
                    "example of long-form structured document types.\n" + //
                    "Our model includes a hierarchical encoder, capturing the discourse structure of the document and a\n" + //
                    "discourse-aware decoder that generates the summary. Our decoder attends to different discourse\n" + //
                    "sections and allows the model to more accurately\n" + //
                    "represent important information from the source\n" + //
                    "resulting in a better context vector. We also introduce two large-scale datasets of long and structured scientific papers obtained from arXiv and\n" + //
                    "PubMed to support both training and evaluating\n" + //
                    "models on the task of long document summarization. Evaluation results show that our method outperforms state-of-the-art summarization models1\n" + //
                    ".";
        case 4:
            return "What kind of books do you enjoy reading?In probability theory and statistics, the Poisson distribution is a discrete probability distribution that expresses the probability of a given number of events occurring in a fixed interval of time or space if these events occur with a known constant mean rate and independently of the time since the last event.[1] It is named after French mathematician Sim\u00E9on Denis Poisson (/\u02C8pw\u0251\u02D0s\u0252n/; French pronunciation: \u200B[pwas\u0254\u0303]). The Poisson distribution can also be used for the number of events in other specified interval types such as distance, area, or volume.\n" + //
                    "\n" + //
                    "For instance, a call center receives an average of 180 calls per hour, 24 hours a day. The calls are independent; receiving one does not change the probability of when the next one will arrive. The number of calls received during any minute has a Poisson probability distribution with mean 3: the most likely numbers are 2 and 3 but 1 and 4 are also likely and there is a small probability of it being as low as zero and a very small probability it could be 10.\n" + //
                    "\n" + //
                    "Another example is the number of decay events that occur from a radioactive source during a defined observation period.\n" + //
                    "\n" + //
                    "";
        case 5:
            return "Cars is a 2006 American computer-animated sports comedy film produced by Pixar Animation Studios for Walt Disney Pictures. The film was directed by John Lasseter from a screenplay by Dan Fogelman, Lasseter, Joe Ranft, Kiel Murray, Phil Lorin, and Jorgen Klubien and a story by Lasseter, Ranft, and Klubien, and was the final film independently produced by Pixar after its purchase by Disney in January 2006. The film features an ensemble voice cast of Owen Wilson, Paul Newman (in his final voice acting theatrical film role), Bonnie Hunt, Larry the Cable Guy, Tony Shalhoub, Cheech Marin, Michael Wallis, George Carlin, Paul Dooley, Jenifer Lewis, Guido Quaroni, Michael Keaton, Katherine Helmond, John Ratzenberger and Richard Petty, while race car drivers Dale Earnhardt Jr. (as \"Junior\"), Mario Andretti, Michael Schumacher and car enthusiast Jay Leno (as \"Jay Limo\") voice themselves.\n" + //
                    "\n" + //
                    "Set in a world populated entirely by anthropomorphic talking cars and other vehicles, it follows a hotshot rookie race car named Lightning McQueen (Wilson) who, on the way to the biggest race of his life, gets stranded in Radiator Springs, a rundown town that's past its glory days, and learns a thing or two about friendship, family, and the things in life that are truly worth waiting for.\n" + //
                    "\n" + //
                    "Development for Cars started in 1998, after finishing the production of A Bug's Life, with a new script titled The Yellow Car, which was about an electric car living in a gas-guzzling world with Jorgen Klubien writing. It was announced that the producers agreed that it could be the next Pixar film after A Bug's Life, scheduled for early 1999, particularly around June 4; the idea was later scrapped in favor of Toy Story 2. Shortly after, production was resumed with major script changes. The film was inspired by Lasseter's experiences on a cross-country road trip. Randy Newman composed the film's score, while artists such as Sheryl Crow, Rascal Flatts, John Mayer and Brad Paisley contributed to the film's soundtrack.\n" + //
                    "\n" + //
                    "Cars premiered on May 26, 2006, at Lowe's Motor Speedway in Concord, North Carolina and was theatrically released in the United States on June 9, to generally positive reviews and commercial success, grossing $462 million worldwide against a budget of $120 million. The film received two nominations at the 79th Academy Awards, including Best Animated Feature, but lost to Happy Feet (but won both the Annie Award for Best Animated Feature and the Golden Globe Award for Best Animated Feature Film). The film was released on DVD on November 7, 2006, on VHS in limited quantities on February 19, 2007 and on Blu-ray on November 6, 2007. The film was accompanied by the short One Man Band for its theatrical and home media releases. The film was dedicated to Joe Ranft, the film's co-director and co-writer, who died in a car crash during the film's production.\n" + //
                    "\n" + //
                    "The success of Cars launched a multimedia franchise and a series of two sequels produced by Pixar and two spin-offs produced by Disneytoon Studios, starting with Cars 2 (2011).";
    }
    return null;
}
    

    public static void main(String[] args) throws IOException, InterruptedException {
        //testing the multi-turn dialog
        // for (int i = 1; i <= 50; i++) {
    // try {
    //     String user_input = getQuestion(i);
    //     String chat_history = ""; // chat history if available
    //     String dialogptResult = dialogpt_medium_pred(user_input, chat_history);
    //     System.out.println("question "+ i + ": " + user_input);
    //     System.err.println("DialoGPT Medium Result: " + dialogptResult);
    // } catch (Exception e) {
    //     System.out.println("Error: " + e);
    // }

      
        // String question1 = "What are the symptome of COVID-19?";

        // String context1 = "COVID-19, also known as the coronavirus, is a viral respiratory illness that emerged in late 2019. It quickly spread across the globe, causing a pandemic. Common symptoms of COVID-19 include fever, cough, sore throat, fatigue, and shortness of breath. In severe cases, it can lead to pneumonia and organ failure.";


        

        // try {
        // // multi-turn chatbot
        // String user_input = "Have you ever traveled to another country?";
        // String chat_history = ""; // hat history if available
        // String dialogptResult = dialogpt_medium_pred(user_input, chat_history);
        // System.out.println("DialoGPT Medium Result: " + dialogptResult);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // try {
        // // question answering
        // String robertaResult = roberta_pred(question1, context1);
        // System.out.println("Roberta Result: " + robertaResult);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // try {
        // // question answering
        // String bartResult = bart_pred(question1, context1);
        // System.out.println("BART Result: " + bartResult);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        //
        // try {
        // String bartLargeResult = bart_large_cnn_sum(text);
        // System.out.println("BART Large Result: " + bartLargeResult);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        // try {
        // //summary
        // String t5BaseResult = t5_base_cnn_sum(text);
        // System.out.println("T5 Base Result: " + t5BaseResult);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    for (int i = 1; i <= 5; i++) {
    try {
      String text = getText(i);
      String t5BaseResult = t5_base_cnn_sum(text);
      System.out.println("0000000000000000000000000000000000000000000");
      System.out.println("TEXT "+ i + ": " + text);
      System.out.println("-------------------------------------------");
      System.out.println("T5 Base RESULTS: " + t5BaseResult);
      System.out.println("-------------------------------------------");
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
    }
    }
}
