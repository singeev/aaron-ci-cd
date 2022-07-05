# Jokes API test project

### Project purpose

The project demonstrates a simplified setup to test 3 endpoints in Joke
API (https://jokes.p.rapidapi.com).

The java application itself (main class - JokesApplication) serves no purpose.

Main project's goal is to run tests.

### Technical stack

* JDK11
* Maven (build tool, dependency management)
* Lombok (annotation processing)
* Logback (logging framework)
* JUnit 5
* RestAssured

### Project structure

The project based on a basic maven archetype for a plain java application.

### How to run

After cloning the repository just run the command:
> mvn test

This command will run all existing tests with *maven-surefire* plugin.

### CI/CD

Since the project source code is stored in Github, continuous integration is configured for **Github
Actions**.

The configuration is in `.github/workflows/main.yml` file. The pipeline has the following steps:

1. Compile the source code
2. Run tests
3. Build the project's artifact - `target/jokes-application-1.0.jar`

The pipeline runs for every new commit in the repository.
