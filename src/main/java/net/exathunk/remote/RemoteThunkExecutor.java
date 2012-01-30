package net.exathunk.remote;

import net.exathunk.base.*;
import net.exathunk.genthrift.*;
import net.exathunk.genthrift.UnknownFuncException;
import org.apache.thrift.TException;

public class RemoteThunkExecutor implements ThunkExecutor {

    private final RemoteExecutionService.Iface iface;

    public RemoteThunkExecutor(RemoteExecutionService.Iface iface) {
        this.iface = iface;
    }

    @Override
    public Thunk<VarCont> submit(NTree<VarContType, FuncId, VarCont> tree) {
        EvalRequest evalRequest = new EvalRequest();

        try {
            RemoteThunk remoteThunk = iface.submitEvalRequest(evalRequest);
            return new RemoteThunkWrapper(iface, remoteThunk);
        } catch (ExecutionException | UnknownFuncException | TException e) {
            return new RemoteThunkWrapper(e);
        }
    }
}
