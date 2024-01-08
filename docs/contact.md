# Contact API

### Create Contact API

POST /api/contacts

Request Header :

- X-API-TOKEN : Access Token

Request Body :

```json
{
  "name": "Example",
  "phone": "08123456789",
  "email": "example@gmail.com"
}
```

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Contact Created Successfully",
  "data": {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
    "name": "Example",
    "phone": "08123456789",
    "email": "example@gmail.com"
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Name must not blank"
}
```

### Update Contact API

PUT /api/contacts/:contactId

Request Header :

- X-API-TOKEN : Access Token

Request Body :

```json
{
  "name": "Example",
  "phone": "08123456789",
  "email": "example@gmail.com"
}
```

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Contact Updated Successfully",
  "data": {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
    "name": "Example",
    "phone": "08123456789",
    "email": "example@gmail.com"
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Name must not blank"
}
```

### Get Contact API

GET /api/contacts/:contactId

Request Header :

- X-API-TOKEN : Access Token

Response Body (Success) :

```json
{
  "status": "OK",
  "message": null,
  "data": {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
    "name": "Example",
    "phone": "08123456789",
    "email": "example@gmail.com"
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Contact Not Found"
}
```

### Search Contact API

GET /api/contacts

Request Param :
 
- name : String
- email : String
- phone : String
- page : Integer : 0
- size : Integer : 10

Request Header :

- X-API-TOKEN : Access Token

Response Body (Success) :

```json
{
  "status": "OK",
  "message": null,
  "data": [
    {
      "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
      "name": "Example",
      "phone": "08123456789",
      "email": "example@gmail.com"
    }
  ],
  "page": {
    "currentPage": 0,
    "totalPage": 10,
    "size": 10
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

### Remove Contact API

DELETE /api/contacts/:contactId

Request Header :

- X-API-TOKEN : Access Token

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Contact Deleted Successfully",
  "data": null
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Contact Not Found"
}
```