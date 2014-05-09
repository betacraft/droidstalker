/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ddmlib.log;

import com.android.ddmlib.log.EventContainer.EventValueType;

import java.util.Locale;


/**
 * Describes an {@link com.android.ddmlib.log.EventContainer} value.
 * <p/>
 * This is a stand-alone object, not linked to a particular Event. It describes the value, by
 * name, type ({@link com.android.ddmlib.log.EventContainer.EventValueType}), and (if needed) value unit ({@link com.android.ddmlib.log.EventValueDescription.ValueType}).
 * <p/>
 * The index of the value is not contained within this class, and is instead dependent on the
 * index of this particular object in the array of {@link com.android.ddmlib.log.EventValueDescription} returned by
 * {@link com.android.ddmlib.log.EventLogParser#getEventInfoMap()} when queried for a particular event tag.
 *
 */
public final class EventValueDescription {

    /**
     * Represents the type of a numerical value. This is used to display values of vastly different
     * type/range in graphs.
     */
    public static enum ValueType {
        NOT_APPLICABLE(0),
        OBJECTS(1),
        BYTES(2),
        MILLISECONDS(3),
        ALLOCATIONS(4),
        ID(5),
        PERCENT(6);

        private int mValue;

        /**
         * Checks that the {@link com.android.ddmlib.log.EventContainer.EventValueType} is compatible with the {@link com.android.ddmlib.log.EventValueDescription.ValueType}.
         * @param type the {@link com.android.ddmlib.log.EventContainer.EventValueType} to check.
         * @throws InvalidValueTypeException if the types are not compatible.
         */
        public void checkType(EventValueType type) throws InvalidValueTypeException {
            if ((type != EventValueType.INT && type != EventValueType.LONG)
                    && this != NOT_APPLICABLE) {
                throw new InvalidValueTypeException(
                        String.format("%1$s doesn't support type %2$s", type, this));
            }
        }

        /**
         * Returns a {@link com.android.ddmlib.log.EventValueDescription.ValueType} from an integer value, or <code>null</code> if no match
         * were found.
         * @param value the integer value.
         */
        public static ValueType getValueType(int value) {
            for (ValueType type : values()) {
                if (type.mValue == value) {
                    return type;
                }
            }
            return null;
        }

        /**
         * Returns the integer value of the enum.
         */
        public int getValue() {
            return mValue;
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.US);
        }

        private ValueType(int value) {
            mValue = value;
        }
    }

    private String mName;
    private EventValueType mEventValueType;
    private ValueType mValueType;

    /**
     * Builds a {@link com.android.ddmlib.log.EventValueDescription} with a name and a type.
     * <p/>
     * If the type is {@link com.android.ddmlib.log.EventContainer.EventValueType#INT} or {@link com.android.ddmlib.log.EventContainer.EventValueType#LONG}, the
     * {@link #mValueType} is set to {@link com.android.ddmlib.log.EventValueDescription.ValueType#BYTES} by default. It set to
     * {@link com.android.ddmlib.log.EventValueDescription.ValueType#NOT_APPLICABLE} for all other {@link com.android.ddmlib.log.EventContainer.EventValueType} values.
     * @param name
     * @param type
     */
    EventValueDescription(String name, EventValueType type) {
        mName = name;
        mEventValueType = type;
        if (mEventValueType == EventValueType.INT || mEventValueType == EventValueType.LONG) {
            mValueType = ValueType.BYTES;
        } else {
            mValueType = ValueType.NOT_APPLICABLE;
        }
    }

    /**
     * Builds a {@link com.android.ddmlib.log.EventValueDescription} with a name and a type, and a {@link com.android.ddmlib.log.EventValueDescription.ValueType}.
     * <p/>
     * @param name
     * @param type
     * @param valueType
     * @throws InvalidValueTypeException if type and valuetype are not compatible.
     *
     */
    EventValueDescription(String name, EventValueType type, ValueType valueType)
            throws InvalidValueTypeException {
        mName = name;
        mEventValueType = type;
        mValueType = valueType;
        mValueType.checkType(mEventValueType);
    }

    /**
     * @return the Name.
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the {@link com.android.ddmlib.log.EventContainer.EventValueType}.
     */
    public EventValueType getEventValueType() {
        return mEventValueType;
    }

    /**
     * @return the {@link com.android.ddmlib.log.EventValueDescription.ValueType}.
     */
    public ValueType getValueType() {
        return mValueType;
    }

    @Override
    public String toString() {
        if (mValueType != ValueType.NOT_APPLICABLE) {
            return String.format("%1$s (%2$s, %3$s)", mName, mEventValueType.toString(),
                    mValueType.toString());
        }

        return String.format("%1$s (%2$s)", mName, mEventValueType.toString());
    }

    /**
     * Checks if the value is of the proper type for this receiver.
     * @param value the value to check.
     * @return true if the value is of the proper type for this receiver.
     */
    public boolean checkForType(Object value) {
        switch (mEventValueType) {
            case INT:
                return value instanceof Integer;
            case LONG:
                return value instanceof Long;
            case STRING:
                return value instanceof String;
            case LIST:
                return value instanceof Object[];
        }

        return false;
    }

    /**
     * Returns an object of a valid type (based on the value returned by
     * {@link #getEventValueType()}) from a String value.
     * <p/>
     * IMPORTANT {@link com.android.ddmlib.log.EventContainer.EventValueType#LIST} and {@link com.android.ddmlib.log.EventContainer.EventValueType#TREE} are not
     * supported.
     * @param value the value of the object expressed as a string.
     * @return an object or null if the conversion could not be done.
     */
    public Object getObjectFromString(String value) {
        switch (mEventValueType) {
            case INT:
                try {
                    return Integer.valueOf(value);
                } catch (NumberFormatException e) {
                    return null;
                }
            case LONG:
                try {
                    return Long.valueOf(value);
                } catch (NumberFormatException e) {
                    return null;
                }
            case STRING:
                return value;
        }

        return null;
    }
}
