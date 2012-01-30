package net.exathunk.base;

import net.exathunk.functional.Unit;
import net.exathunk.genthrift.*;

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
    
    public static NTree<VarContType, FuncId, VarCont> makeTypedTreeFromRemote(
            FuncDefLibrary funcDefLibrary,
            TypeChecker typeChecker,
            NTree<Unit, FuncId, VarCont> parseTree)
            throws TypeException, UnknownFuncException {
        System.out.println(parseTree);
        NTree<VarContType, FuncId, VarCont> typedTree = new NTree<>();
        if (parseTree.isEmpty()) {
            throw new TypeException("Cannot type an empty node");
        } else if (parseTree.isLeaf()) {
            throw new TypeException("Cannot type a root leaf node");
        } else {
            FuncId funcName = parseTree.getLabel();
            FuncId funcId = new FuncId(funcName);
            FuncDef funcDef = funcDefLibrary.getFuncDef(funcId);
            VarContType returnType = funcDef.getReturnType();
            List<VarContType> paramTypes = funcDef.getParameterTypes();
            List<NTree<Unit, FuncId, VarCont>> parseChildren = parseTree.getChildren();
            if (parseChildren.size() != paramTypes.size()) {
                throw new TypeException("Invalid arity for "+funcId+" (have "+parseChildren.size()+
                        " need "+(paramTypes.size())+")");
            }
            List<NTree<VarContType, FuncId, VarCont>> typedChildren =
                    new ArrayList<>(parseChildren.size());

            for (int i = 0; i < parseChildren.size(); ++i) {
                NTree<Unit, FuncId, VarCont> parseChild = parseChildren.get(i);
                VarContType type = paramTypes.get(i);
                if (parseChild.isLeaf()) {
                    VarCont value = parseChild.getValue();
                    if (!typeChecker.canCast(type, value)) {
                        throw new TypeException("Cannot cast "+value+" to "+type);
                    }
                    typedChildren.add(new NTree<VarContType, FuncId, VarCont>(type, value));
                } else {
                    NTree<VarContType, FuncId, VarCont> typedChild =
                            makeTypedTreeFromRemote(funcDefLibrary, typeChecker, parseChild);
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

    public static NTree<Unit, FuncId, VarCont> makeNativeRepFromRemote(
            FuncDefLibrary funcDefLibrary,
            List<VarTreeNode> arguments,
            int rootIndex) throws TypeException, UnknownFuncException {
        VarTreeNode root = arguments.get(rootIndex);
        if (root.isSetValue()) {
            return new NTree<>(Unit.getInstance(), root.getValue());
        }
        FuncId funcId = root.getLabel();
        FuncDef def = funcDefLibrary.getFuncDef(funcId);
        if (root.getChildrenSize() != def.getParameterTypesSize()) {
            throw new TypeException("Expected "+def.getParameterTypesSize()+" nodes, got "+root.getChildrenSize());
        }
        List<NTree<Unit, FuncId, VarCont>> children = new ArrayList<>(def.getParameterTypes().size());
        for (int index : root.getChildren()) {
            VarTreeNode node = arguments.get(index);
            if (node.isSetValue()) {
                VarCont value = node.getValue();
                children.add(new NTree<Unit, FuncId, VarCont>(Unit.getInstance(), value));
            } else {
                NTree<Unit, FuncId, VarCont> child = makeNativeRepFromRemote(funcDefLibrary, arguments, index);
                children.add(child);
            }
        }
        return new NTree<>(Unit.getInstance(), funcId, children);
    }
    
    public static NTree<Unit, FuncId, VarCont> makeNativeRepFromRemote(
            FuncDefLibrary funcDefLibrary,
            FuncId funcId,
            List<VarTree> varTrees) throws TypeException, UnknownFuncException {
        FuncDef def = funcDefLibrary.getFuncDef(funcId);
        if (varTrees.size() != def.getParameterTypesSize()) {
            throw new TypeException("Expected "+def.getParameterTypesSize()+" nodes, got "+varTrees.size());
        }
        List<NTree<Unit, FuncId, VarCont>> children = new ArrayList<>(def.getParameterTypes().size());
        for (VarTree varTree : varTrees) {
            children.add(makeNativeRepFromRemote(funcDefLibrary, varTree.getNodes(), varTree.getRootIndex()));
        }
        return new NTree<>(Unit.getInstance(), funcId, children);
    }

    public static NTree<VarContType, FuncId, VarCont> makeTypedTreeFromRemote(
            FuncDefLibrary funcDefLibrary,
            TypeChecker typeChecker,
            FuncId funcId,
            List<VarTree> varTrees) throws UnknownFuncException, TypeException {
        return makeTypedTreeFromRemote(funcDefLibrary, typeChecker,
                makeNativeRepFromRemote(funcDefLibrary, funcId, varTrees));
    }
}