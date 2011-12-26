package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;

public class ArithmeticThunkFactoryTest {

    public void parseHelper(String expression, Integer intResult) {
	ArithmeticThunkFactory factory = new ArithmeticThunkFactory((StateFactory<State>)Unit.getInstance());
	ArithmeticParser parser = new ArithmeticParser(factory);

	NTree<String, Thunk<Integer>> result = new NTree<String, Thunk<Integer>>(
	    new PresentThunk<Integer>(Unit.getInstance(), intResult));

	NTree<String, Integer> parseTree = parser.parse(expression);
	System.out.println(parseTree.toString());

	NTree<String, Thunk<Integer>> thunkTree = ThunkUtils.makeThunkTree(parseTree, factory);
	System.out.println(thunkTree.toString());

	thunkTree.bindInto(new ThunkUtils.Evaluator<String, Integer>(factory));
	System.out.println(thunkTree.toString());

	assert(thunkTree.equals(result));
    }

    @Test
    public void testParse1() {
	parseHelper("* 3 2", 6);
    }

    @Test
    public void testParse2() {
	parseHelper("+ - 1 2 * 3 / 16 4", 11);
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

	public boolean isFinished() {
	    return numStepsTaken >= numStepsNeeded && thunk.isFinished();
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

	public Value getResult() {
	    if (numStepsTaken >= numStepsNeeded) {
		return thunk.getResult();
	    } else {
		return null;
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
	    return new DelayingThunk<Integer>(numSteps, super.makeThunk(funcId, params));
	}
    }

    @Test
    public void testDelaying() {
	DelayingArithmeticThunkFactory factory = new DelayingArithmeticThunkFactory((StateFactory<State>)Unit.getInstance(), 3);
	ArithmeticParser parser = new ArithmeticParser(factory);

	String expression = "* 3 2";
	NTree<String, Thunk<Integer>> result =
	    new NTree<String, Thunk<Integer>>(new PresentThunk<Integer>(Unit.getInstance(), 6));

	NTree<String, Integer> parseTree = parser.parse(expression);
	System.out.println(parseTree.toString());

	NTree<String, Thunk<Integer>> thunkTree = ThunkUtils.makeThunkTree(parseTree, factory);
	System.out.println(thunkTree.toString());

	ThunkUtils.Evaluator<String, Integer> evaluator = new ThunkUtils.Evaluator<String, Integer>(factory);
	for (int i = 0; i < 3; ++i) {
	    assert(!thunkTree.equals(result));
	    thunkTree.bindInto(evaluator);
	    System.out.println(thunkTree.toString());
	}

	System.out.println(result.toString());
	assert(thunkTree.equals(result));
    }
}