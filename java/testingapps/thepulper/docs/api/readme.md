The Pulper API documentation is publicly available on, and generated via, Postman.

- https://documenter.getpostman.com/view/27996/SzKWucnk

Postman Collection, and Insomnia Workspaces are available for import in the insomnia and postman folders.


---

Below are some links for API documentation research. 

## API Research

- Q: What tools can I use to document and test the API?
- A: https://nordicapis.com/ultimate-guide-to-30-api-documentation-solutions/

## Dredd

- https://dredd.readthedocs.io/en/latest/

* DREDD uses swagger and API Blueprint
* https://apiblueprint.org/
    * https://apiblueprint.org/tools.html
    
    
Dredd:

* picks up swagger syntax errors quickly
* validates against 200 requests
    * more work required for other codes
    
## APIMatic

Convert between different API formats. Also publish as a GUI and generate SDK etc.

- https://www.apimatic.io/transformer

Can read the Insomnia exported file and used the 'documentation' information in Insomnia.

Commercial product - trial plans available.    

## Swagger

- http://editor.swagger.io/
    - Swagger editor useful for pinpointing syntax errors and visualizing the API

## SWAGGYMNIA

Convert Insomnia output to swagger format:
    
- https://github.com/mlabouardy/swaggymnia

## Insomnia Documenter

A 'server' for converting insomnia.json to a readable html output.

- https://www.npmjs.com/package/insomnia-documenter
    
## Others

- https://app.apiary.io/thepulper/editor
- https://next.stoplight.io/eviltester-1/thepulper/version%2F1.0/pulper-openapi.oas2.yml?edit=/definitions/BookItem/authors