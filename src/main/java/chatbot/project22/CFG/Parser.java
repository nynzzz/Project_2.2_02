package chatbot.project22.CFG;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Parser {
    private List<Rule> ruleList;
    Map<List<String>, String> actionMap;
    private Node root;

    public Parser(List<Rule> ruleList, Map<List<String>, String> actionMap) {
        this.ruleList = ruleList;
        this.actionMap = actionMap;
    }

    public List<String> parse(String input) {
        root = new Node("<s>");
        List<String> tokens =  preprocessInput(input);
        List<String> matchedProductions = parse(root, tokens);
        // Return the root node if there are no remaining tokens, or if the remaining tokens can be parsed by the root
        return matchedProductions;
    }

    private List<String> parse(Node node, List<String> input) {
        List<String> matchedProductions = new ArrayList<>();

        if (input.isEmpty()) {
            return matchedProductions;
        }

        System.out.println("Parsing node: " + node.getSymbol() + " with input: " + input);

        // check if the current node has already been matched with a terminal token
        if (!node.getValue().isEmpty()) {
            // System.out.println("Node " + node.getSymbol() + " already matched with value: " + node.getValue());
            matchedProductions.add(node.getValue());
            return matchedProductions;
        }

        //Find rules with the same non-terminal
        for (Rule rule : ruleList) {
            if (rule.getNonTerminal().equals(node.getSymbol())) {
                for (String production : rule.getProductions()) {
                    // System.out.println("Checking production: " + production);
                    List<String> tokens = Arrays.asList(production.split(" "));
                    List<String> matched = matchProduction(node, tokens, input);
                    if (!matched.isEmpty()) {
                        System.out.println("Matched production: " + production + " with input: " + input);
                        matchedProductions.add(node.getSymbol());
                        matchedProductions.addAll(matched);
                        return matchedProductions;
                    }
                }
            }
        }
        return matchedProductions;
    }

    private List<String> matchProduction(Node node, List<String> tokens, List<String> input) {
        int inputIndex = 0;
        List<String> matchedTokens = new ArrayList<>();
        for (String token : tokens) {
            System.out.println("Matching token: " + token + " with input: " + input);
            if (token.startsWith("<") && token.endsWith(">")) {
                Node child = new Node(token);
                node.addChild(child);
                List<String> subInput = input.subList(inputIndex, input.size());
                List<String> matched = parse(child, subInput);
                if (!matched.isEmpty()) {
                    matchedTokens.addAll(matched);
                    inputIndex += countWords(child);
                } else {
                    System.out.println("Failed to match token: " + token + " with input: " + input);
                    node.removeChild(child);
                    return new ArrayList<>();
                }
            } else {
                if (inputIndex < input.size() && token.equalsIgnoreCase(input.get(inputIndex))) {
                    Node child = new Node(token);
                    child.addValue(token);
                    node.addChild(child);
                    matchedTokens.add(token);
                    inputIndex++;
                } else {
                    // System.out.println("FAIL");
                    return new ArrayList<>();
                }
            }
        }
        return matchedTokens; // Return the matched tokens
    }


    private int countWords(Node node) {
        if (node.getChildren().isEmpty()) {
            return node.getSymbol().split(" ").length;
        }
        int count = 0;
        for (Node child : node.getChildren()) {
            count += countWords(child);
        }
        return count;
    }


    private List<String> preprocessInput(String input) {
        // Remove punctuation and convert to lowercase
        input = input.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        // Split the string into words
        return new ArrayList<>(Arrays.asList(input.split("\\s+")));
    }

    public String getMatchingActionResponse(List<String> sequence) {
        for (Map.Entry<List<String>, String> entry : actionMap.entrySet()) {
            List<String> actionKey = entry.getKey();
            int sequenceIndex = 0;
            int keyIndex = 0;
            while (sequenceIndex < sequence.size() && keyIndex < actionKey.size()) {
                if (actionKey.get(keyIndex).equals("*")) {
                    // Check if there's a token after '*'
                    if (keyIndex + 1 < actionKey.size()) {
                        String nextKeyToken = actionKey.get(keyIndex + 1);
                        while (sequenceIndex < sequence.size() && !sequence.get(sequenceIndex).equals(nextKeyToken)) {
                            sequenceIndex++;
                        }
                    } else { // If '*' is the last token in key, skip to the end of sequence
                        sequenceIndex = sequence.size();
                    }
                    keyIndex++;
                } else if (actionKey.get(keyIndex).equalsIgnoreCase(sequence.get(sequenceIndex))) {
                    sequenceIndex++;
                    keyIndex++;
                } else {
                    break;
                }
            }

            if (keyIndex == actionKey.size()) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static void main(String[] args) {
    List<Rule> ruleList = new ArrayList<>();
    Map<List<String>, String> actionMap = new HashMap<>();

    // Replace with your file path
    String filePath = "src/main/resources/chatbot/project22/CFG/CFG_rules.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            actionMap = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" -> ");
                if (parts[0].equals("Rule")) {
                    ruleList.add(new Rule(parts[1], Arrays.asList(parts[2].split(" \\| "))));
                } else if (parts[0].equals("Action")) {
                    String[] actionParts = parts[2].split(" : ");
                    List<String> key = Arrays.asList(actionParts[0].split(" "));
                    String value = actionParts[1];
                    actionMap.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // print ruleList
        System.out.println("Rules:");
        for (Rule rule : ruleList) {
            System.out.println(rule.getNonTerminal() + " -> " + rule.getProductions());
            
        }
        // print actionList
        System.out.println("Actions:");
        for (Map.Entry<List<String>, String> entry : actionMap.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        Parser parser = new Parser(ruleList, actionMap);
        int count = 0;
        ArrayList<String> sentences = new ArrayList<>();
        sentences.add("How is the weather in Berlin today?");
        sentences.add("How is the weather in New York tomorrow?");
        sentences.add("How is the weather in Berlin? ");
        sentences.add("How is the weather in Berlin tomorrow?");
        sentences.add("How is the weather in New York today? ");
        sentences.add("How is the weather in Berlin yesterday?");
        sentences.add("How is the weather in Brussels tomorrow?");
        sentences.add("How is the weather in Brussels today?");
        sentences.add("How is the weather in Brussels yesterday?");
        sentences.add("My mother is in New York today. What is the weather?");
        sentences.add("what is the weather in Berlin and Brussels today?");
        sentences.add("Berlin today What weather is the in?");
        sentences.add("My best friend is in Berlin today. What is the weather?");

        

        for(int i = 0; i<sentences.size(); i++){
            List<String> parseResult = parser.parse(sentences.get(i));
            System.out.println("Parse result: " + parseResult);
            String response = parser.getMatchingActionResponse(parseResult);
            if (response != null) {
                System.out.println("Response: " + response);
                count++;
            } else {
                System.out.println("Response: I have no idea :(");
            }
        }
        System.out.println("Accuracy: " + count + "/" + sentences.size());
        // List<String> parseResult = parser.parse("How is the weather in Amsterdam overmorrow?");
        // System.out.println("Parse result: " + parseResult);
        // String response = parser.getMatchingActionResponse(parseResult);
        // if (response != null) {
        //     System.out.println("Response: " + response);

        // } else {
        //     System.out.println("Response: I have no idea :(");
        // }
    }





    //A method that asks for missing rules and actions if not all rules and actions are provided in the input.
    public void checkMissingRulesAndActions(List<String> input) {
        Set<String> missingRules = new HashSet<>();
        Set<String> missingActions = new HashSet<>();

        // Check missing rules
        for (Rule rule : ruleList) {
            boolean found = false;
            for (String production : rule.getProductions()) {
                List<String> tokens = Arrays.asList(production.split(" "));
                if (containsTokens(input, tokens)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                missingRules.add(rule.getNonTerminal());
            }
        }

        // Check missing actions
        for (Map.Entry<List<String>, String> entry : actionMap.entrySet()) {
            List<String> actionKey = entry.getKey();
            if (!containsTokens(input, actionKey)) {
                missingActions.add(String.join(" ", actionKey));
            }
        }

        // Ask input the missing rule
        if (!missingRules.isEmpty()) {
            //save(input);
            System.out.println("Please input the values for the missing rules:");
            for (String missingRule : missingRules) {
                System.out.println(missingRule);
            }
        }

        // Ask input the missing action
        if (!missingActions.isEmpty()) {
            //save(input);
            System.out.println("Please input the values for the missing actions:");
            for (String missingAction : missingActions) {
                System.out.println(missingAction);
            }
        }

    }

    private boolean containsTokens(List<String> input, List<String> tokens) {
        int inputIndex = 0;
        int tokenIndex = 0;
        while (inputIndex < input.size() && tokenIndex < tokens.size()) {
            if (tokens.get(tokenIndex).equals("*")) {
                // Check if there's a token after '*'
                if (tokenIndex + 1 < tokens.size()) {
                    String nextToken = tokens.get(tokenIndex + 1);
                    while (inputIndex < input.size() && !input.get(inputIndex).equals(nextToken)) {
                        inputIndex++;
                    }
                } else { // If '*' is the last token, skip to the end of input
                    inputIndex = input.size();
                }
                tokenIndex++;
            } else if (tokens.get(tokenIndex).equalsIgnoreCase(input.get(inputIndex))) {
                inputIndex++;
                tokenIndex++;
            } else {
                return false;
            }
        }
        return tokenIndex == tokens.size();
    }





}

