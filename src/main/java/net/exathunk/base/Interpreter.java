package net.exathunk.base;

import net.exathunk.functional.Unit;
import net.exathunk.functional.VisitException;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.ExecutionException;

public class Interpreter<Source, Type, FuncId, PValue, Value> {
    private final NTreeParser<Source, FuncId, PValue> parser;
    private final TypeChecker<Type, PValue, Value> valueTyper;
    private final ThunkFactory<Type, FuncId, Value> thunkFactory;
    private final ThunkExecutor<Value> executor;

    public Interpreter(NTreeParser<Source, FuncId, PValue> parser,
                       TypeChecker<Type, PValue, Value> valueTyper,
                       ThunkFactory<Type, FuncId, Value> thunkFactory,
                       ThunkExecutor<Value> executor) {
        this.parser = parser;
        this.valueTyper = valueTyper;
        this.thunkFactory = thunkFactory;
        this.executor = executor;
    }

    public Thunk<Value> interpret(Source expression) throws UnknownFuncException, ParseException, TypeException, VisitException, ExecutionException {
        Logger logger = Logger.getLogger("Intepreter");

        logger.log(Level.FINE, "Expression {0}", expression);

        NTree<Unit, FuncId, PValue> parseTree = parser.parse(expression);
        logger.log(Level.FINE, "Parse tree {0}", parseTree);

        NTree<Type, FuncId, Value> typedTree = TypeCheckerUtils.makeTypedTree(thunkFactory, valueTyper, parseTree);
        logger.log(Level.FINE, "Typed tree {0}", typedTree);

        return thunkFactory.makeThunk(executor, typedTree);
    }

    public ThunkExecutor<Value> getExecutor() { return executor; }
}