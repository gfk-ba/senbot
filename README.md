senbot
======

Java based Cucumber and Selenium framework.

goals
======
Our goal is to make it easy to create, run and report on automated tests for webbased applications (by using behavior driven development).

features
======
* Cucumber based tests
* Selenium based tests using different browsers
* Run tests in parallel (Has some issue's)
* Generate readable reports (Work in progress)

prerequisites
======
To run the framework tests you minimal need:
* Java JDK v1.6+ (http://www.java.com)
* Maven (http://maven.apache.org/, install latest version in your local path)
* Google Chrome (www.google.com/chrome, for the default configured example tests)
* Chrome WebDriver (http://code.google.com/p/chromedriver/downloads/list, put the executable in your local path)
* Firefox (www.firefox.com, for the default configured example tests)

initial test run
=======
After the prerequisites are installed clone the repository<br>
Run a ```mvn clean install``` in the cloned directory<br>
This will build the senbot and fire the example tests

new project
=======
After a succesfull senbot build and test run you can use the Maven archetype to generate a new project based on the senbot framework.
```
mvn archetype:generate -DarchetypeGroupId=com.gfk.senbot -DarchetypeArtifactId=SenBotArchetype -DgroupId=com.yourdomain.namespace -DartifactId=YourProjectName
```

This should result in a new folder in your current directory called YourProjectName, start and test it with ```mvn clean install```

Some usefull runtime tips
=======
When running your SenBot through maven you can provide custom cucumber runtime options to append to the variables defined in your JUnit tests like so
```
mvn test -Dfeatures="--tags @myOtherTag"
```
or run a single feature file on a single runner like so
```
mvn test -Dtest=com.domain.NameOfYourJUnitTest -Dfeatures="path/to/your/file.feature"
```
Changing the default domain or test environment can be done with
```
mvn test -Domain=mydomain.com -Denv=FF
```
