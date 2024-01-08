# User API

### Register API

POST /api/users

Request Body :

```json
{
  "username": "example",
  "password": "password",
  "name": "Example"
}
```

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Register Successfully",
  "data": null
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Name must not blank"
}
```

### Login API

POST /api/auth/login

Request Header :

- X-API-TOKEN : Access Token

Request Body :

```json
{
  "username": "example",
  "password": "password"
}
```

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Login Successfully",
  "data": {
    "accessToken": "Access Token",
    "expiredAt": 123456789
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Wrong Password"
}
```

### Current API

GET /api/users/current

Request Header :

- X-API-TOKEN : Access Token

Response Body (Success) :

```json
{
  "status": "OK",
  "message": null,
  "data": {
    "username": "example",
    "name": "Example"
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Unauthorized"
}
```

### Update User API

PATCH /api/users/current

Request Header : 

- X-API-TOKEN : Access Token

Request Body :

```json
{
  "name": "Example",
  "password": "password"
}
```

Response Body (Success) :

```json
{
  "status": "OK",
  "message": null,
  "data": {
    "username": "example",
    "name": "Example"
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Unauthorized"
}
```

### Logout API

PATCH /api/auth/logout

Request Header :

- X-API-TOKEN : Access Token

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Logout Successfully",
  "data": null
}
```