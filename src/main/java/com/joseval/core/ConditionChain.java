package com.joseval.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConditionChain {

	private List<Condition> conditions;
	private List<Integer> logicJoins;

	public ConditionChain() {
	}

	public ConditionChain(List<Condition> conditions, List<Integer> logicJoins) {
		this.conditions = conditions;
		this.logicJoins = logicJoins;
	}
	
	
	public boolean overallEval(Map<String, Object> factMap) {
		boolean acumulateCondition=conditions.get(0).validate(factMap);
		
		for(int i=1;i<conditions.size();++i) {
			if(logicJoins.get(i-1).equals(1))acumulateCondition &= conditions.get(i).validate(factMap);
			else acumulateCondition |= conditions.get(i).validate(factMap);
			
			
		}
		return acumulateCondition;
	}
	

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public List<Integer> getLogicJoins() {
		return logicJoins;
	}

	public void setLogicJoins(List<Integer> logicJoins) {
		this.logicJoins = logicJoins;
	}

	public static class Builder {

		private List<Condition> conditions = new ArrayList<>();
		private List<Integer> logicJoins = new ArrayList<>();

		public Builder() {

		}

		public Builder condition(Condition condition) {
			conditions.add(condition);
			return this;
		}

		public Builder and() {
			logicJoins.add(1);
			return this;
		}

		public Builder or() {
			logicJoins.add(0);
			return this;
		}

		public ConditionChain build() {
			
			if(conditions.size()!=logicJoins.size()+1) {
				throw new RuntimeException("Number of conditions must be > 0 and = to joins (and, or) -1 current is "
						+conditions.size()+","+logicJoins.size());
			}
			
			return new ConditionChain(conditions,logicJoins);
		}

	}
}
