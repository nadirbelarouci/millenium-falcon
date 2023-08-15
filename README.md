### Requirements:
- Java 17 & Maven 3.8.8 for backend
- Node 20.5 for Angular app
- Bash & jq for scripts
- Docker & docker-compose


### Project structure: 
3 atoms for backend
- millenium-falcon-app: contains the web application and the cli application classes and necessary spring boot configuration
- millenium-falcon-api: contains request / response models
- millenium-falcon-logic: contains the core logic and the algorithm to find a route.

1 atom for ui
- millenium-falcon-ui: Angular app
  - single app.component

### Endpoints: 
- RouteController

  | Method | Path                       | Params / Request Body | Description                                                               |
  |--------|----------------------------|-----------------------|---------------------------------------------------------------------------|
  | GET    | /api/v1/routes             | None                  | Returns all routes from db                                                |
  | POST   | /api/v1/routes/travel-plan | EmpireIntelRequest    | Finds a travel plan between departure and arrival considering EmpireIntel |

### How to build:
#### Backend:

```shell
mvn -T1.0C clean package -DskipTests -Dverification.skip -Plocal-dev
# or to run tests
mvn -T1.0C clean package -Plocal-dev
```
#### Frontend:
```shell
npm install
```
### How to run: 
#### Webapp:
```shell
./millenium-falcon.sh <path_to_millenium_falcon_file>
```
This commands builds the project, then it dockerize both backend and frontend in containers.
see docker-compose.yml for more details
- Backend : http://localhost:8080
- Frontend: http://localhost:4200
#### Cli:
```shell
./r2d2.sh <path_to_millenium_falcon_file> <path_to_empire_file>
```
Note: the cli uses a pre-built jar in the cli folder. 
to build the jar for the cli use profile cli: 
```shell
mvn -T1.0C clean package -DskipTests -Dverification.skip -Pcli
# or to run tests
mvn -T1.0C clean package -Pcli
```

