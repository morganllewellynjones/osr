### Summary
Documenting my understanding of hibernate features and implementation based on my tests and reading.

## Dirty checking
Hibernate's default dirty checking strategy is to take a snapshot of an entity when it is made persistent. For example, when an Entity is inserted,updated or retrieved from the database, hibernate takes a snapshot of the current state of that entity at the time that it interacts with the database. It tracks this information for every entity. When the persistence context is flushed, hibernate checks every single entity in the persistence context and compares it to the snapshot states of the last known database values for each one. If any of the fields in any of the entities are different then the snapshot, hibernate runs an update statement to synchronize the state.


This is actually pretty costly in terms of performance. https://vladmihalcea.com/the-anatomy-of-hibernate-dirty-checking/

Hibernate also offers another strategy for dirty checking, `interception based`. This listens to mutations on the entities to detect updates, but has a some niche accuracy issues where it may miss updates.

https://docs.hibernate.org/orm/7.2/introduction/html_single/#bytecode-enhancer

## Equals
## Hashcode
## Association Fetching
## JDBC Batching
