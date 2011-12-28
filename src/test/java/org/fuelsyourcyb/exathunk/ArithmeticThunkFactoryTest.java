package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ArithmeticThunkFactoryTest {

    public class IntTypeChecker implements  TypeChecker<Class, String, Integer> {
	public boolean check(Class type, String fromValue) {
	    try {
		convert(type, fromValue);
		return true;
	    } catch (TypeException e) {
		return false;
	    }
	}

	public Integer convert(Class type, String fromValue) throws TypeException {
	    if (!Integer.class.equals(type)) {
		throw new TypeException("Expected Integer type");
	    }

	    try {
		return new Integer(fromValue);
	    } catch (NumberFormatException e) {
		throw new TypeException("Invalid integer", e);
	    }
	}
    }

    public void parseHelper(ThunkFactory<Class, String, Integer> factory, String expression, Integer intResult, int evaluations) throws UnknownFuncException, ParseException, TypeException, VisitException {
	SexpParser parser = new SexpParser();
	TypeChecker<Class, String, Integer> checker = new IntTypeChecker();

	NTree<Class, String, Thunk<Integer>> result = new NTree<Class, String, Thunk<Integer>>(Integer.class,
	    new PresentThunk<>(Unit.getInstance(), intResult));

	NTree<Unit, String, String> parseTree = parser.parse(expression);
	System.out.println(parseTree.toString());

	NTree<Class, String, Integer> typedTree = TypeCheckerUtils.makeTypedTree(factory, checker, parseTree);

	NTree<Class, String, Thunk<Integer>> thunkTree = ThunkUtils.makeThunkTree(typedTree, factory);
	System.out.println(thunkTree.toString());

	for (int i = 0; i < evaluations; ++i) {
	    thunkTree.accept(new ThunkUtils.StepEvaluator<Class, String, Integer>(factory));
	    System.out.println(thunkTree.toString());
	}

	assert(thunkTree.equals(result));
    }

    @Test
    public void testParse1() throws Exception {
	ThunkFactory<Class, String, Integer> factory = new ArithmeticThunkFactory((StateFactory<State>)Unit.getInstance());
	parseHelper(factory, "(* 3 2)", 6, 1);
    }

    @Test
    public void testParse2() throws Exception {
	ThunkFactory<Class, String, Integer> factory = new ArithmeticThunkFactory((StateFactory<State>)Unit.getInstance());
	parseHelper(factory, "(+ (- 1 2) (* 3 (/ 16 4)))", 11, 1);
    }

    private class DelayingThunk<Value> implements Thunk<Value> {
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

	public Pair<Class, Thunk<Integer>> makeThunk(String funcId, List<Pair<Class, Integer>> params) throws UnknownFuncException {
	    Pair<Class, Thunk<Integer>> pair = super.makeThunk(funcId, params);
	    return new Pair<Class, Thunk<Integer>>(pair.getFirst(), new DelayingThunk<Integer>(numSteps, pair.getSecond()));
	}
    }

    @Test
    public void testDelaying1() throws Exception {
	ThunkFactory<Class, String, Integer> factory = new DelayingArithmeticThunkFactory((StateFactory<State>)Unit.getInstance(), 3);
	parseHelper(factory, "(* 3 2)", 6, 3);
    }

    @Test
    public void testDelaying2() throws Exception {
	ThunkFactory<Class, String, Integer> factory = new DelayingArithmeticThunkFactory((StateFactory<State>)Unit.getInstance(), 3);
	parseHelper(factory, "(+ (- 1 2) (* 3 (/ 16 4)))", 11, 9);
    }
}