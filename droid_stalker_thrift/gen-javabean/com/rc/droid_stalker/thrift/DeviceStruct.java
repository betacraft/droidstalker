/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.rc.droid_stalker.thrift;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.server.AbstractNonblockingServer.*;
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
 * Device Structure
 */
public class DeviceStruct implements org.apache.thrift.TBase<DeviceStruct, DeviceStruct._Fields>, java.io.Serializable, Cloneable, Comparable<DeviceStruct> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DeviceStruct");

  private static final org.apache.thrift.protocol.TField SERIAL_NUMBER_FIELD_DESC = new org.apache.thrift.protocol.TField("serialNumber", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField AVD_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("avdName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField DEVICE_STATE_FIELD_DESC = new org.apache.thrift.protocol.TField("deviceState", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField IS_EMULATOR_FIELD_DESC = new org.apache.thrift.protocol.TField("isEmulator", org.apache.thrift.protocol.TType.BOOL, (short)4);
  private static final org.apache.thrift.protocol.TField BATTERY_PERCENTAGE_FIELD_DESC = new org.apache.thrift.protocol.TField("batteryPercentage", org.apache.thrift.protocol.TType.I16, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new DeviceStructStandardSchemeFactory());
    schemes.put(TupleScheme.class, new DeviceStructTupleSchemeFactory());
  }

  private String serialNumber; // required
  private String avdName; // required
  private String deviceState; // required
  private boolean isEmulator; // required
  private short batteryPercentage; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    SERIAL_NUMBER((short)1, "serialNumber"),
    AVD_NAME((short)2, "avdName"),
    DEVICE_STATE((short)3, "deviceState"),
    IS_EMULATOR((short)4, "isEmulator"),
    BATTERY_PERCENTAGE((short)5, "batteryPercentage");

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
        case 1: // SERIAL_NUMBER
          return SERIAL_NUMBER;
        case 2: // AVD_NAME
          return AVD_NAME;
        case 3: // DEVICE_STATE
          return DEVICE_STATE;
        case 4: // IS_EMULATOR
          return IS_EMULATOR;
        case 5: // BATTERY_PERCENTAGE
          return BATTERY_PERCENTAGE;
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
  private static final int __ISEMULATOR_ISSET_ID = 0;
  private static final int __BATTERYPERCENTAGE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.SERIAL_NUMBER, new org.apache.thrift.meta_data.FieldMetaData("serialNumber", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.AVD_NAME, new org.apache.thrift.meta_data.FieldMetaData("avdName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.DEVICE_STATE, new org.apache.thrift.meta_data.FieldMetaData("deviceState", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.IS_EMULATOR, new org.apache.thrift.meta_data.FieldMetaData("isEmulator", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.BATTERY_PERCENTAGE, new org.apache.thrift.meta_data.FieldMetaData("batteryPercentage", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I16)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DeviceStruct.class, metaDataMap);
  }

  public DeviceStruct() {
  }

  public DeviceStruct(
    String serialNumber,
    String avdName,
    String deviceState,
    boolean isEmulator,
    short batteryPercentage)
  {
    this();
    this.serialNumber = serialNumber;
    this.avdName = avdName;
    this.deviceState = deviceState;
    this.isEmulator = isEmulator;
    setIsEmulatorIsSet(true);
    this.batteryPercentage = batteryPercentage;
    setBatteryPercentageIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DeviceStruct(DeviceStruct other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetSerialNumber()) {
      this.serialNumber = other.serialNumber;
    }
    if (other.isSetAvdName()) {
      this.avdName = other.avdName;
    }
    if (other.isSetDeviceState()) {
      this.deviceState = other.deviceState;
    }
    this.isEmulator = other.isEmulator;
    this.batteryPercentage = other.batteryPercentage;
  }

  public DeviceStruct deepCopy() {
    return new DeviceStruct(this);
  }

  @Override
  public void clear() {
    this.serialNumber = null;
    this.avdName = null;
    this.deviceState = null;
    setIsEmulatorIsSet(false);
    this.isEmulator = false;
    setBatteryPercentageIsSet(false);
    this.batteryPercentage = 0;
  }

  public String getSerialNumber() {
    return this.serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public void unsetSerialNumber() {
    this.serialNumber = null;
  }

  /** Returns true if field serialNumber is set (has been assigned a value) and false otherwise */
  public boolean isSetSerialNumber() {
    return this.serialNumber != null;
  }

  public void setSerialNumberIsSet(boolean value) {
    if (!value) {
      this.serialNumber = null;
    }
  }

  public String getAvdName() {
    return this.avdName;
  }

  public void setAvdName(String avdName) {
    this.avdName = avdName;
  }

  public void unsetAvdName() {
    this.avdName = null;
  }

  /** Returns true if field avdName is set (has been assigned a value) and false otherwise */
  public boolean isSetAvdName() {
    return this.avdName != null;
  }

  public void setAvdNameIsSet(boolean value) {
    if (!value) {
      this.avdName = null;
    }
  }

  public String getDeviceState() {
    return this.deviceState;
  }

  public void setDeviceState(String deviceState) {
    this.deviceState = deviceState;
  }

  public void unsetDeviceState() {
    this.deviceState = null;
  }

  /** Returns true if field deviceState is set (has been assigned a value) and false otherwise */
  public boolean isSetDeviceState() {
    return this.deviceState != null;
  }

  public void setDeviceStateIsSet(boolean value) {
    if (!value) {
      this.deviceState = null;
    }
  }

  public boolean isIsEmulator() {
    return this.isEmulator;
  }

  public void setIsEmulator(boolean isEmulator) {
    this.isEmulator = isEmulator;
    setIsEmulatorIsSet(true);
  }

  public void unsetIsEmulator() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ISEMULATOR_ISSET_ID);
  }

  /** Returns true if field isEmulator is set (has been assigned a value) and false otherwise */
  public boolean isSetIsEmulator() {
    return EncodingUtils.testBit(__isset_bitfield, __ISEMULATOR_ISSET_ID);
  }

  public void setIsEmulatorIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ISEMULATOR_ISSET_ID, value);
  }

  public short getBatteryPercentage() {
    return this.batteryPercentage;
  }

  public void setBatteryPercentage(short batteryPercentage) {
    this.batteryPercentage = batteryPercentage;
    setBatteryPercentageIsSet(true);
  }

  public void unsetBatteryPercentage() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __BATTERYPERCENTAGE_ISSET_ID);
  }

  /** Returns true if field batteryPercentage is set (has been assigned a value) and false otherwise */
  public boolean isSetBatteryPercentage() {
    return EncodingUtils.testBit(__isset_bitfield, __BATTERYPERCENTAGE_ISSET_ID);
  }

  public void setBatteryPercentageIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __BATTERYPERCENTAGE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case SERIAL_NUMBER:
      if (value == null) {
        unsetSerialNumber();
      } else {
        setSerialNumber((String)value);
      }
      break;

    case AVD_NAME:
      if (value == null) {
        unsetAvdName();
      } else {
        setAvdName((String)value);
      }
      break;

    case DEVICE_STATE:
      if (value == null) {
        unsetDeviceState();
      } else {
        setDeviceState((String)value);
      }
      break;

    case IS_EMULATOR:
      if (value == null) {
        unsetIsEmulator();
      } else {
        setIsEmulator((Boolean)value);
      }
      break;

    case BATTERY_PERCENTAGE:
      if (value == null) {
        unsetBatteryPercentage();
      } else {
        setBatteryPercentage((Short)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case SERIAL_NUMBER:
      return getSerialNumber();

    case AVD_NAME:
      return getAvdName();

    case DEVICE_STATE:
      return getDeviceState();

    case IS_EMULATOR:
      return Boolean.valueOf(isIsEmulator());

    case BATTERY_PERCENTAGE:
      return Short.valueOf(getBatteryPercentage());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case SERIAL_NUMBER:
      return isSetSerialNumber();
    case AVD_NAME:
      return isSetAvdName();
    case DEVICE_STATE:
      return isSetDeviceState();
    case IS_EMULATOR:
      return isSetIsEmulator();
    case BATTERY_PERCENTAGE:
      return isSetBatteryPercentage();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof DeviceStruct)
      return this.equals((DeviceStruct)that);
    return false;
  }

  public boolean equals(DeviceStruct that) {
    if (that == null)
      return false;

    boolean this_present_serialNumber = true && this.isSetSerialNumber();
    boolean that_present_serialNumber = true && that.isSetSerialNumber();
    if (this_present_serialNumber || that_present_serialNumber) {
      if (!(this_present_serialNumber && that_present_serialNumber))
        return false;
      if (!this.serialNumber.equals(that.serialNumber))
        return false;
    }

    boolean this_present_avdName = true && this.isSetAvdName();
    boolean that_present_avdName = true && that.isSetAvdName();
    if (this_present_avdName || that_present_avdName) {
      if (!(this_present_avdName && that_present_avdName))
        return false;
      if (!this.avdName.equals(that.avdName))
        return false;
    }

    boolean this_present_deviceState = true && this.isSetDeviceState();
    boolean that_present_deviceState = true && that.isSetDeviceState();
    if (this_present_deviceState || that_present_deviceState) {
      if (!(this_present_deviceState && that_present_deviceState))
        return false;
      if (!this.deviceState.equals(that.deviceState))
        return false;
    }

    boolean this_present_isEmulator = true;
    boolean that_present_isEmulator = true;
    if (this_present_isEmulator || that_present_isEmulator) {
      if (!(this_present_isEmulator && that_present_isEmulator))
        return false;
      if (this.isEmulator != that.isEmulator)
        return false;
    }

    boolean this_present_batteryPercentage = true;
    boolean that_present_batteryPercentage = true;
    if (this_present_batteryPercentage || that_present_batteryPercentage) {
      if (!(this_present_batteryPercentage && that_present_batteryPercentage))
        return false;
      if (this.batteryPercentage != that.batteryPercentage)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(DeviceStruct other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetSerialNumber()).compareTo(other.isSetSerialNumber());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSerialNumber()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serialNumber, other.serialNumber);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAvdName()).compareTo(other.isSetAvdName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAvdName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.avdName, other.avdName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDeviceState()).compareTo(other.isSetDeviceState());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDeviceState()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.deviceState, other.deviceState);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetIsEmulator()).compareTo(other.isSetIsEmulator());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetIsEmulator()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.isEmulator, other.isEmulator);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetBatteryPercentage()).compareTo(other.isSetBatteryPercentage());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBatteryPercentage()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.batteryPercentage, other.batteryPercentage);
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
    StringBuilder sb = new StringBuilder("DeviceStruct(");
    boolean first = true;

    sb.append("serialNumber:");
    if (this.serialNumber == null) {
      sb.append("null");
    } else {
      sb.append(this.serialNumber);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("avdName:");
    if (this.avdName == null) {
      sb.append("null");
    } else {
      sb.append(this.avdName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("deviceState:");
    if (this.deviceState == null) {
      sb.append("null");
    } else {
      sb.append(this.deviceState);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("isEmulator:");
    sb.append(this.isEmulator);
    first = false;
    if (!first) sb.append(", ");
    sb.append("batteryPercentage:");
    sb.append(this.batteryPercentage);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetSerialNumber()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'serialNumber' is unset! Struct:" + toString());
    }

    if (!isSetAvdName()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'avdName' is unset! Struct:" + toString());
    }

    if (!isSetDeviceState()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'deviceState' is unset! Struct:" + toString());
    }

    if (!isSetIsEmulator()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'isEmulator' is unset! Struct:" + toString());
    }

    if (!isSetBatteryPercentage()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'batteryPercentage' is unset! Struct:" + toString());
    }

    // check for sub-struct validity
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
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DeviceStructStandardSchemeFactory implements SchemeFactory {
    public DeviceStructStandardScheme getScheme() {
      return new DeviceStructStandardScheme();
    }
  }

  private static class DeviceStructStandardScheme extends StandardScheme<DeviceStruct> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DeviceStruct struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // SERIAL_NUMBER
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.serialNumber = iprot.readString();
              struct.setSerialNumberIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // AVD_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.avdName = iprot.readString();
              struct.setAvdNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // DEVICE_STATE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.deviceState = iprot.readString();
              struct.setDeviceStateIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // IS_EMULATOR
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.isEmulator = iprot.readBool();
              struct.setIsEmulatorIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // BATTERY_PERCENTAGE
            if (schemeField.type == org.apache.thrift.protocol.TType.I16) {
              struct.batteryPercentage = iprot.readI16();
              struct.setBatteryPercentageIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, DeviceStruct struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.serialNumber != null) {
        oprot.writeFieldBegin(SERIAL_NUMBER_FIELD_DESC);
        oprot.writeString(struct.serialNumber);
        oprot.writeFieldEnd();
      }
      if (struct.avdName != null) {
        oprot.writeFieldBegin(AVD_NAME_FIELD_DESC);
        oprot.writeString(struct.avdName);
        oprot.writeFieldEnd();
      }
      if (struct.deviceState != null) {
        oprot.writeFieldBegin(DEVICE_STATE_FIELD_DESC);
        oprot.writeString(struct.deviceState);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(IS_EMULATOR_FIELD_DESC);
      oprot.writeBool(struct.isEmulator);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(BATTERY_PERCENTAGE_FIELD_DESC);
      oprot.writeI16(struct.batteryPercentage);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DeviceStructTupleSchemeFactory implements SchemeFactory {
    public DeviceStructTupleScheme getScheme() {
      return new DeviceStructTupleScheme();
    }
  }

  private static class DeviceStructTupleScheme extends TupleScheme<DeviceStruct> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DeviceStruct struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.serialNumber);
      oprot.writeString(struct.avdName);
      oprot.writeString(struct.deviceState);
      oprot.writeBool(struct.isEmulator);
      oprot.writeI16(struct.batteryPercentage);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DeviceStruct struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.serialNumber = iprot.readString();
      struct.setSerialNumberIsSet(true);
      struct.avdName = iprot.readString();
      struct.setAvdNameIsSet(true);
      struct.deviceState = iprot.readString();
      struct.setDeviceStateIsSet(true);
      struct.isEmulator = iprot.readBool();
      struct.setIsEmulatorIsSet(true);
      struct.batteryPercentage = iprot.readI16();
      struct.setBatteryPercentageIsSet(true);
    }
  }

}

