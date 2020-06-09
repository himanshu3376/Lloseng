import java.io.IOException;
import common.*;
import java.io.*;

/*
@author Himanshu Sehgal 2020
*/

public class ServerConsole implements ChatIF {
    
    final public static int DEFAULT_PORT = 5555;    //Default port

    EchoServer server;

    public ServerConsole (int port) {
        //First we will create a new instance of the server, if it is already running, an error is thrown.
        //Then we will check to see if the server can listen to clients
        try {
            server = new EchoServer(port);        
        } catch (Exception e) {
            System.out.println ("Error setting up connection at: " + port + " Terminating server.");
            System.exit(1);
        }
        try {
            server.listen();
        } catch (IOException ex){
            System.out.println("Error, couldn't listen for clients.");
        }
    }

    @Override
    public void display(String message) {
        System.out.println("Server MSG > " + message);  //Signature that shows up when server sends message
    }

    public void accept() {
        try { 
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            String msg;
            
            while (true) {
                msg = console.readLine();
                server.handleMessageFromServerUI(msg);
            }
        }catch (Exception ex){
            System.out.println("Error reading from ServerConsole.");
        }
    }

    public static void main(String[] args){
        int port = 0;
        try {
            port = Integer.parseInt(args[0]);   //gets port from command line
        } catch (Throwable t){
            port = DEFAULT_PORT;
        }

        ServerConsole sv = new ServerConsole(port);
        System.out.println("Connected at port: " + port);
        sv.accept();
    }
}