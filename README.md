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


### Documentation
Various documentation is available:

* The [home page](https://www.joda.org/joda-time/)
* Two user guides - [quick](https://www.joda.org/joda-time/quickstart.html) and [full](https://www.joda.org/joda-time/userguide.html)
* The [Javadoc](https://www.joda.org/joda-time/apidocs/index.html)
* The [FAQ](https://www.joda.org/joda-time/faq.html) list
* Information on [downloading and installing](https://www.joda.org/joda-time/installation.html) Joda-Time including release notes


### Releases
[Release 2.10.3](https://www.joda.org/joda-time/download.html) is the current latest release.
This release is considered stable and worthy of the 2.x tag.
It depends on JDK 1.5 or later.

Available in the [Maven Central repository](https://search.maven.org/search?q=g:joda-time%20AND%20a:joda-time&core=gav)

**Maven configuration:**
```xml
<dependency>
  <groupId>joda-time</groupId>
  <artifactId>joda-time</artifactId>
  <version>2.10.3</version>
</dependency>
```

**Gradle configuration:**
```groovy
compile 'joda-time:joda-time:2.10.3'
```

![Tidelift dependency check](https://tidelift.com/badges/github/JodaOrg/joda-time)


### Related projects
Related projects at GitHub:
- https://github.com/JodaOrg/joda-time-hibernate
- https://github.com/JodaOrg/joda-time-jsptags
- https://github.com/JodaOrg/joda-time-i18n

Other related projects:
- https://www.joda.org/joda-time/related.html


### Support
Please use [Stack Overflow](https://stackoverflow.com/questions/tagged/jodatime) for general usage questions.
GitHub [issues](https://github.com/JodaOrg/joda-time/issues) and [pull requests](https://github.com/JodaOrg/joda-time/pulls)
should be used when you want to help advance the project.
Commercial support is available via the
[Tidelift subscription](https://tidelift.com/subscription/pkg/maven-joda-time-joda-time?utm_source=maven-joda-time-joda-time&utm_medium=referral&utm_campaign=readme).

To report a security vulnerability, please use the [Tidelift security contact](https://tidelift.com/security).
Tidelift will coordinate the fix and disclosure.


### Release process

* Update version (pom.xml, README.md, index.md, MANIFEST.MF, changes.xml)
* Commit and push
* Ensure on Java SE 8
* `mvn clean deploy -Doss.repo -Dgpg.passphrase=""`
* Release project in [Nexus](https://oss.sonatype.org)
* Website will be built and released by Travis

## Contributors

### Code Contributors

This project exists thanks to all the people who contribute. [[Contribute](CONTRIBUTING.md)].
<a href="https://github.com/JodaOrg/joda-time/graphs/contributors"><img src="https://opencollective.com/joda/contributors.svg?width=890&button=false" /></a>

### Financial Contributors

Become a financial contributor and help us sustain our community. [[Contribute](https://opencollective.com/joda/contribute)]

#### Individuals

<a href="https://opencollective.com/joda"><img src="https://opencollective.com/joda/individuals.svg?width=890"></a>

#### Organizations

Support this project with your organization. Your logo will show up here with a link to your website. [[Contribute](https://opencollective.com/joda/contribute)]

<a href="https://opencollective.com/joda/organization/0/website"><img src="https://opencollective.com/joda/organization/0/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/1/website"><img src="https://opencollective.com/joda/organization/1/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/2/website"><img src="https://opencollective.com/joda/organization/2/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/3/website"><img src="https://opencollective.com/joda/organization/3/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/4/website"><img src="https://opencollective.com/joda/organization/4/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/5/website"><img src="https://opencollective.com/joda/organization/5/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/6/website"><img src="https://opencollective.com/joda/organization/6/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/7/website"><img src="https://opencollective.com/joda/organization/7/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/8/website"><img src="https://opencollective.com/joda/organization/8/avatar.svg"></a>
<a href="https://opencollective.com/joda/organization/9/website"><img src="https://opencollective.com/joda/organization/9/avatar.svg"></a>
