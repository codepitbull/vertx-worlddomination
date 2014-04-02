# MCP #

This is the outward facing part of the project. The MCP provides the web-interface.

After running go to [http://127.0.0.0:9000/status.html](http://127.0.0.0:9000/status.html) to see a status message or to [http://127.0.0.0:9000/index.html](http://127.0.0.0:9000/index.html) to control your army.

## COMMANDS ##

Commands are typed into the MCP-main-input-field. The convention is to provided the robot name and then a series of commands. The following command sequence would tell the robot 'awesomeBot' to move forward, turn left twice and then fire a shot:

__awesomeBot:forward left left shoot__

Known commands are:

- forward: Move forward
- left: turn left 90 degrees
- right: turn right 90 degrees
- shoot: fire gun

## RUN DIRECTLY ##


```
bin/vertx runMod de.codepitbull.mcp~mcp-module~1.0.0-final -conf conf.json -cluster
bin/vertx runMod de.codepitbull.mcp~mcp-module~1.0.0-final -conf /Users/jmader/Documents/3_development/vcs/github_codepitbull/vertx-lejos/mcp/conf.json -cluster
```