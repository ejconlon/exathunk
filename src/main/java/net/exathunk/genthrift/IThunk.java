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

public class IThunk implements org.apache.thrift.TBase<IThunk, IThunk._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("IThunk");

  private static final org.apache.thrift.protocol.TField THUNK_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("thunkId", org.apache.thrift.protocol.TType.STRUCT, (short)1);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new IThunkStandardSchemeFactory());
    schemes.put(TupleScheme.class, new IThunkTupleSchemeFactory());
  }

  private ThunkId thunkId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    THUNK_ID((short)1, "thunkId");

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
        case 1: // THUNK_ID
          return THUNK_ID;
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
    tmpMap.put(_Fields.THUNK_ID, new org.apache.thrift.meta_data.FieldMetaData("thunkId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ThunkId.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(IThunk.class, metaDataMap);
  }

  public IThunk() {
  }

  public IThunk(
    ThunkId thunkId)
  {
    this();
    this.thunkId = thunkId;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public IThunk(IThunk other) {
    if (other.isSetThunkId()) {
      this.thunkId = new ThunkId(other.thunkId);
    }
  }

  public IThunk deepCopy() {
    return new IThunk(this);
  }

  @Override
  public void clear() {
    this.thunkId = null;
  }

  public ThunkId getThunkId() {
    return this.thunkId;
  }

  public void setThunkId(ThunkId thunkId) {
    this.thunkId = thunkId;
  }

  public void unsetThunkId() {
    this.thunkId = null;
  }

  /** Returns true if field thunkId is set (has been assigned a value) and false otherwise */
  public boolean isSetThunkId() {
    return this.thunkId != null;
  }

  public void setThunkIdIsSet(boolean value) {
    if (!value) {
      this.thunkId = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case THUNK_ID:
      if (value == null) {
        unsetThunkId();
      } else {
        setThunkId((ThunkId)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case THUNK_ID:
      return getThunkId();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case THUNK_ID:
      return isSetThunkId();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof IThunk)
      return this.equals((IThunk)that);
    return false;
  }

  public boolean equals(IThunk that) {
    if (that == null)
      return false;

    boolean this_present_thunkId = true && this.isSetThunkId();
    boolean that_present_thunkId = true && that.isSetThunkId();
    if (this_present_thunkId || that_present_thunkId) {
      if (!(this_present_thunkId && that_present_thunkId))
        return false;
      if (!this.thunkId.equals(that.thunkId))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(IThunk other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    IThunk typedOther = (IThunk)other;

    lastComparison = Boolean.valueOf(isSetThunkId()).compareTo(typedOther.isSetThunkId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetThunkId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.thunkId, typedOther.thunkId);
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
    StringBuilder sb = new StringBuilder("IThunk(");
    boolean first = true;

    sb.append("thunkId:");
    if (this.thunkId == null) {
      sb.append("null");
    } else {
      sb.append(this.thunkId);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetThunkId()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'thunkId' is unset! Struct:" + toString());
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

  private static class IThunkStandardSchemeFactory implements SchemeFactory {
    public IThunkStandardScheme getScheme() {
      return new IThunkStandardScheme();
    }
  }

  private static class IThunkStandardScheme extends StandardScheme<IThunk> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, IThunk struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // THUNK_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.thunkId = new ThunkId();
              struct.thunkId.read(iprot);
              struct.setThunkIdIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, IThunk struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.thunkId != null) {
        oprot.writeFieldBegin(THUNK_ID_FIELD_DESC);
        struct.thunkId.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class IThunkTupleSchemeFactory implements SchemeFactory {
    public IThunkTupleScheme getScheme() {
      return new IThunkTupleScheme();
    }
  }

  private static class IThunkTupleScheme extends TupleScheme<IThunk> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, IThunk struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      struct.thunkId.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, IThunk struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.thunkId = new ThunkId();
      struct.thunkId.read(iprot);
      struct.setThunkIdIsSet(true);
    }
  }

}

