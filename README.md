# Database TDD Example

## Why?

Data teams shouldn't be exempt from testing their code.  This example provides 
just one simple way to develop Databases using a test-driven approach.

## Getting Started

### Start the DB server

`docker run --name some-postgres -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres:9.6.3`

Default username is `postgres`

### Create tests table

```sql
create table tests
 (
 	id uuid not null
 		constraint tests_pkey
 			primary key,
 	name text,
 	test text,
 	expected text,
 	failure_message text
 )
 ;
```
 
### Insert tests into table 

