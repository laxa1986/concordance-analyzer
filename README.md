# concordance-analyzer

Hello!

Here is the short info about the "concordance analyzer" app

## Usage:
1) change file extension concordance-analyzer-1.0-SNAPSHOT.zip to .jar
2) make sure you have jvm8 installed on your computer (>java -version in console to check)
3) open terminal app
4) navigate in the directory where concordance-analyzer-1.0-SNAPSHOT.jar located
5) type the command:
java -jar concordance-analyzer-1.0-SNAPSHOT.jar <path-to-text-file-you-wanna-analyze>
6) you'll see parsed concordence right here in console

## Build: (in case you lost executable file, but still have sources)
1) make sure you have maven build tool installed on you computer (>mvn -v in console to check)
2) open terminal app
3) navigate in the directory where pom.xml file located
4) type to command:
mvn clean package
5) you'll get executable file located in ./target/concordance-analyzer-1.0-SNAPSHOT.jar

## App limitations:
1) time of work: O(n) where n is text size
2) consume a lot of memory: require whole text to be in memory to start parsing. can be improved for streaming way, but I afraid it would be less readable 
3) ignore numbers
4) can analyze text on any language, but text should be in UTF-8 encoding
5) some things like print indent, place to output the result are not configurable
6) only main analyzer covered by tests