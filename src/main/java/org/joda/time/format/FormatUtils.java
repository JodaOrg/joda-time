/*
 *  Copyright 2001-2014 Stephen Colebourne
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
package org.joda.time.format;

import java.io.IOException;
import java.io.Writer;

/**
 * Utility methods used by formatters.
 * <p>
 * FormatUtils is thread-safe and immutable.
 *
 * @author Brian S O'Neill
 * @since 1.0
 */
public class FormatUtils {

    private static final double LOG_10 = Math.log(10);

    /**
     * Restricted constructor.
     */
    private FormatUtils() {
    }

    /**
     * Converts an integer to a string, prepended with a variable amount of '0'
     * pad characters, and appends it to the given buffer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param buf receives integer converted to a string
     * @param value value to convert to a string
     * @param size minimum amount of digits to append
     */
    public static void appendPaddedInteger(StringBuffer buf, int value, int size) {
        try {
            appendPaddedInteger((Appendable)buf, value, size);
        } catch (IOException e) {
            // StringBuffer does not throw IOException
        }
    }

    /**
     * Converts an integer to a string, prepended with a variable amount of '0'
     * pad characters, and appends it to the given appendable.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param appenadble receives integer converted to a string
     * @param value value to convert to a string
     * @param size minimum amount of digits to append
     * @since 2.4
     */
    public static void appendPaddedInteger(Appendable appenadble, int value, int size) throws IOException {
        if (value < 0) {
            appenadble.append('-');
            if (value != Integer.MIN_VALUE) {
                value = -value;
            } else {
                for (; size > 10; size--) {
                    appenadble.append('0');
                }
                appenadble.append("" + -(long)Integer.MIN_VALUE);
                return;
            }
        }
        if (value < 10) {
            for (; size > 1; size--) {
                appenadble.append('0');
            }
            appenadble.append((char)(value + '0'));
        } else if (value < 100) {
            for (; size > 2; size--) {
                appenadble.append('0');
            }
            // Calculate value div/mod by 10 without using two expensive
            // division operations. (2 ^ 27) / 10 = 13421772. Add one to
            // value to correct rounding error.
            int d = ((value + 1) * 13421772) >> 27;
            appenadble.append((char) (d + '0'));
            // Append remainder by calculating (value - d * 10).
            appenadble.append((char) (value - (d << 3) - (d << 1) + '0'));
        } else {
            int digits;
            if (value < 1000) {
                digits = 3;
            } else if (value < 10000) {
                digits = 4;
            } else {
                digits = (int)(Math.log(value) / LOG_10) + 1;
            }
            for (; size > digits; size--) {
                appenadble.append('0');
            }
            appenadble.append(Integer.toString(value));
        }
    }

    /**
     * Converts an integer to a string, prepended with a variable amount of '0'
     * pad characters, and appends it to the given buffer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param buf receives integer converted to a string
     * @param value value to convert to a string
     * @param size minimum amount of digits to append
     */
    public static void appendPaddedInteger(StringBuffer buf, long value, int size) {
        try {
            appendPaddedInteger((Appendable)buf, value, size);
        } catch (IOException e) {
            // StringBuffer does not throw IOException
        }
    }

    /**
     * Converts an integer to a string, prepended with a variable amount of '0'
     * pad characters, and appends it to the given buffer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param appendable receives integer converted to a string
     * @param value value to convert to a string
     * @param size minimum amount of digits to append
     * @since 2.4
     */
    public static void appendPaddedInteger(Appendable appendable, long value, int size) throws IOException {
        int intValue = (int)value;
        if (intValue == value) {
            appendPaddedInteger(appendable, intValue, size);
        } else if (size <= 19) {
            appendable.append(Long.toString(value));
        } else {
            if (value < 0) {
                appendable.append('-');
                if (value != Long.MIN_VALUE) {
                    value = -value;
                } else {
                    for (; size > 19; size--) {
                        appendable.append('0');
                    }
                    appendable.append("9223372036854775808");
                    return;
                }
            }
            int digits = (int)(Math.log(value) / LOG_10) + 1;
            for (; size > digits; size--) {
                appendable.append('0');
            }
            appendable.append(Long.toString(value));
        }
    }

    /**
     * Converts an integer to a string, prepended with a variable amount of '0'
     * pad characters, and writes it to the given writer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param out receives integer converted to a string
     * @param value value to convert to a string
     * @param size minimum amount of digits to append
     */
    public static void writePaddedInteger(Writer out, int value, int size)
        throws IOException
    {
        if (value < 0) {
            out.write('-');
            if (value != Integer.MIN_VALUE) {
                value = -value;
            } else {
                for (; size > 10; size--) {
                    out.write('0');
                }
                out.write("" + -(long)Integer.MIN_VALUE);
                return;
            }
        }
        if (value < 10) {
            for (; size > 1; size--) {
                out.write('0');
            }
            out.write(value + '0');
        } else if (value < 100) {
            for (; size > 2; size--) {
                out.write('0');
            }
            // Calculate value div/mod by 10 without using two expensive
            // division operations. (2 ^ 27) / 10 = 13421772. Add one to
            // value to correct rounding error.
            int d = ((value + 1) * 13421772) >> 27;
            out.write(d + '0');
            // Append remainder by calculating (value - d * 10).
            out.write(value - (d << 3) - (d << 1) + '0');
        } else {
            int digits;
            if (value < 1000) {
                digits = 3;
            } else if (value < 10000) {
                digits = 4;
            } else {
                digits = (int)(Math.log(value) / LOG_10) + 1;
            }
            for (; size > digits; size--) {
                out.write('0');
            }
            out.write(Integer.toString(value));
        }
    }

    /**
     * Converts an integer to a string, prepended with a variable amount of '0'
     * pad characters, and writes it to the given writer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param out receives integer converted to a string
     * @param value value to convert to a string
     * @param size minimum amount of digits to append
     */
    public static void writePaddedInteger(Writer out, long value, int size)
        throws IOException
    {
        int intValue = (int)value;
        if (intValue == value) {
            writePaddedInteger(out, intValue, size);
        } else if (size <= 19) {
            out.write(Long.toString(value));
        } else {
            if (value < 0) {
                out.write('-');
                if (value != Long.MIN_VALUE) {
                    value = -value;
                } else {
                    for (; size > 19; size--) {
                        out.write('0');
                    }
                    out.write("9223372036854775808");
                    return;
                }
            }
            int digits = (int)(Math.log(value) / LOG_10) + 1;
            for (; size > digits; size--) {
                out.write('0');
            }
            out.write(Long.toString(value));
        }
    }

    /**
     * Converts an integer to a string, and appends it to the given buffer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param buf receives integer converted to a string
     * @param value value to convert to a string
     */
    public static void appendUnpaddedInteger(StringBuffer buf, int value) {
        try {
            appendUnpaddedInteger((Appendable) buf, value);
        } catch (IOException e) {
            // StringBuffer do not throw IOException
        }
    }

    /**
     * Converts an integer to a string, and appends it to the given appendable.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param appendable receives integer converted to a string
     * @param value value to convert to a string
     * @since 2.4
     */
    public static void appendUnpaddedInteger(Appendable appendable, int value) throws IOException {
        if (value < 0) {
            appendable.append('-');
            if (value != Integer.MIN_VALUE) {
                value = -value;
            } else {
                appendable.append("" + -(long)Integer.MIN_VALUE);
                return;
            }
        }
        if (value < 10) {
            appendable.append((char)(value + '0'));
        } else if (value < 100) {
            // Calculate value div/mod by 10 without using two expensive
            // division operations. (2 ^ 27) / 10 = 13421772. Add one to
            // value to correct rounding error.
            int d = ((value + 1) * 13421772) >> 27;
            appendable.append((char) (d + '0'));
            // Append remainder by calculating (value - d * 10).
            appendable.append((char) (value - (d << 3) - (d << 1) + '0'));
        } else {
            appendable.append(Integer.toString(value));
        }
    }

    /**
     * Converts an integer to a string, and appends it to the given buffer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param buf receives integer converted to a string
     * @param value value to convert to a string
     */
    public static void appendUnpaddedInteger(StringBuffer buf, long value) {
        try {
            appendUnpaddedInteger((Appendable) buf, value);
        } catch (IOException e) {
            // StringBuffer do not throw IOException
        }
    }

    /**
     * Converts an integer to a string, and appends it to the given appendable.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param appendable receives integer converted to a string
     * @param value value to convert to a string
     */
    public static void appendUnpaddedInteger(Appendable appendable, long value) throws IOException {
        int intValue = (int)value;
        if (intValue == value) {
            appendUnpaddedInteger(appendable, intValue);
        } else {
            appendable.append(Long.toString(value));
        }
    }

    /**
     * Converts an integer to a string, and writes it to the given writer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param out receives integer converted to a string
     * @param value value to convert to a string
     */
    public static void writeUnpaddedInteger(Writer out, int value)
        throws IOException
    {
        if (value < 0) {
            out.write('-');
            if (value != Integer.MIN_VALUE) {
                value = -value;
            } else {
                out.write("" + -(long)Integer.MIN_VALUE);
                return;
            }
        }
        if (value < 10) {
            out.write(value + '0');
        } else if (value < 100) {
            // Calculate value div/mod by 10 without using two expensive
            // division operations. (2 ^ 27) / 10 = 13421772. Add one to
            // value to correct rounding error.
            int d = ((value + 1) * 13421772) >> 27;
            out.write(d + '0');
            // Append remainder by calculating (value - d * 10).
            out.write(value - (d << 3) - (d << 1) + '0');
        } else {
            out.write(Integer.toString(value));
        }
    }

    /**
     * Converts an integer to a string, and writes it to the given writer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param out receives integer converted to a string
     * @param value value to convert to a string
     */
    public static void writeUnpaddedInteger(Writer out, long value)
        throws IOException
    {
        int intValue = (int)value;
        if (intValue == value) {
            writeUnpaddedInteger(out, intValue);
        } else {
            out.write(Long.toString(value));
        }
    }

    /**
     * Calculates the number of decimal digits for the given value,
     * including the sign.
     */
    public static int calculateDigitCount(long value) {
        if (value < 0) {
            if (value != Long.MIN_VALUE) {
                return calculateDigitCount(-value) + 1;
            } else {
                return 20;
            }
        }
        return 
            (value < 10 ? 1 :
             (value < 100 ? 2 :
              (value < 1000 ? 3 :
               (value < 10000 ? 4 :
                ((int)(Math.log(value) / LOG_10) + 1)))));
    }

    static int parseTwoDigits(CharSequence text, int position) {
        int value = text.charAt(position) - '0';
        return ((value << 3) + (value << 1)) + text.charAt(position + 1) - '0';
    }

    static String createErrorMessage(final String text, final int errorPos) {
        int sampleLen = errorPos + 32;
        String sampleText;
        if (text.length() <= sampleLen + 3) {
            sampleText = text;
        } else {
            sampleText = text.substring(0, sampleLen).concat("...");
        }
        
        if (errorPos <= 0) {
            return "Invalid format: \"" + sampleText + '"';
        }
        
        if (errorPos >= text.length()) {
            return "Invalid format: \"" + sampleText + "\" is too short";
        }
        
        return "Invalid format: \"" + sampleText + "\" is malformed at \"" +
            sampleText.substring(errorPos) + '"';
    }

}
