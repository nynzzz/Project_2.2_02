package project.multi.agent.rules;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

import project.multi.agent.Token;

/**
 * 
 * 
 * Action <SCHEDULE> * <DAY> Saturday ---> There are no lectures on Saturday
 * 
 * 
 * 
 */

@Rule(name = "Lecture on Monday", description = "this rules is triggered when is Monday ")
public class RuleScheduleOnMonday {

	@Condition
	public boolean when(@Fact("DAY") String day, @Fact("SCHEDULE") String schedule,@Fact("TIME") String time) {

		if (day.equals(" Monday ") && schedule != null &&  time.equals(" 9 "))
			return true;
		else
			return false;
	}

	@Action
	public void result() {
		System.err.println("We start the week with math");
	}

}