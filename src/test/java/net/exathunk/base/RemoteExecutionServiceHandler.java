package net.exathunk.base;

import net.exathunk.genthrift.*;
import net.exathunk.genthrift.ExecutionException;
import net.exathunk.genthrift.UnknownFuncException;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RemoteExecutionServiceHandler implements RemoteExecutionService.Iface {

    private final FuncDefLibrary library;
    private final TypeChecker typeChecker;
    private final ThunkExecutor executor;
    
    private long idSrc = 0;
    private final HashMap<String, Thunk<VarCont>> thunkTable = new HashMap<>();

    public RemoteExecutionServiceHandler(FuncDefLibrary library, TypeChecker typeChecker, ThunkExecutor executor) {
        this.library = library;
        this.typeChecker = typeChecker;
        this.executor = executor;
    }
    
    @Override
    public FuncDef getFuncDef(FuncId funcId) throws UnknownFuncException, TException {
        try {
            return library.getFuncDef(funcId);
        } catch (net.exathunk.base.UnknownFuncException e) {
            throw new UnknownFuncException(e.getMessage());
        }
    }

    @Override
    public List<FuncDef> getFuncDefs(List<FuncId> funcIds) throws UnknownFuncException, TException {
        try {
            List<FuncDef> defs = new ArrayList<>(funcIds.size());
            for (FuncId funcId : funcIds) {
                defs.add(library.getFuncDef(funcId));
            }
            return defs;
        } catch (net.exathunk.base.UnknownFuncException e) {
            throw new UnknownFuncException(e.getMessage());
        }
    }

    @Override
    public RemoteThunk submitEvalRequest(EvalRequest evalRequest) throws ExecutionException, UnknownFuncException, TException {
        
        try {
            NTree<VarContType, FuncId, VarCont> typedTree = TypeCheckerUtils.makeTypedTreeFromRemote(
                    library, typeChecker, evalRequest.getFuncId(), evalRequest.getEvalArgs());
            Thunk<VarCont> thunk = executor.submit(typedTree);
            synchronized (this) {
                long id = idSrc++;
                String idStr = ""+id;
                RemoteThunk remoteThunk = new RemoteThunk(idStr);
                thunkTable.put(idStr, thunk);
                return remoteThunk;
            }
        } catch (TypeException | net.exathunk.base.UnknownFuncException e) {
            // TODO un-fuck exceptions
            throw new ExecutionException(e.getMessage());
        }
    }

    @Override
    public VarCont thunkGet(RemoteThunk thunk) throws ExecutionException, NotDoneException, TException {
        synchronized (this) {
            Thunk<VarCont> localThunk = thunkTable.get(thunk.getId());
            if (localThunk == null) {
                throw new ExecutionException("Thunk not present: "+thunk.getId());
            } else if (!localThunk.isDone()) {
                throw new NotDoneException("Thunk not done: "+thunk.getId());
            } else {
                VarCont value;
                try {
                    value = localThunk.get();
                } catch (java.util.concurrent.ExecutionException e) {
                    throw new ExecutionException(e.getMessage());
                }
                thunkTable.remove(thunk.getId());
                return value;
            }
        }
    }
}
