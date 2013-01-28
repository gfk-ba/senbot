senbot
======

Java based Cucumber and Selenium framework.

goals
======
Our goal is to make it easy to create, run and report on automated tests for webbased applications (by using behavior driven development).

prerequisites
======
* Maven (http://maven.apache.org/)
* Java JDK (http://www.java.com)
* Google Chrome (www.google.com/chrome , Just to run the default configured example tests)
* Chromedriver (http://code.google.com/p/chromedriver/downloads/list , put the executable somewhere in your path)

senbot build test run
=======
Clone the repository somewhere and do a ```mvn clean install```, this will build the senbot and fire the example tests

new project
=======
After a succesfull senbot build and test run you can use the Maven archetype to generate a new project based on the senbot framework.
```
mvn archetype:generate -DarchetypeGroupId=com.gfk.senbot -DarchetypeArtifactId=SenBotArchetype
-DgroupId=com.gfk.demo -DartifactId=RunnerDemo
```

This should result in a new folder in your current directory called RunnerDemo, start and test it with ```mvn clean install```
