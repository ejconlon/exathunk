package net.exathunk.base;

import net.exathunk.functional.Unit;
import net.exathunk.genthrift.FuncDef;
import net.exathunk.genthrift.FuncId;
import net.exathunk.genthrift.VarCont;
import net.exathunk.genthrift.VarContType;

import java.util.List;
import java.util.ArrayList;

public class TypeCheckerUtils {
    public static NTree<VarContType, FuncId, VarCont> makeTypedTree(
            FuncDefLibrary funcDefLibrary,
            TypeChecker typeChecker,
            NTree<Unit, String, String> parseTree)
            throws TypeException, UnknownFuncException {
        System.out.println(parseTree);
        NTree<VarContType, FuncId, VarCont> typedTree = new NTree<>();
        if (parseTree.isEmpty()) {
            throw new TypeException("Cannot type an empty node");
        } else if (parseTree.isLeaf()) {
            throw new TypeException("Cannot type a root leaf node");
        } else {
            String funcName = parseTree.getLabel();
            FuncId funcId = new FuncId(funcName);
            FuncDef funcDef = funcDefLibrary.getFuncDef(funcId);
            VarContType returnType = funcDef.getReturnType();
            List<VarContType> paramTypes = funcDef.getParameterTypes();
            List<NTree<Unit, String, String>> parseChildren = parseTree.getChildren();
            if (parseChildren.size() != paramTypes.size()) {
                throw new TypeException("Invalid arity for "+funcId+" (have "+parseChildren.size()+
                        " need "+(paramTypes.size())+")");
            }
            List<NTree<VarContType, FuncId, VarCont>> typedChildren =
                    new ArrayList<>(parseChildren.size());

            for (int i = 0; i < parseChildren.size(); ++i) {
                NTree<Unit, String, String> parseChild = parseChildren.get(i);
                VarContType type = paramTypes.get(i);
                if (parseChild.isLeaf()) {
                    String fromValue = parseChild.getValue();
                    VarCont toValue = typeChecker.cast(type, fromValue);
                    typedChildren.add(new NTree<VarContType, FuncId, VarCont>(type, toValue));
                } else {
                    NTree<VarContType, FuncId, VarCont> typedChild =
                            makeTypedTree(funcDefLibrary, typeChecker, parseChild);
                    if (!typeChecker.canCast(typedChild.getType(), type)) {
                        throw new TypeException("Could not type result of "+
                                typedChild.getLabel() + ". Expected "+
                                type + " but got "+
                                typedChild.getType());
                    }
                    typedChildren.add(typedChild);
                }
            }
            typedTree.setBranch(returnType, funcId, typedChildren);
        }
        return typedTree;
    }
}