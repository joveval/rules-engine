package com.joseval.core;
import static com.joseval.core.RulesManager.Operator.*;

import java.util.HashMap;
import java.util.Map;

import com.joseval.core.RulesManager.Operator;

public class Main {
	public static void main(String[] args) {
		Map<String, Object> factParams = new HashMap<>();
		factParams.put("a", 1);
		factParams.put("b", 0);
		factParams.put("c", 2);
		factParams.put("d", 3);
		factParams.put("e", 11);
		Operator operator = And(
								Equals("a", 1),
								NotNull("c"),
								Or(
									Equals("d",20),
									Not(LessOrEqualThan("e", 10))
								)
							   );
		boolean result = operator.runValidation(factParams);
		System.out.println(operator.getStringResult()+" = "+result);
	}
}
