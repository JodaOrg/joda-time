Joda-Time
---------

Joda-Time provides a quality replacement for the Java date and time classes.
The design allows for multiple calendar systems, while still providing a simple API.
The 'default' calendar is the ISO8601 standard which is used by XML.
The Gregorian, Julian, Buddhist, Coptic, Ethiopic and Islamic systems are also included, and we welcome further additions.
Supporting classes include time zone, duration, format and parsing. 

As a flavour of Joda-Time, here's some example code:

```java
public boolean isAfterPayDay(DateTime datetime) {
  if (datetime.getMonthOfYear() == 2) {   // February is month 2!!
    return datetime.getDayOfMonth() > 26;
  }
  return datetime.getDayOfMonth() > 28;
}

public Days daysToNewYear(LocalDate fromDate) {
  LocalDate newYear = fromDate.plusYears(1).withDayOfYear(1);
  return Days.daysBetween(fromDate, newYear);
}

public boolean isRentalOverdue(DateTime datetimeRented) {
  Period rentalPeriod = new Period().withDays(2).withHours(12);
  return datetimeRented.plus(rentalPeriod).isBeforeNow();
}

public String getBirthMonthText(LocalDate dateOfBirth) {
  return dateOfBirth.monthOfYear().getAsText(Locale.ENGLISH);
}
```

Joda-Time is licensed under the business-friendly [Apache 2.0 licence](http://www.joda.org/joda-time/license.html).


### Documentation
Various documentation is available:

* The [home page](http://www.joda.org/joda-time/)
* Two user guides - [quick](http://www.joda.org/joda-time/quickstart.html) and [full](http://www.joda.org/joda-time/userguide.html)
* The [Javadoc](http://www.joda.org/joda-time/apidocs/index.html)
* The [FAQ](http://www.joda.org/joda-time/faq.html) list
* Information on [downloading and installing](http://www.joda.org/joda-time/installation.html) Joda-Time including release notes


### Releases
[Release 2.9.4](http://www.joda.org/joda-time/download.html) is the current latest release.
This release is considered stable and worthy of the 2.x tag.
It depends on JDK 1.5 or later.

Available in the [Maven Central repository](http://search.maven.org/#artifactdetails|joda-time|joda-time|2.9.4|jar)

**Maven configuration:**
```xml
<dependency>
  <groupId>joda-time</groupId>
  <artifactId>joda-time</artifactId>
  <version>2.9.4</version>
</dependency>
```

**Gradle configuration:**
```groovy
compile 'joda-time:joda-time:2.9.4'
```

### Related projects
Related projects at GitHub:
- https://github.com/JodaOrg/joda-time-hibernate
- https://github.com/JodaOrg/joda-time-jsptags
- https://github.com/JodaOrg/joda-time-i18n

Other related projects:
- http://www.joda.org/joda-time/related.html


### Support
Please use GitHub issues and Pull Requests for support.


### History
Issue tracking and active development is at GitHub.
Historically, the project was at [Sourceforge](https://sourceforge.net/projects/joda-time/).
