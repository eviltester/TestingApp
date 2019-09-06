// Based on http://todomvc.com/examples/vanillajs/js/store.js
// amended to be non-anonymous function and make ids a little more unique
// amended remove and drop to allow call without a callback

function Storage(){

    dbName = "mylocalstorage"; // by default

    // TODO - match the LIST storage in the GUI and local storage match the API storage
    // TODO - have generic local Storage class (because it isn't really generic, it is just for the todo app)
    // TODO - create a TodosStorage which handles the additional responsibility of todos e.g. add todos- to the front of the names if not existing and filter out those which don't etc.
    // TODO - create a separate API storage class
    // TODO - create the API storage to match the TODO storage in the GUI and local storage

    this.listStores = function(callback){

        callback = callback || function () {};

        var stores = [];

        for (let i=0; i< localStorage.length; i++) {
            stores.push(localStorage.key(i));
        }

        callback.call(this, stores);
        return stores;
    }

    // javascript for rest api initial hack version GET lists
/*
    this.listStores = function(callback){

        callback = callback || function () {};

        var stores = [];

        var request = new XMLHttpRequest();
        request.open('GET', '../lists', false, "user","password")
        request.onreadystatechange = function() {

            if(request.readyState === 4 && request.status === 200) {

                console.log(request.responseText);
                var responseData = JSON.parse(request.responseText);
                for (let i=0; i< responseData.lists.length; i++) {
                    stores.push(responseData.lists[i].title);
                }

                callback.call(this, stores);
            }
        }
        request.send()


        return stores;
    }
*/

    /**
     * Creates a new client side storage object and will create an empty
     * collection if no collection already exists.
     *
     * @param {string} name The name of our DB we want to use
     * @param {function} callback
     */
    this.createStore = function(name, callback){

        callback = callback || function () {};

        this.dbName = name;

        if (!localStorage.getItem(name)) {
            var emptytodoslist = [];

            localStorage.setItem(name, JSON.stringify(emptytodoslist));
        }

        callback.call(this, JSON.parse(localStorage.getItem(name)));
    }

    // javascript for rest api initial hack version POST lists
/*
    this.createStore = function(name, callback){

        callback = callback || function () {};

        this.dbName = name;

        var request = new XMLHttpRequest();
        request.open('POST', '../lists', false, "user","password");
        request.setRequestHeader("Content-Type", "application/json");
        request.onreadystatechange = function() {

            if (request.readyState === 4 && request.status === 201) {

                var responseData = JSON.parse(request.responseText);

                console.log(request.responseText);
                callback.call(this, responseData.lists[0].title);
            }
        }

        request.send(JSON.stringify({"title":name}));

        return name;
    }
*/


    // TODO ITEMS ONLY
    /**
     * Finds items based on a query given as a JS object
     *
     * @param {object} query The query to match against (i.e. {foo: 'bar'})
     * @param {function} callback	 The callback to fire when the query has
     * completed running
     *
     * @example
     * db.find({foo: 'bar', hello: 'world'}, function (data) {
     *	 // data will return any items that have foo: bar and
     *	 // hello: world in their properties
     * });
     */
    this.find = function(query, callback) {
        if (!callback) {
            return;
        }

        // get all the todos
        var todos = JSON.parse(localStorage.getItem(this.dbName));

        callback.call(this, todos.filter(function (todo) {
            for (var q in query) {
                if (query[q] !== todo[q]) {
                    return false;
                }
            }
            return true;
        }));
    };

    // TODO ITEMS ONLY
    /**
     * Will retrieve all data from the collection
     *
     * @param {function} callback The callback to fire upon retrieving data
     */
    this.findAll = function(callback) {
        callback = callback || function () {};
        callback.call(this, JSON.parse(localStorage.getItem(this.dbName)));
    };



    /**
     * Will save the given data to the DB. If no item exists it will create a new
     * item, otherwise it'll simply update an existing item's properties
     *
     * @param {object} updateData The data to save back into the DB
     * @param {function} callback The callback to fire after saving
     * @param {number} id An optional param to enter an ID of an item to update
     */
    this.save = function(updateData, callback, id) {
        var todos = JSON.parse(localStorage.getItem(this.dbName));

        callback = callback || function() {};

        // If an ID was actually given, find the item and update each property
        if (id) {
            for (var i = 0; i < todos.length; i++) {
                if (todos[i].id === id) {
                    for (var key in updateData) {
                        todos[i][key] = updateData[key];
                    }
                    break;
                }
            }

            localStorage.setItem(this.dbName, JSON.stringify(todos));
            callback.call(this, todos);
        } else {
            // Generate an ID - AJR added the random number to allow creating multiple values in same millisecond
            // does not guarantee a unique id but a better chance of avoiding conflict
            updateData.id = new Date().getTime() + Math.floor(Math.random() * 10000);

            todos.push(updateData);
            localStorage.setItem(this.dbName, JSON.stringify(todos));
            callback.call(this, [updateData]);
        }
    };

    /**
     * Will remove an item from the Store based on its ID
     *
     * @param {number} id The ID of the item you want to remove
     * @param {function} callback The callback to fire after saving
     */
    this.remove = function(id, callback){
        callback = callback || function() {};

        var todos = JSON.parse(localStorage.getItem(this.dbName));

        for (var i = 0; i < todos.length; i++) {
            if (todos[i].id == id) {
                todos.splice(i, 1);
                break;
            }
        }

        localStorage.setItem(this.dbName, JSON.stringify(todos));
        callback.call(this, todos);
    };

    /**
     * Will drop all storage and start fresh - delete everything but keep storage db
     *
     * @param {function} callback The callback to fire after dropping the data
     */
    this.drop = function(callback) {
        callback = callback || function() {};
        var todos = [];
        localStorage.setItem(this.dbName, JSON.stringify(todos));
        callback.call(this, todos);
    };

    this.dropNamed = function(named, callback) {
        callback = callback || function() {};
        var todos = [];
        localStorage.removeItem(named);
        callback.call(this, todos);
    };

    this.renamedb= function(from, to){
        // todo: change this to a return object and/or callbacks for success, fail
        // if from does not exist fail
        if(!localStorage[from]){
            return "cannot rename a list that does not exist"; // fail
        }
        // if to does exist then fail
        if(!!localStorage[to]){
            return "cannot rename to a list that already exists";
        }
        localStorage.setItem(to, localStorage.getItem(from));
        localStorage.removeItem(from);
        return ""; // ok
    }
};