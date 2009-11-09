/*
 *  Copyright 2001-2005 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time.convert;

/**
 * A set of converters, which allows exact converters to be quickly
 * selected. This class is threadsafe because it is (essentially) immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
class ConverterSet {
    private final Converter[] iConverters;

    // A simple immutable hashtable: closed hashing, linear probing, sized
    // power of 2, at least one null slot.
    private Entry[] iSelectEntries;

    ConverterSet(Converter[] converters) {
        // Since this is a package private constructor, we trust ourselves not
        // to alter the array outside this class.
        iConverters = converters;
        iSelectEntries = new Entry[1 << 4]; // 16
    }

    /**
     * Returns the closest matching converter for the given type, or null if
     * none found.
     *
     * @param type type to select, which may be null
     * @throws IllegalStateException if multiple converters match the type
     * equally well
     */
    Converter select(Class type) throws IllegalStateException {
        // Check the hashtable first.
        Entry[] entries = iSelectEntries;
        int length = entries.length;
        int index = type == null ? 0 : type.hashCode() & (length - 1);

        Entry e;
        // This loop depends on there being at least one null slot.
        while ((e = entries[index]) != null) {
            if (e.iType == type) {
                return e.iConverter;
            }
            if (++index >= length) {
                index = 0;
            }
        }

        // Not found in the hashtable, so do actual work.

        Converter converter = selectSlow(this, type);
        e = new Entry(type, converter);

        // Save the entry for future selects. This class must be threadsafe,
        // but there is no synchronization. Since the hashtable is being used
        // as a cache, it is okay to destroy existing entries. This isn't
        // likely to occur unless there is a high amount of concurrency. As
        // time goes on, cache updates will occur less often, and the cache
        // will fill with all the necessary entries.

        // Do all updates on a copy: slots in iSelectEntries must not be
        // updated by multiple threads as this can allow all null slots to be
        // consumed.
        entries = (Entry[])entries.clone();

        // Add new entry.
        entries[index] = e;

        // Verify that at least one null slot exists!
        for (int i=0; i<length; i++) {
            if (entries[i] == null) {
                // Found a null slot, swap in new hashtable.
                iSelectEntries = entries;
                return converter;
            }
        }

        // Double capacity and re-hash.

        int newLength = length << 1;
        Entry[] newEntries = new Entry[newLength];
        for (int i=0; i<length; i++) {
            e = entries[i];
            type = e.iType;
            index = type == null ? 0 : type.hashCode() & (newLength - 1);
            while (newEntries[index] != null) {
                if (++index >= newLength) {
                    index = 0;
                }
            }
            newEntries[index] = e;
        }

        // Swap in new hashtable.
        iSelectEntries = newEntries;
        return converter;
    }

    /**
     * Returns the amount of converters in the set.
     */
    int size() {
        return iConverters.length;
    }

    /**
     * Copies all the converters in the set to the given array.
     */
    void copyInto(Converter[] converters) {
        System.arraycopy(iConverters, 0, converters, 0, iConverters.length);
    }

    /**
     * Returns a copy of this set, with the given converter added. If a
     * matching converter is already in the set, the given converter replaces
     * it. If the converter is exactly the same as one already in the set, the
     * original set is returned.
     *
     * @param converter  converter to add, must not be null
     * @param removed  if not null, element 0 is set to the removed converter
     * @throws NullPointerException if converter is null
     */
    ConverterSet add(Converter converter, Converter[] removed) {
        Converter[] converters = iConverters;
        int length = converters.length;

        for (int i=0; i<length; i++) {
            Converter existing = converters[i];
            if (converter.equals(existing)) {
                // Already in the set.
                if (removed != null) {
                    removed[0] = null;
                }
                return this;
            }
            
            if (converter.getSupportedType() == existing.getSupportedType()) {
                // Replace the converter.
                Converter[] copy = new Converter[length];
                    
                for (int j=0; j<length; j++) {
                    if (j != i) {
                        copy[j] = converters[j];
                    } else {
                        copy[j] = converter;
                    }
                }

                if (removed != null) {
                    removed[0] = existing;
                }
                return new ConverterSet(copy);
            }
        }

        // Not found, so add it.
        Converter[] copy = new Converter[length + 1];
        System.arraycopy(converters, 0, copy, 0, length);
        copy[length] = converter;
        
        if (removed != null) {
            removed[0] = null;
        }
        return new ConverterSet(copy);
    }

    /**
     * Returns a copy of this set, with the given converter removed. If the
     * converter was not in the set, the original set is returned.
     *
     * @param converter  converter to remove, must not be null
     * @param removed  if not null, element 0 is set to the removed converter
     * @throws NullPointerException if converter is null
     */
    ConverterSet remove(Converter converter, Converter[] removed) {
        Converter[] converters = iConverters;
        int length = converters.length;

        for (int i=0; i<length; i++) {
            if (converter.equals(converters[i])) {
                return remove(i, removed);
            }
        }

        // Not found.
        if (removed != null) {
            removed[0] = null;
        }
        return this;
    }

    /**
     * Returns a copy of this set, with the converter at the given index
     * removed.
     *
     * @param index index of converter to remove
     * @param removed if not null, element 0 is set to the removed converter
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    ConverterSet remove(final int index, Converter[] removed) {
        Converter[] converters = iConverters;
        int length = converters.length;
        if (index >= length) {
            throw new IndexOutOfBoundsException();
        }

        if (removed != null) {
            removed[0] = converters[index];
        }

        Converter[] copy = new Converter[length - 1];
                
        int j = 0;
        for (int i=0; i<length; i++) {
            if (i != index) {
                copy[j++] = converters[i];
            }
        }
        
        return new ConverterSet(copy);
    }

    /**
     * Returns the closest matching converter for the given type, but not very
     * efficiently.
     */
    private static Converter selectSlow(ConverterSet set, Class type) {
        Converter[] converters = set.iConverters;
        int length = converters.length;
        Converter converter;

        for (int i=length; --i>=0; ) {
            converter = converters[i];
            Class supportedType = converter.getSupportedType();

            if (supportedType == type) {
                // Exact match.
                return converter;
            }

            if (supportedType == null || (type != null && !supportedType.isAssignableFrom(type))) {
                // Eliminate the impossible.
                set = set.remove(i, null);
                converters = set.iConverters;
                length = converters.length;
            }
        }

        // Haven't found exact match, so check what remains in the set.

        if (type == null || length == 0) {
            return null;
        }
        if (length == 1) {
            // Found the one best match.
            return converters[0];
        }

        // At this point, there exist multiple potential converters.

        // Eliminate supertypes.
        for (int i=length; --i>=0; ) {
            converter = converters[i];
            Class supportedType = converter.getSupportedType();
            for (int j=length; --j>=0; ) {
                if (j != i && converters[j].getSupportedType().isAssignableFrom(supportedType)) {
                    // Eliminate supertype.
                    set = set.remove(j, null);
                    converters = set.iConverters;
                    length = converters.length;
                    i = length - 1;
                }
            }
        }        
        
        // Check what remains in the set.

        if (length == 1) {
            // Found the one best match.
            return converters[0];
        }

        // Class c implements a, b {}
        // Converters exist only for a and b. Which is better? Neither.

        StringBuffer msg = new StringBuffer();
        msg.append("Unable to find best converter for type \"");
        msg.append(type.getName());
        msg.append("\" from remaining set: ");
        for (int i=0; i<length; i++) {
            converter = converters[i];
            Class supportedType = converter.getSupportedType();

            msg.append(converter.getClass().getName());
            msg.append('[');
            msg.append(supportedType == null ? null : supportedType.getName());
            msg.append("], ");
        }

        throw new IllegalStateException(msg.toString());
    }

    static class Entry {
        final Class iType;
        final Converter iConverter;

        Entry(Class type, Converter converter) {
            iType = type;
            iConverter = converter;
        }
    }

}
