# The REST Listicator

- Web Server based app which responds to REST HTTP Requests
- Features
   - List management
   - User management
   - Feature-Toggles to configure the app

## The application has been deployed to Heroku

You can run the application locally or run it from a cloud system.

The application has been deployed to Heroku at

- https://rest-list-system.herokuapp.com/listicator/
 
The cloud version will:
 
- reset the shared default lists every 5 minutes.
- allocate a unique session when `/sessionid` is called with 2 minutes of inactivity

## Multi-User Support

By default all requests use the 'global' set of data. This makes the application easy to use for single users and multiple users can access this, although they will be amending each others data.

If a user wants a unique session, perhaps because they want to automate and have deterministic results without interference from other users they must use a `X-SESSIONID` header.

`GET /sessionid` e.g. GET http://localhost:4567/listicator/sessionid 

## Install and Running Locally

[download the jar from TestingApp Releases on Github](https://github.com/eviltester/TestingApp/releases)


- run by typing:
    - `java -jar <insertnameofjarfilehere>.jar`
    - e.g. if you downloaded `rest-list-system.jar` then it would be `java -jar rest-list-system.jar`

If you double click it then it will be running in the background on port 4567 - you might have to use task manager to kill the Java VM that it is running on to exit.

## Built in Users

Three users are created by default with different permissions: `superadmin`, `admin`, `user` - all have the default password set to `password`

Use Basic authentication to use these users.

- username: `superadmin`, password: `password`
- username: `admin`, password: `password`
- username: `user`, password: `password`

## Command Line Arguments For Local Execution

- `-port=1234` set the port to supplied integer (defaults to 4567)
- `-bugfixes=false` (defaults to true)
- `-resettimer=MILLISECONDS` e.g. `-resettimer=1000`
    - sets the default list application timer, by default locally this is 0 as single user mode is inferred
    - on the cloud this is set to 5 minutes (300000 milliseconds)
    - this can also be configured by setting and environment variable or property called `RestListicatorApiResetMillis`
- `-maxsessionseconds=SECONDS` e.g. `-maxsessionseconds=60`
    - sets the individual session created by `/sessionid` to be deleted after 60 seconds of inactivity
    - this can also be configured by setting and environment variable or property called `RestListicatorMaxSessionSeconds`

## End Points Summary


- `/heartbeat` - is the server alive?
- `/lists` - manage the List entities - create, amend lists
- `/lists/{guid}` - create, amend, delete a List
- `/users` - user management - create, delete
- `/users/{username}/password` - amend a User's password
- `/users/{username}/apikey` - amend a User's api key
- `/feature-toggles` - `superadmin` can toggle app features on and off
- `/sessionid` - get a unique sessionid to isolate your testing from other users, use the session id in a `X-SESSIONID` header


> NOTE:
> if you see `{username}` or `{guid}` mentioned in the end point documentation.
> This means "replace {username} with an actual username"
> e.g. `/users/{username}/apikey` would be `/users/admin/apikey`


The end points may be nested in a sub path e.g. `/listicator/heartbeat`

Check with your system admin to find out how the application has been configured.

On heroku the requests are of the form:

- `GET https://rest-list-system.herokuapp.com/listicator/heartbeat`

Running locally it is likely to be (by default):

- `GET https://localhost:4567/heartbeat`


---

## End Points

### Heartbeat

- `/heartbeat`
    - `GET` - return 200 to indicate server is running

e.g.

~~~~~~~~
curl -i -X GET http://localhost:4567/heartbeat
~~~~~~~~

~~~~~~~~
curl -v -X GET http://localhost:4567/heartbeat
~~~~~~~~

---

### Lists

- `/lists`
    - `GET`
        - return all the lists in the system
    - `POST`
        - requires authorized and authenticated user
        - create a list with partial payload e.g. `{title:'my title'}`
    - `PUT`
        - requires authorized and authenticated user
        - create a list with full payload i.e. `title`, `description`, `guid`, `createdDate`, `amendedDate`

---

#### Lists Examples

_Note: continuation character on mac is `\` and on Windows it is `^`_

~~~~~~~~
curl -X GET ^
  http://localhost:4567/lists ^
  -H "accept: application/json"
~~~~~~~~

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


~~~~~~~~
curl -X GET ^
  http://localhost:4567/lists ^
  -H "accept: application/xml"
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

---

### List

- /lists/{guid}
    - `GET` - return the details of a particular list
        - can filter list with `?title=exactTitle`
        - can filter list with `?title="partialTitle"`
    - `PUT` - amend a list or create a new list when supplied with full details of the list
    - `POST` - amend details of a list
        - user must be authenticated
        - only user with appropriate permission can amend a list
            - admin can amend any, user can ammend unowned, and lists they own
        - resource will be created if it does not exist
    - `PATCH` - partial amend of a list
        - user must be authenticated
        - only user with appropriate permission can delete a list
        - resource must exist
    - `DELETE` - delete a list
        - user must be authenticated
        - only user with appropriate permission can delete a list
            - admin can delete any list
            - users can delete their own, or unowned, or admin created lists
- /lists/{guid}?without={fieldlist}
    - `GET` - return the details of a particular list but without the fields in the comma separated `fieldlist` e.g. `guid,title`

---

#### List Examples


~~~~~~~~
curl -X GET \
  http://localhost:4567/lists/d4625287-989a-4454-b01a-cb99545a87a6 \
  -H 'accept: application/json'
~~~~~~~~

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


~~~~~~~~
curl -X GET ^
  http://localhost:4567/lists/d4625287-989a-4454-b01a-cb99545a87a6 ^
  -H 'accept: application/xml'
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

---


`POST /lists`

With a body

~~~~~~~~
{title:'my title custom'}
~~~~~~~~

Would create a list - try experimenting with the different fields: `guid`, `description`, `createdDate`, `amendedDate` etc.

---

`PATCH /lists/{guid}`

Where `{guid}` would be an actual guid value e.g. `d4625287-989a-4454-b01a-cb99545a87a6`

~~~~~~~~
{
    "guid": "d4625287-989a-4454-b01a-cb99545a87a6",
    "title": "title2",
}
~~~~~~~~

_currently does not comply with https://tools.ietf.org/html/rfc7396 _

---

### Users

- `/users`
    - `GET` - return usernames of all users
        - can be filtered with `?username=exactmatch` e.g. `?username=admin`
        - can be filtered with `?username="partialmatch"` e.g. `?username="adm"`

~~~~~~~~
curl -X GET http://localhost:4567/users
~~~~~~~~

- `/users`
     - `POST` - creat a user with `username` and `password`
         - calling user must be authenticated
         - calling user must be admin or have CREATE_USER permissions
         - username must be minimum of 6 chars
         - password must be minimum of 6 chars
         - 201 returned on success
         - 409 returned if user already exists

~~~~~~~~
curl -X POST \
  http://localhost:4567/users \
  -H 'accept: application/json' \
  -H 'authorization: Basic YWRtaW46cGFzc3dvcmQ=' \
  -H 'content-type: application/json' \
  -d '{username:"username", password:"password"}'
~~~~~~~~

---

### User

- `/users/{username}`
    - `GET` - return details of a user
        - user must be authenticated
        - user can only get their own details, not those of anyone else
        - admin users can get details of any user

~~~~~~~~
curl -X GET http://localhost:4567/users/superadmin ^
 -H "authorization: Basic YWRtaW46cGFzc3dvcmQ="
~~~~~~~~

- `/users/{username}/password`
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


- `/users/{username}/apikey`
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

- `/feature-toggles`
    - `GET` - return status of all feature toggles
        - user must be authenticated
    - `POST` - amend a list of feature toggles
        - user must be authenticated
        - only user with appropriate permission can set feature toggles

---

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



## Known Bugs

The system has been coded with some known bugs - these are all fixed by default. If you would like to test your testing skills then start with `-bugfixes=false` to have the known bugs present in the application.

See if you can find them.

`java -jar rest-list-system.jar -bugfixes=false`


---


## Details

This application has been written by [Alan Richardson](https://eviltester.com)

Copyright [Compendium Developments Ltd](https://compendiumdev.co.uk)

Source code is available as part of "[The Evil Tester's Compendium of Testing Apps](https://github.com/eviltester/TestingApp)" with source on Github.

Deployed to Heroku at [rest-list-system.herokuapp.com/listicator/](https://rest-list-system.herokuapp.com/listicator/)

Recommended tools for exploratory testing of REST API:

- [Insomnia](https://insomnia.rest/)
- [Postman](https://www.getpostman.com/)

If you are interested in learning how to test APIs then you might find the book by Alan Richardson called "[Automating and Testing a REST API](https://www.eviltester.com/page/books/automating-testing-api-casestudy/)" useful.

The book has a [support page with many videos and sample code](https://compendiumdev.co.uk/page/tracksrestsupport).

---

### More Details About the Author

- [www.eviltester.com](https://www.eviltester.com) - Software Testing Blogs and Articles
- [www.compendiumdev.co.uk](https://www.compendiumdev.co.uk) - Consultancy & Training
- [digitalonlinetactics.com](https://digitalonlinetactics.com) - Online Marketing Blog
- [uk.linkedin.com/in/eviltester](https://uk.linkedin.com/in/eviltester)
- [twitter.com/eviltester](https://twitter.com/eviltester)
- [patreon.com/eviltester](https://patreon.com/eviltester) - I release a lot of exclusive training content on Patreon