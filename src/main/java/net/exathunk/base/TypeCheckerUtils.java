package net.exathunk.base;

import net.exathunk.functional.Unit;

import java.util.List;
import java.util.ArrayList;

public class TypeCheckerUtils {
    public static <Type, FuncId, FromValue, ToValue> NTree<Type, FuncId, ToValue> makeTypedTree(
            ThunkFactory<Type, FuncId, ToValue> thunkFactory,
            TypeChecker<Type, FromValue, ToValue> typeChecker,
            NTree<Unit, FuncId, FromValue> parseTree)
            throws TypeException, UnknownFuncException {
        System.out.println(parseTree);
        NTree<Type, FuncId, ToValue> typedTree = new NTree<>();
        if (parseTree.isEmpty()) {
            throw new TypeException("Cannot type an empty node");
        } else if (parseTree.isLeaf()) {
            throw new TypeException("Cannot type a root leaf node");
        } else {
            FuncId funcId = parseTree.getLabel();
            Type returnType = thunkFactory.getReturnType(funcId);
            List<Type> paramTypes = thunkFactory.getParameterTypes(funcId);
            List<NTree<Unit, FuncId, FromValue>> parseChildren = parseTree.getChildren();
            if (parseChildren.size() != paramTypes.size()) {
                throw new TypeException("Invalid arity for "+funcId+" (have "+parseChildren.size()+
                        " need "+(paramTypes.size())+")");
            }
            List<NTree<Type, FuncId, ToValue>> typedChildren =
                    new ArrayList<>(parseChildren.size());

            for (int i = 0; i < parseChildren.size(); ++i) {
                NTree<Unit, FuncId, FromValue> parseChild = parseChildren.get(i);
                Type type = paramTypes.get(i);
                if (parseChild.isLeaf()) {
                    FromValue fromValue = parseChild.getValue();
                    ToValue toValue = typeChecker.cast(type, fromValue);
                    typedChildren.add(new NTree<Type, FuncId, ToValue>(type, toValue));
                } else {
                    NTree<Type, FuncId, ToValue> typedChild =
                            makeTypedTree(thunkFactory, typeChecker, parseChild);
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