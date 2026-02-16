Truthfully, this project is just an excuse to test out hibernate.
A lot of this testing is being done by observing the logs. Not all tests have assert statements.

I have learned however that the logs provided by hibernate are not the actual queries executed.
These hibernate generated queries must first be passed to jdbc, where they may be batched or manipulated in other ways before being sent to the database.
Currently working on implementing a database proxy for this, and testing a few other things.

`./gradlew test` to run the test suite. 

The existing code is just a skeleton to be used by the tests.
