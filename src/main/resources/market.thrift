namespace java net.exathunk.genthrift

/**
 * An underlying data type of a singleton variable
 */
enum VarType {
     VOID, BOOL, BYTE, I16, I32, I64, DOUBLE, STRING
}

/**
 * A union of concrete variable types, to be accompanied by a VarType
 */
struct Var {
       1: optional bool boolVar,
       2: optional byte byteVar,
       3: optional i16 i16Var,
       4: optional i32 i32Var,
       5: optional i64 i64Var,
       6: optional double doubleVar,
       7: optional string stringVar,
       8: optional binary binaryVar
}

/**
 * A container type
 */
enum ContType {
     VOID, SINGLETON, LIST, SET, MAP
}

/**
 * A container type and the wrapped types.
 * keyType will be null for all but MAP ContType.
 */
struct VarContType {
       1: required ContType contType,
       2: optional VarType keyType,
       3: optional VarType valueType,
}

/**
 * A concrete container type, to be accompanied by a VarContType.
 */
struct VarCont {
       1: optional Var singletonCont,
       2: optional list<Var> listCont,
       3: optional set<Var> setCont,
       4: optional map<Var, Var> mapCont
}

/**
 * The type of a tree of VarConts.  Inner nodes can have labels
 * of a different type from branch node values.
 */
struct VarTreeType {
       1: required VarContType labelType,
       2: required VarContType valueType
}

/**
 * A node in a VarTree. It will be either a leaf and only have a value,
 * or it will be a branch with a label and children.
 * See VarTree for child index info.
 */
struct VarTreeNode {
       1: optional VarCont value,
       2: optional VarCont label,
       3: optional list<i32> children
}

/**
 * A tree of VarConts, to be accompanied by a VarTreeType.
 * Because Thrift does not natively support recursive types,
 * we have to use indices in a list to address children.
 */
struct VarTree {
       1: required list<VarTreeNode> nodes,
       2: required i32 rootIndex
}

/**
 * A unique identifier of a function
 */
struct FuncId {
       1: required string name
}

enum Strictness {
    STRICT, LENIENT, LAZY
}

/**
 * Function definition
 */
struct FuncDef {
    1: required FuncId funcId,
    2: required VarContType returnType,
    3: required list<VarContType> parameterTypes,
    4: optional list<Strictness> strictnesses
}

/**
 * A request to evaluate the given function
 */
struct EvalRequest {
       1: required FuncId funcId
       2: list<VarTree> evalArgs
}

/**
 * An identifier of a future value.
 */
struct RemoteThunkId {
       1: required string id
}

/**
 * A future.
 */
struct RemoteThunk {
       1: required RemoteThunkId thunkId
}

/**
 * Something went wrong...
 */
exception ExecutionException {
       1: required string reason
}
exception UnknownFuncException {
       1: required string reason
}
exception NotDoneException {
       1: required string reason
}

service RemoteExecutionService {
    FuncDef getFuncDef(1: FuncId funcId)
        throws (1: UnknownFuncException unknown)
    list<FuncDef> getFuncDefs(1: list<FuncId> funcIds)
        throws (1: UnknownFuncException unknown)
    RemoteThunk submitEvalRequest(1: EvalRequest evalRequest)
    	throws (1: ExecutionException execution, 2: UnknownFuncException unknown)
	VarCont thunkGet(1: RemoteThunk thunk)
	    throws (1: ExecutionException execution, 2: NotDoneException notDone)
}

