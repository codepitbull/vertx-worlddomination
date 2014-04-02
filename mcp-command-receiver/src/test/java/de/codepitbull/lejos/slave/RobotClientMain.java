package de.codepitbull.lejos.slave;

import de.codepitbull.lejos.slave.command.CommandBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Jochen Mader
 */
public class RobotClientMain {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(2000);
        Socket socket = serverSocket.accept();
        socket.setSoLinger(true, 0);
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        System.out.println("Connected to: "+in.readLine());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Command");
        OutputStream stream = socket.getOutputStream();
        long commandCounter = 0;
        while(true) {
            String s = br.readLine();
            String command = "";
            if("t".equals(s)) {
                command = CommandBuilder.travel(200f, Long.toString(commandCounter++));
            }
            if("l".equals(s)) {
                command = CommandBuilder.left(1, Long.toString(commandCounter++));
            }
            if("r".equals(s)) {
                command = CommandBuilder.right(1, Long.toString(commandCounter++));
            }
            if("m".equals(s)) {
                command = CommandBuilder.measure(Long.toString(commandCounter++));
            }
            if("s".equals(s)) {
                command = CommandBuilder.shoot(1, Long.toString(commandCounter++));
            }
            stream.write(command.getBytes());
            stream.flush();
            System.out.println("Response: "+in.readLine());
            Thread.sleep(3000);
        }
    }

}
