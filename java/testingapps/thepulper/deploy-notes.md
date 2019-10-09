# deploy to heroku

Development notes.

First install heroku

https://devcenter.heroku.com/articles/heroku-cli

I'm on mac

- `brew tap heroku/brew && brew install heroku`
- `heroku plugins:install heroku-cli-deploy`
- `heroku login`
- `mvn package`
- `heroku deploy:jar target/thepulper-1-2-4-jar-with-dependencies.jar --app thepulper`

had to add `:target/*` to the procfile when deploying fat app

after deployment:

https://thepulper.herokuapp.com


To run locally

`heroku local -f Procfile.mac.local`