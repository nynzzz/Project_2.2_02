package chatbot.project22.CYKParser;

import java.util.*;

public class CNFConverter {
    private Map<String, List<List<String>>> productions;
    private static String startSymbol;

    public CNFConverter() {
        productions = new HashMap<>();
        startSymbol = "";
    }



    public static boolean isCNF(String str){


          /*
    algorithm:
    1. check length of output
    if length >2:
            not cnf
    if length =2:
                    if: all upper case -> it is cnf
                    else -> its not cnf
    if length = 1: cnf

     */
        String[] splitStr =  str.split("(?!^)");
        if( splitStr.length >2){
            return false;
        }
        else if(splitStr.length==2){
            if(Character.isLowerCase(str.charAt(0))|| Character.isLowerCase(str.charAt(1))){
              return false;
            }
            else return true;
        }else  return true;

    }


    // add new production
    public void addProduction(String nonTerminal, List<String> production) {
        List<List<String>> nonTerminalProductions = productions.getOrDefault(nonTerminal, new ArrayList<>());
        nonTerminalProductions.add(production);
        productions.put(nonTerminal, nonTerminalProductions);
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public void convertToCNF() {
// step 1 to 4:

        //step1 done in main.
        removeUselessSymbols();
        removeUnitProductions();
       // convertToChomskyNormalForm();
    }


    private void eliminateTerminals(){
        List<String> nonTerminals = new ArrayList<>(productions.keySet());
        for (String nonTerminal : nonTerminals) {
            List<List<String>> nonTerminalProductions = productions.get(nonTerminal);
            List<List<String>> unitProductions = new ArrayList<>();

        }

        }




    private void removeUnitProductions() {
        List<String> nonTerminals = new ArrayList<>(productions.keySet());
     //   List<String> terminals = new ArrayList<>(productions.);

        for (String nonTerminal : nonTerminals) {
            List<List<String>> nonTerminalProductions = productions.get(nonTerminal); // right side of productions
            List<List<String>> unitProductions = new ArrayList<>(); // new array for saving unit productions
            int index=0;
            for (List<String> production : nonTerminalProductions) { //consider each right side
                List<String>  production2=replaceTerminal(production);
                List<String>  production3=replaceTripleNonterminals(production2);



                productions.get(nonTerminal).remove(production);
                productions.get(nonTerminal).add(production3);
                List<List<String>> newProduction = new ArrayList<>(nonTerminalProductions);


                if (production.size() == 1 && !isTerminal(production.get(0))) { // e.g.: A -> B
                    unitProductions.add(production);
                    String unitNonTerminal = production.get(0);
                    if(unitNonTerminal.equals(nonTerminal)){
                        newProduction.add(new ArrayList<>(production));
                    }

                    List<List<String>> realResults = productions.get(production.get(0));
                    if(realResults !=null){
                        newProduction.addAll(realResults);
                    }
                    newProduction.remove(production);
                    System.out.println("removed    ");

                    productions.put(nonTerminal,newProduction);

                    System.out.println("real Result: "+ realResults);

                }/*
                else{
                    List<String> production2 = new ArrayList<>();
                    for(String str: production){
                        if(str.length()==1 && !isTerminal(str)){
                            String unitNonTerminal = str;
                            if(unitNonTerminal.equals(nonTerminal)){
                                newProduction.add(new ArrayList<>(production));

                            }
                            else{
                                production2.add(str); //keep original good strings
                            }
                            List<List<String>> realResult = productions.get(str);
                            if(realResult !=null){
                                newProduction.addAll(realResult);

                            }
                            newProduction.remove(production);
                            //production.remove(str);
                         //   newProduction.add(production2);
                            productions.put(nonTerminal,newProduction);


                        }
                    }
                }
                */
                index++;
            }
            System.out.println("unitProduction:    r3wrqr33333");
/*
            while (!unitProductions.isEmpty()) {
                List<String> unitProduction = unitProductions.remove(0); // pick up each unit Production
                String unitNonTerminal = unitProduction.get(0);
                System.out.println("unitProduction:    "+ unitProduction);
                System.out.println("unitNonterminal:    "+ unitNonTerminal);
                List<List<String>> unitNonTerminalProductions = productions.get(unitNonTerminal);
                if (unitNonTerminalProductions != null) {
                    for (List<String> unitNonTerminalProduction : unitNonTerminalProductions) {
                        if (!nonTerminalProductions.contains(unitNonTerminalProduction)) {
                            nonTerminalProductions.add(unitNonTerminalProduction);
                            unitProductions.add(unitNonTerminalProduction);
                        }
                    }
                }
            }
            */
        }
    }


    // step 2:
    private void removeUselessSymbols() {
        List<String> reachableSymbols = new ArrayList<>();
        List<String> productiveSymbols = new ArrayList<>();

        // Step 1: Find reachable symbols
        List<String> queue = new ArrayList<>();
        queue.add(startSymbol);
        reachableSymbols.add(startSymbol);

        while (!queue.isEmpty()) {
            String symbol = queue.remove(0);
            List<List<String>> symbolProductions = productions.get(symbol);

            if (symbolProductions != null) {
                for (List<String> production : symbolProductions) {
                    for (String prodSymbol : production) {
                        if (!reachableSymbols.contains(prodSymbol)) {
                            reachableSymbols.add(prodSymbol);
                            queue.add(prodSymbol);
                        }
                    }
                }
            }
        }


        // Step 2: Find productive symbols
        boolean changed = true;
        while (changed) {
            changed = false;

            for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
                String symbol = entry.getKey();
                List<List<String>> symbolProductions = entry.getValue();

                for (List<String> production : symbolProductions) {
                    boolean isProductive = true;

                    for (String prodSymbol : production) {
                        if (!productiveSymbols.contains(prodSymbol) && !isTerminal(prodSymbol)) {
                            isProductive = false;
                            break;
                        }
                    }

                    if (isProductive && !productiveSymbols.contains(symbol)) {
                        productiveSymbols.add(symbol);
                        changed = true;
                    }
                }
            }
        }

        // Step 3: Remove unreachable and non-productive symbols
        List<String> nonTerminals = new ArrayList<>(productions.keySet());
        for (String nonTerminal : nonTerminals) {
            if (!reachableSymbols.contains(nonTerminal) || !productiveSymbols.contains(nonTerminal)) {
                productions.remove(nonTerminal);
            }
        }
    }


    public static List<String> replaceTerminal(List<String> production) {
        List<String> prod = new ArrayList<>();

        for (String str : production) {
            if (str.length() > 1 && hasLowercase(str)) {
                String rule = "";

                for (char c : str.toCharArray()) {
                    if (Character.isLowerCase(c)) {
                        String nonTerminal = findNonTerminal(c);
                        if (nonTerminal != null) {
                            rule += nonTerminal;
                        } else {
                            // If no non-terminal leads to this lowercase, introduce a new non-terminal symbol
                            char newNonTerminal = putNonTerminal();
                            rule += newNonTerminal;
                        //    prod.add(newNonTerminal+"");
                        }
                    } else {
                        rule += c;
                    }
                }
          //          rule = "Igot lower case";
                prod.add(rule);
            } else {
                prod.add(str);
            }
        }

        return prod;
    }

    private static char putNonTerminal() {
        Random random = new Random();
        char letter = (char) (random.nextInt(26) + 'A');
        return letter;
    }


    public static boolean hasLowercase(String ch) {
        for (int i = 'a'; i <= 'z'; i++) {
            char lowercaseChar = (char) i;
            for (int j = 0; j < ch.length(); j++) {
                if (ch.charAt(j) == lowercaseChar) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String findNonTerminal(char c) {

        return null;

    }



    /*
    private void convertToChomskyNormalForm() {
        List<String> nonTerminals = new ArrayList<>(productions.keySet());

        for (String nonTerminal : nonTerminals) {
            List<List<String>> nonTerminalProductions = productions.get(nonTerminal);
            List<List<String>> newProductions = new ArrayList<>(nonTerminalProductions);

            for (List<String> production : nonTerminalProductions) {
                if (production.size() > 2) {
                    String currentNonTerminal = nonTerminal;
                    List<String> remainingSymbols = new ArrayList<>(production.subList(1, production.size()));
                    List<String> newProduction = new ArrayList<>();

                    while (remainingSymbols.size() > 2) {
                        String newNonTerminal = generateNewNonTerminal();

                        List<String> newProductionSegment = new ArrayList<>(remainingSymbols.subList(0, 2));
                        newProductionSegment.add(newNonTerminal);

                        newProductions.add(newProductionSegment);

                        remainingSymbols = new ArrayList<>(remainingSymbols.subList(1, remainingSymbols.size()));
                        currentNonTerminal = newNonTerminal;
                    }

                    newProduction.add(production.get(0));
                    newProduction.addAll(remainingSymbols);
                    newProductions.add(newProduction);

                    productions.put(nonTerminal, newProductions);
                }
            }
        }
    }
    */

    private String generateNewNonTerminal() {
        String newNonTerminal = "";
        for (char c = 'A'; c <= 'Z'; c++) {
            String symbol = String.valueOf(c);
            if (!productions.containsKey(symbol)) {
                newNonTerminal = symbol;
                break;
            }
        }
        return newNonTerminal;
    }

    private boolean isTerminal(String symbol) {
        return !productions.containsKey(symbol);
    }

    public void printProductions() {
        for (Map.Entry<String, List<List<String>>> entry : productions.entrySet()) {
            String nonTerminal = entry.getKey();
            List<List<String>> nonTerminalProductions = entry.getValue();
            for (List<String> production : nonTerminalProductions) {
                StringBuilder sb = new StringBuilder();
                sb.append(nonTerminal).append(" -> ");
                for (String symbol : production) {
                    sb.append(symbol).append(" ");
                }
                System.out.println(sb.toString().trim());
            }
        }
    }











    public static void main(String[] args) {
        CNFConverter converter = new CNFConverter();

        converter.setStartSymbol("S");

        // Add productions
        converter.addProduction("S", Arrays.asList("A", "B","C"));
        converter.addProduction("A",  Arrays.asList("bC","a"));
        converter.addProduction("B",  Arrays.asList("b","C"));// todo:
        converter.addProduction("C",  Arrays.asList("c"));
     //   converter.addProduction("S0", Arrays.asList("S"));

        // Set start symbol

        // Convert to CNF
        converter.convertToCNF();

        // Print productions
        converter.printProductions();



        System.out.println(isCNF("a"));
        System.out.println(isCNF("A"));
        System.out.println(isCNF("Abb"));
    }
    /*
    algorithm:
    1. check length of output
    if length >2:
            not cnf
    if length =2:
                    if: all upper case -> it is cnf
                    else -> its not cnf
    if length = 1: cnf

     */





}