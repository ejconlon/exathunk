package net.exathunk.base;

import net.exathunk.functional.Unit;
import net.exathunk.functional.VisitException;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.ExecutionException;

public class Interpreter {
    private final NTreeParser parser;
    private final TypeChecker valueTyper;
    private final NFuncLibrary library;
    private final ThunkExecutor<VarCont> executor;

    public Interpreter(NTreeParser parser,
                       TypeChecker valueTyper,
                       NFuncLibrary library,
                       ThunkExecutor<VarCont> executor) {
        this.parser = parser;
        this.valueTyper = valueTyper;
        this.library = library;
        this.executor = executor;
    }

    public Thunk<VarCont> interpret(String expression) throws UnknownFuncException, ParseException, TypeException, VisitException, ExecutionException {
        Logger logger = Logger.getLogger("Intepreter");

        logger.log(Level.FINE, "Expression {0}", expression);

        NTree<Unit, String, String> parseTree = parser.parse(expression);
        logger.log(Level.FINE, "Parse tree {0}", parseTree);

        NTree<VarContType, FuncId, VarCont> typedTree = TypeCheckerUtils.makeTypedTree(library, valueTyper, parseTree);
        logger.log(Level.FINE, "Typed tree {0}", typedTree);

        return ThunkUtils.makeThunk(library, executor, typedTree);
    }

    public ThunkExecutor<VarCont> getExecutor() { return executor; }
}