# The Listicator

## Design Notes

List (guid, title, description, owner) - Entry (guid, title, note, created-date, amended-date, state, owner, history [ date, title, note, state], metadata[ {key, value}])

if not logged in can see list names, but not entries

Decided to keep it small and simple to be grockable

Future - add notes to lists and entries - Entry Note - ListNote(text, owner, created-date)

Source list of up to date standards:

https://www.rfc-editor.org/standards
https://www.w3.org/Protocols/

- semantics and content https://www.rfc-editor.org/rfc/rfc7231.txt


Useful sites:

- http://restcookbook.com/
- http://www.bitnative.com/2012/08/26/how-restful-is-your-api/
- https://medium.com/@trevorhreed/you-re-api-isn-t-restful-and-that-s-good-b2662079cf0e

REST

- cacheable (on server side to avoid reprocessing of request)


## Mandatory to meet training requirements


- exploratory test
- build training docs

- need to test on Heroku and see if it works

## TODO - Optional


- swagger documentation and experimentation
    - https://groups.google.com/forum/#!forum/swagger-swaggersocket

- generic validation libraries
    - https://groups.google.com/forum/#!topic/swagger-swaggersocket/lPb7umX2a0E
    - https://bitbucket.org/atlassian/swagger-request-validator
    - https://github.com/java-json-tools/json-schema-validator


Consider adding hal - http://stateless.co/hal_specification.html

Consider Error reporting https://tools.ietf.org/html/rfc7807

// For reliable multi-user access
[] user can make lists public or private
[] private lists are only displayed to the user who owns it

// to explore standards
[] 200 should return the payloads to comply with standard https://tools.ietf.org/html/rfc7231#section-6.3.1 - make a feature toggle


[] default users created via environment variables
[] allow deleting of all lists
[] only admin can delete all lists

[] PATCH /LISTS/{guid}
    [] handle reporting to cover mix of yes, no, partial

[] user  
    [] admin user can delete user

[] user permissions
    [] admin can amend permissions through api

[] shutdown app via api - protected so only admin can do this

[] multi user

[] entries
[] history

[] /LISTS/id     - id is an in-memory attribute and never loaded
[] re-ordering lists

[] add message format checking at API test level

Tech Debt: - would want to do prior to code release

[] use logging instead of system.out.println to avoid cluttering logs with known cast errors for payload conversion

[] simplify payload conversion class

[] user authentication
    [] session cookie  - treat this as feature toggle as it could be a bug and vulnerability rather than a pro

## Possibly

[] create 'permissions' for users, start with - and + annotation

based on endpoint?

CRUD

/heartbeat
/lists |R|
/lists/* |C(PUT,POST)|R|U(POST,PATCH)|D|

CRUD Action(allowable verbs which can do the action)
|-GET|+POST| - -ve and =ve can override CRUD action definitions
[==.owner]

[] documentation - swagger?

[] honour a form payload type for submits?

[] hal format? http://stateless.co/hal_specification.html

[] profile links? https://tools.ietf.org/html/rfc6906

Report Errors in 7807
https://tools.ietf.org/html/rfc7807


[] Options returns verbs based on permissions for the user - have this as a 'bug toggle'



## DONE



20170910

- user can amend and delete lists they own, or those created by admin
- should allow multi-user access through heroku

earlier...

- [x] allow changing of port when starting application
- [x] create a -bugfixes=true flag which switches off all feature toggles to add all the bugs



20170816

- admin users can create users

20170815

- GET /LISTS?title=sfdsf - return a list if match exact, support partial match when "sfdsf"
- suspect bug that %20 etc in url won't url decode

- user can change their own /apikey
- admin user can change any user's apikey
- user can change their own /password
- admin user can change any user's password

- allow anonymous GET /users

20170723

- added a bug fix with feature toggle BUG_FIX_011 for content-type in response not being set as json or xml

20170720

- [x] no patching of GUID - unless you are an admin
- enforce guid must exist when patching - i.e. patch can not create


20170719

- PUT should only work on full entities
- POST and PATCH the same until we implement patch protocol

- review against other testing REST and their application functionality

- https://github.com/mwinteringham/restful-booker
   - GET, PUT, POST, DELETE
   - good docs
   - XML & JSON
   - session cookies

baz
- http://api.zippopotam.us/
- http://ergast.com


possibly useful

http://rem-rest-api.herokuapp.com/

[x] GET /LISTS/{guid}/?without=field,list,to,ignore
[x] add some feature toggles for permissions
[x] authentication required to patch list
[x] authentication required to delete list

- 20170718
[x] only admin can put a list of lists
[x] creating a list needs an authenticated user with appropriate permissions
[x] create default admin user - authkey(for custom header), username, password for basic auth
[x] only superadmin user can get and toggle features

[x] Basic Auth and API Key protection
    [x] protect GET /user/username  - user can get their own
    [x] protect GET /user/username  - user cannot get others
    [x] protect GET /user/username  - admin can get any

[x] split IntegrationTest into smaller tests
[x] create BasicAuthHeader class to handle basic auth processing and unit test

- 20170717

[x] API Key
    [x] user authentication
    [x] options on /user/username
    [x] can use apikey instead of username/password basic auth
    [x] GET /user/username returns apikey
    [x] 405 on anything but get

[x] user authentication
    [x] basic auth

[x] added feature toggle for WWW-Authenticate header on 401

[x] enforce permissions at REST API superadmin user can only set feature toggles on off

[x] can create users and permissions in code
[x] enums for verbs and endpoints

- 20170716

[x] create user with permissions
[x] default permissions to all verbs as true
[x] can create user with different permissions in code


- 20170714
- 
[x] options for each endpoint http://zacstewart.com/2012/04/14/http-options-method.html

~~~~~~~~
    200 OK
    Allow: HEAD,GET,PUT,DELETE,OPTIONS
~~~~~~~~

   - added options for heartbeat

- 20170713

[x] add feature toggles to allow simulation of bug fixes
[x] create API to amend feature toggles and switch them on and off   GET/POST
[x] return the list or list of lists in the payload after create
[x] when create single item then   use location header in the response
location: /lists/{guid}

- 20170706 - [x] PUT /LISTS/{guid}
                 [x] domain
                 [x] api  
                 [x] REST
                 - amend existing, add new
                 - cannot create new GUI when PUT to specific GUID
                 - does not allow patch of GUID when amending

             [x] PUT /LISTS
                 - can create new GUID when PUT to /LISTS
- 20170706 - [x] PATCH /LISTS/{guid}
    [x] domain
    [x] api
    [x] REST - new verb
- 20170706 - [x] GET /LISTS/{guid}
                 [x] domain
                 [x] api
                 [x] REST  - new routing required
- 20170705 - [x] DELETE /LISTS/{guid}  
    [x] domain
    [x] api
    [x] REST - because it is a new verb, need to check routing
- 20170705 - convert project from a course manager to a list/entry manager