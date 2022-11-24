# Prost - Group 6 project
# Technical
## Database
The database is included in the runtime of this application, that means once you run
this app, the database starts with it.

If you want to look directly into the database tables, you have 2 options:

- Database toolbar of IntelliJ IDEA
- The H2-Console (available once the app is running on the endpoint 
[/h2-console](http://localhost:8080/h2-console))

Login credentials (set up in resources/application.yml):
```yaml
username: prostadmin
password: strongpassword
url: jdbc:h2:mem:prost
```

### Database migrations / data import
You don't have to care about migrations and changes to the database schema.
On every restart of the application the JPA models are loaded and the DDL is created
and executed. **That means you can add, modify or delete anything from the *entity* package
without worrying about the changes you make impacting other people working on the project.** 
Data import is not yet implemented, but most likely will be via
Spring Data JDBC database scripts.

## Linting
#### Java
Linting of the code is enabled via the *spotless* Gradle plugin.
In order to use it in IntelliJ IDEA, just open your Gradle toolbar
and navigate to verification -> spotlessApply.

Or you can execute the task via the command line. 

Windows:
```batch
gradlew.bat spotlessApply
```
UN*X:
```sh
./gradlew spotlessApply
```


#### Other
Prettier