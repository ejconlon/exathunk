package net.exathunk.base;

import net.exathunk.functional.Unit;
import net.exathunk.genthrift.*;
import net.exathunk.schemey.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.logging.Logger;
import java.util.logging.Level;

public class EvalRequestTest {

    private final FuncDefLibrary library = new SchemeyNFuncLibrary();
    private final TypeChecker checker = new SchemeyTypeChecker();
    private final NTreeParser parser = new SexpParser();
    private final Logger logger = Logger.getLogger("EvalRequestTest");

    private EvalRequest makeEvalRequest(final String expression) throws Exception {
        logger.log(Level.FINE, "Expression {0}", expression);

        NTree<Unit, String, String> parseTree = parser.parse(expression);
        logger.log(Level.FINE, "Parse tree {0}", parseTree);

        NTree<VarContType, FuncId, VarCont> typedTree = TypeCheckerUtils.makeTypedTree(library, checker, parseTree);
        logger.log(Level.FINE, "Typed tree {0}", typedTree);

        EvalRequest evalRequest = TypeCheckerUtils.makeEvalRequest(library, checker, typedTree);
        logger.log(Level.FINE, "Eval request {0}", evalRequest);

        return evalRequest;
    }

    @Test
    public void testFuncIdAggregator() throws Exception {
        final String expression = "(* 4 5)";
        EvalRequest evalRequest = makeEvalRequest(expression);
    }
}