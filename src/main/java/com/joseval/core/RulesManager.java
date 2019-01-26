package com.joseval.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Getter;
import lombok.Singular;

public class RulesManager {

	@Data
	@AllArgsConstructor
	@Builder(builderClassName = "Builder", builderMethodName = "builder")
	public static class Operator {

		private Function<ToOperateInput, Boolean> operationFunction;
		private String spectedParamName;
		@Singular
		private List<Object> spectedValues;
		private Map<String, Object> espectedValues;
		private Operator[] childrenOperators;
		private OperationType operation;
		@Default
		private String stringResult = "First run and then print these variable";

		public boolean runValidation(Map<String, Object> factParams) {
			String operationStr = "";
			boolean response = false;

			switch (operation.getValue()) {
			case ARITHMETIC:
				BigDecimal spectedNumber = toBigDecimal(spectedValues.get(0));
				BigDecimal actualNumber = toBigDecimal(factParams.get(spectedParamName));
				int comparitionVal = actualNumber.compareTo(spectedNumber);
				switch (operation) {

				case GETHAN:
					operationStr = " >= ";
					response = comparitionVal >= 0;
					break;
				case GTHAN:
					operationStr = " > ";
					response = comparitionVal > 0;
					break;
				case LETHAN:
					operationStr = " <= ";
					response = comparitionVal <= 0;
					break;
				case LTHAN:
					operationStr = " < ";
					response = comparitionVal < 0;
					break;
				default:
					break;
				}
				operationStr = spectedParamName +"["+actualNumber+"]" + operationStr + spectedNumber;
				break;
			case DATE:

				switch (operation) {
				case BETWEEN:

					try {

						Date spectedDateBegin = (Date) spectedValues.get(0);
						Date spectedDateEnd = (Date) spectedValues.get(0);

						Date actualDate = (Date) factParams.get(spectedParamName);
						response = actualDate.after(spectedDateEnd) && actualDate.before(spectedDateBegin);
						operationStr = spectedParamName+"["+actualDate+"]" + " between " + spectedDateBegin + " and " + spectedDateEnd;
					} catch (Exception e) {
						// break;
					}

					break;
				default:
				}

				break;
			case LOGIC:
				
					switch (operation) {
					case OR:
						response = false;
						for (int i = 0; i < childrenOperators.length; ++i) {
							Operator operator = childrenOperators[i];
						response |= operator.runValidation(factParams);
						operationStr += i < childrenOperators.length - 1 ? "( " + operator.getStringResult() + " ) or "
								: "( " + operator.getStringResult() + " )";
						}
						break;
					case AND:
						response = true;
						for (int i = 0; i < childrenOperators.length; ++i) {
							Operator operator = childrenOperators[i];
						response &= operator.runValidation(factParams);
						operationStr += i < childrenOperators.length - 1 ? "( " + operator.getStringResult() + " ) and "
								: "( " + operator.getStringResult() + " )";
						}
						break;
					case NOT:
						Operator operator = childrenOperators[0];
						response = !operator.runValidation(factParams);
						operationStr += "~( " + operator.getStringResult()+ " )";
						break;
					default:
						System.out.println("It's possible the existence of a bug problem here.");
					}
				

				break;
			case NEUTRAL:

				Object actualNeutral = factParams.get(spectedParamName);
				Object spectedNeutral=null;
				switch (operation) {

				case EQUALS:
					
					spectedNeutral = spectedValues.get(0);
					operationStr = " = ";
					response = actualNeutral.equals(spectedNeutral);
					break;
				case NEQUALS:
					spectedNeutral = spectedValues.get(0);
					operationStr = " != ";
					response = !actualNeutral.equals(spectedNeutral);
					break;
				case NOTNULL:
					response = actualNeutral != null;
					operationStr = " is not NULL ";
					break;
				case NULL:
					response = actualNeutral == null;
					operationStr = " is NULL ";
					break;
				default:
					System.out.println("It's possible the existence of a bug problem here.");
					break;

				}
				if(spectedNeutral!=null) {
					operationStr = spectedParamName +"["+actualNeutral+"]" + operationStr + spectedNeutral.toString();
				}else {
					operationStr = spectedParamName +"["+actualNeutral+"]" + operationStr;
				}
				break;
			case STRING:
				String spectedString = (String) spectedValues.get(0);
				String factString = (String) factParams.get(spectedParamName);
				switch (operation) {
				case BWITH:
					operationStr = " beginsWith ";
					response = factString.startsWith(spectedString);
					break;
				case CONTAINS:
					operationStr = " contains ";
					response = factString.contains(spectedString);
					break;
				case EWITH:
					
					operationStr = " endsWith ";
					response = factString.endsWith(spectedString);
					break;
				case MATCH:
					operationStr = " matches ";
					response = factString.matches(spectedString);
					break;
				case NCONTAINS:
					operationStr = " notContains ";
					response = !factString.contains(spectedString);
					break;
				case NMATCH:
					operationStr = " notMatches ";
					response = !factString.matches(spectedString);
					break;
				default:
					System.out.println("It's possible the existence of a bug problem here.");
					break;

				}
				operationStr = spectedParamName +"["+factString+"]" +operationStr + spectedString;
				break;
			default:
				System.out.println("It's possible the existence of a bug problem here.");
				break;

			}
			stringResult = operationStr;//+" {"+response+"} ";
			return response;
		}

		private BigDecimal toBigDecimal(Object from) {
			// log.debug("Object name={}",from.getClass().getName());
			BigDecimalFromType type = BigDecimalFromType.valueOf(from.getClass().getSimpleName());
			switch (type) {
			case BigDecimal:
				return (BigDecimal) from;
			case BigInteger:
				BigInteger bigIntVal = (BigInteger) from;

				return new BigDecimal(bigIntVal);
			case Double:
				Double doubleVal = (Double) from;

				return new BigDecimal(doubleVal);
			case Float:
				Float floatVal = (Float) from;

				return new BigDecimal(floatVal);
			case Integer:
				Integer intValue = (Integer) from;

				return new BigDecimal(intValue);
			default:
				return null;

			}

		}

		@Getter
		@AllArgsConstructor
		public static enum BigDecimalFromType {
			String(new String().getClass().getName()), Double(new Double(0).getClass().getName()),
			Integer(new Integer(0).getClass().getName()), Float(new Float(0).getClass().getName()),
			BigInteger(new BigInteger("0").getClass().getName()), BigDecimal(new BigDecimal("0").getClass().getName()),
			Date(new Date().getClass().getName());

			private final String value;

		}

		public static Operator Or(Operator... operators) {

			return logicOperator(OperationType.OR, operators);
		}

		public static Operator And(Operator... operators) {

			return logicOperator(OperationType.AND, operators);
		}

		public static Operator Not(Operator operator) {

			return logicOperator(OperationType.NOT, operator);
		}

		public static Operator Equals(String paramName, Object espectedValue) {
			return neutralBinaryOperator(OperationType.EQUALS, paramName, espectedValue);
		}

		public static Operator NotEquals(String paramName, Object espectedValue) {
			return neutralBinaryOperator(OperationType.NEQUALS, paramName, espectedValue);
		}

		public static Operator LessThan(String paramName, Object espectedValue) {
			return arithmeticOperator(OperationType.LTHAN, paramName, espectedValue);
		}

		public static Operator LessOrEqualThan(String paramName, Object espectedValue) {
			return arithmeticOperator(OperationType.LETHAN, paramName, espectedValue);
		}

		public static Operator GreatherThan(String paramName, Object espectedValue) {
			return arithmeticOperator(OperationType.GTHAN, paramName, espectedValue);
		}

		public static Operator GreatherOrEqualThan(String paramName, Object espectedValue) {
			return arithmeticOperator(OperationType.GETHAN, paramName, espectedValue);
		}

		public static Operator BeginsWith(String paramName, String spectedValue) {
			return stringOperator(OperationType.BWITH, paramName, spectedValue);
		}

		public static Operator EndsWith(String paramName, String spectedValue) {
			return stringOperator(OperationType.EWITH, paramName, spectedValue);
		}

		public static Operator Match(String paramName, String spectedValue) {
			return stringOperator(OperationType.MATCH, paramName, spectedValue);
		}

		public static Operator NoMatch(String paramName, String spectedValue) {
			return stringOperator(OperationType.NMATCH, paramName, spectedValue);
		}

		public static Operator Contains(String paramName, String spectedValue) {
			return stringOperator(OperationType.CONTAINS, paramName, spectedValue);
		}

		public static Operator NotContains(String paramName, String spectedValue) {
			return stringOperator(OperationType.NCONTAINS, paramName, spectedValue);
		}

		public static Operator IsNull(String paramName) {
			return neutralMonoOperator(OperationType.NULL, paramName);
		}

		public static Operator NotNull(String paramName) {
			return neutralMonoOperator(OperationType.NOTNULL, paramName);
		}

		private static Operator neutralMonoOperator(OperationType operationType, String paramName) {
			return new Operator.Builder().operation(operationType).spectedParamName(paramName).build();
		}

		private static Operator neutralBinaryOperator(OperationType operationType, String paramName,
				Object spectedValue) {
			return new Operator.Builder().operation(operationType).spectedParamName(paramName)
					.spectedValue(spectedValue).build();
		}

		private static Operator stringOperator(OperationType operationType, String paramName, String operand) {
			return new Operator.Builder().operation(operationType).spectedParamName(paramName).spectedValue(operand)
					.build();
		}

		private static Operator arithmeticOperator(OperationType operationType, String paramName, Object operand) {
			return new Operator.Builder().operation(operationType).spectedParamName(paramName).spectedValue(operand)
					.build();
		}

		private static Operator logicOperator(OperationType operationType, Operator... operators) {
			return new Operator.Builder().childrenOperators(operators).operation(operationType).build();
		}

		private static Operator logicOperator(OperationType operationType, Operator operator) {
			return logicOperator(operationType, new Operator[] { operator });
		}

	}

	@Getter
	@AllArgsConstructor
	public static enum OperationType {
		OR(OperationCategory.LOGIC), AND(OperationCategory.LOGIC), NOT(OperationCategory.LOGIC),
		LTHAN(OperationCategory.ARITHMETIC), GTHAN(OperationCategory.ARITHMETIC), LETHAN(OperationCategory.ARITHMETIC),
		GETHAN(OperationCategory.ARITHMETIC), BWITH(OperationCategory.STRING), EWITH(OperationCategory.STRING),
		MATCH(OperationCategory.STRING), NMATCH(OperationCategory.STRING), CONTAINS(OperationCategory.STRING),
		NCONTAINS(OperationCategory.STRING), BETWEEN(OperationCategory.DATE), NULL(OperationCategory.NEUTRAL),
		NOTNULL(OperationCategory.NEUTRAL), EQUALS(OperationCategory.NEUTRAL), NEQUALS(OperationCategory.NEUTRAL);

		private final OperationCategory value;

	}

	public static enum OperationCategory {
		LOGIC, // Or,And,Xor,Xand,Not
		ARITHMETIC, // LessThan,GreaterThan,LessThanOrEq,GreaterThanOrEq
		STRING, // BeginsWith,EndsWith,Contains,NotContains,Match,NoMatch
		DATE, // Between
		NEUTRAL // Equals,NotEquals,Null,NotNull
	}

	@Data
	public static class ToOperateInput {
		private Map<String, Object> spectedParams;
		private Map<String, Object> realValues;
	}

}
