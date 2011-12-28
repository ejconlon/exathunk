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

    public Interpreter<String, Class, String, String, Object> makeInterpreter(ThunkFactory<Class, String, Object> factory) {
	NTreeParser<String, String, String> parser = new SexpParser();
	TypeChecker<Class, String, Object> checker = new IntTypeChecker();
	NTree.Visitor<Class, String, Thunk<Object>> visitor = new ThunkUtils.StepVisitor<Class, String, Object>(factory);
	NTreeEvaluator<Class, String, Object> evaluator = new DefaultEvaluator<>(visitor);
	return new Interpreter<>(parser, checker, factory, evaluator);
    }

    @Test
    public void testParse1() throws Exception {
	ThunkFactory<Class, String, Object> factory = new ArithmeticThunkFactory();
	Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter(factory);
	assertEquals(6, interpreter.interpret("(* 3 2)"));
    }

    @Test
    public void testParse2() throws Exception {
	ThunkFactory<Class, String, Object> factory = new ArithmeticThunkFactory();
	Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter(factory);
	assertEquals(11, interpreter.interpret("(+ (- 1 2) (* 3 (/ 16 4)))"));
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

	public void run() {
	    while (!isDone()) {
		step();
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
	Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter(factory);
	assertEquals(6, interpreter.interpret("(* 3 2)"));
    }

    @Test
    public void testDelaying2() throws Exception {
	ThunkFactory<Class, String, Object> factory = new DelayingArithmeticThunkFactory(3);
	Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter(factory);
	assertEquals(11, interpreter.interpret("(+ (- 1 2) (* 3 (/ 16 4)))"));
    }

    @Test
    public void testTyping() throws Exception {
	ThunkFactory<Class, String, Object> factory = new ArithmeticThunkFactory();
	Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter(factory);
	assertEquals(2, interpreter.interpret("(- (len foo) 1)"));
    }
}