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
public class AndroidAppStruct implements org.apache.thrift.TBase<AndroidAppStruct, AndroidAppStruct._Fields>, java.io.Serializable, Cloneable, Comparable<AndroidAppStruct> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("AndroidAppStruct");

  private static final org.apache.thrift.protocol.TField PACKAGE_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("packageName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField ACTIVITY_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("activityName", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField APPLICATION_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("applicationName", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField APPLICATION_ICON_FIELD_DESC = new org.apache.thrift.protocol.TField("applicationIcon", org.apache.thrift.protocol.TType.STRING, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new AndroidAppStructStandardSchemeFactory());
    schemes.put(TupleScheme.class, new AndroidAppStructTupleSchemeFactory());
  }

  private String packageName; // required
  private String activityName; // required
  private String applicationName; // required
  private String applicationIcon; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PACKAGE_NAME((short)1, "packageName"),
    ACTIVITY_NAME((short)2, "activityName"),
    APPLICATION_NAME((short)3, "applicationName"),
    APPLICATION_ICON((short)4, "applicationIcon");

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
        case 1: // PACKAGE_NAME
          return PACKAGE_NAME;
        case 2: // ACTIVITY_NAME
          return ACTIVITY_NAME;
        case 3: // APPLICATION_NAME
          return APPLICATION_NAME;
        case 4: // APPLICATION_ICON
          return APPLICATION_ICON;
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
    tmpMap.put(_Fields.PACKAGE_NAME, new org.apache.thrift.meta_data.FieldMetaData("packageName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ACTIVITY_NAME, new org.apache.thrift.meta_data.FieldMetaData("activityName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.APPLICATION_NAME, new org.apache.thrift.meta_data.FieldMetaData("applicationName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.APPLICATION_ICON, new org.apache.thrift.meta_data.FieldMetaData("applicationIcon", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(AndroidAppStruct.class, metaDataMap);
  }

  public AndroidAppStruct() {
  }

  public AndroidAppStruct(
    String packageName,
    String activityName,
    String applicationName,
    String applicationIcon)
  {
    this();
    this.packageName = packageName;
    this.activityName = activityName;
    this.applicationName = applicationName;
    this.applicationIcon = applicationIcon;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public AndroidAppStruct(AndroidAppStruct other) {
    if (other.isSetPackageName()) {
      this.packageName = other.packageName;
    }
    if (other.isSetActivityName()) {
      this.activityName = other.activityName;
    }
    if (other.isSetApplicationName()) {
      this.applicationName = other.applicationName;
    }
    if (other.isSetApplicationIcon()) {
      this.applicationIcon = other.applicationIcon;
    }
  }

  public AndroidAppStruct deepCopy() {
    return new AndroidAppStruct(this);
  }

  @Override
  public void clear() {
    this.packageName = null;
    this.activityName = null;
    this.applicationName = null;
    this.applicationIcon = null;
  }

  public String getPackageName() {
    return this.packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public void unsetPackageName() {
    this.packageName = null;
  }

  /** Returns true if field packageName is set (has been assigned a value) and false otherwise */
  public boolean isSetPackageName() {
    return this.packageName != null;
  }

  public void setPackageNameIsSet(boolean value) {
    if (!value) {
      this.packageName = null;
    }
  }

  public String getActivityName() {
    return this.activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  public void unsetActivityName() {
    this.activityName = null;
  }

  /** Returns true if field activityName is set (has been assigned a value) and false otherwise */
  public boolean isSetActivityName() {
    return this.activityName != null;
  }

  public void setActivityNameIsSet(boolean value) {
    if (!value) {
      this.activityName = null;
    }
  }

  public String getApplicationName() {
    return this.applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  public void unsetApplicationName() {
    this.applicationName = null;
  }

  /** Returns true if field applicationName is set (has been assigned a value) and false otherwise */
  public boolean isSetApplicationName() {
    return this.applicationName != null;
  }

  public void setApplicationNameIsSet(boolean value) {
    if (!value) {
      this.applicationName = null;
    }
  }

  public String getApplicationIcon() {
    return this.applicationIcon;
  }

  public void setApplicationIcon(String applicationIcon) {
    this.applicationIcon = applicationIcon;
  }

  public void unsetApplicationIcon() {
    this.applicationIcon = null;
  }

  /** Returns true if field applicationIcon is set (has been assigned a value) and false otherwise */
  public boolean isSetApplicationIcon() {
    return this.applicationIcon != null;
  }

  public void setApplicationIconIsSet(boolean value) {
    if (!value) {
      this.applicationIcon = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PACKAGE_NAME:
      if (value == null) {
        unsetPackageName();
      } else {
        setPackageName((String)value);
      }
      break;

    case ACTIVITY_NAME:
      if (value == null) {
        unsetActivityName();
      } else {
        setActivityName((String)value);
      }
      break;

    case APPLICATION_NAME:
      if (value == null) {
        unsetApplicationName();
      } else {
        setApplicationName((String)value);
      }
      break;

    case APPLICATION_ICON:
      if (value == null) {
        unsetApplicationIcon();
      } else {
        setApplicationIcon((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PACKAGE_NAME:
      return getPackageName();

    case ACTIVITY_NAME:
      return getActivityName();

    case APPLICATION_NAME:
      return getApplicationName();

    case APPLICATION_ICON:
      return getApplicationIcon();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PACKAGE_NAME:
      return isSetPackageName();
    case ACTIVITY_NAME:
      return isSetActivityName();
    case APPLICATION_NAME:
      return isSetApplicationName();
    case APPLICATION_ICON:
      return isSetApplicationIcon();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof AndroidAppStruct)
      return this.equals((AndroidAppStruct)that);
    return false;
  }

  public boolean equals(AndroidAppStruct that) {
    if (that == null)
      return false;

    boolean this_present_packageName = true && this.isSetPackageName();
    boolean that_present_packageName = true && that.isSetPackageName();
    if (this_present_packageName || that_present_packageName) {
      if (!(this_present_packageName && that_present_packageName))
        return false;
      if (!this.packageName.equals(that.packageName))
        return false;
    }

    boolean this_present_activityName = true && this.isSetActivityName();
    boolean that_present_activityName = true && that.isSetActivityName();
    if (this_present_activityName || that_present_activityName) {
      if (!(this_present_activityName && that_present_activityName))
        return false;
      if (!this.activityName.equals(that.activityName))
        return false;
    }

    boolean this_present_applicationName = true && this.isSetApplicationName();
    boolean that_present_applicationName = true && that.isSetApplicationName();
    if (this_present_applicationName || that_present_applicationName) {
      if (!(this_present_applicationName && that_present_applicationName))
        return false;
      if (!this.applicationName.equals(that.applicationName))
        return false;
    }

    boolean this_present_applicationIcon = true && this.isSetApplicationIcon();
    boolean that_present_applicationIcon = true && that.isSetApplicationIcon();
    if (this_present_applicationIcon || that_present_applicationIcon) {
      if (!(this_present_applicationIcon && that_present_applicationIcon))
        return false;
      if (!this.applicationIcon.equals(that.applicationIcon))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public int compareTo(AndroidAppStruct other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetPackageName()).compareTo(other.isSetPackageName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPackageName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.packageName, other.packageName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetActivityName()).compareTo(other.isSetActivityName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetActivityName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.activityName, other.activityName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetApplicationName()).compareTo(other.isSetApplicationName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetApplicationName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.applicationName, other.applicationName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetApplicationIcon()).compareTo(other.isSetApplicationIcon());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetApplicationIcon()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.applicationIcon, other.applicationIcon);
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
    StringBuilder sb = new StringBuilder("AndroidAppStruct(");
    boolean first = true;

    sb.append("packageName:");
    if (this.packageName == null) {
      sb.append("null");
    } else {
      sb.append(this.packageName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("activityName:");
    if (this.activityName == null) {
      sb.append("null");
    } else {
      sb.append(this.activityName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("applicationName:");
    if (this.applicationName == null) {
      sb.append("null");
    } else {
      sb.append(this.applicationName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("applicationIcon:");
    if (this.applicationIcon == null) {
      sb.append("null");
    } else {
      sb.append(this.applicationIcon);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (!isSetPackageName()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'packageName' is unset! Struct:" + toString());
    }

    if (!isSetActivityName()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'activityName' is unset! Struct:" + toString());
    }

    if (!isSetApplicationName()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'applicationName' is unset! Struct:" + toString());
    }

    if (!isSetApplicationIcon()) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'applicationIcon' is unset! Struct:" + toString());
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class AndroidAppStructStandardSchemeFactory implements SchemeFactory {
    public AndroidAppStructStandardScheme getScheme() {
      return new AndroidAppStructStandardScheme();
    }
  }

  private static class AndroidAppStructStandardScheme extends StandardScheme<AndroidAppStruct> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, AndroidAppStruct struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PACKAGE_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.packageName = iprot.readString();
              struct.setPackageNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ACTIVITY_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.activityName = iprot.readString();
              struct.setActivityNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // APPLICATION_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.applicationName = iprot.readString();
              struct.setApplicationNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // APPLICATION_ICON
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.applicationIcon = iprot.readString();
              struct.setApplicationIconIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, AndroidAppStruct struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.packageName != null) {
        oprot.writeFieldBegin(PACKAGE_NAME_FIELD_DESC);
        oprot.writeString(struct.packageName);
        oprot.writeFieldEnd();
      }
      if (struct.activityName != null) {
        oprot.writeFieldBegin(ACTIVITY_NAME_FIELD_DESC);
        oprot.writeString(struct.activityName);
        oprot.writeFieldEnd();
      }
      if (struct.applicationName != null) {
        oprot.writeFieldBegin(APPLICATION_NAME_FIELD_DESC);
        oprot.writeString(struct.applicationName);
        oprot.writeFieldEnd();
      }
      if (struct.applicationIcon != null) {
        oprot.writeFieldBegin(APPLICATION_ICON_FIELD_DESC);
        oprot.writeString(struct.applicationIcon);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class AndroidAppStructTupleSchemeFactory implements SchemeFactory {
    public AndroidAppStructTupleScheme getScheme() {
      return new AndroidAppStructTupleScheme();
    }
  }

  private static class AndroidAppStructTupleScheme extends TupleScheme<AndroidAppStruct> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, AndroidAppStruct struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.packageName);
      oprot.writeString(struct.activityName);
      oprot.writeString(struct.applicationName);
      oprot.writeString(struct.applicationIcon);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, AndroidAppStruct struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.packageName = iprot.readString();
      struct.setPackageNameIsSet(true);
      struct.activityName = iprot.readString();
      struct.setActivityNameIsSet(true);
      struct.applicationName = iprot.readString();
      struct.setApplicationNameIsSet(true);
      struct.applicationIcon = iprot.readString();
      struct.setApplicationIconIsSet(true);
    }
  }

}

