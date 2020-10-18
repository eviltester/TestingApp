# Overview

A simple overview of the app to support usage and workshops.

The application has multiple versions. Select a version using the admin drop down or admin menu screen. The 'help' menu shows high level changes for each version. This document describes them in more detail. Note this document may have 'spoilers' in terms of deliberate bugs to find etc.

"The Pulper" is a CRUD app (Create, Read, Update, Delete) for Pulp Magazine and book collections.

The Pulper Application Links:

- https://thepulper.herokuapp.com/apps/pulp/
    - live version
- https://www.eviltester.com/page/tools/thepulper/
    - downloads and API documentation links
- API Documentation
    - https://documenter.getpostman.com/view/27996/SzKWucnk    
    
It comes pre-populated with data.

It should support multiple users, but this depends on what people do with it. Each user is given a unique session and the data is stored on a per user basis, so users should not be able to interfere with each other. This has the risk of consuming more memory, if many people use the application. Inactive sessions are removed after about 10 to 15 minutes of inactivity, and the user will be provided with a new session. 

- https://eviltester.com/thepulper
- https://thepulper.herokuapp.com/
- https://github.com/eviltester/TestingApp/releases

I recommend using a local version which you can download from the releases page above, in case the heroku version becomes over loaded.
