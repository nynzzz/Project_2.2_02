package chatbot.project22.CYKParser;

import java.util.*;

public class CYKAlgorithm {

    public static boolean parseString(String inputString, List<String> nonTerminals, Map<String, List<String>> productions) {
        int n = inputString.length();
        int r = nonTerminals.size();
        boolean[][][] table = new boolean[n][n][r];

        // Initialize the diagonal cells of the table
        for (int i = 0; i < n; i++) {
            String terminal = inputString.charAt(i)+"";
            for (int j = 0; j < r; j++) {
                String nonTerminal = nonTerminals.get(j);
                if (productions.get(nonTerminal).contains(terminal)) {
                    table[i][i][j] = true;
                    //        System.out.println("True001  True001");
                }
            }
        }

        // Fill out the rest of the table
        for(int len =2; len<=n; len++){
            for(int i=0; i<=n-len;i++){
                int j=i +len -1;
                for(int k=i; k<=j-1;k++){
                    for(String nonTerminal:nonTerminals){
                        List<String> rules = productions.get(nonTerminal);
                        for(String rule: rules){
                            //    System.out.println("Rule length:                        "+ rule.length());

                            if(rule.length()==1&& rule.substring(0,1).equals(inputString.substring(j,j+1))){
                                table[i][j][nonTerminals.indexOf(nonTerminal)] = true;
                            }



                            if(rule.length()==2){
                                System.out.println("Lennnght:  --  " +rule.charAt(0)+"     "+ rule.charAt(1));
                                String firstSymbol = rule.substring(0, 1);
                                String secondSymbol = rule.substring(1, 2);
                                int firstIndex = nonTerminals.indexOf(firstSymbol);
                                int secondIndex = nonTerminals.indexOf(secondSymbol);

                                System.out.println("FirstIndex:  "+ firstIndex);
                                System.out.println("SecondIndex:  "+ secondIndex);

                                if(table[i][k][firstIndex]&&table[k+1][j][secondIndex]){
                                    table[i][j][nonTerminals.indexOf(nonTerminal)] = true;
                                    //                            System.out.println("True 000000222");
                                }
                            }
                        }
                    }
                }
            }



        }
        String startSymbol = nonTerminals.get(0);
        int startSymbolIndex = nonTerminals.indexOf(startSymbol);
        System.out.println("Result:       "+table[0][n-1][nonTerminals.indexOf(startSymbol)] );
        return table[0][n-1][nonTerminals.indexOf(startSymbol)];

    }







    public static void main(String[] args) {
        List<String> nonTerminals = new ArrayList<>(Arrays.asList("S", "A", "B"));
        Map<String, List<String>> productions = new HashMap<>();
        productions.put("S", Arrays.asList("AB", "BA"));
        productions.put("A", Arrays.asList("a"));
        productions.put("B", Arrays.asList("b"));
        productions.put("C", Arrays.asList("e"));

        String inputString1 = "abababab";
        String inputString2 = "baa";
        String inputString3 = "bae";
        boolean canParse1 = parseString(inputString1, nonTerminals, productions);
        //   boolean canParse2 = parseString(inputString2, nonTerminals, productions);
        //       boolean canParse3 = parseString(inputString3, nonTerminals, productions);

        System.out.println("Can parse \"" + inputString1 + "\": " + canParse1);
        //    System.out.println("Can parse \"" + inputString2 + "\": " + canParse2);
        //    System.out.println("Can parse \"" + inputString3 + "\": " + canParse3);
    }



}
