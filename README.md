# Prost - Group 6 project
# Technical
## Database
The database is included in the runtime of this application, that means once you run
this app, the database starts with it.

If you want to look directly into the database tables, you have 2 options:
+ Database toolbar of IntelliJ IDEA
+ The H2-Console (available once the app is running on the endpoint
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

## Frontend
If you've never gotten into contact with either [TailwindCSS](https://tailwindcss.com/) 
or [FontAwesome](https://fontawesome.com/), then I strongly recommend at least looking at
their homepages. These are very cool libraries, that make your life easier.

I have used the [*pnpm*](https://pnpm.io/) package manager, because it is slightly faster
than the alternatives (*npm* & *yarn*). It is possible to use them instead.

A CSS library [TailwindCSS](https://tailwindcss.com/) is installed and prepared for use.
The bundle is optimized - only the classes used in the project will be compiled into a bundle.

Everything (Javascript and CSS) is being bundled with [Webpack](https://webpack.js.org/).
While CSS is extended with [PostCSS](https://postcss.org/) and a couple of plugins.

Development mode can be activated with the following command:
```bash
pnpm dev
```
This runs a watcher, that looks for changes in the *frontend* directory and also for
changes in Thymeleaf templates. Upon change the static assets are recompiled.

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
```bash
./gradlew spotlessApply
```

#### Other
Linting of config files (yml/json/...) and frontend assets (js/css/...) is provided by Prettier
(and ESLint for JS). Linting of those files can be run with a command in *package.json*.
```bash
pnpm lint
```
