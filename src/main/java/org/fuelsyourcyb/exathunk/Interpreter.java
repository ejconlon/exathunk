package org.fuelsyourcyb.exathunk;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.ExecutionException;

public class Interpreter<Source, Type, FuncId, PValue, Value> {
    private final NTreeParser<Source, FuncId, PValue> parser;
    private final TypeChecker<Type, PValue, Value> valueTyper;
    private final ThunkFactory<Type, FuncId, Value> thunkFactory;
    private final NTreeEvaluator<Type, FuncId, Value> evaluator;
    
    public Interpreter(NTreeParser<Source, FuncId, PValue> parser,
		       TypeChecker<Type, PValue, Value> valueTyper,
		       ThunkFactory<Type, FuncId, Value> thunkFactory,
		       NTreeEvaluator<Type, FuncId, Value> evaluator) {
	this.parser = parser;
	this.valueTyper = valueTyper;
	this.thunkFactory = thunkFactory;
	this.evaluator = evaluator;
    }

    public Value interpret(Source expression) throws UnknownFuncException, ParseException, TypeException, VisitException, ExecutionException {
	Logger logger = Logger.getLogger("Intepreter");

	logger.log(Level.FINE, "Expression {0}", expression);

	NTree<Unit, FuncId, PValue> parseTree = parser.parse(expression);
	logger.log(Level.FINE, "Parse tree {0}", parseTree);

	NTree<Type, FuncId, Value> typedTree = TypeCheckerUtils.makeTypedTree(thunkFactory, valueTyper, parseTree);
	logger.log(Level.FINE, "Typed tree {0}", typedTree);

	NTree<Type, FuncId, Thunk<Value>> thunkTree = ThunkUtils.makeThunkTree(thunkFactory, typedTree);
	logger.log(Level.FINE, "Thunk tree {0}", thunkTree);

	return evaluator.evaluate(thunkTree);
    }
}