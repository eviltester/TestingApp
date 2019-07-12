var hooks = require('hooks');

hooks.before('/apps/pulp/api/books > Create or amend a single or multiple books > 201 > application/json', (transaction) => {
    transaction.request.body=JSON.stringify({
        "books":[
            {
                "title": "The Land of Little People Terror",
                "publicationYear": 1980,
                "seriesId": "Issue 1",
                "authors": [
                    {
                        "id": "4"
                    }
                ],
                "series": {
                    "id": "1"
                },
                "publisher": {
                    "id": "1"
                }
            }
        ]
    });
});
hooks.before('/apps/pulp/api/books > Create or amend a single or multiple books > 200 > application/json', (transaction) => {
    transaction.skip=true;
});