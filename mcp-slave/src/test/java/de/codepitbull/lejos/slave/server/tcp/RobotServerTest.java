package de.codepitbull.lejos.slave.server.tcp;

import de.codepitbull.lejos.slave.controller.RobotController;
import de.codepitbull.lejos.slave.command.CommandBuilder;
import de.codepitbull.lejos.slave.server.commands.Direction;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Jochen Mader
 */
public class RobotServerTest {

    @Test
    public void testConnect() throws Exception{
        RobotController robotController = mock(RobotController.class);
        when(robotController.travel(anyFloat())).thenReturn(true);
        RobotServerRunnable robotServerRunnable = new RobotServerRunnable.Builder()
                .setName("rob1")
                .setHost("127.0.0.1")
                .setPort(2000)
                .setReconnectInterval(0)
                .setRobotController(robotController)
                .setX(0f)
                .setY(0f)
                .setDirection(Direction.N)
                .build();
        ServerThread serverThread = new ServerThread();
        serverThread.start();
        Thread robotServerThread = new Thread(robotServerRunnable);
        robotServerThread.start();
        serverThread.join();
        robotServerRunnable.stop();
        verify(robotController).rotateRight(2);
        verify(robotController).travel(50f);
        verify(robotController).rotateLeft(2);
        verify(robotController).fireGun(1);
    }

    @Test
    public void testReConnect() throws Exception{
        RobotController robotController = mock(RobotController.class);
        RobotServerRunnable robotServerRunnable = new RobotServerRunnable.Builder()
                .setName("rob1")
                .setHost("127.0.0.1")
                .setPort(2001)
                .setReconnectInterval(50)
                .setRobotController(robotController)
                .setX(0f)
                .setY(0f)
                .setDirection(Direction.N)
                .build();
        TerminatingServerThread serverThread = new TerminatingServerThread();
        serverThread.start();
        Thread robotServerThread = new Thread(robotServerRunnable);
        robotServerThread.start();
        serverThread.join();
        robotServerRunnable.stop();
    }


    private static void readAndAssert(Socket socket) throws IOException {
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        assertEquals("hello:rob1,0.0,0.0,N",in.readLine());
    }

    private static class ServerThread extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(2000);
                Socket socket = serverSocket.accept();
                socket.setSoLinger(true, 0);
                readAndAssert(socket);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                socket.getOutputStream().write(CommandBuilder.right(2, "1").getBytes());
                socket.getOutputStream().flush();
                assertEquals("A:1:1:", in.readLine());
                socket.getOutputStream().write(CommandBuilder.travel(50f, "2").getBytes());
                socket.getOutputStream().flush();
                assertEquals("A:2:1:", in.readLine());
                socket.getOutputStream().write(CommandBuilder.left(2, "3").getBytes());
                socket.getOutputStream().flush();
                assertEquals("A:3:1:", in.readLine());
                socket.getOutputStream().write(CommandBuilder.shoot(1, "4").getBytes());
                socket.getOutputStream().flush();
                assertEquals("A:4:1:",in.readLine());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class TerminatingServerThread extends Thread {
        @Override
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(2001);
                Socket socket = serverSocket.accept();
                socket.setSoLinger(true, 0);
                readAndAssert(socket);
                socket.close();
                serverSocket.close();
                serverSocket = new ServerSocket(2001);
                socket = serverSocket.accept();
                socket.setSoLinger(true, 0);
                readAndAssert(socket);
                socket.close();
                serverSocket.close();
                serverSocket = new ServerSocket(2001);
                socket = serverSocket.accept();
                socket.setSoLinger(true, 0);
                readAndAssert(socket);
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
