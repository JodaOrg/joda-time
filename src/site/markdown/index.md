## <i></i> About

Joda-Time provides a quality replacement for the Java date and time classes.

Joda-Time is the *de facto* standard date and time library for Java prior to Java SE 8.
Users are now asked to migrate to `java.time` (JSR-310).

Joda-Time is licensed under the business-friendly [Apache 2.0 licence](licenses.html).


## <i></i> Features

A selection of key features:

* `LocalDate` - date without time
* `LocalTime` - time without date
* `Instant` - an instantaneous point on the time-line
* `DateTime` - full date and time with time-zone
* `DateTimeZone` - a better time-zone
* `Duration` and `Period` - amounts of time
* `Interval` - the time between two instants
* A comprehensive and flexible formatter-parser


## <i></i> Documentation

Various documentation is available:

* The [getting started](quickstart.html) guide
* The helpful [user guide](userguide.html)
* The [key concepts](key.html) and [chronology](cal.html) guides
* The [Javadoc](apidocs/index.html)
* The list of [FAQ](faq.html)s.
* The [change notes](changes-report.html) for each release
* The [GitHub](https://github.com/JodaOrg/joda-time) source repository


---

## <i></i> Why Joda Time?

The standard date and time classes prior to Java SE 8 are poor.
By tackling this problem head-on, Joda-Time became
the de facto standard date and time library for Java prior to Java SE 8.
**Note that from Java SE 8 onwards, users are asked to migrate to `java.time` (JSR-310) -
a core part of the JDK which replaces this project.**

The design allows for multiple calendar systems, while still providing a simple API.
The "default" calendar is the [ISO8601](cal_iso.html) standard which is used by many other standards.
The Gregorian, Julian, Buddhist, Coptic, Ethiopic and Islamic calendar systems are also included.
Supporting classes include time zone, duration, format and parsing.

As a flavour of Joda-Time, here's some example code:

<div class="source">
<pre>
public boolean isAfterPayDay(<a href="apidocs/org/joda/time/DateTime.html"><b>DateTime</b></a> datetime) {
  if (datetime.getMonthOfYear() == 2) {   // February is month 2!!
    return datetime.getDayOfMonth() > 26;
  }
  return datetime.getDayOfMonth() > 28;
}
</pre>
<pre>
public <a href="apidocs/org/joda/time/Days.html"><b>Days</b></a> daysToNewYear(<a href="apidocs/org/joda/time/LocalDate.html"><b>LocalDate</b></a> fromDate) {
  <a href="apidocs/org/joda/time/LocalDate.html"><b>LocalDate</b></a> newYear = fromDate.plusYears(1).withDayOfYear(1);
  return <a href="apidocs/org/joda/time/Days.html"><b>Days</b></a>.daysBetween(fromDate, newYear);
}
</pre>
<pre>
public boolean isRentalOverdue(<a href="apidocs/org/joda/time/DateTime.html"><b>DateTime</b></a> datetimeRented) {
  <a href="apidocs/org/joda/time/Period.html"><b>Period</b></a> rentalPeriod = new <a href="apidocs/org/joda/time/Period.html"><b>Period</b></a>().withDays(2).withHours(12);
  return datetimeRented.plus(rentalPeriod).isBeforeNow();
}
</pre>
<pre>
public String getBirthMonthText(<a href="apidocs/org/joda/time/LocalDate.html"><b>LocalDate</b></a> dateOfBirth) {
  return dateOfBirth.monthOfYear().getAsText(Locale.ENGLISH);
}
</pre>
</div>


## <i></i> Rationale

Here are some of our reasons for developing and using Joda-Time:

* <b>Easy to Use</b>.
Calendar makes accessing 'normal' dates difficult, due to the lack of simple methods.
Joda-Time has straightforward <a href="field.html">field accessors</a> such as
<code>getYear()</code> or <code>getDayOfWeek()</code>.
* <b>Easy to Extend</b>.
The JDK supports multiple calendar systems via subclasses of <code>Calendar</code>.
This is clunky, and in practice it is very difficult to write another calendar system.
Joda-Time supports multiple calendar systems via a pluggable system based on the
<code>Chronology</code> class.
* <b>Comprehensive Feature Set</b>.
The library is intended to provide all the functionality that is required for date-time
calculations. It already provides out-of-the-box features, such as support for oddball
date formats, which are difficult to replicate with the JDK.
* <b>Up-to-date Time Zone calculations</b>.
The <a href="timezones.html">time zone implementation</a> is based on
<a href="https://github.com/JodaOrg/global-tz">global-tz</a>, which is a fork of the
original data provided by IANA.
It is updated several times a year. New Joda-Time releases incorporate all changes
made to this database. Should the changes be needed earlier,
<a href="tz_update.html">manually updating the zone data</a> is easy.
* <b>Calendar support</b>.
The library provides [8 calendar systems](cal.html).
* <b>Easy interoperability</b>.
The library internally uses a millisecond instant which is identical to the JDK and similar
to other common time representations. This makes interoperability easy, and Joda-Time comes
with out-of-the-box JDK interoperability.
* <b>Better Performance Characteristics</b>.
Calendar has strange performance characteristics as it recalculates fields at unexpected moments.
Joda-Time does only the minimal calculation for the field that is being accessed.
* <b>Good Test Coverage</b>.
Joda-Time has a comprehensive set of developer tests, providing assurance of the library's quality.
* <b>Complete Documentation</b>.
There is a full <a href="userguide.html">User Guide</a> which provides an overview and covers
common usage scenarios. The <a href="apidocs/index.html">javadoc</a>
is extremely detailed and covers the rest of the API.
* <b>Maturity</b>.
The library has been under active development since 2002.
It is a mature and reliable code base.
A number of <a href="related.html">related projects</a> are now available.
* <b>Open Source</b>.
Joda-Time is licenced under the business friendly <a href="licenses.html">Apache License Version 2.0</a>.

---

## <i></i> Releases

[Release 2.12.2](download.html) is the current latest release.
This release is considered stable and worthy of the 2.x tag.
See the [change notes](changes-report.html) for full details.

Joda-Time requires Java SE 5 or later and has [no dependencies](dependencies.html).
There is a *compile-time* dependency on [Joda-Convert](/joda-convert/),
but this is not required at runtime thanks to the magic of annotations.

Available in [Maven Central](https://search.maven.org/search?q=g:joda-time%20AND%20a:joda-time&core=gav).

```xml
<dependency>
  <groupId>joda-time</groupId>
  <artifactId>joda-time</artifactId>
  <version>2.12.2</version>
</dependency>
```

The 2.x product line will be supported using standard Java mechanisms.
The main public API will remain backwards compatible for both source and binary in the 2.x stream.
The version number will change to 3.0 to indicate a significant change in compatibility.

Joda-Time v2.x is an evolution of the 1.x codebase, not a major rewrite.
It is almost completely source and binary compatible with version 1.x.
Key changes included the use of Java SE 5 or later, generics, and the removal of some (but not all)
deprecated methods. See the [upgrade notes](upgradeto200.html) for full details when upgrading from 1.x
including information on the corner cases that are not compatible.
The ancient release [1.6.2](https://sourceforge.net/projects/joda-time/files/joda-time/1.6.2/) was
the last release to support Java SE 4 and the last v1.x release.

Java module name: `org.joda.time`.

---

### For Enterprise

[Available as part of the Tidelift Subscription](https://tidelift.com/subscription/pkg/maven-joda-time-joda-time?utm_source=maven-joda-time-joda-time&utm_medium=referral&utm_campaign=enterprise).

Joda and the maintainers of thousands of other packages are working with Tidelift to deliver one
enterprise subscription that covers all of the open source you use.

If you want the flexibility of open source and the confidence of commercial-grade software, this is for you.
[Learn more](https://tidelift.com/subscription/pkg/maven-joda-time-joda-time?utm_source=maven-joda-time-joda-time&utm_medium=referral&utm_campaign=enterprise).


### Support

Please use [Stack Overflow](https://stackoverflow.com/questions/tagged/jodatime) for general usage questions.
GitHub [issues](https://github.com/JodaOrg/joda-time/issues) and [pull requests](https://github.com/JodaOrg/joda-time/pulls)
should be used when you want to help advance the project.

Any donations to support the project are accepted via [OpenCollective](https://opencollective.com/joda).

Note that Joda-Time is considered to be a largely "finished" project.
No major enhancements are planned.
If using Java SE 8, please migrate to `java.time` (JSR-310).

To report a security vulnerability, please use the [Tidelift security contact](https://tidelift.com/security).
Tidelift will coordinate the fix and disclosure.
