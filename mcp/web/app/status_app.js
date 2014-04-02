require(["vertxbus","sockjs","jquery","bootstrap"], function() {
    var eb = new vertx.EventBus('http://localhost:9000/eventbus');

    function redrawContent(message) {
        $("#content").html(message)
    }

    eb.onopen = function() {
        eb.send('robots.status', '', function(message) {
            redrawContent(message)
            eb.registerHandler("robots.disconnect", function(message) {
                eb.send('robots.status', '', function(message) {
                    redrawContent(message)
                })
            })
            eb.registerHandler("robots.announce", function(message) {
                eb.send('robots.status', '', function(message) {
                    redrawContent(message)
                })
            })
        });
    }
});