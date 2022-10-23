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

import java.util.Locale;

import org.joda.time.Period;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This class is a Junit unit test for PeriodFormat.
 *
 * @author Stephen Colebourne
 */
public class TestPeriodFormat extends TestCase {

    private static final Locale BG = new Locale("bg");
    private static final Locale CA = new Locale("ca");
    private static final Locale ZH = new Locale("zh");
    private static final Locale CS = new Locale("cs");
    private static final Locale DA = new Locale("da");
    private static final Locale DE = new Locale("de");
    private static final Locale EL = new Locale("el");
    private static final Locale EN = new Locale("en");
    private static final Locale ES = new Locale("es");
    private static final Locale EU = new Locale("eu");
    private static final Locale FI = new Locale("fi");
    private static final Locale FR = new Locale("fr");
    private static final Locale HI = new Locale("hi");
    private static final Locale HU = new Locale("hu");
    private static final Locale IN = new Locale("in");
    private static final Locale IW = new Locale("iw");
    private static final Locale JA = new Locale("ja");
    private static final Locale MS = new Locale("ms");
    private static final Locale NL = new Locale("nl");
    private static final Locale NN = new Locale("nn");
    private static final Locale NO = new Locale("no");
    private static final Locale PL = new Locale("pl");
    private static final Locale PT = new Locale("pt");
    private static final Locale RO = new Locale("ro");
    private static final Locale RU = new Locale("ru");
    private static final Locale SQ = new Locale("sq");
    private static final Locale SK = new Locale("sk");
    private static final Locale SV = new Locale("sv");
    private static final Locale TR = new Locale("tr");
    private static final Locale UK = new Locale("uk");

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

    @Override
    protected void setUp() throws Exception {
        originalLocale = Locale.getDefault();
        Locale.setDefault(DE);
    }

    @Override
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

    // -----------------------------------------------------------------------
    // wordBased - minimal test for locales
    // -----------------------------------------------------------------------
    private static Object[][] data_formatStandard() {
        return new Object[][] {
            {BG, "2 години, 1 месец, 1 ден, 5 часа, 6 минути, 7 секунди и 8 милисекунди"},
            {CA, "2 anys, 1 mes, 1 dia, 5 hores, 6 minuts, 7 segons i 8 milisegons"},
            {ZH, "2年1个月1天5小时6分7秒8毫秒"},
            {CS, "2 roky, 1 měsíc, 1 den, 5 hodin, 6 minut, 7 sekund a 8 milisekund"},
            {DA, "2 år, 1 måned, 1 dag, 5 timer, 6 minutter, 7 sekunder og 8 millisekunder"},
            {DE, "2 Jahre, 1 Monat, 1 Tag, 5 Stunden, 6 Minuten, 7 Sekunden und 8 Millisekunden"},
            {EN, "2 years, 1 month, 1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds"},
            {EL, "2 χρόνια, 1 μήνας, 1 μέρα, 5 ώρες, 6 λεπτά, 7 δευτερόλεπτα και 8 χιλιοστά του δευτερολέπτου"},
            {ES, "2 años, 1 mes, 1 día, 5 horas, 6 minutos, 7 segundos y 8 milisegundos"},
            {EU, "2 urte, 1 hilabete, 1 egun, 5 ordu, 6 minuto, 7 segundo eta 8 milisegundo"},
            {FR, "2 années, 1 mois, 1 jour, 5 heures, 6 minutes, 7 secondes et 8 millisecondes"},
            {FI, "2 vuotta, 1 kuukausi, 1 päivä, 5 tuntia, 6 minuuttia, 7 sekuntia ja 8 millisekuntia"},
            {HI, "2 साल, 1 महीना, 1 दिन, 5 घंटे, 6 मिनट, 7 सेकंड और 8 मिलीसेकंड"},
            {HU, "2 év, 1 hónap, 1 nap, 5 óra, 6 perc, 7 másodperc és 8 miliszekundum"},
            {IN, "2 tahun, 1 bulan, 1 hari, 5 jam, 6 menit, 7 detik dan8 millidetik"},
            {IW, "2 שנים, 1 חודש, 1 יום, 5 שעות, 6 דקות, 7 שניות ו-8 אלפיות שנייה"},
            {JA, "2年1か月1日5時間6分7秒8ミリ秒"},
            {MS, "2 tahun, 1 bulan, 1 hari, 5 jam, 6 minit, 7 saat dan 8 milisaat"},
            {NL, "2 jaar, 1 maand, 1 dag, 5 uur, 6 minuten, 7 seconden en 8 milliseconden"},
            {NN, "2 år, 1 månad, 1 dag, 5 timar, 6 minutt, 7 sekund og 8 millisekund"},
            {NO, "2 år, 1 måned, 1 dag, 5 timer, 6 minutter, 7 sekunder og 8 millisekunder"},
            {PL, "2 lata, 1 miesiąc, 1 dzie\u0144, 5 godzin, 6 minut, 7 sekund i 8 milisekund"},
            {PT, "2 anos, 1 mês, 1 dia, 5 horas, 6 minutos, 7 segundos e 8 milissegundos"},
            {RO, "2 ani, 1 lună, 1 zi, 5 ore, 6 minute, 7 secunde și 8 milisecunde"},
            {RU, "2 года, 1 месяц, 1 день, 5 часов, 6 минут, 7 секунд и 8 миллисекунд"},
            {SK, "2 roky, 1 mesiac, 1 deň, 5 hodín, 6 minút, 7 sekúnd a 8 milisekúnd"},
            {SQ, "2 vjet, 1 muaj, 1 ditë, 5 orë, 6 minuta, 7 sekonda dhe 8 milisekonda"},
            {SV, "2 år, 1 månad, 1 dag, 5 timmar, 6 minuter, 7 sekunder och 8 millisekunder"},
            {TR, "2 yıl, 1 ay, 1 gün, 5 saat, 6 dakika, 7 saniye ve 8 milisaniye"},
            {UK, "2 року, 1 місяць, 1 день, 5 годин, 6 хвилин, 7 секунд и 8 мілісекунд"},
        };
    }

    public void test_wordBased_formatStandard() {
        Period p = new Period(2, 1, 0, 1, 5, 6, 7, 8);
        Object[][] data = data_formatStandard();
        for (int i = 0; i < data.length; i++) {
            Locale locale = (Locale) data[i][0];
            String actual = PeriodFormat.wordBased(locale).print(p);
            String expected = data[i][1].toString();
            assertEquals(locale.toString(), expected, actual);
        }
    }

    //-----------------------------------------------------------------------
    // wordBased(Locale.FRENCH)
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

    // -----------------------------------------------------------------------
    // wordBased(new Locale("pl")
    // -----------------------------------------------------------------------
    public void test_wordBased_pl_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 dni", PeriodFormat.wordBased(PL).print(p));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_pl_formatTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals("2 dni i 5 godzin", PeriodFormat.wordBased(PL).print(p));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_pl_parseOneField() {
        Period p = Period.days(2);
        assertEquals(p, PeriodFormat.wordBased(PL).parsePeriod("2 dni"));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_pl_parseTwoFields() {
        Period p = Period.days(2).withHours(5);
        assertEquals(p, PeriodFormat.wordBased(PL).parsePeriod("2 dni i 5 godzin"));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_pl_checkRedundantSeparator() {
        try {
            PeriodFormat.wordBased(PL).parsePeriod("2 dni and 5 godzin ");
            fail("No exception was caught");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_pl_cached() {
        assertSame(PeriodFormat.wordBased(PL), PeriodFormat.wordBased(PL));
    }
    
    // -----------------------------------------------------------------------
    public void test_wordBased_pl_regEx() {
        PeriodFormatter pf = PeriodFormat.wordBased(PL);
        assertEquals("1 rok", pf.print(Period.years(1)));
        assertEquals("2 lata", pf.print(Period.years(2)));
        assertEquals("5 lat", pf.print(Period.years(5)));
        assertEquals("12 lat", pf.print(Period.years(12)));
        assertEquals("15 lat", pf.print(Period.years(15)));
        assertEquals("1112 lat", pf.print(Period.years(1112)));
        assertEquals("1115 lat", pf.print(Period.years(1115)));
        assertEquals("2112 lat", pf.print(Period.years(2112)));
        assertEquals("2115 lat", pf.print(Period.years(2115)));
        assertEquals("2212 lat", pf.print(Period.years(2212)));
        assertEquals("2215 lat", pf.print(Period.years(2215)));
        assertEquals("22 lata", pf.print(Period.years(22)));
        assertEquals("25 lat", pf.print(Period.years(25)));
        assertEquals("1122 lata", pf.print(Period.years(1122)));
        assertEquals("1125 lat", pf.print(Period.years(1125)));
        assertEquals("2122 lata", pf.print(Period.years(2122)));
        assertEquals("2125 lat", pf.print(Period.years(2125)));
        assertEquals("2222 lata", pf.print(Period.years(2222)));
        assertEquals("2225 lat", pf.print(Period.years(2225)));
        
        assertEquals("1 miesi\u0105c", pf.print(Period.months(1)));
        assertEquals("2 miesi\u0105ce", pf.print(Period.months(2)));
        assertEquals("5 miesi\u0119cy", pf.print(Period.months(5)));
        assertEquals("12 miesi\u0119cy", pf.print(Period.months(12)));
        assertEquals("15 miesi\u0119cy", pf.print(Period.months(15)));
        assertEquals("1112 miesi\u0119cy", pf.print(Period.months(1112)));
        assertEquals("1115 miesi\u0119cy", pf.print(Period.months(1115)));
        assertEquals("2112 miesi\u0119cy", pf.print(Period.months(2112)));
        assertEquals("2115 miesi\u0119cy", pf.print(Period.months(2115)));
        assertEquals("2212 miesi\u0119cy", pf.print(Period.months(2212)));
        assertEquals("2215 miesi\u0119cy", pf.print(Period.months(2215)));
        assertEquals("22 miesi\u0105ce", pf.print(Period.months(22)));
        assertEquals("25 miesi\u0119cy", pf.print(Period.months(25)));
        assertEquals("1122 miesi\u0105ce", pf.print(Period.months(1122)));
        assertEquals("1125 miesi\u0119cy", pf.print(Period.months(1125)));
        assertEquals("2122 miesi\u0105ce", pf.print(Period.months(2122)));
        assertEquals("2125 miesi\u0119cy", pf.print(Period.months(2125)));
        assertEquals("2222 miesi\u0105ce", pf.print(Period.months(2222)));
        assertEquals("2225 miesi\u0119cy", pf.print(Period.months(2225)));

        assertEquals("1 tydzie\u0144", pf.print(Period.weeks(1)));
        assertEquals("2 tygodnie", pf.print(Period.weeks(2)));
        assertEquals("5 tygodni", pf.print(Period.weeks(5)));
        assertEquals("12 tygodni", pf.print(Period.weeks(12)));
        assertEquals("15 tygodni", pf.print(Period.weeks(15)));
        assertEquals("1112 tygodni", pf.print(Period.weeks(1112)));
        assertEquals("1115 tygodni", pf.print(Period.weeks(1115)));
        assertEquals("2112 tygodni", pf.print(Period.weeks(2112)));
        assertEquals("2115 tygodni", pf.print(Period.weeks(2115)));
        assertEquals("2212 tygodni", pf.print(Period.weeks(2212)));
        assertEquals("2215 tygodni", pf.print(Period.weeks(2215)));
        assertEquals("22 tygodnie", pf.print(Period.weeks(22)));
        assertEquals("25 tygodni", pf.print(Period.weeks(25)));
        assertEquals("1122 tygodnie", pf.print(Period.weeks(1122)));
        assertEquals("1125 tygodni", pf.print(Period.weeks(1125)));
        assertEquals("2122 tygodnie", pf.print(Period.weeks(2122)));
        assertEquals("2125 tygodni", pf.print(Period.weeks(2125)));
        assertEquals("2222 tygodnie", pf.print(Period.weeks(2222)));
        assertEquals("2225 tygodni", pf.print(Period.weeks(2225)));

        assertEquals("1 dzie\u0144", pf.print(Period.days(1)));
        assertEquals("2 dni", pf.print(Period.days(2)));
        assertEquals("5 dni", pf.print(Period.days(5)));
        assertEquals("12 dni", pf.print(Period.days(12)));
        assertEquals("15 dni", pf.print(Period.days(15)));
        assertEquals("22 dni", pf.print(Period.days(22)));
        assertEquals("25 dni", pf.print(Period.days(25)));

        assertEquals("1 godzina", pf.print(Period.hours(1)));
        assertEquals("2 godziny", pf.print(Period.hours(2)));
        assertEquals("5 godzin", pf.print(Period.hours(5)));
        assertEquals("12 godzin", pf.print(Period.hours(12)));
        assertEquals("15 godzin", pf.print(Period.hours(15)));
        assertEquals("1112 godzin", pf.print(Period.hours(1112)));
        assertEquals("1115 godzin", pf.print(Period.hours(1115)));
        assertEquals("2112 godzin", pf.print(Period.hours(2112)));
        assertEquals("2115 godzin", pf.print(Period.hours(2115)));
        assertEquals("2212 godzin", pf.print(Period.hours(2212)));
        assertEquals("2215 godzin", pf.print(Period.hours(2215)));
        assertEquals("22 godziny", pf.print(Period.hours(22)));
        assertEquals("25 godzin", pf.print(Period.hours(25)));
        assertEquals("1122 godziny", pf.print(Period.hours(1122)));
        assertEquals("1125 godzin", pf.print(Period.hours(1125)));
        assertEquals("2122 godziny", pf.print(Period.hours(2122)));
        assertEquals("2125 godzin", pf.print(Period.hours(2125)));
        assertEquals("2222 godziny", pf.print(Period.hours(2222)));
        assertEquals("2225 godzin", pf.print(Period.hours(2225)));

        assertEquals("1 minuta", pf.print(Period.minutes(1)));
        assertEquals("2 minuty", pf.print(Period.minutes(2)));
        assertEquals("5 minut", pf.print(Period.minutes(5)));
        assertEquals("12 minut", pf.print(Period.minutes(12)));
        assertEquals("15 minut", pf.print(Period.minutes(15)));
        assertEquals("1112 minut", pf.print(Period.minutes(1112)));
        assertEquals("1115 minut", pf.print(Period.minutes(1115)));
        assertEquals("2112 minut", pf.print(Period.minutes(2112)));
        assertEquals("2115 minut", pf.print(Period.minutes(2115)));
        assertEquals("2212 minut", pf.print(Period.minutes(2212)));
        assertEquals("2215 minut", pf.print(Period.minutes(2215)));
        assertEquals("22 minuty", pf.print(Period.minutes(22)));
        assertEquals("25 minut", pf.print(Period.minutes(25)));
        assertEquals("1122 minuty", pf.print(Period.minutes(1122)));
        assertEquals("1125 minut", pf.print(Period.minutes(1125)));
        assertEquals("2122 minuty", pf.print(Period.minutes(2122)));
        assertEquals("2125 minut", pf.print(Period.minutes(2125)));
        assertEquals("2222 minuty", pf.print(Period.minutes(2222)));
        assertEquals("2225 minut", pf.print(Period.minutes(2225)));

        assertEquals("1 sekunda", pf.print(Period.seconds(1)));
        assertEquals("2 sekundy", pf.print(Period.seconds(2)));
        assertEquals("5 sekund", pf.print(Period.seconds(5)));
        assertEquals("12 sekund", pf.print(Period.seconds(12)));
        assertEquals("15 sekund", pf.print(Period.seconds(15)));
        assertEquals("1112 sekund", pf.print(Period.seconds(1112)));
        assertEquals("1115 sekund", pf.print(Period.seconds(1115)));
        assertEquals("2112 sekund", pf.print(Period.seconds(2112)));
        assertEquals("2115 sekund", pf.print(Period.seconds(2115)));
        assertEquals("2212 sekund", pf.print(Period.seconds(2212)));
        assertEquals("2215 sekund", pf.print(Period.seconds(2215)));
        assertEquals("22 sekundy", pf.print(Period.seconds(22)));
        assertEquals("25 sekund", pf.print(Period.seconds(25)));
        assertEquals("1122 sekundy", pf.print(Period.seconds(1122)));
        assertEquals("1125 sekund", pf.print(Period.seconds(1125)));
        assertEquals("2122 sekundy", pf.print(Period.seconds(2122)));
        assertEquals("2125 sekund", pf.print(Period.seconds(2125)));
        assertEquals("2222 sekundy", pf.print(Period.seconds(2222)));
        assertEquals("2225 sekund", pf.print(Period.seconds(2225)));

        assertEquals("1 milisekunda", pf.print(Period.millis(1)));
        assertEquals("2 milisekundy", pf.print(Period.millis(2)));
        assertEquals("5 milisekund", pf.print(Period.millis(5)));
        assertEquals("12 milisekund", pf.print(Period.millis(12)));
        assertEquals("15 milisekund", pf.print(Period.millis(15)));
        assertEquals("1112 milisekund", pf.print(Period.millis(1112)));
        assertEquals("1115 milisekund", pf.print(Period.millis(1115)));
        assertEquals("2112 milisekund", pf.print(Period.millis(2112)));
        assertEquals("2115 milisekund", pf.print(Period.millis(2115)));
        assertEquals("2212 milisekund", pf.print(Period.millis(2212)));
        assertEquals("2215 milisekund", pf.print(Period.millis(2215)));
        assertEquals("22 milisekundy", pf.print(Period.millis(22)));
        assertEquals("25 milisekund", pf.print(Period.millis(25)));
        assertEquals("1122 milisekundy", pf.print(Period.millis(1122)));
        assertEquals("1125 milisekund", pf.print(Period.millis(1125)));
        assertEquals("2122 milisekundy", pf.print(Period.millis(2122)));
        assertEquals("2125 milisekund", pf.print(Period.millis(2125)));
        assertEquals("2222 milisekundy", pf.print(Period.millis(2222)));
        assertEquals("2225 milisekund", pf.print(Period.millis(2225)));
    }
    
    // -----------------------------------------------------------------------
    // wordBased(new Locale("ru")
    // -----------------------------------------------------------------------
    public void test_wordBased_ru_FormatOneField() {
        Period p = Period.days(2);
        assertEquals("2 дня", PeriodFormat.wordBased(RU).print(p));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_ru_formatTwoFields() {
        Period p = Period.years(1).withMonths(2);
        assertEquals("1 год и 2 месяца", PeriodFormat.wordBased(RU).print(p));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_ru_parseOneField() {
        Period p = Period.years(1);
        assertEquals(p, PeriodFormat.wordBased(RU).parsePeriod("1 год"));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_ru_parseTwoFields() {
        Period p = Period.hours(1).withMillis(5);
        assertEquals(p, PeriodFormat.wordBased(RU).parsePeriod("1 час и 5 миллисекунд"));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_ru_checkRedundantSeparator() {
        try {
            PeriodFormat.wordBased(RU).parsePeriod("2 дня and 5 минут");
            fail("No exception was caught");
        } catch (Exception e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_ru_cached() {
        assertSame(PeriodFormat.wordBased(RU), PeriodFormat.wordBased(RU));
    }

    // -----------------------------------------------------------------------
    public void test_wordBased_ru_regEx() {
        PeriodFormatter pf = PeriodFormat.wordBased(RU);
        assertEquals("1 год", pf.print(Period.years(1)));
        assertEquals("11 лет", pf.print(Period.years(11)));
        assertEquals("21 год", pf.print(Period.years(21)));
        assertEquals("101 год", pf.print(Period.years(101)));
        assertEquals("111 лет", pf.print(Period.years(111)));
        assertEquals("121 год", pf.print(Period.years(121)));
        assertEquals("2001 год", pf.print(Period.years(2001)));
        assertEquals("2 года", pf.print(Period.years(2)));
        assertEquals("3 года", pf.print(Period.years(3)));
        assertEquals("4 года", pf.print(Period.years(4)));
        assertEquals("12 лет", pf.print(Period.years(12)));
        assertEquals("13 лет", pf.print(Period.years(13)));
        assertEquals("14 лет", pf.print(Period.years(14)));
        assertEquals("22 года", pf.print(Period.years(22)));
        assertEquals("23 года", pf.print(Period.years(23)));
        assertEquals("24 года", pf.print(Period.years(24)));
        assertEquals("102 года", pf.print(Period.years(102)));
        assertEquals("112 лет", pf.print(Period.years(112)));
        assertEquals("124 года", pf.print(Period.years(124)));
        assertEquals("5 лет", pf.print(Period.years(5)));
        assertEquals("15 лет", pf.print(Period.years(15)));
        assertEquals("25 лет", pf.print(Period.years(25)));
        assertEquals("105 лет", pf.print(Period.years(105)));
        assertEquals("1005 лет", pf.print(Period.years(1005)));

        assertEquals("1 месяц", pf.print(Period.months(1)));
        assertEquals("11 месяцев", pf.print(Period.months(11)));
        assertEquals("21 месяц", pf.print(Period.months(21)));
        assertEquals("101 месяц", pf.print(Period.months(101)));
        assertEquals("111 месяцев", pf.print(Period.months(111)));
        assertEquals("121 месяц", pf.print(Period.months(121)));
        assertEquals("2001 месяц", pf.print(Period.months(2001)));
        assertEquals("2 месяца", pf.print(Period.months(2)));
        assertEquals("3 месяца", pf.print(Period.months(3)));
        assertEquals("4 месяца", pf.print(Period.months(4)));
        assertEquals("12 месяцев", pf.print(Period.months(12)));
        assertEquals("13 месяцев", pf.print(Period.months(13)));
        assertEquals("14 месяцев", pf.print(Period.months(14)));
        assertEquals("22 месяца", pf.print(Period.months(22)));
        assertEquals("23 месяца", pf.print(Period.months(23)));
        assertEquals("24 месяца", pf.print(Period.months(24)));
        assertEquals("102 месяца", pf.print(Period.months(102)));
        assertEquals("112 месяцев", pf.print(Period.months(112)));
        assertEquals("124 месяца", pf.print(Period.months(124)));
        assertEquals("5 месяцев", pf.print(Period.months(5)));
        assertEquals("15 месяцев", pf.print(Period.months(15)));
        assertEquals("25 месяцев", pf.print(Period.months(25)));
        assertEquals("105 месяцев", pf.print(Period.months(105)));
        assertEquals("1005 месяцев", pf.print(Period.months(1005)));

        assertEquals("1 неделя", pf.print(Period.weeks(1)));
        assertEquals("11 недель", pf.print(Period.weeks(11)));
        assertEquals("21 неделя", pf.print(Period.weeks(21)));
        assertEquals("101 неделя", pf.print(Period.weeks(101)));
        assertEquals("111 недель", pf.print(Period.weeks(111)));
        assertEquals("121 неделя", pf.print(Period.weeks(121)));
        assertEquals("2001 неделя", pf.print(Period.weeks(2001)));
        assertEquals("2 недели", pf.print(Period.weeks(2)));
        assertEquals("3 недели", pf.print(Period.weeks(3)));
        assertEquals("4 недели", pf.print(Period.weeks(4)));
        assertEquals("12 недель", pf.print(Period.weeks(12)));
        assertEquals("13 недель", pf.print(Period.weeks(13)));
        assertEquals("14 недель", pf.print(Period.weeks(14)));
        assertEquals("22 недели", pf.print(Period.weeks(22)));
        assertEquals("23 недели", pf.print(Period.weeks(23)));
        assertEquals("24 недели", pf.print(Period.weeks(24)));
        assertEquals("102 недели", pf.print(Period.weeks(102)));
        assertEquals("112 недель", pf.print(Period.weeks(112)));
        assertEquals("124 недели", pf.print(Period.weeks(124)));
        assertEquals("5 недель", pf.print(Period.weeks(5)));
        assertEquals("15 недель", pf.print(Period.weeks(15)));
        assertEquals("25 недель", pf.print(Period.weeks(25)));
        assertEquals("105 недель", pf.print(Period.weeks(105)));
        assertEquals("1005 недель", pf.print(Period.weeks(1005)));

        assertEquals("1 день", pf.print(Period.days(1)));
        assertEquals("11 дней", pf.print(Period.days(11)));
        assertEquals("21 день", pf.print(Period.days(21)));
        assertEquals("101 день", pf.print(Period.days(101)));
        assertEquals("111 дней", pf.print(Period.days(111)));
        assertEquals("121 день", pf.print(Period.days(121)));
        assertEquals("2001 день", pf.print(Period.days(2001)));
        assertEquals("2 дня", pf.print(Period.days(2)));
        assertEquals("3 дня", pf.print(Period.days(3)));
        assertEquals("4 дня", pf.print(Period.days(4)));
        assertEquals("12 дней", pf.print(Period.days(12)));
        assertEquals("13 дней", pf.print(Period.days(13)));
        assertEquals("14 дней", pf.print(Period.days(14)));
        assertEquals("22 дня", pf.print(Period.days(22)));
        assertEquals("23 дня", pf.print(Period.days(23)));
        assertEquals("24 дня", pf.print(Period.days(24)));
        assertEquals("102 дня", pf.print(Period.days(102)));
        assertEquals("112 дней", pf.print(Period.days(112)));
        assertEquals("124 дня", pf.print(Period.days(124)));
        assertEquals("5 дней", pf.print(Period.days(5)));
        assertEquals("15 дней", pf.print(Period.days(15)));
        assertEquals("25 дней", pf.print(Period.days(25)));
        assertEquals("105 дней", pf.print(Period.days(105)));
        assertEquals("1005 дней", pf.print(Period.days(1005)));

        assertEquals("1 час", pf.print(Period.hours(1)));
        assertEquals("11 часов", pf.print(Period.hours(11)));
        assertEquals("21 час", pf.print(Period.hours(21)));
        assertEquals("101 час", pf.print(Period.hours(101)));
        assertEquals("111 часов", pf.print(Period.hours(111)));
        assertEquals("121 час", pf.print(Period.hours(121)));
        assertEquals("2001 час", pf.print(Period.hours(2001)));
        assertEquals("2 часа", pf.print(Period.hours(2)));
        assertEquals("3 часа", pf.print(Period.hours(3)));
        assertEquals("4 часа", pf.print(Period.hours(4)));
        assertEquals("12 часов", pf.print(Period.hours(12)));
        assertEquals("13 часов", pf.print(Period.hours(13)));
        assertEquals("14 часов", pf.print(Period.hours(14)));
        assertEquals("22 часа", pf.print(Period.hours(22)));
        assertEquals("23 часа", pf.print(Period.hours(23)));
        assertEquals("24 часа", pf.print(Period.hours(24)));
        assertEquals("102 часа", pf.print(Period.hours(102)));
        assertEquals("112 часов", pf.print(Period.hours(112)));
        assertEquals("124 часа", pf.print(Period.hours(124)));
        assertEquals("5 часов", pf.print(Period.hours(5)));
        assertEquals("15 часов", pf.print(Period.hours(15)));
        assertEquals("25 часов", pf.print(Period.hours(25)));
        assertEquals("105 часов", pf.print(Period.hours(105)));
        assertEquals("1005 часов", pf.print(Period.hours(1005)));

        assertEquals("1 минута", pf.print(Period.minutes(1)));
        assertEquals("11 минут", pf.print(Period.minutes(11)));
        assertEquals("21 минута", pf.print(Period.minutes(21)));
        assertEquals("101 минута", pf.print(Period.minutes(101)));
        assertEquals("111 минут", pf.print(Period.minutes(111)));
        assertEquals("121 минута", pf.print(Period.minutes(121)));
        assertEquals("2001 минута", pf.print(Period.minutes(2001)));
        assertEquals("2 минуты", pf.print(Period.minutes(2)));
        assertEquals("3 минуты", pf.print(Period.minutes(3)));
        assertEquals("4 минуты", pf.print(Period.minutes(4)));
        assertEquals("12 минут", pf.print(Period.minutes(12)));
        assertEquals("13 минут", pf.print(Period.minutes(13)));
        assertEquals("14 минут", pf.print(Period.minutes(14)));
        assertEquals("22 минуты", pf.print(Period.minutes(22)));
        assertEquals("23 минуты", pf.print(Period.minutes(23)));
        assertEquals("24 минуты", pf.print(Period.minutes(24)));
        assertEquals("102 минуты", pf.print(Period.minutes(102)));
        assertEquals("112 минут", pf.print(Period.minutes(112)));
        assertEquals("124 минуты", pf.print(Period.minutes(124)));
        assertEquals("5 минут", pf.print(Period.minutes(5)));
        assertEquals("15 минут", pf.print(Period.minutes(15)));
        assertEquals("25 минут", pf.print(Period.minutes(25)));
        assertEquals("105 минут", pf.print(Period.minutes(105)));
        assertEquals("1005 минут", pf.print(Period.minutes(1005)));

        assertEquals("1 секунда", pf.print(Period.seconds(1)));
        assertEquals("11 секунд", pf.print(Period.seconds(11)));
        assertEquals("21 секунда", pf.print(Period.seconds(21)));
        assertEquals("101 секунда", pf.print(Period.seconds(101)));
        assertEquals("111 секунд", pf.print(Period.seconds(111)));
        assertEquals("121 секунда", pf.print(Period.seconds(121)));
        assertEquals("2001 секунда", pf.print(Period.seconds(2001)));
        assertEquals("2 секунды", pf.print(Period.seconds(2)));
        assertEquals("3 секунды", pf.print(Period.seconds(3)));
        assertEquals("4 секунды", pf.print(Period.seconds(4)));
        assertEquals("12 секунд", pf.print(Period.seconds(12)));
        assertEquals("13 секунд", pf.print(Period.seconds(13)));
        assertEquals("14 секунд", pf.print(Period.seconds(14)));
        assertEquals("22 секунды", pf.print(Period.seconds(22)));
        assertEquals("23 секунды", pf.print(Period.seconds(23)));
        assertEquals("24 секунды", pf.print(Period.seconds(24)));
        assertEquals("102 секунды", pf.print(Period.seconds(102)));
        assertEquals("112 секунд", pf.print(Period.seconds(112)));
        assertEquals("124 секунды", pf.print(Period.seconds(124)));
        assertEquals("5 секунд", pf.print(Period.seconds(5)));
        assertEquals("15 секунд", pf.print(Period.seconds(15)));
        assertEquals("25 секунд", pf.print(Period.seconds(25)));
        assertEquals("105 секунд", pf.print(Period.seconds(105)));
        assertEquals("1005 секунд", pf.print(Period.seconds(1005)));

        assertEquals("1 миллисекунда", pf.print(Period.millis(1)));
        assertEquals("11 миллисекунд", pf.print(Period.millis(11)));
        assertEquals("21 миллисекунда", pf.print(Period.millis(21)));
        assertEquals("101 миллисекунда", pf.print(Period.millis(101)));
        assertEquals("111 миллисекунд", pf.print(Period.millis(111)));
        assertEquals("121 миллисекунда", pf.print(Period.millis(121)));
        assertEquals("2001 миллисекунда", pf.print(Period.millis(2001)));
        assertEquals("2 миллисекунды", pf.print(Period.millis(2)));
        assertEquals("3 миллисекунды", pf.print(Period.millis(3)));
        assertEquals("4 миллисекунды", pf.print(Period.millis(4)));
        assertEquals("12 миллисекунд", pf.print(Period.millis(12)));
        assertEquals("13 миллисекунд", pf.print(Period.millis(13)));
        assertEquals("14 миллисекунд", pf.print(Period.millis(14)));
        assertEquals("22 миллисекунды", pf.print(Period.millis(22)));
        assertEquals("23 миллисекунды", pf.print(Period.millis(23)));
        assertEquals("24 миллисекунды", pf.print(Period.millis(24)));
        assertEquals("102 миллисекунды", pf.print(Period.millis(102)));
        assertEquals("112 миллисекунд", pf.print(Period.millis(112)));
        assertEquals("124 миллисекунды", pf.print(Period.millis(124)));
        assertEquals("5 миллисекунд", pf.print(Period.millis(5)));
        assertEquals("15 миллисекунд", pf.print(Period.millis(15)));
        assertEquals("25 миллисекунд", pf.print(Period.millis(25)));
        assertEquals("105 миллисекунд", pf.print(Period.millis(105)));
        assertEquals("1005 миллисекунд", pf.print(Period.millis(1005)));
    }

    //-----------------------------------------------------------------------
    // Cross check languages
    //-----------------------------------------------------------------------
    public void test_wordBased_fr_from_de() {
        Locale.setDefault(DE);
        Period p = new Period(0, 0, 0, 1, 5, 6, 7, 8);
        assertEquals("1 jour, 5 heures, 6 minutes, 7 secondes et 8 millisecondes", PeriodFormat.wordBased(FR).print(p));
    }

    public void test_wordBased_fr_from_nl() {
        Locale.setDefault(NL);
        Period p = new Period(0, 0, 0, 1, 5, 6, 7, 8);
        assertEquals("1 jour, 5 heures, 6 minutes, 7 secondes et 8 millisecondes", PeriodFormat.wordBased(FR).print(p));
    }

    public void test_wordBased_en_from_de() {
        Locale.setDefault(DE);
        Period p = new Period(0, 0, 0, 1, 5, 6, 7, 8);
        assertEquals("1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds", PeriodFormat.wordBased(EN).print(p));
    }

    public void test_wordBased_en_from_nl() {
        Locale.setDefault(NL);
        Period p = new Period(0, 0, 0, 1, 5, 6, 7, 8);
        assertEquals("1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds", PeriodFormat.wordBased(EN).print(p));
    }

    public void test_wordBased_en_from_pl() {
        Locale.setDefault(PL);
        Period p = new Period(0, 0, 0, 1, 5, 6, 7, 8);
        assertEquals("1 day, 5 hours, 6 minutes, 7 seconds and 8 milliseconds", PeriodFormat.wordBased(EN).print(p));
    }

    public void test_wordBased_pl_from_fr() {
        Locale.setDefault(FR);
        Period p = new Period(0, 0, 0, 1, 5, 6, 7, 8);
        assertEquals("1 dzie\u0144, 5 godzin, 6 minut, 7 sekund i 8 milisekund", PeriodFormat.wordBased(PL).print(p));
    }

    //-----------------------------------------------------------------------
    public void test_getDefault_localeValue() {
        PeriodFormatter pf = PeriodFormat.getDefault();
        assertEquals(Locale.ENGLISH, pf.getLocale());
    }

    public void test_wordBased_localeValue() {
        PeriodFormatter pf = PeriodFormat.wordBased();
        assertEquals(DE, pf.getLocale());
    }

    public void test_wordBasedWithLocale_localeValue() {
        PeriodFormatter pf = PeriodFormat.wordBased(FR);
        assertEquals(FR, pf.getLocale());
    }

    //-----------------------------------------------------------------------
    public void test_wordBased_en_withLocale_pt() {
        Period p = Period.days(2).withHours(5);
        PeriodFormatter format1 = PeriodFormat.wordBased(EN);
        assertEquals("2 days and 5 hours", format1.print(p));
        assertEquals(p, format1.parsePeriod("2 days and 5 hours"));
        assertEquals(EN, format1.getLocale());
        
        PeriodFormatter format2 = format1.withLocale(PT);
        assertEquals("2 dias e 5 horas", format2.print(p));
        assertEquals(p, format2.parsePeriod("2 dias e 5 horas"));
        assertEquals(PT, format2.getLocale());
        
        PeriodFormatter format3 = format1.withLocale(DE);
        assertEquals("2 Tage und 5 Stunden", format3.print(p));
        assertEquals(p, format3.parsePeriod("2 Tage und 5 Stunden"));
        assertEquals(DE, format3.getLocale());
        
        PeriodFormatter format4 = format1.withLocale(null);
        assertEquals("2 days and 5 hours", format4.print(p));
        assertEquals(p, format4.parsePeriod("2 days and 5 hours"));
        assertEquals(null, format4.getLocale());
    }

}
