package com.gfk.senbot.framework.cucumber;

import java.util.List;

import gherkin.formatter.Formatter;
import gherkin.formatter.JSONFormatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;

public class WrappedJSONFormatter implements Formatter, Reporter {

	private final JSONFormatter wrapped;

	public WrappedJSONFormatter(JSONFormatter wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public void background(Background arg0) {
		wrapped.background(arg0);
	}

	@Override
	public void close() {
		wrapped.close();
	}

	@Override
	public void done() {
		wrapped.done();
	}

	@Override
	public void eof() {
		wrapped.eof();
	}

	@Override
	public void examples(Examples arg0) {
		wrapped.equals(arg0);
	}

	@Override
	public void feature(Feature arg0) {
		wrapped.feature(arg0);
	}

	@Override
	public void scenario(Scenario arg0) {
		wrapped.scenario(arg0);
	}

	@Override
	public void scenarioOutline(ScenarioOutline arg0) {
		wrapped.scenarioOutline(arg0);
	}

	@Override
	public void step(Step arg0) {
		wrapped.step(arg0);
	}

	@Override
	public void syntaxError(String arg0, String arg1, List<String> arg2, String arg3, Integer arg4) {
		wrapped.syntaxError(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void uri(String arg0) {
		wrapped.uri(arg0);
	}

	@Override
	public void after(Match arg0, Result arg1) {
		wrapped.after(arg0, arg1);
	}

	@Override
	public void before(Match arg0, Result arg1) {
		wrapped.before(arg0, arg1);
	}

	@Override
	public void embedding(String arg0, byte[] arg1) {
		wrapped.embedding(arg0, arg1);
	}

	@Override
	public void match(Match arg0) {
		wrapped.match(arg0);
	}

	@Override
	public void result(Result arg0) {
		wrapped.result(arg0);
	}

	@Override
	public void write(String arg0) {
		wrapped.write(arg0);
	}

}
