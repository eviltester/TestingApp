
// based on http://todomvc.com/examples/vanillajs/js/view.js
// trimmed out helper functions to make more basic
// added the checked attribute on completed todos

function View(template) {

    this.template = template;

    this.ENTER_KEY = 13;
    this.ESCAPE_KEY = 27;

    delegate = function (target, selector, type, handler) {
        function dispatchEvent(event) {
            var targetElement = event.target;
            var potentialElements = document.querySelectorAll(selector, target);
            var hasMatch = Array.prototype.indexOf.call(potentialElements, targetElement) >= 0;

            if (hasMatch) {
                handler.call(targetElement, event);
            }
        }

        // https://developer.mozilla.org/en-US/docs/Web/Events/blur
        var useCapture = type === 'blur' || type === 'focus';

        target.addEventListener(type, dispatchEvent, !!useCapture);
    };

    this.todoList = document.querySelector('.todo-list');
    this.todoItemCounter = document.querySelector('.todo-count');
    this.clearCompleted = document.querySelector('.clear-completed');
    this.main = document.querySelector('.main');
    this.footer = document.querySelector('.footer');
    this.toggleAll = document.querySelector('.toggle-all');
    this.newTodo = document.querySelector('.new-todo');

    this.removeItem = function (id) {
        var elem = document.querySelector('[data-id="' + id + '"]');

        if (elem) {
            this.todoList.removeChild(elem);
        }
    };

    this.clearCompletedButton = function (completedCount, visible) {
        this.clearCompleted.innerHTML = this.template.clearCompletedButton(completedCount);
        this.clearCompleted.style.display = visible ? 'block' : 'none';
    };

    this.setFilter = function (currentPage) {
        document.querySelector('.filters .selected').className = '';
        document.querySelector('.filters [href="#/' + currentPage + '"]').className = 'selected';
    };

    this.elementComplete = function (id, completed) {
        var listItem = document.querySelector('[data-id="' + id + '"]');

        if (!listItem) {
            return;
        }

        // this makes the basic javascript version more compatible with backbone version
        // note this doesn't use the template
        if(completed){
            listItem.querySelector("input.toggle").setAttribute("checked", true);
        }else{
            listItem.querySelector("input.toggle").removeAttribute("checked")
        }

        listItem.className = completed ? 'completed' : '';

        // In case it was toggled from an event and not by clicking the checkbox
        listItem.querySelector('input').checked = completed;
    };

    this.editItem = function (id, title) {
        var listItem = document.querySelector('[data-id="' + id + '"]');

        if (!listItem) {
            return;
        }

        var editing = "editing";
        if(listItem.className.length>0){
            editing = " " + editing;
        }

        listItem.className = listItem.className + editing;

        var input = document.createElement('input');
        input.className = 'edit';

        listItem.appendChild(input);
        input.focus();
        input.value = title;
    };

    this.editItemDone = function (id, title) {
        var listItem = document.querySelector('[data-id="' + id + '"]');

        if (!listItem) {
            return;
        }

        var input = listItem.querySelector('input.edit');
        listItem.removeChild(input);

        listItem.className = listItem.className.replace('editing', '');

        var nodesList = listItem.querySelectorAll("label");

        for (var node of nodesList) {
            node.textContent = title;
        }
    };

    this.render = function (viewCmd, parameter) {
        var self = this;
        var viewCommands = {
            showEntries: function () {
                self.todoList.innerHTML = self.template.show(parameter);
            },
            removeItem: function () {
                self.removeItem(parameter);
            },
            updateElementCount: function () {
                self.todoItemCounter.innerHTML = self.template.itemCounter(parameter);
            },
            clearCompletedButton: function () {
                self.clearCompletedButton(parameter.completed, parameter.visible);
            },
            contentBlockVisibility: function () {
                self.main.style.display = self.footer.style.display = parameter.visible ? 'block' : 'none';
            },
            toggleAll: function () {
                self.toggleAll.checked = parameter.checked;
            },
            setFilter: function () {
                self.setFilter(parameter);
            },
            clearNewTodo: function () {
                self.newTodo.value = '';
            },
            elementComplete: function () {
                self.elementComplete(parameter.id, parameter.completed);
            },
            editItem: function () {
                self.editItem(parameter.id, parameter.title);
            },
            editItemDone: function () {
                self.editItemDone(parameter.id, parameter.title);
            }
        };

        if(viewCommands.hasOwnProperty(viewCmd)) {
            viewCommands[viewCmd]();
        }
    };

    parent = function (element, tagName) {
        if (!element.parentNode) {
            return;
        }
        if (element.parentNode.tagName.toLowerCase() === tagName.toLowerCase()) {
            return element.parentNode;
        }
        return parent(element.parentNode, tagName);
    };

    this.itemId = function (element) {
        var li = parent(element, 'li');
        return parseInt(li.dataset.id, 10);
    };

    this.bindItemEditDone = function (handler) {
        var self = this;
        delegate(self.todoList, 'li .edit', 'blur', function () {
            if (!this.dataset.iscanceled) {
                handler({
                    id: self.itemId(this),
                    title: this.value
                });
            }
        });

        delegate(self.todoList, 'li .edit', 'keypress', function (event) {
            if (event.keyCode === self.ENTER_KEY) {
                // Remove the cursor from the input when you hit enter just like if it
                // were a real form
                this.blur();
            }
        });
    };

    this.bindItemEditCancel = function (handler) {
        var self = this;
        delegate(self.todoList, 'li .edit', 'keyup', function (event) {
            if (event.keyCode === self.ESCAPE_KEY) {
                this.dataset.iscanceled = true;
                this.blur();

                handler({id: self.itemId(this)});
            }
        });
    };

    this.bind = function (event, handler) {
        var self = this;
        if (event === 'newTodo') {
            self.newTodo.addEventListener('change', function () {
                handler(self.newTodo.value);
            });

        } else if (event === 'removeCompleted') {
            self.clearCompleted.addEventListener('click', function () {
                handler();
            });

        } else if (event === 'toggleAll') {
            self.toggleAll.addEventListener( 'click', function () {
                handler({completed: this.checked});
            });

        } else if (event === 'itemEdit') {
            delegate(self.todoList, 'li label', 'dblclick', function () {
                handler({id: self.itemId(this)});
            });

        } else if (event === 'itemRemove') {
            delegate(self.todoList, '.destroy', 'click', function () {
                handler({id: self.itemId(this)});
            });

        } else if (event === 'itemToggle') {
            delegate(self.todoList, '.toggle', 'click', function () {
                handler({
                    id: self.itemId(this),
                    completed: this.checked
                });
            });

        } else if (event === 'itemEditDone') {
            self.bindItemEditDone(handler);

        } else if (event === 'itemEditCancel') {
            self.bindItemEditCancel(handler);
        }
    };
}
