require.config({
    shim: {
        bootstrap: ["jquery"]
    },
    paths: {
        bootstrap: '../static/bootstrap/js/bootstrap',
        requirejs: '../static/require',
        jquery: '../static/jquery-2.1.0',
        sockjs: '../static/sockjs.min',
        vertxbus: '../static/vertxbus-2.1',
        jcanvas: '../static/jcanvas'
    },
    "shim": {
        "jcanvas": ["jquery"]
    }
});
