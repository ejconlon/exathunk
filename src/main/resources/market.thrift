namespace java net.exathunk.genthrift

enum RawType {
     VOID, BOOL, BYTE, I16, I32, I64, DOUBLE, STRING
}

struct EvalArg {
       1: required RawType rawType,
       2: optional bool boolArg,
       3: optional byte byteArg,
       4: optional i16 i16Arg,
       5: optional i32 i32Arg,
       6: optional i64 i64Arg,
       7: optional double doubleArg,
       8: optional string stringArg,
       9: optional binary binaryArg
}

struct FuncId {
       1: required string name
}

struct EvalRequest {
       1: required FuncId funcId
       2: list<EvalArg> evalArgs
}

struct ThunkId {
       1: required string id
}

struct IThunk {
       1: required ThunkId thunkId
}

struct ExecutionException {
       1: required string reason
}

service EvalService {
	IThunk submitEvalRequest(1: EvalRequest evalRequest)
}

service ThunkService {
	EvalArg thunkGet(1: IThunk thunk)
}

