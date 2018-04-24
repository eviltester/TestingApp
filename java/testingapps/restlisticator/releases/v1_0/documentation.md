# The Listicator

- Web Server based app which responds to REST HTTP Requests
- Features
   - List management
   - User management
   - Feature-Toggles to configure the app

## End Points

- /heartbeat
    - `GET` - return 200 to indictate server is running
- /lists
    - `GET` - return all the lists in the system
    - `POST`
        - requires authorized user
    - `PUT`

---

'GET /lists'

`Accept: application/json`

~~~~~~~~
{
    "lists": [
        {
            "guid": "d4625287-989a-4454-b01a-cb99545a87a6",
            "title": "title",
            "description": "",
            "createdDate": "2017-07-19-15-53-14",
            "amendedDate": "2017-07-19-15-53-14"
        }
    ]
}
~~~~~~~~

`Accept: application/xml`

~~~~~~~~
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<lists>
    <list>
        <guid>d4625287-989a-4454-b01a-cb99545a87a6</guid>
        <title>title</title>
        <description></description>
        <createdDate>2017-07-19-15-53-14</createdDate>
        <amendedDate>2017-07-19-15-53-14</amendedDate>
    </list>
</lists>
~~~~~~~~




- /lists/{guid}
    - `GET` - return the details of a particular list
        - can filter list with `?title=exactTitle`
        - can filter list with `?title="partialTitle"`
    - `PUT` - amend a list or create a new list when supplied with full details of the list
    - `POST` - amend details of a list
        - user must be authenticated
        - only user with appropriate permission can delete a list
        - resource will be created if it does not exist
    - `PATCH` - partial amend of a list
        - user must be authenticated
        - only user with appropriate permission can delete a list
        - resource must exist
    - `DELETE` - delete a list
        - user must be authenticated
        - only user with appropriate permission can delete a list
- /lists/{guid}?without={fieldlist}
    - `GET` - return the details of a particular list but without the fields in the comma separated `fieldlist` e.g. `guid,title`

---

GET /lists/{guid}

`Accept: application/json`

~~~~~~~~
{
    "guid": "d4625287-989a-4454-b01a-cb99545a87a6",
    "title": "title",
    "description": "",
    "createdDate": "2017-07-19-15-53-14",
    "amendedDate": "2017-07-19-15-53-14"
}
~~~~~~~~

`Accept: application/xml`

~~~~~~~~
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<list>
    <guid>d4625287-989a-4454-b01a-cb99545a87a6</guid>
    <title>title</title>
    <description></description>
    <createdDate>2017-07-19-15-53-14</createdDate>
    <amendedDate>2017-07-19-15-53-14</amendedDate>
</list>
~~~~~~~~

---

PATCH /lists/{guid}

{
    "guid": "d4625287-989a-4454-b01a-cb99545a87a6",
    "title": "title2",
}

_currently does not comply with https://tools.ietf.org/html/rfc7396 _

- /users
    - `GET` - return usernames of all users
        - can be filtered with `?username=exactmatch` e.g. `?username=admin`
        - can be filtered with `?username="partialmatch"` e.g. `?username="adm"`
     - `POST` - creat a user with `username` and `password`
         - calling user must be authenticated
         - calling user must be admin or have CREATE_USER permissions
         - username must be minimum of 6 chars
         - password must be minimum of 6 chars
         - 201 returned on success
         - 409 returned if user already exists
- /users/{username}
    - `GET` - return details of a user
        - user must be authenticated
        - user can only get their own details, not those of anyone else
        - admin users can get details of any user
- /users/{username}/password
    - `PUT` - set the password
        - user must be authenticated
        - user can only set their own details, not those of anyone else
        - admin users can set details of any user
        - password must be 6 characters minimum
        - payload is a partial user object containing the password
        - payload type must be set in the content-type header or assumes JSON
        - returns 204 No Content if successful


`PUT /users/{username}/password`

XML:

~~~~~~~~
<user><password>newPassword</password></user>
~~~~~~~~

JSON:

~~~~~~~~
{password:'newPassword'}
~~~~~~~~


- /users/{username}/apikey
    - `PUT` - set the API AuthKey
        - user must be authenticated
        - user can only set their own details, not those of anyone else
        - admin users can set details of any user
        - apikey must be 10 characters minimum
        - payload is a partial user object containing the password
        - payload type must be set in the content-type header or assumes JSON
        - returns 204 No Content if successful


`PUT /users/{username}/apikey`

XML:

~~~~~~~~
<user><apikey>newApiKeyIsThisYes</apikey></user>
~~~~~~~~

JSON:

~~~~~~~~
{apikey:'newApiKeyIsThisYes'}
~~~~~~~~

- /feature-toggles
    - `GET` - return status of all feature toggles
        - user must be authenticated
    - `POST` - amend a list of feature toggles
        - user must be authenticated
        - only user with appropriate permission can set feature toggles


## Payload representation

- `Content-Type`
    - Use the `Content-Type` header to specify the format of the payload as either XML or JSON
    - A value of `application/json` means the payload is represented as JSON
    - A value of `application/xml` means the payload is represented as XML
    - A `400` error will be returned if the payload can not be converted into the appropriate representation
    - If no `Content-Type` is supplied then the  application assumes a JSON representation.

- `Accept`
    - Use the `Accept` header to specify the format of the desired response
    - A value of `application/json` means you want to receive JSON
    - A value of `application/xml` means you want to receive XML


##  Authentication

- Some requests require an authenticated user.
- The system accepts Simple Basic Authentication
    - an `Authentication` header with the value `Basic` followed by the `username:password` base 64 encoded
- The system also accepts a custom header `X-API-AUTH` with a value of the `api key` defined for each user. the `api key` is visible to the user when they make a `GET` request for their user details


## Verbs

- The system accepts the `X-HTTP-Method-Override` header to allow clients that don't support verbs such as `PATCH`

## General

- `OPTIONS` can be used on all endpoints to receive the `Allow` header with the allowed verbs.
- `404` is returned if the end point is not recognised
- `405` is returned if a method which is not mentioned in `Allow` is used on an an endpoint
- `500` is returned in the event of an unhandled condition in the server - this should never occur
