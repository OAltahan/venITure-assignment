// We know that $ is jQuery, so we are just waiting the document to be loaded
// as the button we want to change its text is created dynamically, i.e. put on
// the document after it is loaded (cannot change its text until the document is loaded)
// And we know that if we pass a function to the jQuery object itself directly ($)
// (using it as a function) then it registers the function that is passed as the
// first parameter of the $ function to be executed after the document is loaded.
AJS.$(function () {
    AJS.$("#create_link").text('something else');
})
