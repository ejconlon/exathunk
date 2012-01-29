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

/**
 * A concrete container type, to be accompanied by a VarContType.
 */
public class VarCont implements org.apache.thrift.TBase<VarCont, VarCont._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("VarCont");

  private static final org.apache.thrift.protocol.TField SINGLETON_CONT_FIELD_DESC = new org.apache.thrift.protocol.TField("singletonCont", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField LIST_CONT_FIELD_DESC = new org.apache.thrift.protocol.TField("listCont", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField SET_CONT_FIELD_DESC = new org.apache.thrift.protocol.TField("setCont", org.apache.thrift.protocol.TType.SET, (short)3);
  private static final org.apache.thrift.protocol.TField MAP_CONT_FIELD_DESC = new org.apache.thrift.protocol.TField("mapCont", org.apache.thrift.protocol.TType.MAP, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new VarContStandardSchemeFactory());
    schemes.put(TupleScheme.class, new VarContTupleSchemeFactory());
  }

  private Var singletonCont; // optional
  private List<Var> listCont; // optional
  private Set<Var> setCont; // optional
  private Map<Var,Var> mapCont; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SINGLETON_CONT((short)1, "singletonCont"),
    LIST_CONT((short)2, "listCont"),
    SET_CONT((short)3, "setCont"),
    MAP_CONT((short)4, "mapCont");

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
        case 1: // SINGLETON_CONT
          return SINGLETON_CONT;
        case 2: // LIST_CONT
          return LIST_CONT;
        case 3: // SET_CONT
          return SET_CONT;
        case 4: // MAP_CONT
          return MAP_CONT;
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
  private _Fields optionals[] = {_Fields.SINGLETON_CONT,_Fields.LIST_CONT,_Fields.SET_CONT,_Fields.MAP_CONT};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SINGLETON_CONT, new org.apache.thrift.meta_data.FieldMetaData("singletonCont", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Var.class)));
    tmpMap.put(_Fields.LIST_CONT, new org.apache.thrift.meta_data.FieldMetaData("listCont", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Var.class))));
    tmpMap.put(_Fields.SET_CONT, new org.apache.thrift.meta_data.FieldMetaData("setCont", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.SetMetaData(org.apache.thrift.protocol.TType.SET, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Var.class))));
    tmpMap.put(_Fields.MAP_CONT, new org.apache.thrift.meta_data.FieldMetaData("mapCont", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Var.class), 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Var.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(VarCont.class, metaDataMap);
  }

  public VarCont() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public VarCont(VarCont other) {
    if (other.isSetSingletonCont()) {
      this.singletonCont = new Var(other.singletonCont);
    }
    if (other.isSetListCont()) {
      List<Var> __this__listCont = new ArrayList<Var>();
      for (Var other_element : other.listCont) {
        __this__listCont.add(new Var(other_element));
      }
      this.listCont = __this__listCont;
    }
    if (other.isSetSetCont()) {
      Set<Var> __this__setCont = new HashSet<Var>();
      for (Var other_element : other.setCont) {
        __this__setCont.add(new Var(other_element));
      }
      this.setCont = __this__setCont;
    }
    if (other.isSetMapCont()) {
      Map<Var,Var> __this__mapCont = new HashMap<Var,Var>();
      for (Map.Entry<Var, Var> other_element : other.mapCont.entrySet()) {

        Var other_element_key = other_element.getKey();
        Var other_element_value = other_element.getValue();

        Var __this__mapCont_copy_key = new Var(other_element_key);

        Var __this__mapCont_copy_value = new Var(other_element_value);

        __this__mapCont.put(__this__mapCont_copy_key, __this__mapCont_copy_value);
      }
      this.mapCont = __this__mapCont;
    }
  }

  public VarCont deepCopy() {
    return new VarCont(this);
  }

  @Override
  public void clear() {
    this.singletonCont = null;
    this.listCont = null;
    this.setCont = null;
    this.mapCont = null;
  }

  public Var getSingletonCont() {
    return this.singletonCont;
  }

  public void setSingletonCont(Var singletonCont) {
    this.singletonCont = singletonCont;
  }

  public void unsetSingletonCont() {
    this.singletonCont = null;
  }

  /** Returns true if field singletonCont is set (has been assigned a value) and false otherwise */
  public boolean isSetSingletonCont() {
    return this.singletonCont != null;
  }

  public void setSingletonContIsSet(boolean value) {
    if (!value) {
      this.singletonCont = null;
    }
  }

  public int getListContSize() {
    return (this.listCont == null) ? 0 : this.listCont.size();
  }

  public java.util.Iterator<Var> getListContIterator() {
    return (this.listCont == null) ? null : this.listCont.iterator();
  }

  public void addToListCont(Var elem) {
    if (this.listCont == null) {
      this.listCont = new ArrayList<Var>();
    }
    this.listCont.add(elem);
  }

  public List<Var> getListCont() {
    return this.listCont;
  }

  public void setListCont(List<Var> listCont) {
    this.listCont = listCont;
  }

  public void unsetListCont() {
    this.listCont = null;
  }

  /** Returns true if field listCont is set (has been assigned a value) and false otherwise */
  public boolean isSetListCont() {
    return this.listCont != null;
  }

  public void setListContIsSet(boolean value) {
    if (!value) {
      this.listCont = null;
    }
  }

  public int getSetContSize() {
    return (this.setCont == null) ? 0 : this.setCont.size();
  }

  public java.util.Iterator<Var> getSetContIterator() {
    return (this.setCont == null) ? null : this.setCont.iterator();
  }

  public void addToSetCont(Var elem) {
    if (this.setCont == null) {
      this.setCont = new HashSet<Var>();
    }
    this.setCont.add(elem);
  }

  public Set<Var> getSetCont() {
    return this.setCont;
  }

  public void setSetCont(Set<Var> setCont) {
    this.setCont = setCont;
  }

  public void unsetSetCont() {
    this.setCont = null;
  }

  /** Returns true if field setCont is set (has been assigned a value) and false otherwise */
  public boolean isSetSetCont() {
    return this.setCont != null;
  }

  public void setSetContIsSet(boolean value) {
    if (!value) {
      this.setCont = null;
    }
  }

  public int getMapContSize() {
    return (this.mapCont == null) ? 0 : this.mapCont.size();
  }

  public void putToMapCont(Var key, Var val) {
    if (this.mapCont == null) {
      this.mapCont = new HashMap<Var,Var>();
    }
    this.mapCont.put(key, val);
  }

  public Map<Var,Var> getMapCont() {
    return this.mapCont;
  }

  public void setMapCont(Map<Var,Var> mapCont) {
    this.mapCont = mapCont;
  }

  public void unsetMapCont() {
    this.mapCont = null;
  }

  /** Returns true if field mapCont is set (has been assigned a value) and false otherwise */
  public boolean isSetMapCont() {
    return this.mapCont != null;
  }

  public void setMapContIsSet(boolean value) {
    if (!value) {
      this.mapCont = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SINGLETON_CONT:
      if (value == null) {
        unsetSingletonCont();
      } else {
        setSingletonCont((Var)value);
      }
      break;

    case LIST_CONT:
      if (value == null) {
        unsetListCont();
      } else {
        setListCont((List<Var>)value);
      }
      break;

    case SET_CONT:
      if (value == null) {
        unsetSetCont();
      } else {
        setSetCont((Set<Var>)value);
      }
      break;

    case MAP_CONT:
      if (value == null) {
        unsetMapCont();
      } else {
        setMapCont((Map<Var,Var>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SINGLETON_CONT:
      return getSingletonCont();

    case LIST_CONT:
      return getListCont();

    case SET_CONT:
      return getSetCont();

    case MAP_CONT:
      return getMapCont();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SINGLETON_CONT:
      return isSetSingletonCont();
    case LIST_CONT:
      return isSetListCont();
    case SET_CONT:
      return isSetSetCont();
    case MAP_CONT:
      return isSetMapCont();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof VarCont)
      return this.equals((VarCont)that);
    return false;
  }

  public boolean equals(VarCont that) {
    if (that == null)
      return false;

    boolean this_present_singletonCont = true && this.isSetSingletonCont();
    boolean that_present_singletonCont = true && that.isSetSingletonCont();
    if (this_present_singletonCont || that_present_singletonCont) {
      if (!(this_present_singletonCont && that_present_singletonCont))
        return false;
      if (!this.singletonCont.equals(that.singletonCont))
        return false;
    }

    boolean this_present_listCont = true && this.isSetListCont();
    boolean that_present_listCont = true && that.isSetListCont();
    if (this_present_listCont || that_present_listCont) {
      if (!(this_present_listCont && that_present_listCont))
        return false;
      if (!this.listCont.equals(that.listCont))
        return false;
    }

    boolean this_present_setCont = true && this.isSetSetCont();
    boolean that_present_setCont = true && that.isSetSetCont();
    if (this_present_setCont || that_present_setCont) {
      if (!(this_present_setCont && that_present_setCont))
        return false;
      if (!this.setCont.equals(that.setCont))
        return false;
    }

    boolean this_present_mapCont = true && this.isSetMapCont();
    boolean that_present_mapCont = true && that.isSetMapCont();
    if (this_present_mapCont || that_present_mapCont) {
      if (!(this_present_mapCont && that_present_mapCont))
        return false;
      if (!this.mapCont.equals(that.mapCont))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(VarCont other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    VarCont typedOther = (VarCont)other;

    lastComparison = Boolean.valueOf(isSetSingletonCont()).compareTo(typedOther.isSetSingletonCont());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSingletonCont()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.singletonCont, typedOther.singletonCont);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetListCont()).compareTo(typedOther.isSetListCont());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetListCont()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.listCont, typedOther.listCont);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetSetCont()).compareTo(typedOther.isSetSetCont());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSetCont()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.setCont, typedOther.setCont);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetMapCont()).compareTo(typedOther.isSetMapCont());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetMapCont()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.mapCont, typedOther.mapCont);
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
    StringBuilder sb = new StringBuilder("VarCont(");
    boolean first = true;

    if (isSetSingletonCont()) {
      sb.append("singletonCont:");
      if (this.singletonCont == null) {
        sb.append("null");
      } else {
        sb.append(this.singletonCont);
      }
      first = false;
    }
    if (isSetListCont()) {
      if (!first) sb.append(", ");
      sb.append("listCont:");
      if (this.listCont == null) {
        sb.append("null");
      } else {
        sb.append(this.listCont);
      }
      first = false;
    }
    if (isSetSetCont()) {
      if (!first) sb.append(", ");
      sb.append("setCont:");
      if (this.setCont == null) {
        sb.append("null");
      } else {
        sb.append(this.setCont);
      }
      first = false;
    }
    if (isSetMapCont()) {
      if (!first) sb.append(", ");
      sb.append("mapCont:");
      if (this.mapCont == null) {
        sb.append("null");
      } else {
        sb.append(this.mapCont);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
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

  private static class VarContStandardSchemeFactory implements SchemeFactory {
    public VarContStandardScheme getScheme() {
      return new VarContStandardScheme();
    }
  }

  private static class VarContStandardScheme extends StandardScheme<VarCont> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, VarCont struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SINGLETON_CONT
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.singletonCont = new Var();
              struct.singletonCont.read(iprot);
              struct.setSingletonContIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // LIST_CONT
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.listCont = new ArrayList<Var>(_list0.size);
                for (int _i1 = 0; _i1 < _list0.size; ++_i1)
                {
                  Var _elem2; // required
                  _elem2 = new Var();
                  _elem2.read(iprot);
                  struct.listCont.add(_elem2);
                }
                iprot.readListEnd();
              }
              struct.setListContIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // SET_CONT
            if (schemeField.type == org.apache.thrift.protocol.TType.SET) {
              {
                org.apache.thrift.protocol.TSet _set3 = iprot.readSetBegin();
                struct.setCont = new HashSet<Var>(2*_set3.size);
                for (int _i4 = 0; _i4 < _set3.size; ++_i4)
                {
                  Var _elem5; // required
                  _elem5 = new Var();
                  _elem5.read(iprot);
                  struct.setCont.add(_elem5);
                }
                iprot.readSetEnd();
              }
              struct.setSetContIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // MAP_CONT
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map6 = iprot.readMapBegin();
                struct.mapCont = new HashMap<Var,Var>(2*_map6.size);
                for (int _i7 = 0; _i7 < _map6.size; ++_i7)
                {
                  Var _key8; // required
                  Var _val9; // required
                  _key8 = new Var();
                  _key8.read(iprot);
                  _val9 = new Var();
                  _val9.read(iprot);
                  struct.mapCont.put(_key8, _val9);
                }
                iprot.readMapEnd();
              }
              struct.setMapContIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, VarCont struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.singletonCont != null) {
        if (struct.isSetSingletonCont()) {
          oprot.writeFieldBegin(SINGLETON_CONT_FIELD_DESC);
          struct.singletonCont.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.listCont != null) {
        if (struct.isSetListCont()) {
          oprot.writeFieldBegin(LIST_CONT_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.listCont.size()));
            for (Var _iter10 : struct.listCont)
            {
              _iter10.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.setCont != null) {
        if (struct.isSetSetCont()) {
          oprot.writeFieldBegin(SET_CONT_FIELD_DESC);
          {
            oprot.writeSetBegin(new org.apache.thrift.protocol.TSet(org.apache.thrift.protocol.TType.STRUCT, struct.setCont.size()));
            for (Var _iter11 : struct.setCont)
            {
              _iter11.write(oprot);
            }
            oprot.writeSetEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.mapCont != null) {
        if (struct.isSetMapCont()) {
          oprot.writeFieldBegin(MAP_CONT_FIELD_DESC);
          {
            oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRUCT, org.apache.thrift.protocol.TType.STRUCT, struct.mapCont.size()));
            for (Map.Entry<Var, Var> _iter12 : struct.mapCont.entrySet())
            {
              _iter12.getKey().write(oprot);
              _iter12.getValue().write(oprot);
            }
            oprot.writeMapEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class VarContTupleSchemeFactory implements SchemeFactory {
    public VarContTupleScheme getScheme() {
      return new VarContTupleScheme();
    }
  }

  private static class VarContTupleScheme extends TupleScheme<VarCont> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, VarCont struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetSingletonCont()) {
        optionals.set(0);
      }
      if (struct.isSetListCont()) {
        optionals.set(1);
      }
      if (struct.isSetSetCont()) {
        optionals.set(2);
      }
      if (struct.isSetMapCont()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetSingletonCont()) {
        struct.singletonCont.write(oprot);
      }
      if (struct.isSetListCont()) {
        {
          oprot.writeI32(struct.listCont.size());
          for (Var _iter13 : struct.listCont)
          {
            _iter13.write(oprot);
          }
        }
      }
      if (struct.isSetSetCont()) {
        {
          oprot.writeI32(struct.setCont.size());
          for (Var _iter14 : struct.setCont)
          {
            _iter14.write(oprot);
          }
        }
      }
      if (struct.isSetMapCont()) {
        {
          oprot.writeI32(struct.mapCont.size());
          for (Map.Entry<Var, Var> _iter15 : struct.mapCont.entrySet())
          {
            _iter15.getKey().write(oprot);
            _iter15.getValue().write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, VarCont struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.singletonCont = new Var();
        struct.singletonCont.read(iprot);
        struct.setSingletonContIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list16 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.listCont = new ArrayList<Var>(_list16.size);
          for (int _i17 = 0; _i17 < _list16.size; ++_i17)
          {
            Var _elem18; // required
            _elem18 = new Var();
            _elem18.read(iprot);
            struct.listCont.add(_elem18);
          }
        }
        struct.setListContIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TSet _set19 = new org.apache.thrift.protocol.TSet(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.setCont = new HashSet<Var>(2*_set19.size);
          for (int _i20 = 0; _i20 < _set19.size; ++_i20)
          {
            Var _elem21; // required
            _elem21 = new Var();
            _elem21.read(iprot);
            struct.setCont.add(_elem21);
          }
        }
        struct.setSetContIsSet(true);
      }
      if (incoming.get(3)) {
        {
          org.apache.thrift.protocol.TMap _map22 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRUCT, org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.mapCont = new HashMap<Var,Var>(2*_map22.size);
          for (int _i23 = 0; _i23 < _map22.size; ++_i23)
          {
            Var _key24; // required
            Var _val25; // required
            _key24 = new Var();
            _key24.read(iprot);
            _val25 = new Var();
            _val25.read(iprot);
            struct.mapCont.put(_key24, _val25);
          }
        }
        struct.setMapContIsSet(true);
      }
    }
  }

}

