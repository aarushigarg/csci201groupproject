# How to use backend API
## Connect to server for first time
*This assumes you have tomcat set up on Eclipse*
1. Open the project on eclipse
2. Right click on the project name
3. Press **Properties** which is the last option
4. Go to **Targeted Runtimes**
5. Select your Apache Tomcat server
6. Apply and close


## Run the API on the server
1. Right click on the project name
2. Select **Run as** -> **Run on server**
3. API is now up at http://localhost:8080/projectBackend


## API documentation
> Base URL: `https://spring-demo-bc-ff2fb46a7e3b.herokuapp.com/api`
> You can see the raw data by going to the `Base URL` + `Endpoint` if you want


## Users API

### Get All Users
- **Method**: GET
- **Endpoint**: `/users`
- **Description**: Returns a list of all users in the system
```json
[
    {
        "id": 1,
        "email": "john.smith@example.com",
        "hashedPassword": "hashed123",
        "weightPounds": 185,
        "heightInches": 70,
        "age": 32,
        "gender": "M",
        "goal": "weightLoss"
    }
]
```

### Get User by ID
- **Method**: GET
- **Endpoint**: `/users/{id}`
- **Parameters**:
    - `id`: User ID (path parameter)
- **Description**: Returns a specific user by their ID


### Create User
- **Method**: POST
- **Endpoint**: `/users`
- **Description**: Creates a new user
- **Request Body**:
```json
{
    "email": "user@example.com",
    "hashedPassword": "hashedpass123",
    "weightPounds": 150,
    "heightInches": 65,
    "age": 25,
    "gender": "F",
    "goal": "weightLoss"
}
```


### Update User
- **Method**: PUT
- **Endpoint**: `/users/{id}`
- **Parameters**:
    - `id`: User ID (path parameter)
- **Description**: Updates an existing user
- **Request Body**: Same as Create User


### Delete User
- **Method**: DELETE
- **Endpoint**: `/users/{id}`
- **Parameters**:
    - `id`: User ID (path parameter)
- **Description**: Deletes a user


---

## Exercises API

### Get Exercises by User ID and Date
- **Method**: GET
- **Endpoint**: `/exercises/user/{userId}/date/{date}`
- **Parameters**:
    - `userId`: User ID (path parameter)
    - `date`: Date in ISO format (YYYY-MM-DD) (path parameter)
- **Description**: Returns all exercises for a specific user on a specific date

```json
[
    {
        "id": 1,
        "userId": 1,
        "date": "2024-11-15",
        "name": "Bench Press",
        "repetitions": 10,
        "sets": 3,
        "durationMins": 15,
        "isAiSuggestion": false,
        "isCompleted": true
    }
]
```

### Get All Exercises for User
- **Method**: GET
- **Endpoint**: `/exercises/user/{userId}`
- **Parameters**:
    - `userId`: User ID (path parameter)
- **Description**: Returns all exercises for a specific user
-

### Create Exercise
- **Method**: POST
- **Endpoint**: `/exercises`
- **Description**: Creates a new exercise entry
- **Request Body**:
```json
{
    "userId": 1,
    "date": "2024-11-15",
    "name": "Squats",
    "repetitions": 12,
    "sets": 4,
    "durationMins": 20
    "isAiSuggestion": false,
    "isCompleted": true
}
```


### Update Exercise
- **Method**: PUT
- **Endpoint**: `/exercises/{id}`
- **Parameters**:
    - `id`: Exercise ID (path parameter)
- **Description**: Updates an existing exercise
- **Request Body**:
```json
{
    "name": "Modified Push-ups",
    "repetitions": 20,
    "sets": 4,
    "durationMins": 15,
    "isAiSuggestion": false,
    "isCompleted": true
}
```


### Delete Exercise
- **Method**: DELETE
- **Endpoint**: `/exercises/{id}`
- **Parameters**:
    - `id`: Exercise ID (path parameter)
- **Description**: Deletes an exercise
- **Response**: 204 No Content (see below table if fails)

---

## Response Codes

| Code | Description |
|------|-------------|
| 200 | OK - Successful request |
| 201 | Created - Resource created successfully |
| 204 | No Content - Resource deleted successfully |
| 400 | Bad Request - Invalid input |
| 404 | Not Found - Resource not found |
| 500 | Internal Server Error - Server error |

## Notes
- JSON data format only, REST api
- Dates must be in (YYYY-MM-DD) format
- User IDs are required for creating exercises

# Postman stuff
- Content-Type header should be set to "application/json" for POST and PUT requests
