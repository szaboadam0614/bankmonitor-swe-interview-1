## Accessing your API

We've installed `curl` in the container running your application, so you can make requests to your API directly from the Shell. For instance, you can run 
- `curl 127.0.0.1:3000/transactions` to see your server's output.
- `curl -X POST -H "Content-type: application/json" -d "{ "reference": "foo", "amount": 100}" "127.0.0.1:3000/transactions"`
- `curl -X PUT -H "Content-type: application/json" -d "{ "reference": "foo", "amount": 300}" "127.0.0.1:3000/transactions/1"`

## Connecting to Database

The database is a PostgreSQL database. The user and password is configured in the `application.yml` file. To connect to the local instance, type: `psql -U postgres -W postgresql://localhost:5432/postgres`.

## Shell

A shell is provided to you so you can inspect your container in more detail. The shell can be used to run maven commands with `mvn`## Accessing your API

We've installed `curl` in the container running your application, so you can make requests to your API directly from the Shell. For instance, you can run `curl 127.0.0.1:3000/transactions` to see your server's output. 

## Connecting to Database

The database is a PostgreSQL database. The user and password is configured in the `application.yml` file. To connect to the local instance, type: `psql -U postgres -W postgresql://localhost:5432/postgres`.

## Shell

A shell is provided to you so you can inspect your container in more detail. The shell can be used to run maven commands with `mvn`

## Following code refactors
- use DTOs at controller layer
- controllers can be generated with openApi (should think about it, if the service is public)
- we can store modified_at values, because we are updating, probably this information can be useful in the future. (Here i would suggest to use the `@EntityListeners(AuditingEntityListener.class)` so hibernate will care about it) 

## Challenges
- migrate h2 db to postgres without loosing data and without downtime
  - we should run two instances of the spring boot app
    1. using H2 in memory database, we shut down gracefully - use `application-h2.yml`
    2. before shutting down, we should start an instance which is using postgres SQL - use `application.yml` - and should port the application to this instance.
    3. after H2 in memory database is down, we should run a migration script - like `migrate.sh` - which populate the postgres database. (not working properly at the moment)
       - here we can dump the h2 db schema, transform it, and execute insert scripts, but for now to keep it simple, I use flyway for db migration which is more future prof solution
       - we should refer to the installed jars (h2, postgres) in the Docker container
  - other option can be using a queue, where we put the incoming request, and while we are changing from H2 to postgres, the request are stored in this queue, and will be processed after the postgres instance is live. Here we also need a migration script.

## Notes
- TODOs (I did not know where to cut the tasks)
  - should create a Dockerfile with the needed jars (h2, postgres) and run the artifact.
  - fix migration script to work properly in container.
- JSONObject is unsorted, so on update it changes the order of the result (reference, amount), can use Gson to keep the sort. (I am not sure, the scope is included this.)
- I am not sure, I understood the task correctly, but to make work with H2 database, I created a schema2.sql, to use sequence. If we say that it is running in production, this solution won`t work, we can create a logic in service layer, to set the ids value.