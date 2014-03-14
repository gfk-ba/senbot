package com.gfk.senbot.documenter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class StepDef {
	
	private Class parentClass;
	private Method method;
	private Annotation stepAnnotation;
	private String stepRegexValue;

	public StepDef(Class parentClass, Method method, Annotation stepAnnotation) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super();
		this.parentClass = parentClass;
		this.method = method;
		this.stepAnnotation = stepAnnotation;
		Method valueFetcher = stepAnnotation.getClass().getMethod("value");
		stepRegexValue = (String) valueFetcher.invoke(stepAnnotation);
	}
	
	public String getFullMethodName() {
		return parentClass.toString() + "." + method.getName();
	}
	
	public String getStepType() {
		return stepAnnotation.annotationType().getSimpleName();
	}
	
	public String getStepRegexValue() {
		return stepRegexValue;
	}
	
	@Override
	public String toString() {
		return getStepType() + " " + getStepRegexValue();
	}
	
	public Type[] getArgumentTypes() {
		return method.getGenericParameterTypes();
//		for(Type type : genericParameterTypes) {
//			System.out.println(type);
//		}
	}


}
