# deploy to heroku

Development notes.

First install heroku

https://devcenter.heroku.com/articles/heroku-cli

I'm on mac

- `brew tap heroku/brew && brew install heroku`
- `heroku plugins:install heroku-cli-deploy`
- `heroku login`
- `mvn package`
- `heroku deploy:jar target/seleniumtestpages-1-3-3-jar-with-dependencies.jar --app testpages`

had to add `:target/*` to the procfile when deploying fat app

after deployment:

https://testpages.herokuapp.com


To run locally

`heroku local -f Procfile.mac.local`