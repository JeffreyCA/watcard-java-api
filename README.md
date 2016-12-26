# WatCard Java API
[ ![Download](https://api.bintray.com/packages/jeffreyca/maven/watcard-java-api/images/download.svg) ](https://bintray.com/jeffreyca/maven/watcard-java-api/_latestVersion)

A Java library for retrieving UWaterloo WatCard data from the web (https://watcard.uwaterloo.ca).

**If you are developing for Android, please use the sister library [watcard-android](https://github.com/JeffreyCA/watcard-android) instead.
This library uses Java 8 and the new Time libraries which are not compatible with all Android versions.**

## Setup
There are 3 different ways of using this library in your project. Use whatever is most convenient for you.

The first method is directly downloading the [latest .jar](https://github.com/JeffreyCA/watcard-java-api/releases) and add it to your project's build path.

Secondly, if you are using Maven to manage your project, `watcard-java-api` is available on jCenter. Just add the following to your `pom.xml` file:

```xml
<dependency>
  <groupId>ca.jeffrey.watcard</groupId>
  <artifactId>watcard-java-api</artifactId>
  <version>1.1</version>
</dependency>
```

Lastly, using Gradle, just include the following to your `build.gradle` file:

```
repositories {
    jCenter()
}

dependencies {
    compile 'ca.jeffrey.watcard:watcard-java-api:1.1'
}
```

## Usage

Read the javadoc [here](https://jeffreyca.github.io/watcard-java-api/).

Examples will be posted here soon.
