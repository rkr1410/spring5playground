### Random stuff
- Use `@Repository` to enable exception translation from JPA exceptions to Spring’s `DataAccessException` hierarchy

### TODO: resolve the `equals/hashCode` conundrum
- application `uuid id` generation (instead of letting hibernate/database generate it)
- go with `Object` implementations
- do a per class implementation based on `instanceof` and the `id` field, and just don't put non-persisted entities in collections
- as above, but create a `@MappedSuperclass` for single implementation and use a single sequence for all tables, [mind the disadvantages](https://stackoverflow.com/questions/1536479/asking-for-opinions-one-sequence-for-all-tables) 


### Check out: `@IdClass/@Embedded/@Embeddable`, `@Stateful`, `@PersistenceContext`

### Stuff to ponder:
- What to use for JSON interfaces validation? Look into Swagger?
- Maybe write a small package with MVC before going full rest/react/whatnot? Just as an example, who knows, may be useful to know the stuff at least a tad. Ditch thymeleaf or not?