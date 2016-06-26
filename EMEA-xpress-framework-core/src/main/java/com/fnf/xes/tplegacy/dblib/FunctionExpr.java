/*
 *	$Header: /view/system_view/vobstore/xes/CVS/xesFramework2/CVSROOT/src/prod/com/fnf/xes/tplegacy/dblib/FunctionExpr.java,v 1.2 2005/11/03 16:29:21 mfwang Exp $
 *	TouchPoint Java Enterprise Services Library
 *	Copyright 2003, WebTone Technologies, Inc., all rights reserved.
 * 
 * 	WARNING: This file contains CONFIDENTIAL and PROPRIETARY information and
 * 	INTELLECTUAL DATA of WebTone Technology, Inc., and is protected by copyright
 * 	law and international treaties.  Unauthorized reproduction or distribution
 * 	may result in severe civil and criminal penalties, and will be prosecuted
 * 	to the maximum extent possible under the law.
 */
package com.fnf.xes.tplegacy.dblib;

/**
 *	The Function Expression class provides the implementation for expressions of SQL statement functions.
 *
 *	@tp.creationdate 2003-04-14
 *	@author XES DEV TEAM
 *	@version 0.1.0
 *	@tp.revision $Revision: 1.2 $
 *	@tp.modified $Date: 2005/11/03 16:29:21 $
 *	@tp.copyright 2003, WebTone Technologies, Inc., all rights reserved.
 */
public class FunctionExpr extends Expression {

	private String pa_function;
	private Expression pa_param1 = null;
	private Expression pa_param2 = null;
	private Expression pa_param3 = null;

	public static FunctionExpr Count() {
		return new FunctionExpr("COUNT", new ValueExpr("*"));
	}

	/**
	 *	@param func
	 *	@param param1
	 *	@param param2
	 *	@param param3
	 */
	public FunctionExpr(String func, Expression param1, Expression param2, Expression param3) {
		pa_function = func;
		pa_param1 = param1;
		pa_param2 = param2;
		pa_param3 = param3;
	}
	/**
	 *	@param func
	 *	@param param1
	 *	@param param2
	 */
	public FunctionExpr(String func, Expression param1, Expression param2) {
		pa_function = func;
		pa_param1 = param1;
		pa_param2 = param2;
	}
	/**
	 *	@param func
	 *	@param param1
	 */
	public FunctionExpr(String func, Expression param1) {
		pa_function = func;
		pa_param1 = param1;
		;
	}
	/**
	 *	@param func
	 */
	public FunctionExpr(String func) {
		pa_function = func;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (pa_function == null && pa_param1 == null)
			return true;
		else
			return false;
	}

	/**
	 *	The default to-string method uses a generic JDBC dialect to render the
	 *	SQL statement for this expression.
	 *	@return Answers a String object containing the properly formatted SQL clause.
	 *	@see 	#toString(Dialect adialect)
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(pa_function);
		if (pa_param1 != null) {
			result.append("(");
			result.append(pa_param1.toString());
			if (pa_param2 != null) {
				result.append(",");
				result.append(pa_param2.toString());
				if (pa_param3 != null) {
					result.append(",");
					result.append(pa_param3.toString());
				}
			}
			result.append(")");
		}
		return result.toString();
	}

	///////////////////////////////////////////////////////////////////////////////
	//	Implemented Expression Interface 
	/**
	 *	The Expression to-string method takes a Dialect object and uses that to properly render the
	 *	the SQL statement for the expression.
	 *	@param adialect	A Dialect object used to properly format this SQL clause.
	 *	@return 	Answers a String object containing the properly formatted SQL clause.
	 */
	public String toString(Dialect adialect) {
		StringBuffer result = new StringBuffer();
		result.append(pa_function);
		if (pa_param1 != null) {
			result.append("(");
			result.append(pa_param1.toString(adialect));
			if (pa_param2 != null) {
				result.append(",");
				result.append(pa_param2.toString(adialect));
				if (pa_param3 != null) {
					result.append(",");
					result.append(pa_param3.toString(adialect));
				}
			}
			result.append(")");
		}
		return result.toString();
	}
	/**
	*	Collects a list of expressions of a given type from the full depth of the expression
	*	heirarchy.
	*	@param exprtype		The string keyword for the expression type to collect. A null string means all.
	*	@return 		Answers an array of expressions extracted from the expresion heirarchy that
	*				match the collect type.
	*/
	public Expression[] collect(String exprtype) {
		int count = 0;
		Expression[] ar1 = null;
		Expression[] ar2 = null;
		Expression[] ar3 = null;

		if (exprtype == null || exprtype.compareTo(exprType()) == 0)
			count = 1;

		if (pa_param1 == null)
			return null;

		ar1 = pa_param1.collect(exprtype);
		if (pa_param2 != null)
			ar2 = pa_param2.collect(exprtype);
		if (pa_param3 != null)
			ar3 = pa_param3.collect(exprtype);
		if (ar1 != null)
			count = count + ar1.length;
		if (ar2 != null)
			count = count + ar2.length;
		if (ar3 != null)
			count = count + ar3.length;
		if (count == 0)
			return null;

		Expression[] array = new Expression[count];
		int i = 0;
		if (exprtype == null || exprtype.compareTo(exprType()) == 0) {
			array[i] = this;
			++i;
		}
		if (ar1 != null)
			for (int j = 0; j < ar1.length; ++j, ++i)
				array[i] = ar1[j];
		if (ar2 != null)
			for (int j = 0; j < ar2.length; ++j, ++i)
				array[i] = ar2[j];
		if (ar3 != null)
			for (int j = 0; j < ar3.length; ++j, ++i)
				array[i] = ar3[j];
		return array;
	}
	/**
	 *	A quick reference for the Expression type. This avoides
	 *	some of the overhead of reflection to find the logical
	 *	type of reflection. It is also possible that alternate
	 *	versions of this expression object might be logically 
	 *	identical while being derived from different class instances.
	 *	@return		Answers a String with the logical name of this
	 *			type of expression. For UnaryExpr class objects
	 *			the String contains 'UNARY'
	 */
	public String exprType() {
		return "FUNCTION";
	}

	/**
	 *	The get operator method is a transparent method that returns
	 *	the operator for this expression. There is still some question
	 *	in the design as to what non-operator based expression objects
	 *	will return for this method.
	 *	@return 	Answers an Operator object for this expression.
	 */
	public Operator getOperator() {
		return null;
	}
	/**
	 *	The is Equivalent tests to see if the expressions are functionally equivolent. This is
	 *	different from being equal as the actual content of the expressions might be different.
	 *	[Currently Unimplemented]
	 *	@param expa	An expressioin to test for equivolence with the current expression.
	 *	@return 	Answers either true of false depending on the equivolence of the current
	 *			and passed expression.
	 */
	public boolean isEquivalent(Expression expa) {
		return false;
	}
	/**
	 *	Tests the expression to see if the operator will result in a boolean type expression. This
	 *	is important in deciding if the expression is fit for Criterion status and usable in a where
	 *	clause.
	 *	@return Answers a Boolean true for expressions that result in only a boolean value, and
	 *		false for expressions that may yeild other types of results. For the UnaryExpr class
	 *		objects the results of this method are determined by the type of operator.
	 */
	public boolean isBoolean() {
		return false;
	}

}