Truthfully, this project is just an excuse to test out hibernate.
A lot of this testing is being done by observing the logs. Not all tests have assert statements.

I have learned however that the logs provided by hibernate are not the actual queries executed.
These hibernate generated queries must first be passed to jdbc, where they may be batched or manipulated in other ways before being sent to the database.
Currently working on implementing a database proxy for this, and testing a few other things.

`./gradlew test --info` to run the test suite and see logs.

The existing code is just a skeleton to be used by the tests. In many cases, tests were performed by modifying entity attributes and re-running tests. For example, by changing the FetchType of associations and then re-running, making classes final, etc. I'm documenting my findings under DISCOVERIES.md.
