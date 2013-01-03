/*
 *  Copyright 2001-2012 Stephen Colebourne
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

import java.util.Locale;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.Period;

/**
 * This class is a Junit unit test for PeriodFormat.
 *
 * @author Stephen Colebourne
 */
public class TestPeriodFormat extends TestCase {

    private static final Locale EN = new Locale("en");
    private static final Locale FR = new Locale("fr");
    private static final Locale PT = new Locale("pt");
    private static final Locale ES = new Locale("es");
    private static final Locale DE = new Locale("de");
    private static final Locale NL = new Locale("nl");
    private static final Locale DA = new Locale("da");
    private static final Locale JA = new Locale("ja");

    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(TestPeriodFormat.class);
    }

    public TestPeriodFormat(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        originalLocale = Locale.getDefault();
        Locale.setDefault(DE);
    }

    protected void tearDown() throws Exception {
        Locale.setDefault(originalLocale);
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testSubclassableConstructor() {
        PeriodFormat f = new PeriodFormat() {
            // test constructor is protected
        };
        assertNotNull(f);
    }

    //-----------------------------------------------------------------------
    // getDefault()
    //-----------------------------------------------------------------------
    public void test_getDefault_formatStandard() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds", PeriodFormat.getDefault().print(p));
    }

    //-----------------------------------------------------------------------
    public void test_getDefault_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 days", PeriodFormat.getDefault().print(p));
    }

    //-----------------------------------------------------------------------
    public void test_getDefault_formatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 days and 5 hours", PeriodFormat.getDefault().print(p));
    }

    //-----------------------------------------------------------------------
    public void test_getDefault_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.getDefault().parsePeriod("2 days"));
    }

    //-----------------------------------------------------------------------
    public void test_getDefault_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.getDefault().parsePeriod("2 days and 5 hours"));
    }

    //-----------------------------------------------------------------------
    public void test_getDefault_checkRedundantSeparator() {
        try {
            PeriodFormat.getDefault().parsePeriod("2 days and 5 hours ");
            fail("No exception was caught");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }    
    
    //-----------------------------------------------------------------------
    public void test_getDefault_cached() {
        assertSame(PeriodFormat.getDefault(), PeriodFormat.getDefault());
    }

    //-----------------------------------------------------------------------
    // wordBased() - default locale (de)
    //-----------------------------------------------------------------------
    public void test_wordBased_default() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 Tag, 5 Stunden, 6 Minuten, 7 Sekunden und 8 Millisekunden", PeriodFormat.wordBased().print(p));
    }

    //-----------------------------------------------------------------------
    // wordBased(Locale.FRENCH)
    //-----------------------------------------------------------------------
    public void test_wordBased_fr_formatStandard() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 jour, 5 heures, 6 minutes, 7 secondes et 8 millisecondes", PeriodFormat.wordBased(FR).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_fr_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 jours", PeriodFormat.wordBased(FR).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_fr_formatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 jours et 5 heures", PeriodFormat.wordBased(FR).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_fr_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.wordBased(FR).parsePeriod("2 jours"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_fr_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.wordBased(FR).parsePeriod("2 jours et 5 heures"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_fr_cached() {
        assertSame(PeriodFormat.wordBased(FR), PeriodFormat.wordBased(FR));
    }

    //-----------------------------------------------------------------------
    // wordBased(Locale pt)
    //-----------------------------------------------------------------------
    public void test_wordBased_pt_formatStandard() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 dia, 5 horas, 6 minutos, 7 segundos e 8 milissegundos", PeriodFormat.wordBased(PT).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_pt_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 dias", PeriodFormat.wordBased(PT).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_pt_formatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 dias e 5 horas", PeriodFormat.wordBased(PT).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_pt_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.wordBased(PT).parsePeriod("2 dias"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_pt_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.wordBased(PT).parsePeriod("2 dias e 5 horas"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_pt_cached() {
        assertSame(PeriodFormat.wordBased(PT), PeriodFormat.wordBased(PT));
    }

    //-----------------------------------------------------------------------
    // wordBased(Locale es)
    //-----------------------------------------------------------------------
    public void test_wordBased_es_formatStandard() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 d\u00EDa, 5 horas, 6 minutos, 7 segundos y 8 milisegundos", PeriodFormat.wordBased(ES).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_es_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 d\u00EDas", PeriodFormat.wordBased(ES).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_es_formatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 d\u00EDas y 5 horas", PeriodFormat.wordBased(ES).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_es_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.wordBased(ES).parsePeriod("2 d\u00EDas"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_es_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.wordBased(ES).parsePeriod("2 d\u00EDas y 5 horas"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_es_cached() {
        assertSame(PeriodFormat.wordBased(ES), PeriodFormat.wordBased(ES));
    }

    //-----------------------------------------------------------------------
    // wordBased(Locale de)
    //-----------------------------------------------------------------------
    public void test_wordBased_de_formatStandard() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 Tag, 5 Stunden, 6 Minuten, 7 Sekunden und 8 Millisekunden", PeriodFormat.wordBased(DE).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_de_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 Tage", PeriodFormat.wordBased(DE).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_de_formatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 Tage und 5 Stunden", PeriodFormat.wordBased(DE).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_de_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.wordBased(DE).parsePeriod("2 Tage"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_de_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.wordBased(DE).parsePeriod("2 Tage und 5 Stunden"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_de_cached() {
        assertSame(PeriodFormat.wordBased(DE), PeriodFormat.wordBased(DE));
    }

    //-----------------------------------------------------------------------
    // wordBased(Locale nl)
    //-----------------------------------------------------------------------
    public void test_wordBased_nl_formatStandard() {
        Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
        assertEquals("1 dag, 5 uur, 6 minuten, 7 seconden en 8 milliseconden", PeriodFormat.wordBased(NL).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_nl_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 dagen", PeriodFormat.wordBased(NL).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_nl_formatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 dagen en 5 uur", PeriodFormat.wordBased(NL).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_nl_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.wordBased(NL).parsePeriod("2 dagen"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_nl_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.wordBased(NL).parsePeriod("2 dagen en 5 uur"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_nl_cached() {
        assertSame(PeriodFormat.wordBased(NL), PeriodFormat.wordBased(NL));
    }

    //-----------------------------------------------------------------------
    // wordBased(Locale da)
    //-----------------------------------------------------------------------
    public void test_wordBased_da_formatMultiple() {
        Period p = new Period(2, 3, 4, 2, 5, 6 ,7, 8);
        assertEquals("2 \u00E5r, 3 m\u00E5neder, 4 uger, 2 dage, 5 timer, 6 minutter, 7 sekunder og 8 millisekunder", PeriodFormat.wordBased(DA).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_da_formatSinglular() {
        Period p = new Period(1, 1, 1, 1, 1, 1, 1, 1);
        assertEquals("1 \u00E5r, 1 m\u00E5ned, 1 uge, 1 dag, 1 time, 1 minut, 1 sekund og 1 millisekund", PeriodFormat.wordBased(DA).print(p));
    }
    
    //-----------------------------------------------------------------------
    public void test_wordBased_da_cached() {
        assertSame(PeriodFormat.wordBased(DA), PeriodFormat.wordBased(DA));
    }
    
    //-----------------------------------------------------------------------
    // wordBased(Locale ja)
    //-----------------------------------------------------------------------
    public void test_wordBased_ja_formatMultiple() {
        Period p = new Period(2, 3, 4, 2, 5, 6 ,7, 8);
        assertEquals("2\u5E743\u304B\u67084\u9031\u95932\u65E55\u6642\u95936\u52067\u79D28\u30DF\u30EA\u79D2", PeriodFormat.wordBased(JA).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_ja_formatSingular() {
        Period p = new Period(1, 1, 1, 1, 1, 1, 1, 1);
        assertEquals("1\u5E741\u304B\u67081\u9031\u95931\u65E51\u6642\u95931\u52061\u79D21\u30DF\u30EA\u79D2", PeriodFormat.wordBased(JA).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_ja_cached() {
        assertSame(PeriodFormat.wordBased(JA), PeriodFormat.wordBased(JA));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_ja_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.wordBased(JA).parsePeriod("2\u65E5"));
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_ja_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.wordBased(JA).parsePeriod("2\u65E55\u6642\u9593"));
    }
    
    //-----------------------------------------------------------------------
    public void test_wordBased_ja_checkRedundantSeparator() {
        try {
            // Spaces are not valid separators in Japanese
            PeriodFormat.wordBased(JA).parsePeriod("2\u65E5 ");
            fail("No exception was caught");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }   
    
    //-----------------------------------------------------------------------
    // Cross check languages
    //-----------------------------------------------------------------------
    public void test_wordBased_fr_from_de() {
      Locale.setDefault(DE);
      Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
      assertEquals("1 jour, 5 heures, 6 minutes, 7 secondes et 8 millisecondes", PeriodFormat.wordBased(FR).print(p));
  }

    public void test_wordBased_fr_from_nl() {
      Locale.setDefault(NL);
      Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
      assertEquals("1 jour, 5 heures, 6 minutes, 7 secondes et 8 millisecondes", PeriodFormat.wordBased(FR).print(p));
  }

    public void test_wordBased_en_from_de() {
      Locale.setDefault(DE);
      Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
      assertEquals("1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds", PeriodFormat.wordBased(EN).print(p));
  }

    public void test_wordBased_en_from_nl() {
      Locale.setDefault(NL);
      Period p = new Period(0, 0, 0, 1, 5, 6 ,7, 8);
      assertEquals("1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds", PeriodFormat.wordBased(EN).print(p));
  }

}
