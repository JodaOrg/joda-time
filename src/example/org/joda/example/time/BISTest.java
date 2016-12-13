package com.example;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.chrono.BISChronology;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class BISTest {
    public static void main(String args[]){
        System.out.println("this is bishwash");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy / M / d");

        DateTime dt = new DateTime(ISOChronology.getInstance());
//        System.out.println("ISO chronology :: "  + fmt.print(dt));
//        System.out.println("ISO chronology year "  + dt.get(DateTimeFieldType.year()));
//        System.out.println("ISO chronology month "  + dt.get(DateTimeFieldType.monthOfYear()));
//        System.out.println("ISO chronology day "  + dt.get(DateTimeFieldType.dayOfMonth()));
//        System.out.println("ISO chronology hrs "  + dt.get(DateTimeFieldType.hourOfDay()));
        System.out.println("ISO chronology mins "  + dt.monthOfYear().getAsText());

        DateTime bdt = new DateTime(BuddhistChronology.getInstance());
        System.out.println("Coptic chronology year " + bdt.get(DateTimeFieldType.year()));
        System.out.println("Coptic chronology month " + bdt.get(DateTimeFieldType.monthOfYear())+" \t DAY >> "+ bdt.monthOfYear().getAsText());

//        DateTime nepbidt = new DateTime(NepChronology.getInstance());
//        System.out.println("BIS chronology :: "  + fmt.print(nepbidt));

        DateTime dtISO = new DateTime(1944, 4, 10, 12, 0, 0, 0);
        for (int i = 0; i < 10000; i++) {
            dtISO = dtISO.plusDays(1);
            DateTime dtNep = dtISO.withChronology(BISChronology.getInstance());
            dtNep.getDayOfWeek();
            System.out.println("ISO chronology :: "  + fmt.print(dtISO) +"<< \t >>"+ "BIS chronology :: "  + fmt.print(dtNep)+" \t DAY >> "+dtNep.monthOfYear().getAsShortText());
        }


//        System.out.println("nepbidt bishwash chronology year " + nepbidt.get(DateTimeFieldType.year()));
//        System.out.println("nepbidt bishwash chronology month " + nepbidt.get(DateTimeFieldType.monthOfYear()));
//        System.out.println("nepbidt bishwash chronology day " + nepbidt.get(DateTimeFieldType.dayOfMonth()));
//        System.out.println("nepbidt bishwash  hrs "  + dt.get(DateTimeFieldType.hourOfDay()));
//        System.out.println("nepbidt bishwash  mins "  + dt.get(DateTimeFieldType.minuteOfHour()));

//        DateTime ab = new DateTime(IslamicChronology.getInstance());
//        System.out.println("test chronology year " + ab.get(DateTimeFieldType.year()));
//        System.out.println("test chronology month " + ab.get(DateTimeFieldType.monthOfYear()));


//        for (int i = 2000; i <= 2090; i++) {
//            int total = DateUtils.getTotalDaysInYear(i);
//            System.out.println("total days in year "+ i+" >> "+ total+ " mdays "+ DateUtils.getDayOfMonth(i, 200));
//        }
    }
}
