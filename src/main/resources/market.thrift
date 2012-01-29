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
       2: optional bool boolVar,
       3: optional byte byteVar,
       4: optional i16 i16Var,
       5: optional i32 i32Var,
       6: optional i64 i64Var,
       7: optional double doubleVar,
       8: optional string stringVar,
       9: optional binary binaryVar
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
    2: required list<ContType> argTypes,
    3: optional list<Strictness> strictnesses
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
struct ThunkId {
       1: required string id
}

/**
 * A future.
 */
struct Thunk {
       1: required ThunkId thunkId
}

/**
 * Something went wrong...
 */
struct ExecutionException {
       1: required string reason
}

/**
 * Submit a closure for evaluation and receive a future.
 */
service EvalService {
	Thunk submitEvalRequest(1: EvalRequest evalRequest)
}

/**
 * Call upon that future.
 */
service ThunkService {
	VarCont thunkGet(1: Thunk thunk)
}

