# Life+ Backend

## Database setup
- Run PostgreSQL server in port 5432 (default).
- Create database lifeplusdb.
- Give permission to:
    - user: postgres
    - password: password
    
#### Init database
- Database is initialized by default.
- Add program argument --initdb=false to disable.

## Rest Api docs
- Run application
- go to http://localhost:8080/swagger-ui.html

## AWS S3
- Provide credentials through environmental variables: accessKey=[XXXX];secretKey=[XXXX]
- If no credentials are provided, only reads are allowed.