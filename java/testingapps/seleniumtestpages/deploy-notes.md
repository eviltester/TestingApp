# deploy to heroku

Development notes.

First install heroku

https://devcenter.heroku.com/articles/heroku-cli

- if installed already then `brew update`, `brew tap update`

Mac:

- `brew tap heroku/brew && brew install heroku`

Then:

- `heroku plugins:install heroku-cli-deploy`
- `heroku login`
- `mvn package`
- `cd testingapps/seleniumtestpages`
- `heroku login`
- `heroku deploy:jar target/seleniumtestpages-1-4-1-jar-with-dependencies.jar --app testpages`

had to add `:target/*` to the procfile when deploying fat app

after deployment:

https://testpages.herokuapp.com


To run locally

`heroku local -f Procfile.mac.local`