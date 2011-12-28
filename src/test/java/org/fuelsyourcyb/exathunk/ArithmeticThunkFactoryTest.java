package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class ArithmeticThunkFactoryTest {

    public class IntTypeChecker implements  TypeChecker<Class, String, Object> {
	public boolean check(Class type, String fromValue) {
	    try {
		convert(type, fromValue);
		return true;
	    } catch (TypeException e) {
		return false;
	    }
	}

	public Object convert(Class type, String fromValue) throws TypeException {
	    if (Integer.class.equals(type)) {
		try {
		    return new Integer(fromValue);
		} catch (NumberFormatException e) {
		    throw new TypeException("Invalid integer", e);
		}
	    } else if (String.class.equals(type)) {
		return fromValue;
	    } else {
		throw new TypeException("Cannot convert "+fromValue+" to "+type);
	    }
	}
    }

    public void parseHelper(ThunkFactory<Class, String, Object> factory, String expression, Integer intResult, int evaluations) throws UnknownFuncException, ParseException, TypeException, VisitException, ExecutionException {
	SexpParser parser = new SexpParser();
	TypeChecker<Class, String, Object> checker = new IntTypeChecker();

	NTree<Class, String, Thunk<Object>> result = new NTree<Class, String, Thunk<Object>>(Integer.class,
	    new PresentThunk<Object>(intResult));

	NTree<Unit, String, String> parseTree = parser.parse(expression);
	System.out.println(parseTree.toString());

	NTree<Class, String, Object> typedTree = TypeCheckerUtils.makeTypedTree(factory, checker, parseTree);

	NTree<Class, String, Thunk<Object>> thunkTree = ThunkUtils.makeThunkTree(factory, typedTree);
	System.out.println(thunkTree.toString());

	for (int i = 0; i < evaluations; ++i) {
	    thunkTree.accept(new ThunkUtils.StepEvaluator<Class, String, Object>(factory));
	    System.out.println(thunkTree.toString());
	}

	assert(thunkTree.equals(result));
    }

    @Test
    public void testParse1() throws Exception {
	ThunkFactory<Class, String, Object> factory = new ArithmeticThunkFactory();
	parseHelper(factory, "(* 3 2)", 6, 1);
    }

    @Test
    public void testParse2() throws Exception {
	ThunkFactory<Class, String, Object> factory = new ArithmeticThunkFactory();
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
	    return ThunkUtils.statelessEquals(this, (Thunk)o);
	}
    }

    private class DelayingArithmeticThunkFactory extends ArithmeticThunkFactory {
	private final Integer numSteps;

	public DelayingArithmeticThunkFactory(Integer numSteps) {
	    this.numSteps = numSteps;
	}

	public Thunk<Object> makeThunk(String funcId, List<Object> params) throws UnknownFuncException, ExecutionException {
	    return new DelayingThunk<>(numSteps, super.makeThunk(funcId, params));
	}
    }

    @Test
    public void testDelaying1() throws Exception {
	ThunkFactory<Class, String, Object> factory = new DelayingArithmeticThunkFactory(3);
	parseHelper(factory, "(* 3 2)", 6, 3);
    }

    @Test
    public void testDelaying2() throws Exception {
	ThunkFactory<Class, String, Object> factory = new DelayingArithmeticThunkFactory(3);
	parseHelper(factory, "(+ (- 1 2) (* 3 (/ 16 4)))", 11, 9);
    }
}