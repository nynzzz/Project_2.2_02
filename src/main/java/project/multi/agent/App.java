package project.multi.agent;

import java.util.Arrays;

/**
 * Hello world!
 */
public final class App {
        App() {
        }

        public String parse(String sentence) {
                // "Which lectures are there on Saturday at 11"; -->>> Array Simbol ...
                // ["Which","lectures","are"]

                return "There are no lectures on Saturday";
        }

        private String[] splitInput(String sentence) {
                // TODO Auto-generated method stub
                return sentence.split(" ");

        }

        /**
         * Says hello to the world.
         * 
         * @param args The arguments of the program.
         */
        public static void main(String[] args) {

                // "Which lectures are there on Saturday at 11";

                Symbol S = new Symbol("S", Arrays.asList(
                                Arrays.asList(new Symbol("ACTION")),
                                Arrays.asList(new Symbol("LOCATION"))));

                Symbol ACTION = new Symbol("ACTION", Arrays.asList(
                                Arrays.asList(new Symbol("LOCATION")),
                                Arrays.asList(new Symbol("SCHEDULE"))));

                Symbol SCHEDULE = new Symbol("SCHEDULE", Arrays.asList(
                                Arrays.asList(new Symbol("TIMEEXPRESSION"), new Symbol("which"),
                                                new Symbol("lectures")),
                                Arrays.asList(new Symbol("Which"), new Symbol("lectures"), new Symbol("are"),
                                                new Symbol("there"), new Symbol("TIMEEXPRESSION"))));

                Symbol TIMEEXPRESSION = new Symbol("TIMEEXPRESSION", Arrays.asList(
                                Arrays.asList(new Symbol("on"), new Symbol("DAY"), new Symbol("at"),
                                                new Symbol("TIME")),
                                Arrays.asList(new Symbol("at"), new Symbol("TIME"), new Symbol("on"),
                                                new Symbol("DAY"))));

                Symbol TIME = new Symbol("TIME", Arrays.asList(
                                Arrays.asList(new Symbol("9")),
                                Arrays.asList(new Symbol("12"))));

                Symbol LOCATION = new Symbol("LOCATION", Arrays.asList(
                                Arrays.asList(new Symbol("Where"), new Symbol("is"), new Symbol("ROOM")),
                                Arrays.asList(new Symbol("How"), new Symbol("do"), new Symbol("PRO"), new Symbol("get"),
                                                new Symbol("to"), new Symbol("ROOM")),
                                Arrays.asList(new Symbol("Where"), new Symbol("is"), new Symbol("ROOM"),
                                                new Symbol("located"))));

                Symbol PRO = new Symbol("PRO", Arrays.asList(
                                Arrays.asList(new Symbol("I")),
                                Arrays.asList(new Symbol("you")),
                                Arrays.asList(new Symbol("he")),
                                Arrays.asList(new Symbol("she"))));

                Symbol ROOM = new Symbol("ROOM", Arrays.asList(
                                Arrays.asList(new Symbol("DeepSpace")),
                                Arrays.asList(new Symbol("SpaceBox"))));

                Symbol DAY = new Symbol("DAY", Arrays.asList(
                                Arrays.asList(new Symbol("Monday")),
                                Arrays.asList(new Symbol("Tuesday")),
                                Arrays.asList(new Symbol("Wednesday")),
                                Arrays.asList(new Symbol("Thursday")),
                                Arrays.asList(new Symbol("Friday")),
                                Arrays.asList(new Symbol("Saturday")),
                                Arrays.asList(new Symbol("Sunday"))));

                Symbol ACTION1 = new Symbol("ACTION", Arrays.asList(
                                Arrays.asList(new Symbol("*"), new Symbol("DAY"), new Symbol("Saturday"),
                                                new Symbol("There"), new Symbol("are"), new Symbol("no"),
                                                new Symbol("lectures"), new Symbol("on"), new Symbol("Saturday")),
                                Arrays.asList(new Symbol("*"), new Symbol("DAY"), new Symbol("Monday"),
                                                new Symbol("TIME"), new Symbol("We"), new Symbol("start"),
                                                new Symbol("the"), new Symbol("week"), new Symbol("with"),
                                                new Symbol("math")),
                                Arrays.asList(new Symbol("*"), new Symbol("DAY"), new Symbol("Monday"),
                                                new Symbol("TIME"), new Symbol("On"), new Symbol("Monday"),
                                                new Symbol("noon"), new Symbol("we"), new Symbol("have"),
                                                new Symbol("Theoretical"), new Symbol("Computer"))));

                System.out.println(TIME.toStringLong());
                System.out.println(ROOM.toStringLong());
                System.out.println(ACTION1.toStringLong());
        }

}
