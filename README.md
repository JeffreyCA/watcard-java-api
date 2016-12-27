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
  <version>1.2</version>
</dependency>
```

Lastly, using Gradle, just include the following to your `build.gradle` file:

```Gradle
repositories {
    jCenter()
}

dependencies {
    compile 'ca.jeffrey.watcard:watcard-java-api:1.2'
}
```

## Usage
### Initialization
Create an account:

```java
WatAccount account = new WatAccount("<student id>", "<pin>");
```

Create login request:
```java
try {
    account.login();
}
catch (IllegalArgumentException e) {
    // Handle invalid login information
}
```

Refresh session:
```java
account.newSession();
```

### Personal information
Load personal data:
```java
account.loadPersonalInfo();
```

Get name stored on WatCard account:
```java
String name = account.getName();
```

Get photo URL:
```java
String photoUrl = account.getPhoto();
```

### Balance Data
Load balance data:
```java
account.loadBalances();
```

Get flex & meal plan balances:
```java
float meal = account.getMealBalance();
float flex = account.getFlexBalance();
```

Get specific account balance:
```java
float donMeal = account.getWatBalance(WatBalanceType.DON_MEAL).getValue();
```

Output all balance information:
```java
account.displayBalances();
```
### Transaction Data
Retrieve all transactions (from newest to oldest) since a particular `LocalDateTime`:
```java
LocalDateTime date = LocalDateTime.of(2016, 10, 1, 0, 0); // Oct. 1, 2016 at 00:00
List<WatTransaction> transactions = account.getTransactions(date);
```

Retrieve only a certain number of transactions (from newest to oldest) since a particular `LocalDateTime`:
```java
LocalDateTime date = LocalDateTime.of(2016, 10, 1, 0, 0);
// Retrieve 50 latest transactions up until date
List<WatTransaction> transactions = account.getTransactions(date, 50);
```

Retrieve all transactions between two `LocalDateTime`:
```java
LocalDateTime date1 = LocalDateTime.of(2016, 10, 1, 0, 0); // Oct. 1, 2016 at 00:00
LocalDateTime date2 = LocalDateTime.of(2016, 12, 25, 11, 59); // Dec. 25, 2016 at 11:59

List<WatTransaction> transactions = account.getTransactions(date1, date2);
```

Retrieve all transactions within the last `n` days:

*The true flag means that if "now" is December 25, 2016 09:00, then it retrieves all transactions up until `date` at 09:00.
If the flag was switched to false, it retrieves all transactions from now until `date` 00:00.*
```java
List<WatTransaction> transactions = account.getLastDaysTransactions(n, true);
```
## Other Resources
Read the javadoc [here](https://jeffreyca.github.io/watcard-java-api/).

## Contributing
Improvements and changes are welcome!
