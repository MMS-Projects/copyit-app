package net.mms_projects.utils;

import java.util.LinkedHashMap;

public class InlineSwitch<I, O> {

	private LinkedHashMap<I, O> clauses = new LinkedHashMap<I, O>();
	private O defaultValue;

	public void addClause(I input, O output) {
		clauses.put(input, output);
	}
	
	public void setDefault(O output) {
		this.defaultValue = output;
	}

	public O runSwitch(I input) {
		for (I key : clauses.keySet()) {
			if (key.equals(input)) {
				return clauses.get(key);
			}
		}
		return this.defaultValue;
	}

	public void runSwitch(I input, Runnable runnable) {
		for (I key : clauses.keySet()) {
			if (key.equals(input)) {
				runnable.run();
			}
		}
	}

}
