package net.exathunk.remote;

import net.exathunk.base.*;
import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.RemoteExecutionService;
import org.apache.thrift.TException;

import java.util.List;

public class RemoteFuncDefLibrary implements FuncDefLibrary {

    private final RemoteExecutionService.Iface iface;

    public RemoteFuncDefLibrary(RemoteExecutionService.Iface iface) {
        this.iface = iface;
    }

    @Override
    public boolean knowsFunc(FuncId funcId) {
        try {
            getFuncDef(funcId);
            return true;
        } catch (UnknownFuncException e) {
            return false;
        }
    }

    @Override
    public FuncDef getFuncDef(FuncId funcId) throws UnknownFuncException {
        try {
            return iface.getFuncDef(funcId);
        } catch (TException | net.exathunk.genthrift.UnknownFuncException e) {
            throw new UnknownFuncException(e);
        }
    }

    @Override
    public List<FuncDef> getFuncDefs(List<FuncId> funcIds) throws UnknownFuncException {
        try {
            return iface.getFuncDefs(funcIds);
        } catch (TException | net.exathunk.genthrift.UnknownFuncException e) {
            throw new UnknownFuncException(e);
        }
    }
}
