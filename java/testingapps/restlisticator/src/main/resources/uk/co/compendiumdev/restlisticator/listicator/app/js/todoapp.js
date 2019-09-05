// combined app, controller, model to make easier to grok

function TodoApp(){

    this.activeRoute;
    this.lastActiveRoute;
    this.storage;
    this.view;



    this.setView = function (locationHash) {
        var route = locationHash.split('/')[1];
        var page = route || '';
        this.updateFilterState(page);
    };


    this.addItem = function (title) {

        if (title.trim() === '') {
            return;
        }

        var self = this;
        self.create(title, function () {
            self.view.render('clearNewTodo');
            self.filter(true);
        });
    };

    this.editItem = function (id) {
        var self = this;
        self.read(id, function (data) {
            self.view.render('editItem', {id: id, title: data[0].title});
        });
    };


    this.editItemSave = function (id, title) {
        var self = this;
        title = title.trim();

        if (title.length !== 0) {
            self.update(id, {title: title}, function () {
                self.view.render('editItemDone', {id: id, title: title});
            });
        } else {
            self.removeItem(id);
        }
    };

    this.editItemCancel = function (id) {
        var self = this;
        self.read(id, function (data) {
            self.view.render('editItemDone', {id: id, title: data[0].title});
        });
    };

    this.removeItem = function (id) {
        var self = this;

        self.storage.remove(id, function () {
            self.view.render('removeItem', id);
        });

        self.filter();
    };

    this.removeCompletedItems = function () {
        var self = this;
        self.read({ completed: true }, function (data) {
            data.forEach(function (item) {
                self.removeItem(item.id);
            });
        });

        self.filter();
    };

    this.toggleComplete = function (id, completed, silent) {
        var self = this;
        this.update(id,
            { completed: completed },
            function () {
                        self.view.render('elementComplete', {
                        id: id,
                        completed: completed
                        });
        });

        if (!silent) {
            self.filter();
        }
    };

    this.toggleAll = function (completed) {
        var self = this;
        self.read({ completed: !completed }, function (data) {
            data.forEach(function (item) {
                self.toggleComplete(item.id, completed, true);
            });
        });

        self.filter();
    };


    this.showAll = function () {
        var self = this;
        this.read(function (data) {
            self.view.render('showEntries', data);
        });
    };

    this.showActive = function () {
        var self = this;
        this.read({ completed: false }, function (data) {
            self.view.render('showEntries', data);
        });
    };

    this.showCompleted = function () {
        var self = this;
        this.read({ completed: true }, function (data) {
            self.view.render('showEntries', data);
        });
    };

    this.updateCount = function () {
        var self = this;
        this.getCount(function (todos) {
            self.view.render('updateElementCount', todos.active);
            self.view.render('clearCompletedButton', {
                completed: todos.completed,
                visible: todos.completed > 0
            });

            self.view.render('toggleAll', {checked: todos.completed === todos.total});
            self.view.render('contentBlockVisibility', {visible: todos.total > 0});
        });
    };

    this.filter = function (force) {
        var activeRoute = this.activeRoute.charAt(0).toUpperCase() + this.activeRoute.substr(1);

        // Update the elements on the page, which change with each completed todo
        this.updateCount();

        // If the last active route isn't "All", or we're switching routes, we
        // re-create the todo item elements, calling:
        //   this.show[All|Active|Completed]();
        if (force || this.lastActiveRoute !== 'All' || this.lastActiveRoute !== activeRoute) {
            this['show' + activeRoute]();
        }

        this.lastActiveRoute = activeRoute;
    };

    this.updateFilterState = function (currentPage) {
        // Store a reference to the active route, allowing us to re-filter todo
        // items as they are marked complete or incomplete.
        this.activeRoute = currentPage;

        if (currentPage === '') {
            this.activeRoute = 'All';
        }

        this.filter();

        this.view.render('setFilter', currentPage);
    };



    // from model - simple storage wrapper functionality
    this.getCount = function (callback) {
        var todos = {
            active: 0,
            completed: 0,
            total: 0
        };

        this.storage.findAll(function (data) {
            data.forEach(function (todo) {
                if (todo.completed) {
                    todos.completed++;
                } else {
                    todos.active++;
                }

                todos.total++;
            });
            callback(todos);
        });
    };

    this.read = function (query, callback) {
        var queryType = typeof query;
        callback = callback || function () {};

        if (queryType === 'function') {
            callback = query;
            return this.storage.findAll(callback);
        } else if (queryType === 'string' || queryType === 'number') {
            query = parseInt(query, 10);
            this.storage.find({ id: query }, callback);
        } else {
            this.storage.find(query, callback);
        }
    };

    this.create = function (title, callback) {
        title = title || '';
        callback = callback || function () {};

        var newItem = {
            title: title.trim(),
            completed: false
        };

        this.storage.save(newItem, callback);
    };

    this.update = function (id, data, callback) {
        this.storage.save(data, callback, id);
    };

    this.remove = function (id, callback) {
        this.storage.remove(id, callback);
    };

    this.bindGuiEvents = function(){

        var self = this;
        // bind methods in the app to the GUI
        self.view.bind('newTodo', function (title) {
            self.addItem(title);
        });

        self.view.bind('itemEdit', function (item) {
           self.editItem(item.id);
        });

        self.view.bind('itemEditDone', function (item) {
            self.editItemSave(item.id, item.title);
        });

        self.view.bind('itemEditCancel', function (item) {
            self.editItemCancel(item.id);
        });

        self.view.bind('itemRemove', function (item) {
           self.removeItem(item.id);
        });

        self.view.bind('itemToggle', function (item) {
            self.toggleComplete(item.id, item.completed);
        });

        self.view.bind('removeCompleted', function () {
            self.removeCompletedItems();
        });

        self.view.bind('toggleAll', function (status) {
            self.toggleAll(status.completed);
        });
    }


    // LIST APP FUNCTIONS

    this.setListView = function () {
        this.showListView();
    };

    this.bindListGuiEvents = function(){

        var self = this;
        // bind methods in the app to the GUI
        self.view.bind('newTodo', function (title) {
            self.addListItem(title);
        });

        self.view.bind('itemEdit', function (item) {
            self.editListItem(item.id);
        });

        self.view.bind('itemEditDone', function (item) {
            self.editListItemSave(item.id, item.title);
        });

        self.view.bind('itemEditCancel', function (item) {
            self.editListItemCancel(item.id);
        });

        self.view.bind('itemRemove', function (item) {
            self.removeListItem(item.id);
        });

    }

    this.showListView = function () {
        var data = this.storage.listStores();
        var lists = [];
        data.forEach(
            function(element){
                        if(element.startsWith("todos-")){
                            lists.push(element.substring("todos-".length));
                        }
            }
            );
        this.view.render('showEntries', lists);
    };

    this.addListItem = function (title) {

        if (title.trim() === '') {
            return;
        }

        this.storage.createStore(this.ensureTodosStoragePrefix(title.trim().replace(/ /g,"-")));
        this.view.render('clearNewTodo');
        this.showListView();
    };

    this.removeListItem = function (id) {
        var self = this;

        // are you sure?
        if(!confirm("Are you sure you want to delete "+id + "?")){
            return;
        }
        self.storage.dropNamed(this.ensureTodosStoragePrefix(id), function () {
            self.view.render('removeItem', id);
        });
    };

    this.editListItem = function (id) {
        var self = this;
        self.view.render('editItem', id);
    };


    this.editListItemSave = function (id, title) {
        var self = this;
        title = title.trim().replace(/ /g,"-");

        if (title.length !== 0) {
            // TODO handle rename key from id to title errors
            var retmessage = this.storage.renamedb(this.ensureTodosStoragePrefix(id), this.ensureTodosStoragePrefix(title));
            if(retmessage.length>0){
                console.log(retmessage); // should really show this on screen
                self.view.render('editItemDone',{id: id, title: id});
            }else {
                self.view.render('editItemDone', {id: id, title: title});
            }
        } else {
            self.removeListItem(id);
        }
    };

    this.ensureTodosStoragePrefix = function(name){
        if(name.startsWith("todos-")){
            return name;
        }
        return "todos-"+name;
    }

    this.editListItemCancel = function (id) {
        this.view.render('editItemDone', {id: id, title: id});
    };

    // todo: split into main app common functions
    // todo list component app functions
    // todo list list component app functions
    // todo page should have multiple components which have an interface setView, bindGuiEvents and cycle over them to initialise
    // would allow combining list managent and list view on same page if wanted to for a version of the app
};