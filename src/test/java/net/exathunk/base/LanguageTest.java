package net.exathunk.base;

import net.exathunk.genthrift.VarCont;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.logging.Level;

public class LanguageTest {
    public Interpreter makeInterpreter() {
        NFuncLibrary library = new SchemeyNFuncLibrary();
        NTreeParser parser = new SexpParser();
        TypeChecker checker = new SchemeyTypeChecker();
        ThunkExecutor<VarCont> executor = new DefaultThunkExecutor<>();
        return new Interpreter(parser, checker, library, executor);
    }

    private static Map<String, VarCont> makePosSpecs() {
        Map<String, VarCont> specs = new TreeMap<>();

        specs.put("(* 4 5)", VarUtils.makeLongVarCont(20));
        specs.put("(+ 4 (- 5 2))", VarUtils.makeLongVarCont(7));
        specs.put("(* 3 2)", VarUtils.makeLongVarCont(6));
        specs.put("(+ (- 1 2) (* 3 (/ 16 4)))", VarUtils.makeLongVarCont(11));
        specs.put("(not true)", VarUtils.makeBoolVarCont(false));
        specs.put("(or  true false)", VarUtils.makeBoolVarCont(true));
        specs.put("(- (len foo) 1)", VarUtils.makeLongVarCont(2));
        //specs.put("(or true (bottom))", VarUtils.makeBoolVarCont(true));
        //specs.put("(if true foo bar)", VarUtils.makeStringVarCont("foo"));
        //specs.put("(if false foo bar)", VarUtils.makeStringVarCont("bar"));

        return specs;
    }

    private static Map<String, Class> makeNegSpecs() {
        Map<String, Class> specs = new TreeMap<>();

        specs.put("(* 4 true)", TypeException.class);
        specs.put("(blah 4 true)", UnknownFuncException.class);
        //specs.put("(bottom)", ExecutionException.class);

        return specs;
    }

    @Test
    public void testPosPrograms() throws Exception {
        Interpreter interpreter = makeInterpreter();
        Logger logger = Logger.getLogger("LanguageTest");

        for (Map.Entry<String, VarCont> spec : makePosSpecs().entrySet()) {
            logger.log(Level.FINE, "Testing {0} => {1}", new Object[] { spec.getKey(), spec.getValue() });
            Thunk<VarCont> thunk = interpreter.interpret(spec.getKey());
            Thunk<VarCont> exeThunk = interpreter.getExecutor().submit(thunk);
            assert exeThunk.isDone();
            assertEquals(spec.getValue(), exeThunk.get());
        }
    }

    @Test
    public void testNegPrograms() {
        Interpreter interpreter = makeInterpreter();
        Logger logger = Logger.getLogger("LanguageTest");

        for (Map.Entry<String, Class> spec : makeNegSpecs().entrySet()) {
            logger.log(Level.FINE, "Testing {0} throws {1}", new Object[] { spec.getKey(), spec.getValue() });
            boolean caught = false;
            try {
                Thunk<VarCont> thunk = interpreter.interpret(spec.getKey());
                Thunk<VarCont> exeThunk = interpreter.getExecutor().submit(thunk);
                assert exeThunk.isDone();
                exeThunk.get();
            } catch (Exception e) {
                caught = true;
                System.err.println("Expected "+spec.getValue());
                System.err.println("Got "+e);
                assert(spec.getValue().isInstance(e));
            }

            if (!caught) {
                assert(false);
            }
        }
    }

}