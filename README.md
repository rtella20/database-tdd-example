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
create table tests (
 	id uuid primary key,
 	name text,
 	test text,
 	expected text,
 	failure_message text
 );
```
 
### Insert tests into table
 
Each 'test' is a SELECT query that returns either a number, string, true or false.


## Workflow

Assuming we're starting with an empty database.

#### First things first

1. Run the tests (there should be none!) and validate that everything looks good.
 
#### Let's add a table called app_user to the public schema

1. Write a test to validate that the table exists
  * My test would look something like this: 
  
  `SELECT count(*) 
  FROM pg_catalog.pg_tables 
  WHERE schemaname = 'public' 
  AND tablename = 'app_user';`
  * I expect the result to be `1`
  * The full insert statement would look something like this
  
  ```SQL
  INSERT INTO public.tests (id, name, test, expected, failure_message) 
  VALUES ('d88f5fa4-2e51-4e59-bcc3-babda149161a', 
          'app_user existance', 
          'select count(*) from pg_catalog.pg_tables WHERE schemaname = ''public'' and tablename = ''app_user'';', 
          '1', 
          'app_user table should exist');
  ```

2. Run the tests
3. We should encounter an error because the app_user table does not exist
4. Note the error in the log, it provides us with the actual result of the 
query, the expected value, and the full test detail record.

```
ERROR - Got: 
 +-----+
 |count|
 +-----+
 |    0|
 +-----+
 
 Expected: 
 1
 
 Full Record Returned: 
 +------------------------------------+------------------+--------------------------------------------------+--------+---------------------------+
 |id                                  |name              |test                                              |expected|failure_message            |
 +------------------------------------+------------------+--------------------------------------------------+--------+---------------------------+
 |d88f5fa4-2e51-4e59-bcc3-babda149161a|app_user existance|select count(*) from pg_catalog.pg_tables WHERE...|1       |app_user table should exist|
 +------------------------------------+------------------+--------------------------------------------------+--------+---------------------------+
```

5. Now make it pass!

```SQL
CREATE TABLE app_user ();
```

6. Run the tests to verify the change had the expected impact.

```
STATUS: Success

Total Tests Run: 1
Successful tests: 1
Failed tests: 0
```

6. Repeat...
