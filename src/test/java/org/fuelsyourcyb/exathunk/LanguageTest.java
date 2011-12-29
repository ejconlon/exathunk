package org.fuelsyourcyb.exathunk;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LanguageTest {
    public Interpreter<String, Class, String, String, Object> makeInterpreter() {
	ThunkFactory<Class, String, Object> factory = new ArithmeticThunkFactory();
	NTreeParser<String, String, String> parser = new SexpParser();
	TypeChecker<Class, String, Object> checker = new IntTypeChecker();
	NTree.Visitor<Class, String, Thunk<Object>> visitor = new ThunkUtils.StepVisitor<Class, String, Object>(factory);
	NTreeEvaluator<Class, String, Object> evaluator = new DefaultEvaluator<>(visitor);
	return new Interpreter<>(parser, checker, factory, evaluator);
    }

    private static Map<String, Object> makePosSpecs() {
	Map<String, Object> specs = new TreeMap<String, Object>();

	specs.put("(* 4 5)", 20);
	specs.put("(+ 4 (- 5 2))", 7);
	specs.put("(not true)", false);
	specs.put("(or  true false)", true);
	
	return specs;
    }

    private static Map<String, Class> makeNegSpecs() {
	Map<String, Class> specs = new TreeMap<String, Class>();

	specs.put("(* 4 true)", TypeException.class);
	specs.put("(blah 4 true)", UnknownFuncException.class);

	return specs;
    }

    @Test
    public void testPosPrograms() throws Exception {
	Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter();
	Logger logger = Logger.getLogger("LanguageTest");

	for (Map.Entry<String, Object> spec : makePosSpecs().entrySet()) {
	    logger.log(Level.FINE, "Testing {0} => {1}", new Object[] { spec.getKey(), spec.getValue() });
		assertEquals(spec.getValue(), interpreter.interpret(spec.getKey()));
	}
    }

    @Test
    public void testNegPrograms() {
	Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter();
	Logger logger = Logger.getLogger("LanguageTest");

	for (Map.Entry<String, Class> spec : makeNegSpecs().entrySet()) {
	    logger.log(Level.FINE, "Testing {0} throws {1}", new Object[] { spec.getKey(), spec.getValue() });
	    boolean caught = false;
	    try {
		interpreter.interpret(spec.getKey());
	    } catch (Exception e) {
		caught = true;
		assert(spec.getValue().isInstance(e));
	    }

	    if (!caught) {
		System.err.println("Expected "+spec.getValue());
		assert(false);
	    }
	}
    }

}