package project.multi.agent;

import java.util.ArrayList;
import java.util.Collection;

import org.jeasy.rules.api.Fact;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;

import project.multi.agent.rules.RuleScheduleOnSaturday;

public class DumyRuleEngine {

	// private List<IChessMove> realMoves;

	private Facts facts;

	private Rules rules;

	private DefaultRulesEngine rulesEngine;

	private String sentence;

	public DumyRuleEngine(String sentence) {
		this.sentence = sentence;

	}

	public void run() {

		// add facts
		facts = new Facts();

		// define rules
		RuleScheduleOnSaturday rule = new RuleScheduleOnSaturday();
		rules = new Rules();
		rules.register(rule);

		// fire rules on known facts
		rulesEngine = new DefaultRulesEngine();
		rulesEngine.fire(rules, facts);

	}

	private Collection<Facts> buildFacts(String sentence2) {
		// TODO Auto-generated method stub

		ArrayList<Facts> arrayList = new ArrayList<Facts>();
		String name;

		return arrayList;
	}

}
