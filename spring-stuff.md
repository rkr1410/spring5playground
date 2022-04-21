- `@Transactional` won't be used on methods other than `public`, nor methods called from the same object (due to proxying). To get a proxy, inject `ApplicationContext` and call `getBean(ThisClassName.class).annotatedMethod()`.


### Check out: `@Qualifier`, `@Bean`, logging