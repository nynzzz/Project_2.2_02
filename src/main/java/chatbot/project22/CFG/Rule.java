package chatbot.project22.CFG;

import java.util.List;

class Rule {
    private String nonTerminal;
    private List<String> productions;

    public Rule(String nonTerminal, List<String> productions) {
        this.nonTerminal = nonTerminal;
        this.productions = productions;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public List<String> getProductions() {
        return productions;
    }
}
