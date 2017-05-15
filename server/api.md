<a name="top"></a>
# trailforce v1.0.0

Geo-location based learning app to enhance fieldtrips and heritage trails

- [General](#general)
	- [Ping API](#ping-api)
	
- [Point](#point)
	- [Add a new point](#add-a-new-point)
	- [Get a point by its ID](#get-a-point-by-its-id)
	- [Add a captioned image to a point](#add-a-captioned-image-to-a-point)
	
- [Trail](#trail)
	- [Add a new trail](#add-a-new-trail)
	- [Get trail by code](#get-trail-by-code)
	- [Get trail by ID](#get-trail-by-id)
	
- [User](#user)
	- [Login a user](#login-a-user)
	- [Add a new user](#add-a-new-user)
	- [Verify new user](#verify-new-user)
	
- [trails](#trails)
	- [Get all trails](#get-all-trails)
	


# General

## Ping API
[Back to top](#top)



	GET /




### Success Response

Success-Response:

```
HTTP/1.1 200 OK
{
    "message" : "Received"
}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Received&quot;</p>|

# Point

## Add a new point
[Back to top](#top)



	POST /point/new





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| token | String | <p>JWT token of verified user</p>|
| name | String | <p>Name of point</p>|
| description | String | <p>Description of point</p>|
| picture | File | <p>Picture for trail</p>|
| lat | Number | <p>Point's lat</p>|
| lng | Number | <p>Point's lng</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "point" : {point}
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| point | json | <p>Created point</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Invalid Autnetication Token:

```
	HTTP/1.1 400 Bad Request
	{
	    "message" : "Invalid autnetication token",
     "data" : {err}
	}
```
JWT Error:

```
	HTTP/1.1 400 Bad Request
	{
	    "message" : "JWT Error",
     "data" : {err}
	}
```
Missing Field:

```
HTTP/1.1 400 Bad Request
{
    "message" : "No trail name!"
}
```
Missing Field:

```
HTTP/1.1 400 Bad Request
{
    "message" : "No trail description!"
}
```
## Get a point by its ID
[Back to top](#top)



	GET /point/:id





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| id | String | <p>Point ID</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "point" : {point}
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| point | json | <p>Point with specific ID</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Database Error:

```
	HTTP/1.1 404 Not Found
	{
	    "message" : "Point not found",
     "data" : {err}
	}
```
## Add a captioned image to a point
[Back to top](#top)



	POST /point/image





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| token | String | <p>JWT token of verified user</p>|
| image | File | <p>Image file</p>|
| caption | String | <p>Image caption</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "image" : {image}
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| image | json | <p>Added image</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
# Trail

## Add a new trail
[Back to top](#top)



	POST /trail/new





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| token | String | <p>JWT token of verified user</p>|
| name | String | <p>Name of trail</p>|
| description | String | <p>Description of trail</p>|
| code | String | <p>Trail's code</p>|
| points | String[] | <p>Array of ids of points in trail</p>|
| picture | File | <p>Picture for trail</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "trail" : {trail}
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| trail | json | <p>Created trail</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Invalid Autnetication Token:

```
	HTTP/1.1 400 Bad Request
	{
	    "message" : "Invalid autnetication token",
     "data" : {err}
	}
```
JWT Error:

```
	HTTP/1.1 400 Bad Request
	{
	    "message" : "JWT Error",
     "data" : {err}
	}
```
Missing Field:

```
HTTP/1.1 400 Bad Request
{
    "message" : "No trail name!"
}
```
Missing Field:

```
HTTP/1.1 400 Bad Request
{
    "message" : "No trail description!"
}
```
## Get trail by code
[Back to top](#top)



	GET /trail/code/:code





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| code | String | <p>Trail's code</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "trail" : {trail}
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| trail | json | <p>Trail with specified code</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Database Error:

```
	HTTP/1.1 404 Not Found
	{
	    "message" : "Trail not found",
     "data" : {err}
	}
```
## Get trail by ID
[Back to top](#top)



	GET /trail/:id





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| id | String | <p>Trail's ID</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "trail" : {trail}
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| trail | json | <p>Trail with specified ID</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Database Error:

```
	HTTP/1.1 404 Not Found
	{
	    "message" : "Trail not found",
     "data" : {err}
	}
```
# User

## Login a user
[Back to top](#top)



	POST /login





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| email | String | <p>User email</p>|
| password | String | <p>User password</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "token" : JWT,
     "expiry" : 1
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| token | String | <p>JWT</p>|
| expiry | Number | <p>Expiry in days</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Not Found:

```
	HTTP/1.1 404 Not Found
	{
	    "message" : "User not found",
     "data" : {err}
	}
```
Incorrect Password:

```
HTTP/1.1 400 Bad Request
{
    "message" : "Incorrect password"
}
```
## Add a new user
[Back to top](#top)



	POST /user/new





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| email | String | <p>Email of the new user</p>|
| name | String | <p>Name of the new user</p>|
| password | String | <p>Password of the new user</p>|

### Success Response

Success-Response:

```
HTTP/1.1 200 OK
{
    "message" : "Success"
}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Mail Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Error sending email",
     "data" : {err}
	}
```
Missing Field:

```
HTTP/1.1 400 Bad Request
{
    "message" : "No email"
}
```
Missing Field:

```
HTTP/1.1 400 Bad Request
{
    "message" : "No name"
}
```
Missing Field:

```
HTTP/1.1 400 Bad Request
{
    "message" : "No password"
}
```
## Verify new user
[Back to top](#top)



	GET /user/new/:code





### Parameter Parameters

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| code | String | <p>Code sent by email from /user/new</p>|

### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "token" : JWT,
     "expiry" : 1
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| token | String | <p>JWT</p>|
| expiry | Number | <p>Expiry in days</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
Not Found:

```
	HTTP/1.1 404 Not Found
	{
	    "message" : "Code not found",
     "data" : {err}
	}
```
# trails

## Get all trails
[Back to top](#top)



	GET /trails




### Success Response

Success-Response:

```
	HTTP/1.1 200 OK
	{
	    "message" : "Success",
     "trails" : [trail...]
	}
```

### Success 200

| Name     | Type       | Description                           |
|:---------|:-----------|:--------------------------------------|
| message | String | <p>&quot;Success!&quot;</p>|
| trails | json | <p>Array of all the trails in the database</p>|

### Error Response

Database Error:

```
	HTTP/1.1 500 Internal Server Error
	{
	    "message" : "Database error",
     "data" : {err}
	}
```
