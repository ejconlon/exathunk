package net.exathunk.remote;

import net.exathunk.base.*;
import net.exathunk.genthrift.NotDoneException;
import net.exathunk.genthrift.RemoteExecutionService;
import net.exathunk.genthrift.RemoteThunk;
import net.exathunk.genthrift.VarCont;
import org.apache.thrift.TException;

import java.util.concurrent.ExecutionException;

public class RemoteThunkWrapper implements Thunk<VarCont> {

    private final RemoteExecutionService.Iface iface;
    private final RemoteThunk remoteThunk;
    private Throwable thrown;
    private VarCont value;

    public RemoteThunkWrapper(RemoteExecutionService.Iface iface, RemoteThunk remoteThunk) {
        this.iface = iface;
        this.remoteThunk = remoteThunk;
    }
    
    public RemoteThunkWrapper(Throwable thrown) {
        this(null, null);
        this.thrown = thrown;
    }
    
    @Override
    public void run() {
        isDone(); 
    }

    @Override
    public boolean isDone() {
        if (value != null || thrown != null) return true;
        try {
            value = iface.thunkGet(remoteThunk);
            return true;
        } catch (net.exathunk.genthrift.ExecutionException | TException e) {
            thrown = e;
            return true;
        } catch (NotDoneException e) {
            return false;
        }
    }

    @Override
    public VarCont get() throws ExecutionException {
        if (value == null && thrown == null) isDone();
        if (thrown != null) throw new SystemExecutionException(thrown);
        return value;
    }
}
