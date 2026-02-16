### Summary
Documenting my understanding of hibernate features and implementation based on my tests and reading.

## Dirty checking
Hibernate's default dirty checking strategy is to take a snapshot of an entity when it is made persistent. For example, when an Entity is inserted,updated or retrieved from the database, hibernate takes a snapshot of the current state of that entity at the time that it interacts with the database. It tracks this information for every entity. When the persistence context is flushed, hibernate checks every single entity in the persistence context and compares it to the snapshot states of the last known database values for each one. If any of the fields in any of the entities are different then the snapshot, hibernate runs an update statement to synchronize the state.

This is actually pretty costly in terms of performance. https://vladmihalcea.com/the-anatomy-of-hibernate-dirty-checking/

Hibernate also offers another strategy for dirty checking, `interception based`. This listens to mutations on the entities to detect updates, but has a some niche accuracy issues where it may miss updates.

https://docs.hibernate.org/orm/7.2/introduction/html_single/#bytecode-enhancer

## Equals
The advice I have found online about implementing equals and hashcode tends to be verbose, confusing and divergent. Hibernate's official documentation offers a very simple definition for the `equals` implementation.

https://docs.hibernate.org/orm/7.2/introduction/html_single/#equals-and-hash

Interestingly, `equals` is not used for dirty checking. Hibernate uses its own snapshots of entity state for dirty checking comparison, it doesn't rely on your hand-rolled implementation of the `equals` function. The `equals` function is used most often for entities when you are storing associated data in Set's. Therefore, the equals function should be used to determine if two objects are supposed to refer to the same database entity, not to see if their state is exactly the same. Hibernate recommends using the NaturalId of the table for an `equals` implementation.

## Hashcode
## Association Fetching
## JDBC Batching
