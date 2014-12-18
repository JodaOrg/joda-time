## Quick start guide

This guide provides a quick introduction to Joda-Time.
For more information, see the full [user guide](userguide.html).

### Date and Time

Date and time is a surprisingly complex domain.
The many classes in Joda-Time are designed to allow the nuances of the domain to be fully expressed.

The five date-time classes that will be used most are:

* [`Instant`](apidocs/org/joda/time/Instant.html) - Immutable class representing an instantaneous point on the time-line
* [`DateTime`](apidocs/org/joda/time/DateTime.html) - Immutable replacement for JDK `Calendar`
* [`LocalDate`](apidocs/org/joda/time/LocalDate.html) - Immutable class representing a local date without a time (no time-zone)
* [`LocalTime`](apidocs/org/joda/time/LocalTime.html) - Immutable class representing a time without a date (no time-zone)
* [`LocalDateTime`](apidocs/org/joda/time/LocalDateTime.html) - Immutable class representing a local date and time (no time-zone)

An `Instant` is a good class to use for the timestamp of an event, as there is no calendar system or time-zone to worry about.
A `LocalDate` is a good class to use to represent a date of birth, as there is no need to refer to the time of day.
A `LocalTime` is a good class to use to represent the time of day that a shop opens or closes.
A `DateTime` is a good class to use as a general purpose replacement for the JDK `Calendar` class, where
the time-zone information is important.
For more detail, see the documentation on [instants](key_instant.html) and [partials](key_partial.html).


### Using the Date and Time classes

Each date-time class provides a variety of constructors.
These include the `Object` constructor.
This allows you to construct an instance from a variety of different objects:
For example, a `DateTime` can be constructed from the following objects:

* `Date` - a JDK instant
* `Calendar` - a JDK calendar
* `String` - in ISO8601 format
* `Long` - in milliseconds
* any Joda-Time date-time class

The use of an `Object` constructor is a little unusual, but it is used because the
list of types that can be converted is extensible.
The main advantage is that converting from a JDK `Date` or `Calendar` to a Joda-Time
class is easy - simply pass the JDK class into the constructor.
For example, this code converts a `java.util.Date` to a `DateTime`:

```
  java.util.Date juDate = new Date();
  DateTime dt = new DateTime(juDate);
```

Each date-time class provides simple easy methods
to access the date-time [fields](field.html).
For example, to access the month and year you can use:

```
  DateTime dt = new DateTime();
  int month = dt.getMonthOfYear();  // where January is 1 and December is 12
  int year = dt.getYear();
```

All the main date-time classes are immutable, like `String`, and cannot be changed
after creation. However, simple methods have been provided to alter field values
in a newly created object. For example, to set the year, or add 2 hours you can use:

```
  DateTime dt = new DateTime();
  DateTime year2000 = dt.withYear(2000);
  DateTime twoHoursLater = dt.plusHours(2);
```

In addition to the basic get methods, each date-time class provides property
methods for each field. These provide access to the full wealth of Joda-Time
functionality. For example, to access details about a month or year:

```
  DateTime dt = new DateTime();
  String monthName = dt.monthOfYear().getAsText();
  String frenchShortName = dt.monthOfYear().getAsShortText(Locale.FRENCH);
  boolean isLeapYear = dt.year().isLeap();
  DateTime rounded = dt.dayOfMonth().roundFloorCopy();
```

### Calendar systems and time-zones

Joda-Time provides support for multiple [calendar systems](key_chronology.html) and the full range of time-zones.
The [`Chronology`](apidocs/org/joda/time/Chronology.html)
and [`DateTimeZone`](apidocs/org/joda/time/DateTimeZone.html)
classes provide this support.

Joda-Time defaults to using the ISO calendar system, which is the *de facto* civil calendar
used by the world. The default time-zone is the same as the default of the JDK.
These default values can be overridden whenever necessary.
Please note that the ISO calendar system is historically inaccurate before 1583.

Joda-Time uses a pluggable mechanism for calendars.
By contrast, the JDK uses subclasses such as `GregorianCalendar`.
This code obtains a Joda-Time chronology by calling one of the factory methods on the `Chronology` implementation:

```
  Chronology coptic = CopticChronology.getInstance();
```

Time zones are implemented as part of the chronology.
The code obtains a Joda-Time chronology in the Tokyo time-zone:

```
  DateTimeZone zone = DateTimeZone.forID("Asia/Tokyo");
  Chronology gregorianJuian = GJChronology.getInstance(zone);
```

### Intervals and time periods

Joda-Time provides support for intervals and time periods.

An [interval](key_interval.html) is represented by the [`Interval`](apidocs/org/joda/time/Interval.html) class.
It holds a start and end date-time, and allows operations based around that range of time.

A time [period](key_period.html) is represented by the [`Period`](apidocs/org/joda/time/Period.html) class.
This holds a period such as 6 months, 3 days and 7 hours.
You can create a `Period` directly, or derive it from an interval.

A time [duration](key_duration.html) is represented by the [`Duration`](apidocs/org/joda/time/Duration.html) class.
This holds an exact duration in milliseconds.
You can create a `Duration` directly, or derive it from an interval.

Although a period and a duration may seem similar, they operate differently.
For example, consider adding one day to a `DateTime` at the daylight savings cutover:

```
  DateTime dt = new DateTime(2005, 3, 26, 12, 0, 0, 0);
  DateTime plusPeriod = dt.plus(Period.days(1));
  DateTime plusDuration = dt.plus(new Duration(24L*60L*60L*1000L));
```

Adding a period will add 23 hours in this case, not 24 because of the daylight
savings change, thus the time of the result will still be midday.
Adding a duration will add 24 hours no matter what, thus the time of the result will change to 13:00.

### More information

See the following for more information:

* The [full user guide](userguide.html)
* The [key concepts](key.html)
* The [available calendar systems](cal.html)
* The [FAQs](faq.html)
* The [Javadoc](apidocs/index.html)
