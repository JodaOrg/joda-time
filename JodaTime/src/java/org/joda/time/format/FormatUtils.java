/*
 * Joda Software License, Version 1.0
 *
 *
 * Copyright (c) 2001-2004 Stephen Colebourne.  
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:  
 *       "This product includes software developed by the
 *        Joda project (http://www.joda.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The name "Joda" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact licence@joda.org.
 *
 * 5. Products derived from this software may not be called "Joda",
 *    nor may "Joda" appear in their name, without prior written
 *    permission of the Joda project.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE JODA AUTHORS OR THE PROJECT
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Joda project and was originally 
 * created by Stephen Colebourne <scolebourne@joda.org>. For more
 * information on the Joda project, please see <http://www.joda.org/>.
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
 */
public class FormatUtils {
    private static final double LOG_10 = Math.log(10);

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
     * @param size minumum amount of digits to append
     */
    public static void appendPaddedInteger(StringBuffer buf, int value, int size) {
        if (value < 0) {
            buf.append('-');
            if (value != Integer.MIN_VALUE) {
                value = -value;
            } else {
                for (; size > 10; size--) {
                    buf.append('0');
                }
                buf.append("" + -(long)Integer.MIN_VALUE);
                return;
            }
        }
        if (value < 10) {
            for (; size > 1; size--) {
                buf.append('0');
            }
            buf.append((char)(value + '0'));
        } else if (value < 100) {
            for (; size > 2; size--) {
                buf.append('0');
            }
            buf.append((char)(value / 10 + '0'));
            buf.append((char)(value % 10 + '0'));
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
                buf.append('0');
            }
            buf.append(Integer.toString(value));
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
     * @param size minumum amount of digits to append
     */
    public static void appendPaddedInteger(StringBuffer buf, long value, int size) {
        int intValue = (int)value;
        if (intValue == value) {
            appendPaddedInteger(buf, intValue, size);
        } else if (size <= 19) {
            buf.append(Long.toString(value));
        } else {
            if (value < 0) {
                buf.append('-');
                if (value != Long.MIN_VALUE) {
                    value = -value;
                } else {
                    for (; size > 19; size--) {
                        buf.append('0');
                    }
                    buf.append("9223372036854775808");
                    return;
                }
            }
            int digits = (int)(Math.log(value) / LOG_10) + 1;
            for (; size > digits; size--) {
                buf.append('0');
            }
            buf.append(Long.toString(value));
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
     * @param size minumum amount of digits to append
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
            out.write(value / 10 + '0');
            out.write(value % 10 + '0');
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
     * @param size minumum amount of digits to append
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
        if (value < 0) {
            buf.append('-');
            if (value != Integer.MIN_VALUE) {
                value = -value;
            } else {
                buf.append("" + -(long)Integer.MIN_VALUE);
                return;
            }
        }
        if (value < 10) {
            buf.append((char)(value + '0'));
        } else if (value < 100) {
            buf.append((char)(value / 10 + '0'));
            buf.append((char)(value % 10 + '0'));
        } else {
            buf.append(Integer.toString(value));
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
        int intValue = (int)value;
        if (intValue == value) {
            appendUnpaddedInteger(buf, intValue);
        } else {
            buf.append(Long.toString(value));
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
            out.write(value / 10 + '0');
            out.write(value % 10 + '0');
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

    static int parseTwoDigits(String text, int position) {
        int value = text.charAt(position) - '0';
        return ((value << 3) + (value << 1)) + text.charAt(position + 1) - '0';
    }
}
