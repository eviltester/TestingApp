cp -R java/testingapps/compendiumdevapps/src/main/resources/web/apps docs/apps
cp -R java/testingapps/compendiumdevapps/src/main/resources/web/games docs/games
cp -R java/testingapps/shared-styles/src/main/resources/web/css docs/css
cd docs
find . -name '*.html' |xargs perl -pi -e 's/\"\/css\//\"https:\/\/eviltester\.github\.io\/TestingApp\/css\//g'
find . -name '*.html' |xargs perl -pi -e 's/\"\/games\//\"https:\/\/eviltester\.github\.io\/TestingApp\/games\//g'
find . -name '*.html' |xargs perl -pi -e 's/\"\/apps\//\"https:\/\/eviltester\.github\.io\/TestingApp\/apps\//g'