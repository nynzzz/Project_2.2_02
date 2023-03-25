package project.multi.agent;

import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;
import org.junit.jupiter.api.Test;

import project.multi.agent.rules.RuleScheduleOnMonday;
import project.multi.agent.rules.RuleScheduleOnSaturday;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Unit test for simple App.
 */
class AppTestFromYAML {
	/**
	 * Rigorous Test.
	 */
	@Test
	void testApp() {

		// "Which lectures are there on Saturday "; --> I have no idea

		String sentence; // --> There are no lectures on Saturday
		sentence = "Which lectures are there on Saturday at 12 ";
		sentence = "Which lectures are there on Monday at 9 "; // --> There are no lectures on Saturday

		// step 1 split in array :
		// [Which, lectures, are, there, on, Saturday, at, 11]
		// step 2
		// found token
		// [Which, lectures, are, there, on, {DAY,Saturday}, at, 11]
		// [Which, lectures, are, there, on, {DAY,Saturday}, at, {TIME,11}]
		// [Which, lectures, are, there, on, {DAY,Saturday}, at, {TIME,11}]
		// [Which, lectures, are, there, {TIMEEXPRESSION,{DAY,Saturday}, at, {TIME,11}
		// }]

		// Which lectures are there on Saturday at 11
		// Which lectures are there on DAY at 11
		// Which lectures are there on DAY at TIME
		// Which lectures are there TIMEXPRESSION
		// Which lectures are there TIMEXPRESSION
		// <SCHEDULE>
		// <ACTION>
		// <S

		ArrayList<Token> list = new ArrayList<Token>();

		Lexer lexer = new Lexer(sentence);

		Token e;

		do {
			e = lexer.getNextToken();
			list.add(e);
			System.out.println(e);
		} while (e.getType() != TokenType.ERROR);

		// create FACTS

		// add facts
		Facts facts = new Facts();
		for (Token token : list) {

			Fact<String> fact = new Fact<String>(token.getType().name(), token.getLexeme());
			facts.add(fact);
		}

		// define rules
		Rules rules = new Rules();

		MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader());
		try {

			ClassLoader classLoader = getClass().getClassLoader();
			String fileName = "rules.yaml";
			URL resourceUrl = classLoader.getResource(fileName);
			FileReader rulesDescriptor = new FileReader(resourceUrl.getFile());
			rules = ruleFactory.createRules(rulesDescriptor);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// fire rules on known facts
		DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
		rulesEngine.fire(rules, facts);

		/*
		 * String expected="There are no lectures on Saturday";
		 * 
		 * App app =new App();
		 * 
		 * String actual = app.parse(sentence);
		 * 
		 * assertEquals(expected, actual);
		 * 
		 */

		/**
		 * 
		 * Action <DAY> Monday <TIME> 11 On Monday noon we have Theoratical Computer
		 * Science
		 * 
		 * 1. Monday at 11 ----> On Monday noon we have Theoratical Computer Science 2.
		 * Monday alle 11 ----> On Monday noon we have Theoratical Computer Science 3.
		 * Monday fuck 11 ----> On Monday noon we have Theoratical Computer Science
		 * 
		 * 
		 */
	}

}
