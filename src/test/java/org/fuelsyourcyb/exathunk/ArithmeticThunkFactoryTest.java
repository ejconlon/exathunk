package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ArithmeticThunkFactoryTest {

    private static class IdFunc<Value> implements Func1<Value, Value> {
	public Value runFunc(Value value) { return value; }
    }

    private static class IntCast implements Func1<String, Integer> {
	public Integer runFunc(String arg) { return new Integer(arg); }
    }

    private static class TypeException extends Exception {
	public TypeException(String message) { super(message); }
    }

    private static interface Typer<FromFuncId, ToFuncId, FromValue, ToValue> {
	NTree<ToFuncId, ToValue> type(NTree<FromFuncId, FromValue> parseTree) throws TypeException;
    }

    private static class DefaultTyper<FromFuncId, ToFuncId, FromValue, ToValue> implements Typer<FromFuncId, ToFuncId, FromValue, ToValue> {

	private final ThunkFactory<ToFuncId, ToValue> thunkFactory;
	private final Func1<FromFuncId, ToFuncId> funcIdFunc;
	private final Func1<FromValue, ToValue> valueFunc;

	public DefaultTyper(ThunkFactory<ToFuncId, ToValue> thunkFactory,
			    Func1<FromFuncId, ToFuncId> funcIdFunc,
			    Func1<FromValue, ToValue> valueFunc) {
	    this.thunkFactory = thunkFactory;
	    this.funcIdFunc = funcIdFunc;
	    this.valueFunc = valueFunc;
	}

	public NTree<ToFuncId, ToValue> type(NTree<FromFuncId, FromValue> parseTree) {
	    NTree<ToFuncId, ToValue> typedTree = new NTree<>();
	    if (parseTree.isEmpty()) {
		// pass
	    } else if (parseTree.isLeaf()) {
		typedTree.setValue(valueFunc.runFunc(parseTree.getValue()));
	    } else {
		ToFuncId funcId = funcIdFunc.runFunc(parseTree.getLabel());
		List<NTree<ToFuncId, ToValue>> children = new ArrayList<NTree<ToFuncId, ToValue>>(parseTree.getChildren().size());
		for (NTree<FromFuncId, FromValue> parseChild : parseTree.getChildren()) {
		    children.add(type(parseChild));
		}
		typedTree.setBranch(funcId, children);
	    }
 	    return typedTree;
	}
    }


    public void parseHelper(String expression, Integer intResult) throws ThunkEvaluationException, ParseException, TypeException {
	ThunkFactory<String, Integer> factory = new ArithmeticThunkFactory((StateFactory<State>)Unit.getInstance());
	SexpParser parser = new SexpParser();
	Typer<String, String, String, Integer> typer = new DefaultTyper<>(factory, new IdFunc<String>(), new IntCast());

	NTree<String, Thunk<Integer>> result = new NTree<String, Thunk<Integer>>(
	    new PresentThunk<>(Unit.getInstance(), intResult));

	NTree<String, String> parseTree = parser.parse(expression);
	System.out.println(parseTree.toString());

	NTree<String, Integer> typedTree = typer.type(parseTree);

	NTree<String, Thunk<Integer>> thunkTree = ThunkUtils.makeThunkTree(typedTree, factory);
	System.out.println(thunkTree.toString());

	thunkTree.bindInto(new ThunkUtils.StepEvaluator<>(factory));
	System.out.println(thunkTree.toString());

	assert(thunkTree.equals(result));
    }

    @Test
    public void testParse1() throws ThunkEvaluationException, ParseException, TypeException {
	parseHelper("(* 3 2)", 6);
    }

    @Test
    public void testParse2() throws ThunkEvaluationException, ParseException, TypeException {
	parseHelper("(+ (- 1 2) (* 3 (/ 16 4)))", 11);
    }

    /*private class DelayingThunk<Value> implements Thunk<Value> {
	private final Integer numStepsNeeded;
	private Integer numStepsTaken;
	private final Thunk<Value> thunk;

	public DelayingThunk(Integer numStepsNeeded, Thunk<Value> thunk) {
	    this.numStepsNeeded = numStepsNeeded;
	    this.thunk = thunk;
	    this.numStepsTaken = 0;
	}

	public void step() {
	    if (numStepsTaken < numStepsNeeded) {
		++numStepsTaken;
	    } else {
		thunk.step();
	    }
	}

	public State getState() {
	    return thunk.getState();
	}

	public boolean cancel(boolean ignored) {
	    return false;
	}

	public boolean isCancelled() {
	    return false;
	}

	public boolean isDone() {
	    return numStepsTaken >= numStepsNeeded && thunk.isDone();
	}

	public Value get() throws InterruptedException, ExecutionException {
	    if (numStepsTaken >= numStepsNeeded) {
		return thunk.get();
	    } else {
		throw new InterruptedException();
	    }
	}

	public Value get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
	    if (numStepsTaken >= numStepsNeeded) {
		return thunk.get(timeout, unit);
	    } else {
		throw new InterruptedException();
	    }
	}

	public String toString() {
	    return "DelayedThunk<"+numStepsTaken+"/"+numStepsNeeded+" "+thunk+">";
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
	    if (o == null || !(o instanceof Thunk)) return false;
	    return ThunkUtils.statefulEquals(this, (Thunk)o);
	}
    }

    private class DelayingArithmeticThunkFactory extends ArithmeticThunkFactory {
	private final Integer numSteps;

	public DelayingArithmeticThunkFactory(StateFactory<State> stateFactory, Integer numSteps) {
	    super(stateFactory);
	    this.numSteps = numSteps;
	}

	public Thunk<Integer> makeThunk(String funcId, List<Integer> params) {
	    return new DelayingThunk<>(numSteps, super.makeThunk(funcId, params));
	}
    }

    @Test
    public void testDelaying() throws ThunkEvaluationException {
	DelayingArithmeticThunkFactory factory = new DelayingArithmeticThunkFactory((StateFactory<State>)Unit.getInstance(), 3);
	ArithmeticParser parser = new ArithmeticParser(factory);

	String expression = "* 3 2";
	NTree<String, Thunk<Integer>> result =
	    new NTree<String, Thunk<Integer>>(new PresentThunk<>(Unit.getInstance(), 6));

	NTree<String, Integer> parseTree = parser.parse(expression);
	System.out.println(parseTree.toString());

	NTree<String, Thunk<Integer>> thunkTree = ThunkUtils.makeThunkTree(parseTree, factory);
	System.out.println(thunkTree.toString());

	ThunkUtils.Evaluator<String, Integer> evaluator = new ThunkUtils.StepEvaluator<>(factory);
	for (int i = 0; i < 3; ++i) {
	    assert(!thunkTree.equals(result));
	    thunkTree.bindInto(evaluator);
	    System.out.println(thunkTree.toString());
	}

	System.out.println(result.toString());
	assert(thunkTree.equals(result));
	}*/
}