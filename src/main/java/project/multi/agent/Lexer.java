package project.multi.agent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private String input;

    public Lexer(String input) {
        this.input = input;
    }

    public Token getNextToken() {

        // Match each token type in turn
        Matcher matcher;
        if ((matcher = matchRule("<S>", "<ACTION>")) != null) {
            return new Token(TokenType.S, matcher.group(1));
        } else if ((matcher = matchRule("<ACTION>", "<LOCATION>|<SCHEDULE>")) != null) {
            return new Token(TokenType.ACTION, matcher.group(1));
        } else if ((matcher = matchRule("<SCHEDULE>", "Which lectures are there <TIMEEXPRESSION>")) != null) {
            return new Token(TokenType.SCHEDULE, matcher.group(1));
        } else if ((matcher = matchRule("<TIMEEXPRESSION>", " on <DAY> at <TIME> | at <TIME> on <DAY> ")) != null) {
            return new Token(TokenType.TIMEEXPRESSION, matcher.group(1));
        } else if ((matcher = matchRule("<TIME>", " 9 | 12 ")) != null) {
            return new Token(TokenType.TIME, matcher.group(1));
        } else if ((matcher = matchRule("<LOCATION>",
                "Where is <ROOM>|How do <PRO> get to <ROOM>|Where is <ROOM> located")) != null) {
            return new Token(TokenType.LOCATION, matcher.group(1));
        } else if ((matcher = matchRule("<PRO>", " I | you | he | she ")) != null) {
            return new Token(TokenType.PRO, matcher.group(1));
        } else if ((matcher = matchRule("<ROOM>", "DeepSpace|SpaceBox")) != null) {
            return new Token(TokenType.ROOM, matcher.group(1));
        } else if ((matcher = matchRule("<DAY>",
                " Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday ")) != null) {
            return new Token(TokenType.DAY, matcher.group(1));
        } else {
            // No match, return an error token
            return new Token(TokenType.ERROR, String.valueOf(input));
        }
    }

    private Matcher matchRule(String rule, String pattern) {

        String regex = "(" + pattern + ")";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);

        if (m.find()) {

            System.out.println(input.substring(m.start(), m.end()));
            input = input.substring(0, m.start()) + " " + rule + " " + input.substring(m.end());
            System.out.println(input);
            return m;
        }

        return null;
    }
}
