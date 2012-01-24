/**
 * Autogenerated by Thrift Compiler (0.8.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package net.exathunk.genthrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EvalRequest implements org.apache.thrift.TBase<EvalRequest, EvalRequest._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("EvalRequest");

  private static final org.apache.thrift.protocol.TField FUNC_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("funcId", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField EVAL_ARGS_FIELD_DESC = new org.apache.thrift.protocol.TField("evalArgs", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new EvalRequestStandardSchemeFactory());
    schemes.put(TupleScheme.class, new EvalRequestTupleSchemeFactory());
  }

  private FuncId funcId; // required
  private List<EvalArg> evalArgs; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    FUNC_ID((short)1, "funcId"),
    EVAL_ARGS((short)2, "evalArgs");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // FUNC_ID
          return FUNC_ID;
        case 2: // EVAL_ARGS
          return EVAL_ARGS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.FUNC_ID, new org.apache.thrift.meta_data.FieldMetaData("funcId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, FuncId.class)));
    tmpMap.put(_Fields.EVAL_ARGS, new org.apache.thrift.meta_data.FieldMetaData("evalArgs", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, EvalArg.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(EvalRequest.class, metaDataMap);
  }

  public EvalRequest() {
  }

  public EvalRequest(
    FuncId funcId,
    List<EvalArg> evalArgs)
  {
    this();
    this.funcId = funcId;
    this.evalArgs = evalArgs;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public EvalRequest(EvalRequest other) {
    if (other.isSetFuncId()) {
      this.funcId = new FuncId(other.funcId);
    }
    if (other.isSetEvalArgs()) {
      List<EvalArg> __this__evalArgs = new ArrayList<EvalArg>();
      for (EvalArg other_element : other.evalArgs) {
        __this__evalArgs.add(new EvalArg(other_element));
      }
      this.evalArgs = __this__evalArgs;
    }
  }

  public EvalRequest deepCopy() {
    return new EvalRequest(this);
  }

  @Override
  public void clear() {
    this.funcId = null;
    this.evalArgs = null;
  }

  public FuncId getFuncId() {
    return this.funcId;
  }

  public void setFuncId(FuncId funcId) {
    this.funcId = funcId;
  }

  public void unsetFuncId() {
    this.funcId = null;
  }

  /** Returns true if field funcId is set (has been assigned a value) and false otherwise */
  public boolean isSetFuncId() {
    return this.funcId != null;
  }

  public void setFuncIdIsSet(boolean value) {
    if (!value) {
      this.funcId = null;
    }
  }

  public int getEvalArgsSize() {
    return (this.evalArgs == null) ? 0 : this.evalArgs.size();
  }

  public java.util.Iterator<EvalArg> getEvalArgsIterator() {
    return (this.evalArgs == null) ? null : this.evalArgs.iterator();
  }

  public void addToEvalArgs(EvalArg elem) {
    if (this.evalArgs == null) {
      this.evalArgs = new ArrayList<EvalArg>();
    }
    this.evalArgs.add(elem);
  }

  public List<EvalArg> getEvalArgs() {
    return this.evalArgs;
  }

  public void setEvalArgs(List<EvalArg> evalArgs) {
    this.evalArgs = evalArgs;
  }

  public void unsetEvalArgs() {
    this.evalArgs = null;
  }

  /** Returns true if field evalArgs is set (has been assigned a value) and false otherwise */
  public boolean isSetEvalArgs() {
    return this.evalArgs != null;
  }

  public void setEvalArgsIsSet(boolean value) {
    if (!value) {
      this.evalArgs = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case FUNC_ID:
      if (value == null) {
        unsetFuncId();
      } else {
        setFuncId((FuncId)value);
      }
      break;

    case EVAL_ARGS:
      if (value == null) {
        unsetEvalArgs();
      } else {
        setEvalArgs((List<EvalArg>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case FUNC_ID:
      return getFuncId();

    case EVAL_ARGS:
      return getEvalArgs();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case FUNC_ID:
      return isSetFuncId();
    case EVAL_ARGS:
      return isSetEvalArgs();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof EvalRequest)
      return this.equals((EvalRequest)that);
    return false;
  }

  public boolean equals(EvalRequest that) {
    if (that == null)
      return false;

    boolean this_present_funcId = true && this.isSetFuncId();
    boolean that_present_funcId = true && that.isSetFuncId();
    if (this_present_funcId || that_present_funcId) {
      if (!(this_present_funcId && that_present_funcId))
        return false;
      if (!this.funcId.equals(that.funcId))
        return false;
    }

    boolean this_present_evalArgs = true && this.isSetEvalArgs();
    boolean that_present_evalArgs = true && that.isSetEvalArgs();
    if (this_present_evalArgs || that_present_evalArgs) {
      if (!(this_present_evalArgs && that_present_evalArgs))
        return false;
      if (!this.evalArgs.equals(that.evalArgs))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(EvalRequest other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    EvalRequest typedOther = (EvalRequest)other;

    lastComparison = Boolean.valueOf(isSetFuncId()).compareTo(typedOther.isSetFuncId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFuncId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.funcId, typedOther.funcId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEvalArgs()).compareTo(typedOther.isSetEvalArgs());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEvalArgs()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.evalArgs, typedOther.evalArgs);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("EvalRequest(");
    boolean first = true;

    sb.append("funcId:");
    if (this.funcId == null) {
      sb.append("null");
    } else {
      sb.append(this.funcId);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("evalArgs:");
    if (this.evalArgs == null) {
      sb.append("null");
    } else {
      sb.append(this.evalArgs);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetFuncId()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'funcId' is unset! Struct:" + toString());
    }

  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class EvalRequestStandardSchemeFactory implements SchemeFactory {
    public EvalRequestStandardScheme getScheme() {
      return new EvalRequestStandardScheme();
    }
  }

  private static class EvalRequestStandardScheme extends StandardScheme<EvalRequest> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, EvalRequest struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // FUNC_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.funcId = new FuncId();
              struct.funcId.read(iprot);
              struct.setFuncIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // EVAL_ARGS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.evalArgs = new ArrayList<EvalArg>(_list0.size);
                for (int _i1 = 0; _i1 < _list0.size; ++_i1)
                {
                  EvalArg _elem2; // required
                  _elem2 = new EvalArg();
                  _elem2.read(iprot);
                  struct.evalArgs.add(_elem2);
                }
                iprot.readListEnd();
              }
              struct.setEvalArgsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, EvalRequest struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.funcId != null) {
        oprot.writeFieldBegin(FUNC_ID_FIELD_DESC);
        struct.funcId.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.evalArgs != null) {
        oprot.writeFieldBegin(EVAL_ARGS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.evalArgs.size()));
          for (EvalArg _iter3 : struct.evalArgs)
          {
            _iter3.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class EvalRequestTupleSchemeFactory implements SchemeFactory {
    public EvalRequestTupleScheme getScheme() {
      return new EvalRequestTupleScheme();
    }
  }

  private static class EvalRequestTupleScheme extends TupleScheme<EvalRequest> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, EvalRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      struct.funcId.write(oprot);
      BitSet optionals = new BitSet();
      if (struct.isSetEvalArgs()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetEvalArgs()) {
        {
          oprot.writeI32(struct.evalArgs.size());
          for (EvalArg _iter4 : struct.evalArgs)
          {
            _iter4.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, EvalRequest struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.funcId = new FuncId();
      struct.funcId.read(iprot);
      struct.setFuncIdIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list5 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.evalArgs = new ArrayList<EvalArg>(_list5.size);
          for (int _i6 = 0; _i6 < _list5.size; ++_i6)
          {
            EvalArg _elem7; // required
            _elem7 = new EvalArg();
            _elem7.read(iprot);
            struct.evalArgs.add(_elem7);
          }
        }
        struct.setEvalArgsIsSet(true);
      }
    }
  }

}

