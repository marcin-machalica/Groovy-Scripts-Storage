# Groovy-Scripts-Storage
Spring Boot application for storing and running Groovy scripts in Java

### Technologies used
- Java 8
- Maven
- Spring Boot
- JPA/Hibernate
- H2, Flyway
- JUnit, Mockito

### API
#### CRUD
| Path  | Method | Success | Failure | Request body | Returned value |
|---------------------|-----|-----|-----|---------------| ------------------------------| 
| /api/groovyscripts  | GET | 200 |     |               | List\<GroovyScript> |
| /api/groovyscripts/{name}  | GET | 200 | 404 |               | GroovyScript |
| /api/groovyscripts  | POST | 201 | 409 | GroovyScript | GroovyScript |
| /api/groovyscripts/{name}  | DELETE | 200 | 404 |  | GroovyScript |
| /api/groovyscripts/{name}  | PUT | 200 | 404/409 | GroovyScript | GroovyScript |

#### Runner
| Path  | Method | Success | Failure | Request body | Returned value |
|---------------------|-----|-----|-----|---------------| ------------------------------| 
| /api/groovyscripts/{name}  | POST | 200 | 404 | Object[] args (not required) | GroovyScriptRunnerResult|
