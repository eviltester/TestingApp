// originally based on http://todomvc.com/examples/vanillajs/js/template.js
// I simplified

function Template() {

    // default template
    this.defaultTemplate
        = '<li data-id="{{name}}">'
        + '<div class="view">'
        + '<a href="todo.html#/&{{name}}">[use]</a> '
        + '<label>{{name}}</label>'
        + '<button class="destroy"></button>'
        + '</div>'
        + '</li>';

    var htmlEscapes = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        '\'': '&#x27;',
        '`': '&#x60;'
    };

    var escapeHtmlChar = function (chr) {
        return htmlEscapes[chr];
    };

    var reUnescapedHtml = /[&<>"'`]/g;
    var reHasUnescapedHtml = new RegExp(reUnescapedHtml.source);

    var escape = function (string) {
        return (string && reHasUnescapedHtml.test(string))
            ? string.replace(reUnescapedHtml, escapeHtmlChar)
            : string;
    };


    /**
     * Creates an <li> HTML string and returns it for placement in your app.
     *
     * NOTE: In real life you should be using a templating engine such as Mustache
     * or Handlebars, however, this is a vanilla JS example.
     *
     * @param {object} data The object containing keys you want to find in the
     *                      template to replace.
     * @returns {string} HTML String of an <li> element
     *
     * @example
     * view.show({
     *	id: 1,
     *	title: "Hello World",
     *	completed: 0,
     * });
     */
    this.show = function (data) {
        var i, l;
        var view = '';

        for (i = 0, l = data.length; i < l; i++) {
            var template = this.defaultTemplate;

            template = template.replace(/{{name}}/g, data[i]);
            view = view + template;
        }

        return view;
    };

};
