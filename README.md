# Suncity 

### Project description

Aplication is used to get information about cities time of sunrise/sunset according to It's coordinates. Two services were implemented:
1. city, which provide opportunities: 
  - to add city to the DB
  http://localhost:8080/city/add
  Parameters: name, long, latt
  - to get city from DB
  http://localhost:8080/city/get
  Parameters: name
  - to list cities from DB
  http://localhost:8080/city/list
  Parameters: none
2. eventtime, which provides opportunity to get information about time of sunrise/sunset
  - to get info
  http://localhost:8080/eventtime
  - Parameters: 
  - action=sunset | sunset | both
  - date=today | yyyy-mm-dd (example 2019-03-03)
  - city (example city=TestCity | city=test&city=test2)
  
  All APIs have the same type POST request method. 

### Prerequisites 
- Maven
- Hibernate
- Spring
- Spring boot
- MySql
- JUnit
- log4j
- Git
### Run 
to Start from Eclipse IDE (or any other) run Application.class from main package as Java Application

to use executable Jar:
- using IDE or terminal make 
```
mvn clean install or mvn clean install -DskipTests=true
```
- after that from target directory in terminal execute
```
java -jar sun_cities-0.0.1-SNAPSHOT.jar
```
It will start application. 

SunCity is developed by [Volodymyr Baranov] 2019.
