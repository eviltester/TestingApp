# Instructor Notes

## Changing Versions

The normal way to change a version is to use the Admin main menu.

But...

Adding a `v` query param to a url will change the version during the call
 
This can be handy for switching between versions without changing the screen and renavigating.

e.g.
 
- http://localhost:4567/apps/pulp/gui/reports/authors/list/navigation?v=1
- http://localhost:4567/apps/pulp/gui/reports/authors/list/navigation?v=10
- or start on a specific version
    - http://localhost:4567/apps/pulp/?v=3
