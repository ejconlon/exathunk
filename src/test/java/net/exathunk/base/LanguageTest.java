package net.exathunk.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LanguageTest {
    public Interpreter<String, Class, String, String, Object> makeInterpreter() {
        ThunkFactory<Class, String, Object> factory = new SchemeyThunkFactory();
        NTreeParser<String, String, String> parser = new SexpParser();
        TypeChecker<Class, String, Object> checker = new SchemeyTypeChecker();
        ThunkExecutor<Object> executor = new DefaultThunkExecutor<>();
        return new Interpreter<>(parser, checker, factory, executor);
    }

    private static Map<String, Object> makePosSpecs() {
        Map<String, Object> specs = new TreeMap<>();

        specs.put("(* 4 5)", 20);
        specs.put("(+ 4 (- 5 2))", 7);
        specs.put("(* 3 2)", 6);
        specs.put("(+ (- 1 2) (* 3 (/ 16 4)))", 11);
        specs.put("(not true)", false);
        specs.put("(or  true false)", true);
        specs.put("(- (len foo) 1)", 2);
        specs.put("(or true (bottom))", true);
        specs.put("(if true foo bar)", "foo");
        specs.put("(if false foo bar)", "bar");
        return specs;
    }

    private static Map<String, Class> makeNegSpecs() {
        Map<String, Class> specs = new TreeMap<>();

        specs.put("(* 4 true)", TypeException.class);
        specs.put("(blah 4 true)", UnknownFuncException.class);
        specs.put("(bottom)", ExecutionException.class);

        return specs;
    }

    @Test
    public void testPosPrograms() throws Exception {
        Interpreter<String, Class, String, String, Object> interpreter = makeInterpreter();
        Logger logger = Logger.getLogger("LanguageTest");

        for (Map.Entry<String, Object> spec : makePosSpecs().entrySet()) {
            logger.log(Level.FINE, "Testing {0} => {1}", new Object[] { spec.getKey(), spec.getValue() });
            Thunk<Object> thunk = interpreter.interpret(spec.getKey());
            interpreter.getExecutor().execute(thunk);
            assert thunk.isDone();
            assertEquals(spec.getValue(), thunk.get());
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
                Thunk<Object> thunk = interpreter.interpret(spec.getKey());
                interpreter.getExecutor().execute(thunk);
                assert thunk.isDone();
                thunk.get();
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