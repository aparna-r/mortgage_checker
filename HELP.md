# Introduction
This project provides 2 rest apis which cater the need of assignment 
1. /api/interest-rates - returns list of current interest rates
2. /api/mortgage-check - checks mortgage feasibility and monthly cost

## Assumptions
1. Used following monthly payment formula:
   ``` 
   emi = (r(1+r)^N * P) / ((1+r)^N - 1)
   r monthly interest rate
   N number of monthly payments
   P principle
   ```
   See: https://en.wikipedia.org/wiki/Mortgage_calculator, same function is used in Microsoft Excel named 'PMT'
2. Monthly cost calculation precision uses setting precision of the IEEE 754-2019 decimal64 format, 16 digits, and a 
   rounding mode HALF_EVEN
3. Monthly cost is rounded to 2 decimal place with rounding mode HALF_UP
4. lastUpdate for interest rate will be returned in UTC in ISO date-time format such as '2025-03-15T13:38:13.62512Z'
5. In memory h2 db is used to store interest rates and populated at application startup using 'data.sql'
6. In the future, an update mechanism to change db entries can be implemented by apis. lastUpdate in MortgageInterestRate 
   entity supports auto updation on create/update.  
7. For security, static analysis tools like checkmarx can be used for vulnerability check. A pen test is recommended before
   going live.

## Prerequisite
1. maven 3
2. jdk 17

## How to

### build project
From project root folder run below command
```
mvn clean install
```
This will build, compile and run unit test as well as integration test. Please check following artifacts
```
target/testassignment-0.0.1-SNAPSHOT.jar for executable jar
target/surefire-reports for unit test report
target/cucumber-reports for integration test report
```

### run application
There are 2 ways to run application.
1. Use spring boot plugin to run. From project root run below command
   ```
   mvn spring-boot:run
   ```
2. Use executable jar file generated after building project directly with below command
   ```
   java -jar target/testassignment-0.0.1-SNAPSHOT.jar
   ```

### call apis

After running application as mentioned above, server starts at localhost on port 8080. Below two samples can be used to
test apis. There is a 'requests.http' file also added in project to make calls easily in intellij.

```
GET http://localhost:8080/api/interest-rates

POST http://localhost:8080/api/mortgage-check
Content-Type: application/json

{
  "income": 10,
  "maturityPeriodInYears": 1,
  "loanValue": 20.1,
  "homeValue": 20.5

}

```

### run performance test
This is a 2 steps process.
1. run application using any method as mentioned above
2. run performance tests by using below command in project root directory
```
mvn gatling:test
```
Finally report is generated at
```
target/gatling
```
