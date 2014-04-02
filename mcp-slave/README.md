#MCP SLAVE
This module contains everything to get your EV3 under the control of Vert.x

Building instructions will follow asap.

Run '__gradle build__' and copy the resulting jar (mcp_slave.jar) to your mindstorm with [Lejos](http://sourceforge.net/p/lejos/wiki/Home/).

Use the following command to get everything started:

__jrun -cp mcp-slave.jar de.codepitbull.lejos.slave.server.tcp.TcpMain server.properties__

The __server.properties__ has the following content.

```
#name of the robot
name = fakeDominator
#host to connect to
host = 192.168.0.100
#host port to connect to
port = 2000
#delay between reconnection attempts
reconnectInterval = 500
#initial position x
x = 0
#initial position y
y = 0
#initial facing
direction = N
```

## Mock 
There's also a Mock-Robot included: __de.codepitbull.lejos.slave.TcpMockMain__

Simply run this class and provide a server.properties-file. It will just connect like the real thing and echo all commands to the stdout.
