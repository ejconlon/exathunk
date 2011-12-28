package org.fuelsyourcyb.exathunk;

import java.util.List;
import java.util.ArrayList;

public class TypeCheckerUtils {
    public static <Type, FuncId, FromValue, ToValue> NTree<Type, FuncId, ToValue> makeTypedTree(
            ThunkFactory<Type, FuncId, ToValue> factory,
            TypeChecker<Type, FromValue, ToValue> checker, 
            NTree<Unit, FuncId, FromValue> parseTree)
	    throws TypeException, UnknownFuncException {
	NTree<Type, FuncId, ToValue> typedTree = new NTree<>();
	if (parseTree.isEmpty()) {
	    throw new TypeException("Cannot type an empty node");
	} else if (parseTree.isLeaf()) {
	    throw new TypeException("Cannot type a root leaf node");
	} else {
	    FuncId funcId = parseTree.getLabel();
	    List<Type> typeSpec = factory.getTypeSpec(funcId);
	    List<NTree<Unit, FuncId, FromValue>> parseChildren = parseTree.getChildren();
	    if (parseChildren.size() != typeSpec.size() - 1) {
		throw new TypeException("Invalid arity for "+funcId+" (have "+parseChildren.size()+
					" need "+(typeSpec.size()-1));
	    }
	    List<NTree<Type, FuncId, ToValue>> typedChildren =
		new ArrayList<>(parseChildren.size());

	    for (int i = 0; i < parseChildren.size(); ++i) {
		NTree<Unit, FuncId, FromValue> parseChild = parseChildren.get(i);
		Type type = typeSpec.get(i);
		if (parseChild.isLeaf()) {
		    FromValue fromValue = parseChild.getValue();
		    ToValue toValue = checker.convert(type, fromValue);
		    typedChildren.add(new NTree<Type, FuncId, ToValue>(type, toValue));
		} else {
		    NTree<Type, FuncId, ToValue> typedChild =
			makeTypedTree(factory, checker, parseChild);
		    if (!type.equals(typedChild.getType())) {
			throw new TypeException("Could not type result of "+
						typedChild.getLabel() + ". Expected "+
						type + " but got "+
						typedChild.getType());
		    }
		    typedChildren.add(typedChild);
		}
	    }
	    typedTree.setBranch(typeSpec.get(typeSpec.size()-1), funcId, typedChildren);
	}
	return typedTree;
    };
}