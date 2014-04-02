require(["vertxbus","sockjs","jquery","bootstrap","jcanvas"], function() {
    var eb = new vertx.EventBus('http://localhost:9000/eventbus');

    eb.onopen = function() {
        eb.registerHandler("robots.disconnect", function(message) {
            $('<p>'+message+' disconnected</p>').prependTo("#content")
        })

        eb.registerHandler("robots.announce", function(message) {
            $('<p>'+message+' connected</p>').prependTo("#content")
        })

    }
    $("#messageinput").keypress(function(e) {
        if(e.which == 13) {
            $(this).val()
            eb.send('robots.command', $(this).val())
            $(this).val("")
        }
    });
});