Joda-Time
---------

Joda-Time provides a quality replacement for the Java date and time classes.
The design allows for multiple calendar systems, while still providing a simple API.
The 'default' calendar is the ISO8601 standard which is used by XML.
The Gregorian, Julian, Buddhist, Coptic, Ethiopic and Islamic systems are also included.
Supporting classes include time zone, duration, format and parsing. 

**Joda-time is no longer in active development except to keep timezone data up to date.**
From Java SE 8 onwards, users are asked to migrate to `java.time` (JSR-310) - a core part of the JDK which replaces this project.
For Android users, `java.time` is [added in API 26+](https://developer.android.com/reference/java/time/package-summary).
Projects needing to support lower API levels can use [the ThreeTenABP library](https://github.com/JakeWharton/ThreeTenABP).

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

Joda-Time is licensed under the business-friendly [Apache 2.0 licence](https://www.joda.org/joda-time/licenses.html).

![Tidelift dependency check](https://tidelift.com/badges/github/JodaOrg/joda-time)
[![CII Best Practices](https://bestpractices.coreinfrastructure.org/projects/6310/badge)](https://bestpractices.coreinfrastructure.org/projects/6310)


### Documentation
Various documentation is available:

* The [home page](https://www.joda.org/joda-time/)
* Two user guides - [quick](https://www.joda.org/joda-time/quickstart.html) and [full](https://www.joda.org/joda-time/userguide.html)
* The [Javadoc](https://www.joda.org/joda-time/apidocs/index.html)
* The [FAQ](https://www.joda.org/joda-time/faq.html) list
* Information on [downloading and installing](https://www.joda.org/joda-time/installation.html) Joda-Time including release notes


### Releases
[Release 2.12.2](https://www.joda.org/joda-time/download.html) is the current latest release.
This release is considered stable and worthy of the 2.x tag.
It depends on JDK 1.5 or later.

Available in the [Maven Central repository](https://search.maven.org/search?q=g:joda-time%20AND%20a:joda-time&core=gav)

**Maven configuration:**
```xml
<dependency>
  <groupId>joda-time</groupId>
  <artifactId>joda-time</artifactId>
  <version>2.12.2</version>
</dependency>
```

**Gradle configuration:**
```groovy
compile 'joda-time:joda-time:2.12.2'
```


### Related projects
Related projects at GitHub:
- https://github.com/JodaOrg/joda-time-hibernate
- https://github.com/JodaOrg/joda-time-jsptags
- https://github.com/JodaOrg/joda-time-i18n

Other related projects:
- https://www.joda.org/joda-time/related.html


### For enterprise
Available as part of the Tidelift Subscription.

Joda and the maintainers of thousands of other packages are working with Tidelift to deliver one enterprise subscription that covers all of the open source you use.

If you want the flexibility of open source and the confidence of commercial-grade software, this is for you.

[Learn more](https://tidelift.com/subscription/pkg/maven-joda-time-joda-time?utm_source=maven-joda-time-joda-time&utm_medium=github)


### Support
Please use [Stack Overflow](https://stackoverflow.com/questions/tagged/jodatime) for general usage questions.
GitHub [issues](https://github.com/JodaOrg/joda-time/issues) and [pull requests](https://github.com/JodaOrg/joda-time/pulls)
should be used when you want to help advance the project.

Any donations to support the project are accepted via [OpenCollective](https://opencollective.com/joda).

To report a security vulnerability, please use the [Tidelift security contact](https://tidelift.com/security).
Tidelift will coordinate the fix and disclosure.


### Development and Contributions
Joda-Time is developed using standard [GitHub tools](https://github.com/JodaOrg/joda-time).
A [checkstyle](https://checkstyle.sourceforge.io/) file is available, and PRs must comply with it.
The project can be built using [Apache Maven](https://maven.apache.org/), such as <code>mvn clean install</code>.
Continuous Integration takes place using [GitHub Actions](https://github.com/JodaOrg/joda-time/actions).
Units tests are written in [JUnit](https://junit.org/) and run as part of the build and continuous integration.
Changes via PR must include appropiate test coverage.

Note that Joda-Time is considered to be a largely “finished” project. No major enhancements are planned. If using Java SE 8, please migrate to java.time (JSR-310).


### Release process

* Update version (pom.xml, README.md, index.md, MANIFEST.MF, changes.xml)
* Commit and push
* Ensure on Java SE 8
* `mvn clean deploy -Doss.repo -Dgpg.passphrase=""`
* Website will be built and released by GitHub Actions
