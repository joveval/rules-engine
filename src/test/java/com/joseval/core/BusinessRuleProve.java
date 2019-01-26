package com.joseval.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.joseval.core.Condition.Eq;

class BusinessRuleProve {

	@Test
	void test() {
		
		
		ConditionChain businessRule = new ConditionChain.Builder()
													.condition(new Eq("intent","assur.default"))
													.build();
		
		Map<String, Object> factualMap = new HashMap<>();
		factualMap.put("val1", 11);
		factualMap.put("val2", 0);
		factualMap.put("intent", null);
		
		assertEquals(false, businessRule.overallEval(factualMap),"Not valid");
		
		
		/*factualMap.replace("val1", 11);
		factualMap.replace("val2", 7);
		
		assertEquals(true, businessRule.overallEval(factualMap),"Not valid");
		
		
		
		factualMap.replace("val1", 10);
		factualMap.replace("val2", 20);
		
		assertEquals(false, businessRule.overallEval(factualMap),"Not valid");
		
		
		
		factualMap.replace("val1", 11);
		factualMap.replace("val2", 20);
		
		assertEquals(false, businessRule.overallEval(factualMap),"Not valid");
		*/
		//fail("Not yet implemented");
	}

}
