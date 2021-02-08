# BeGreen Application - Backend

This is the back end API of a BeGreen Application, built  by the ThinkTech team through the [Tech Returners](https://techreturners.com) Your Journey Into Tech course. 
It is consumed by a front end React application, available [here](https://github.com/ThinkTech-TR/carbontracker-react-app) and connects to an RDS Database.

### Technology Used

This project uses the following technology:

- Serverless Framework
- Java 8
- JUnit
- Mockito
- SQL
- AWS Lambda and API Gateway
- AWS RDS

### Endpoints

The API exposes the following endpoints:

---

##### POST /initialcarbon

[https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/initialcarbon](https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/initialcarbon)

Will respond with an initial carbon footprint when sent the questionnaire data in the format:

```json
{
    "userCategory":"company",
    "diet":"meatEater",
    "carUsage":"electric",
    "carMileage":"over10000",
    "numberInHousehold":"2",
    "houseType":"bungalow",
    "houseAge":"1965-82"
}
```

---

##### GET /users/{userId}/checkuser

[https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/users/{userId}/checkuser](https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/users/{userId}/checkuser)

Will check whether a user already exists in the database

---

##### POST /users/{userId}/addupdateuser

[https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/users/{userId}/addupdateuser](https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/users/{userId}/addupdateuser)

Adds a user with questionnaire data. Or updates the questionnaire data if the user already exists. 
The data must be sent in the format below.

```JSON
 {
         "userCategory":"individual",
         "diet":"vegan",
         "carUsage":"smallPetrolDiesel",
         "carMileage":"1001to5000",
         "numberInHousehold":"1",
         "houseType":"midTerrace",
         "houseAge":"pre1919"    
}
```

---

##### GET /user/{userId}/forDate/{finishDate}/trackingcarbonformonth

[https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/user/{userId}/forDate/2021-02-04/trackingcarbonformonth](https://aeyr60hdff.execute-api.eu-west-2.amazonaws.com/dev/user/{userId}/forDate/2021-02-04/trackingcarbonformonth)

Will return a user's carbon and journeys in the month to date.
```
