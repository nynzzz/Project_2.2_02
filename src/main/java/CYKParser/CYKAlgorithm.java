package CYKParser;

import java.util.*;

public class CYKAlgorithm {

    public static boolean parseString(String inputString, List<String> nonTerminals, Map<String, List<String>> productions) {
        int n = inputString.length();
        boolean[][][] table = new boolean[n][n][nonTerminals.size()];

        // Initialize the diagonal cells of the table
        for (int i = 0; i < n; i++) {
            char symbol = inputString.charAt(i);
            for (int j = 0; j < nonTerminals.size(); j++) {
                String nonTerminal = nonTerminals.get(j);
                if (productions.get(nonTerminal).contains(Character.toString(symbol))) {
                    table[i][i][j] = true;
                }
            }
        }

        // Fill out the rest of the table
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                for (int k = i; k < j; k++) {
                    for (int p = 0; p < nonTerminals.size(); p++) {
                        String nonTerminal = nonTerminals.get(p);
                        List<String> rhs = productions.get(nonTerminal);
                        for (String rule : rhs) {
                            if (rule.length() == 2) {
                                String left = rule.substring(0, 1);
                                String right = rule.substring(1, 2);
                                int leftIndex = nonTerminals.indexOf(left);
                                int rightIndex = nonTerminals.indexOf(right);
                                if (table[i][k][leftIndex] && table[k+1][j][rightIndex]) {
                                    table[i][j][p] = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Check whether the start symbol can derive the entire input string
        String startSymbol = nonTerminals.get(0);
        int startSymbolIndex = nonTerminals.indexOf(startSymbol);
        return table[0][n-1][startSymbolIndex];
    }

    public static void main(String[] args) {
        List<String> nonTerminals = new ArrayList<>(Arrays.asList("S", "A", "B"));
        Map<String, List<String>> productions = new HashMap<>();
        productions.put("S", Arrays.asList("AB", "BA"));
        productions.put("A", Arrays.asList("a"));
        productions.put("B", Arrays.asList("b"));

        String inputString1 = "ab";
        String inputString2 = "baa";
        String inputString3 = "ba";

        boolean canParse1 = parseString(inputString1, nonTerminals, productions);
        boolean canParse2 = parseString(inputString2, nonTerminals, productions);
        boolean canParse3 = parseString(inputString3, nonTerminals, productions);

        System.out.println("Can parse \"" + inputString1 + "\": " + canParse1);
        System.out.println("Can parse \"" + inputString2 + "\": " + canParse2);
        System.out.println("Can parse \"" + inputString3 + "\": " + canParse3);
    }
}
