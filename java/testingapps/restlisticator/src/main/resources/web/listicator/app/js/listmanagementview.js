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

    this.todoList = document.querySelector('.todo-list-list');
    this.main = document.querySelector('.main');
    this.newTodo = document.querySelector('.new-todo-list');

    this.removeItem = function (id) {
        var elem = document.querySelector('[data-id="' + id + '"]');

        if (elem) {
            this.todoList.removeChild(elem);
        }
    };

    this.editItem = function (id) {
        var listItem = document.querySelector('[data-id="' + id + '"]');

        if (!listItem) {
            return;
        }

        var title=id;

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

        listItem.outerHTML = this.template.show([title]);

        // var input = listItem.querySelector('input.edit');
        // listItem.removeChild(input);
        //
        // listItem.className = listItem.className.replace('editing', '');
        //
        // listItem.setAttribute("data-id", title);
        //
        // var nodesList = listItem.querySelectorAll("label");
        //
        // for (var node of nodesList) {
        //     node.textContent = title;
        // }
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
            clearNewTodo: function () {
                self.newTodo.value = '';
            },
            elementComplete: function () {
                self.elementComplete(parameter.id, parameter.completed);
            },
            editItem: function () {
                self.editItem(parameter);
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
        return li.dataset.id;
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

        }  else if (event === 'itemEdit') {
            delegate(self.todoList, 'li label', 'dblclick', function () {
                handler({id: self.itemId(this)});
            });

        } else if (event === 'itemRemove') {
            delegate(self.todoList, '.destroy', 'click', function () {
                handler({id: self.itemId(this)});
            });

        } else if (event === 'itemEditDone') {
            self.bindItemEditDone(handler);

        } else if (event === 'itemEditCancel') {
            self.bindItemEditCancel(handler);
        }
    };
}
