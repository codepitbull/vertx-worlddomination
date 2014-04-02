package de.codepitbull.lejos.slave.server.tcp;

import de.codepitbull.lejos.slave.server.commands.Direction;
import de.codepitbull.lejos.slave.command.CommandEnum;
import de.codepitbull.lejos.slave.controller.RobotController;
import de.codepitbull.lejos.slave.server.commands.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static de.codepitbull.lejos.slave.command.CommandBuilder.separator;

/**
 * @author Jochen Mader
 */
public class RobotServerRunnable implements Runnable{

    private final static Logger LOG = Logger.getLogger(RobotServerRunnable.class.getName());
    private final RobotState robotState;
    private final Map<CommandEnum, Command<?>> commandEnumCommandMap = new HashMap<>();

    private String name;

    private String host;
    private int port;

    private int reconnectInterval;

    private volatile boolean stop = false;

    RobotServerRunnable(Builder builder) {
        this.name = builder.name;
        this.host = builder.host;
        this.port = builder.port;
        this.reconnectInterval = builder.reconnectInterval;
        this.robotState = new RobotState(builder.robotController);
        this.robotState.x = builder.x;
        this.robotState.y = builder.y;
        this.robotState.direction = builder.direction;
        commandEnumCommandMap.put(CommandEnum.MEASURE, new MeasureCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.TRAVEL, new TravelCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.RIGHT, new RotateRightCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.LEFT, new RotateLeftCommand(robotState));
        commandEnumCommandMap.put(CommandEnum.SHOOT, new ShootCommand(robotState));
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void run() {
        Socket socket = null;
        while(!stop) {
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress(host, port), 1000);
                String announcement = "robot:"+name+","+robotState.x+","+robotState.y+","+robotState.direction+"\n";
                LOG.info("Announcing as "+announcement);
                socket.getOutputStream().write(announcement.getBytes());
                socket.getOutputStream().flush();
                LOG.info("Connected");
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                while(true) {
                    String command = in.readLine();
                    LOG.info("Executing "+command);
                    String[] nameAndValueAndCommandId = command.split(separator);
                    //sometimes we get leftover stuff from a closing socket
                    if(nameAndValueAndCommandId.length==3)
                        socket.getOutputStream().write(execute(nameAndValueAndCommandId[0], nameAndValueAndCommandId[1], nameAndValueAndCommandId[2]).getBytes());
                        socket.getOutputStream().flush();
                    sleep(20);
                }
            }
            catch (NullPointerException e) {
                LOG.info(e.toString());
            }
            catch (IOException e) {
                LOG.warning(e.toString());
            }
            catch (NumberFormatException e) {
                LOG.severe(e.toString());
            }
            finally {
                close(socket);
            }
            LOG.info("Disconnected. Trying to reconnect in ms "+reconnectInterval);
            sleep(reconnectInterval);
        }
    }

    private void close(Socket socket) {
        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private String execute(String command, String amount, String commandId) {
        CommandEnum actualCommand = CommandEnum.commandFromString(command);
        if(actualCommand != null) {
            return commandEnumCommandMap.get(actualCommand).execute(amount, commandId);
        }
        else {
            LOG.warning("Unknown command: "+command+" "+amount);
        }
        return "1:0:\n";
    }

    public static class Builder {
        String name;
        String host;
        Float x;
        Float y;
        Direction direction;
        int port = 2000;
        int reconnectInterval = 300;
        RobotController robotController;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setReconnectInterval(int reconnectInterval) {
            this.reconnectInterval = reconnectInterval;
            return this;
        }

        public Builder setRobotController(RobotController robotController) {
            this.robotController = robotController;
            return this;
        }

        public Builder setX(Float x) {
            this.x = x;
            return this;
        }

        public Builder setY(Float y) {
            this.y = y;
            return this;
        }

        public Builder setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public RobotServerRunnable build() {
            if(direction == null)
                throw new RuntimeException("direction not set");
            if(x == null)
                throw new RuntimeException("x not set");
            if(y == null)
                throw new RuntimeException("y not set");
            if(name == null)
                throw new RuntimeException("name not set");
            if(host == null)
                throw new RuntimeException("host not set");
            if(robotController == null)
                throw new RuntimeException("robotController not set");
            return new RobotServerRunnable(this);
        }
    }

}
