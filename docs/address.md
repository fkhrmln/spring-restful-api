# Address API

### Create Address API

POST /api/contacts/:contactId/addresses

Request Header :

- X-API-TOKEN : Access Token

Request Body :

```json
{
  "street": "Mutiara Gading Timur",
  "city": "Bekasi",
  "country": "Indonesia",
  "postalCode": "17158"
}
```

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Address Created Successfully",
  "data": {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
    "street": "Mutiara Gading Timur",
    "city": "Bekasi",
    "country": "Indonesia",
    "postalCode": "17158"
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

### Update Address API

PUT /api/contacts/:contactId/addresses/:addressId

Request Header :

- X-API-TOKEN : Access Token

Request Body :

```json
{
  "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
  "street": "Mutiara Gading Timur",
  "city": "Bekasi",
  "country": "Indonesia",
  "postalCode": "17158"
}
```

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Address Updated Successfully",
  "data": {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
    "street": "Mutiara Gading Timur",
    "city": "Bekasi",
    "country": "Indonesia",
    "postalCode": "17158"
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Address Not Found"
}
```

### Get Address API

GET /api/contacts/:contactId/addresses/:addressId

Request Header :

- X-API-TOKEN : Access Token

Response Body (Success) :

```json
{
  "status": "OK",
  "message": null,
  "data": {
    "id": "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxx",
    "street": "Mutiara Gading Timur",
    "city": "Bekasi",
    "country": "Indonesia",
    "postalCode": "17158"
  }
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Address Not Found"
}
```

### List Address API

GET /api/contacts/:contactId/addresses

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
      "street": "Mutiara Gading Timur",
      "city": "Bekasi",
      "country": "Indonesia",
      "postalCode": "17158"
    }
  ]
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Contact Not Found"
}
```

### Remove Contact API

DELETE /api/contacts/:contactId/addresses/:addressId

Request Header :

- X-API-TOKEN : Access Token

Response Body (Success) :

```json
{
  "status": "OK",
  "message": "Address Deleted Successfully",
  "data": null
}
```

Response Body (Failed) :

```json
{
  "status": "FAIL",
  "errors": "Address Not Found"
}
```