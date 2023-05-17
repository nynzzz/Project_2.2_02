package chatbot.project22.CFG;

import java.util.ArrayList;
import java.util.List;

class Node {
    private String symbol;
    private List<Node> children = new ArrayList<>();
    private String value = "";
    private Node parent;

    public Node(String symbol) {
        this.symbol = symbol;
    }

    public void addChild(Node child) {
        children.add(child);
        child.setParent(this);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public String getSymbol() {
        return symbol;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addValue(String value) {
        this.value += (this.value.isEmpty() ? "" : " ") + value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        String output = symbol;
        if (!children.isEmpty()) {
            output += "\n";
            for (Node child : children) {
                output += child.toString().replaceAll("(?m)^", " ");
            }
        } else {
            output += " " + value;
        }
        return output;
    }

    public void removeChild(Node child) {
        children.remove(child);
        child.setParent(null);
    }
}

