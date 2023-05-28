package chatbot.project22.CYKParser;

import java.util.*;

public class CYKAlgorithm {

    public String  CYKAlgorithm(String input){

        List<String> nonTerminals = new ArrayList<>(Arrays.asList("S","Q","NP","WhPhrase","Verb","Noun","WeatherAdjective"));

        String[] question1 = { "How", "is", "the", "weather", "tomorrow" };

        Map<String, List<String>> productions = new HashMap<>();

        //  productions.put("A", Arrays.asList("a"));
        //   productions.put("B", Arrays.asList("b"));
        //    productions.put("C", Arrays.asList("e"));

        productions.put("S", Arrays.asList("Q"));
        productions.put("Q", Arrays.asList("WhPhrase", "Verb", "NP"));
        productions.put("NP", Arrays.asList("WeatherAdjective", "Noun"));
        productions.put("WhPhrase", Arrays.asList( "How"));
        productions.put("Verb", Arrays.asList("is"));
        productions.put("Noun", Arrays.asList("weather"));
        productions.put("WeatherAdjective", Arrays.asList("sunny"));

        //    String inputString1 = "abababab";
        String inputString1 = input;
        String inputString2 = "bae";
        String inputString3 = "bae";
        boolean canParse1 = parseString(inputString1, nonTerminals, productions);


        String ans = getAnswer(canParse1);




        //   boolean canParse2 = parseString(inputString2, nonTerminals, productions);
        //       boolean canParse3 = parseString(inputString3, nonTerminals, productions);

        System.out.println("Can parse \"" + inputString1 + "\": " + canParse1);
        return  ans;
    }

    public String getAnswer(boolean boo){
        String ans = "The weather will be sunny";
        String noAns = "Can not find answer";
        if(boo) return ans;
        else return noAns;

    }


    public static boolean containsChar(String inputStr, char c){
        for(int i=0; i<inputStr.length();i++){
            if(inputStr.charAt(i)==c)
                return true;
        }

        return false;
    }

    public static boolean parseString(String inputString, List<String> nonTerminals, Map<String, List<String>> productions) {
        int n = inputString.length();
        int r = nonTerminals.size();
        boolean[][][] table = new boolean[n][n][r];

        // Initialize the diagonal cells of the table
        for (int i = 0; i < n; i++) {
            String terminal = inputString.charAt(i)+"";
            char terminal2 = inputString.charAt(i);
            for (int j = 0; j < r; j++) {
                String nonTerminal = nonTerminals.get(j);
          //      System.out.println("aa))))     "+ productions.get(nonTerminal));
        //        System.out.println("bb))))     "+ terminal);
                if (containsChar(productions.get(nonTerminal).toString(),terminal2)) {

                    table[i][i][j] = true;
       //                     System.out.println("True001  True001");
                }
            }
        }

        // Fill out the rest of the table
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                for (int k = 0; k < productions.size(); k++) {
                    for (int l = i; l < j; l++) {
                //        System.out.println("k:     "+ k);
                   //     System.out.println("Poductions:     "+ productions);
                        String []keyArray =productions.keySet().toArray(new String[0]);
                        String key = keyArray[k];

                  //      System.out.println("aaa:     "+ productions.get(key).toString());
                        List<String> production = productions.get(key);
                        for(String str:production){
                            int index = production.indexOf(str);
                           // System.out.println("Pro:       "+str+ "   length:  "+ (str.length()));


                            if (str.length() == 2) {
                                            System.out.println("222     2");
                                for (int m = 0; m < productions.size(); m++) {
                                    //    System.out.println("Production Size:  "+  productions.size()+ "  Table size:  "+ table[i][l].length);

                                         System.out.println("True or false:  table[i][l][k] "+table[i][l][k] );
                                           System.out.println("True or false:  table[l + 1][j][m] "+table[l + 1][j][m]);


                                    String combined = productions.get(key).toString() + productions.get(keyArray[m]).toString();



                                    if (table[i][l][k] || table[l + 1][j][m]) { // todo: put && back

                                      //  String combined = productions.get(key).toString() + productions.get(keyArray[m]).toString();
                                        System.out.println("???     "+ productions.get(key).toString());
                                        System.out.println("???comnbined     "+combined);
                                        System.out.println("???Str     "+str);

                                        System.out.println("Contain: "+ containStr(combined,str)     );
                                        if (containStr(combined,inputString)) {
                                            return true;
                                          //  table[i][j][index] = true; //todo: fix
                                        }
                                    }
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
    public static boolean containStr(String input, String check){
        if(input.length()<check.length()) return false;



        for(int i=0; i< input.length(); i++){
                int count =0;
            for(int j=0; j< check.length();j++){
                if(i+j>=input.length()) return false;
                if(input.charAt(i+j)==check.charAt(j)){
                    count ++;
                }

            }
            if (count == check.length()){
                return true;
            }
            else count =0;
        }


        return false;
    }

    private static int getIndex(List<String> productions, String production) {
        for (int i = 0; i < productions.size(); i++) {
            if (productions.get(i).equals(production)) {
                return i;
            }
        }
        return -1;
    }






    public static void main(String[] args) {
        List<String> nonTerminals = new ArrayList<>(Arrays.asList("S","Q","NP","WhPhrase","Verb","Noun","WeatherAdjective"));

        String[] question1 = { "How", "is", "the", "weather", "tomorrow" };

        Map<String, List<String>> productions = new HashMap<>();

      //  productions.put("A", Arrays.asList("a"));
     //   productions.put("B", Arrays.asList("b"));
    //    productions.put("C", Arrays.asList("e"));

        productions.put("S", Arrays.asList("Q"));
        productions.put("Q", Arrays.asList("WhPhrase", "Verb", "NP"));
        productions.put("NP", Arrays.asList("WeatherAdjective", "Noun"));
        productions.put("WhPhrase", Arrays.asList( "How"));
        productions.put("Verb", Arrays.asList("is"));
        productions.put("Noun", Arrays.asList("weather"));
        productions.put("WeatherAdjective", Arrays.asList("sunny"));

    //    String inputString1 = "abababab";
        String inputString1 = "Noun";
        String inputString2 = "bae";
        String inputString3 = "bae";
        boolean canParse1 = parseString(inputString1, nonTerminals, productions);





        //   boolean canParse2 = parseString(inputString2, nonTerminals, productions);
        //       boolean canParse3 = parseString(inputString3, nonTerminals, productions);

        System.out.println("Can parse \"" + inputString1 + "\": " + canParse1);
        //    System.out.println("Can parse \"" + inputString2 + "\": " + canParse2);
        //    System.out.println("Can parse \"" + inputString3 + "\": " + canParse3);
    }



}
