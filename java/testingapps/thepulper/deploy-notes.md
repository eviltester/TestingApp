# deploy to heroku

Development notes.

First install heroku

https://devcenter.heroku.com/articles/heroku-cli

I'm on mac

- `brew tap heroku/brew && brew install heroku`
- `heroku plugins:install heroku-cli-deploy`
- `heroku login`
- `mvn package`
- `heroku deploy:jar target/thepulper-1-2-2-jar-with-dependencies.jar --app thepulper`

had to add `:target/*` to the procfile when deploying fat app
