package com.joseval.core;

import static org.apache.commons.lang3.ObjectUtils.allNotNull;

import java.util.Map;

public class Condition {

	private String paramName;
	private Object comparitionValue;

	public Condition() {
	}

	public Condition(String paramName, Object comparitionValue) {
		// super();
		this.paramName = paramName;
		this.comparitionValue = comparitionValue;
	}

	public boolean validate(Map<String, Object> factMap) {
		return true;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public Object getComparitionValue() {
		return comparitionValue;
	}

	public void setComparitionValue(Object comparitionValue) {
		this.comparitionValue = comparitionValue;
	}

	public static class LThan extends Condition {
		
		public LThan(String param,Object value) {
			super(param,value);
		}

		@Override
		public boolean validate(Map<String, Object> factMap) {
			// TODO Auto-generated method stub
			if (!allNotNull(super.paramName, super.comparitionValue, factMap))
				return false;
			if (!factMap.containsKey(super.paramName))
				return false;

			if (!factMap.get(super.paramName).getClass().equals(Integer.class)
					|| !super.comparitionValue.getClass().equals(Integer.class))
				return false;

			Integer op1 = (Integer) factMap.get(super.paramName);
			Integer op2 = (Integer) super.comparitionValue;

			return op1.compareTo(op2) < 0;
		}

	}

	public static class Eq extends Condition {
		
		public Eq(String param,Object value) {
			super(param,value);
		}
		
		@Override
		public boolean validate(Map<String, Object> factMap) {
			// TODO Auto-generated method stub
			if (!allNotNull(super.paramName, super.comparitionValue, factMap))
				return false;
			if (!factMap.containsKey(super.paramName))
				return false;

			if(factMap.get(super.paramName)==null)return false;
			
			
			if(!factMap.get(super.paramName).getClass().equals(super.comparitionValue.getClass())) return false;
			
			boolean comparition = false;
			
			switch(super.comparitionValue.getClass().getName()) {
			
			case "java.lang.Integer":
				
				Integer op1 = (Integer) factMap.get(super.paramName);
				Integer op2 = (Integer) super.comparitionValue;
				comparition = op1.compareTo(op2)==0;
				break;
			case "java.lang.String":
				String op1String = (String) factMap.get(super.paramName);
				String op2String = (String) super.comparitionValue;
				comparition = op1String.equals(op2String);
				break;
				default:
					
			}

			return comparition;
		}
	}
	
	public static class GThan extends Condition{
		
		public GThan(String param,Object value) {
			super(param,value);
		}
		
		
		@Override
		public boolean validate(Map<String, Object> factMap) {
			// TODO Auto-generated method stub
			if (!allNotNull(super.paramName, super.comparitionValue, factMap))
				return false;
			if (!factMap.containsKey(super.paramName))
				return false;

			if (!factMap.get(super.paramName).getClass().equals(Integer.class)
					|| !super.comparitionValue.getClass().equals(Integer.class))
				return false;

			Integer op1 = (Integer) factMap.get(super.paramName);
			Integer op2 = (Integer) super.comparitionValue;

			return op1.compareTo(op2) > 0;
		}
	}
	
	
	public static class GOrEqThan extends Condition{
		public GOrEqThan(String param,Object value) {
			super(param,value);
		}
		
		
		@Override
		public boolean validate(Map<String, Object> factMap) {
			// TODO Auto-generated method stub
			if (!allNotNull(super.paramName, super.comparitionValue, factMap))
				return false;
			if (!factMap.containsKey(super.paramName))
				return false;

			if (!factMap.get(super.paramName).getClass().equals(Integer.class)
					|| !super.comparitionValue.getClass().equals(Integer.class))
				return false;

			Integer op1 = (Integer) factMap.get(super.paramName);
			Integer op2 = (Integer) super.comparitionValue;

			return op1.compareTo(op2) >= 0;
		}
	}
	
	
	public static class LOrEqThan extends Condition{
		
		public LOrEqThan(String param,Object value) {
			super(param,value);
		}
		
		@Override
		public boolean validate(Map<String, Object> factMap) {
			// TODO Auto-generated method stub
			if (!allNotNull(super.paramName, super.comparitionValue, factMap))
				return false;
			if (!factMap.containsKey(super.paramName))
				return false;

			if (!factMap.get(super.paramName).getClass().equals(Integer.class)
					|| !super.comparitionValue.getClass().equals(Integer.class))
				return false;

			Integer op1 = (Integer) factMap.get(super.paramName);
			Integer op2 = (Integer) super.comparitionValue;

			return op1.compareTo(op2) <= 0;
		}
	}

}
