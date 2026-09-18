# Evil Tester's Compendium of Testing Apps

A Compendium of applications, pages and REST APIs that you can use to practice your testing and automating.

These are also used in training provided by Alan Richardson (Evil Tester).

- https://compendiumdev.co.uk
- https://eviltester.com

## Legacy status and migration

This project is now a legacy distribution. The actively maintained static apps, tools, and games have moved to [testpages.eviltester.com](https://testpages.eviltester.com/), with the source work now in the sibling `../testpages` project.

For a route-by-route mapping from the old GitHub Pages URLs to the new Test Pages URLs, see [MIGRATED_URLS.md](MIGRATED_URLS.md).

Use the new sections on Test Pages:

- [Apps](https://testpages.eviltester.com/apps/)
- [Tools](https://testpages.eviltester.com/tools/)
- [Fun and Games](https://testpages.eviltester.com/fun-and-games/)

Migrated applications and tools:

- [7 Char Val](https://testpages.eviltester.com/apps/7-char-val/)
- [E-Primer](https://testpages.eviltester.com/apps/e-primer/)
- [Bookmarklet Generator](https://testpages.eviltester.com/tools/bookmarklet-generator/)
- [CounterString Generator](https://testpages.eviltester.com/tools/counterstring-generator/)

Migrated games:

- [Test With A.T.T.I.T.U.D.E](https://testpages.eviltester.com/fun-and-games/test-with-attitude/)
- [Cellular Automata](https://testpages.eviltester.com/fun-and-games/cellular-automata/)
- [Console Driver](https://testpages.eviltester.com/fun-and-games/console-driver/)
- [Canvas Driver](https://testpages.eviltester.com/fun-and-games/canvas-driver/)
- [Coloured Square Game](https://testpages.eviltester.com/fun-and-games/coloured-square-game/)
- [Coloured Square Changing Game](https://testpages.eviltester.com/fun-and-games/coloured-square-changing-game/)
- [Number Hover Text Game](https://testpages.eviltester.com/fun-and-games/number-hover-text-game/)
- [Random Walker](https://testpages.eviltester.com/fun-and-games/random-walker/)
- [Multi-Level Random Walker](https://testpages.eviltester.com/fun-and-games/multi-level-random-walker/)
- [Grid Walker](https://testpages.eviltester.com/fun-and-games/grid-walker/)
- [One To Nine Calculator](https://testpages.eviltester.com/fun-and-games/one-to-nine-calculator/)
- [Protect The Square](https://testpages.eviltester.com/fun-and-games/protect-the-square/)
- [Think of a Number](https://testpages.eviltester.com/fun-and-games/think-of-a-number/) variants: [v1](https://testpages.eviltester.com/fun-and-games/think-of-a-number/version-1/), [v2](https://testpages.eviltester.com/fun-and-games/think-of-a-number/version-2/), [v3](https://testpages.eviltester.com/fun-and-games/think-of-a-number/version-3/), [v4](https://testpages.eviltester.com/fun-and-games/think-of-a-number/version-4/), [v5](https://testpages.eviltester.com/fun-and-games/think-of-a-number/version-5/), [v6](https://testpages.eviltester.com/fun-and-games/think-of-a-number/version-6/)

The I-frame Search Engine and Responsive Test Tool are not being migrated. Newer browser security restrictions mean those iframe-based tools no longer work reliably, so the legacy copies in this project are deprecated.

## How to use

Download the current release `.jar` file or clone the repo and build it yourself.

Run the `.jar` file

e.g.  `java -jar compendium-of-test-apps-v1-1.jar`

The server should start and you'll see something like:

~~~~~~~~
SLF4J: Failed to load class "org.slf4j.impl.StaticLoggerBinder".
SLF4J: Defaulting to no-operation (NOP) logger implementation
SLF4J: See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.
User : superadmin - a791c960-b15d-4948-b971-dbd8212b3125
User : admin - d31b83a0-ca64-4d88-a14d-910d52e30586
User : user - 0a867cf0-d6eb-45c8-90c4-09f4fb108b94
Running on 4567
~~~~~~~~

You can access the apps and pages by visiting:

- http://localhost:4567

## Building it yourself

[![Current Snapshot Build Status](https://travis-ci.org/eviltester/TestingApp.svg?branch=master)](https://travis-ci.org/eviltester/TestingApp)

If you want to build it yourself rather than use the release version then.

In the `\java` folder where the top level `pom.xml` is, run `mvn package`

I release the `.jar` file in the subdirectory `\java\testingapps\testingapp\target`

I release the `*-jar-with-dependencies.jar` and I usually rename it to get rid of the `-jar-with-dependencies.jar` part to make it easier to type.

The individual applications each have their own `...jar-with-dependencies.jar` in their own `target` folders.

Runnable applications are:

- CompendiumDevApps - javascript games and small applications
- RestListicator - a REST API
- SeleniumTestPages - a set of simple pages for Web GUI Automating
- The Pulper - a GUI 'pulp' CRUD App for exploratory testing or automating
- TestingApp - all the above wrapped into one executable jar

## GUI

The GUI will change gradually over time and more applications will be added.

## Separate Apps

All of the parts of the compilation can be built individually if you use the module projects in the sub directories, e.g. if you only want the REST Listicator API then you can build and run just the restlisticator, but most people will find the Compendium compilation as the easiest way to run the apps.

## This Compendium Contains:

- Buggy JavaScript applications as used for training in Technical Web Testing and "Just Enough JavaScript to be Dangerous"
- Selenium Test Pages as used for "Selenium WebDriver with Java training" and the book "Selenium Simplified"
- Rest Listicator as used for REST API training
- The Pulper - a GUI application for exploratory testing and web automating - this has multiple versions built in which can be toggled via the GUI
- A variety of applications used for Technical Testing training and actual production use

## I found a bug, what do I do?

Remember that these are testing apps, some bugs are to be expected.

But if you find anything that causes the local application to crash (that should not happen) or find something that prevents you moving forward then let me know, either via github or [contact me via my website](https://compendiumdev.co.uk/contact)

The [releases](https://github.com/eviltester/TestingApp/releases) should work. Their @Test methods were run during the build. And some interactive investigation would have been performed prior to release. Snapshot is not guaranteed to run, but the build status (as reported by travis) can be seen here [![Current Snapshot Build Status](https://travis-ci.org/eviltester/TestingApp.svg?branch=master)](https://travis-ci.org/eviltester/TestingApp).

You might like to sign up to my [email mailing list](https://www.eviltester.com/page/emaillist/) where I will mention any updates to the application.
