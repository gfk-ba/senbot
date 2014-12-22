Deploying to the Sonatype staging repository
====
To release a stable version first read about how Nexus is used at https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide 

Following these steps from within the SenBotParent folder

* mvn release:clean
* mvn release:prepare
* mvn release:perform

Next, sign the archetype artifacts as the maven archetype:create-from-project does not do this for us and the archetype itself is not a maven module recognised in the parent project, causing it not te be signed automatically.

* ```cd target/checkout/SenBotDemo/target/generated-sources/archetype```
* ```mvn gpg:sign-and-deploy-file -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -DpomFile=pom.xml -Dfile=target/SenBotDemo-archetype-0.3.jar```

Then release it: http://central.sonatype.org/pages/releasing-the-deployment.html