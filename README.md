Rate-Me [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.martarodriguezm/rate-me/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.martarodriguezm/rate-me)
=======

Based on [androidsx rate-me library](https://github.com/androidsx/rate-me)

Rate Me is an Android library that shows dialog to suggest the user to rate the application in the Google Play Store.

With a little twist: if the rating is positive, we take the user to the Play Store directly. Otherwise, we ask him for feedback via email. (This is all configurable.)

<p>
<img src="https://raw.githubusercontent.com/martarodriguezm/rate-me/master/readme-images/rate-me-dialog-in-helium.png" width="256" />
<img src="https://raw.githubusercontent.com/martarodriguezm/rate-me/master/readme-images/rate-me-dialog-in-pixable.png" width="256" />
</p>

Add it to your project
================

Include the library in your `build.gradle`:

```xml
dependencies {
    compile 'com.github.martarodriguezm:rate-me:1.2'
}
```

or to your pom.xml if you are using Maven

```
<dependency>
  <groupId>com.github.martarodriguezm</groupId>
  <artifactId>rate-me</artifactId>
  <version>1.1</version>
  <type>pom</type>
</dependency>
```

How to use
==========

The simplest integration:

```java
new RateMeDialog.Builder(getPackageName())
        .enableFeedbackByEmail("email@example.com")
        .build()
        .show(getFragmentManager(), "plain-dialog");
```

Have a look at the sample project for other examples.

Automatic timer
---------------

You can make the dialog appear after a number of times your app has been opened or after a number of days after the install date:

```java
RateMeDialogTimer.onStart(this);
if (RateMeDialogTimer.shouldShowRateDialog(this, 7, 3)) {
	// show the dialog with the code above
}
```

Do you want to contribute?
==========================

Feel free to report us or add any useful feature to the library, I will be glad to improve it with your help.

Just fork the repository and then send a pull request with the changes.


License
=======

Licensed under the MIT License. See the [LICENSE.md](LICENSE.md) file for more details.
