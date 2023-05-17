package chatbot.project22.CFG;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Parser {
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

        // Check if the current node has already been matched with a terminal token
        if (!node.getValue().isEmpty()) {
            System.out.println("Node " + node.getSymbol() + " already matched with value: " + node.getValue());
            matchedProductions.add(node.getValue());
            return matchedProductions;
        }

        // Find rules with the same non-terminal
        for (Rule rule : ruleList) {
            if (rule.getNonTerminal().equals(node.getSymbol())) {
                for (String production : rule.getProductions()) {
                    System.out.println("Checking production: " + production);
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
                    System.out.println("Failed to match token: " + token + " with input: " + input);
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
                    if (keyIndex + 1 < actionKey.size()) {  // Check if there's a token after '*'
                        String nextKeyToken = actionKey.get(keyIndex + 1);
                        while (sequenceIndex < sequence.size() && !sequence.get(sequenceIndex).equals(nextKeyToken)) {
                            sequenceIndex++;
                        }
                    } else {
                        sequenceIndex = sequence.size();  // If '*' is the last token in key, skip to the end of sequence
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

        // TODO: Replace with your file path
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
        List<String> parseResult = parser.parse("How is the weather in New York tomorrow?");
        System.out.println("Parse result: " + parseResult);
        String response = parser.getMatchingActionResponse(parseResult);
        if (response != null) {
            System.out.println("Response: " + response);
        } else {
            System.out.println("Response: I have no idea :(");
        }
    }

}

