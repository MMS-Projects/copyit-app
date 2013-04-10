package net.mms_projects.utils;

import java.util.LinkedHashMap;

public class InlineSwitch<I, O> {

	private LinkedHashMap<I, O> clauses = new LinkedHashMap<I, O>();

	public void addClause(I input, O output) {
		clauses.put(input, output);
	}

	public O runSwitch(I input) {
		for (I key : clauses.keySet()) {
			if (key == input) {
				return clauses.get(key);
			}
		}
		return null;
	}

	public void runSwitch(I input, Runnable runnable) {
		for (I key : clauses.keySet()) {
			if (key == input) {
				runnable.run();
			}
		}
	}

}
