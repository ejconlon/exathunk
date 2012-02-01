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

        EvalRequest evalRequest = TypeCheckerUtils.makeEvalRequest(library, checker, parseTree);
        logger.log(Level.FINE, "Eval request {0}", evalRequest);

        return evalRequest;
    }

    @Test
    public void testSimple() throws Exception {
        final String expression = "(* 4 5)";
        EvalRequest evalRequest = makeEvalRequest(expression);
        NTree<VarContType, FuncId, VarCont> postTree = TypeCheckerUtils.makeTypedTreeFromRemote(
                library, checker, evalRequest.getFuncId(), evalRequest.getEvalArgs());
        System.out.println(postTree);    // TODO assertions...
    }

    @Test
    public void testComplex() throws Exception {
        final String expression = "(* (- 5 6) (% 9 (/ 4 2)))";
        EvalRequest evalRequest = makeEvalRequest(expression);
        NTree<VarContType, FuncId, VarCont> postTree = TypeCheckerUtils.makeTypedTreeFromRemote(
                library, checker, evalRequest.getFuncId(), evalRequest.getEvalArgs());
        System.out.println(postTree);
    }
}