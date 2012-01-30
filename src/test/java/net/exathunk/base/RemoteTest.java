package net.exathunk.base;

import net.exathunk.genthrift.*;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class RemoteTest {

    private static RemoteExecutionServiceHandler makeHandler() {
        NFuncLibrary library = new SchemeyNFuncLibrary();
        TypeChecker checker = new SchemeyTypeChecker();
        ThunkExecutor executor = new DefaultThunkExecutor(library);
        return new RemoteExecutionServiceHandler(library, checker, executor);
    }
    
    @Test
    public void testRemote() throws Exception {
        String host = "localhost";
        int port = 45679;
        RemoteExecutionServiceHandler handler = makeHandler();

        //try (Server server = new Server(makeHandler(), port); Client client = new Client(host, port)) {
        //    server.open();
        //    new Thread(server).start();
        //    client.open();

            EvalRequest evalRequest = new EvalRequest();
            evalRequest.setFuncId(new FuncId("*"));

            VarTreeNode vtn1 = new VarTreeNode();
            vtn1.setValue(VarUtils.makeLongVarCont(4));
            VarTreeNode vtn2 = new VarTreeNode();
            vtn2.setValue(VarUtils.makeLongVarCont(5));

            evalRequest.addToEvalArgs(new VarTree(Collections.singletonList(vtn1), 0));
            evalRequest.addToEvalArgs(new VarTree(Collections.singletonList(vtn2), 0));

            RemoteThunk thunk = handler.submitEvalRequest(evalRequest);
            VarCont value = handler.thunkGet(thunk);
            assertEquals(20, value.getSingletonCont().getI64Var());
        //}
    }
}
