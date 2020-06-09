// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  boolean svOpen;
  boolean svClosed;
  boolean svRunning;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }
  
  @Override
  protected void clientConnected(ConnectionToClient client){
    String message = (client.getInfo("User ID ") + ":" + " at port: " + getPort() + " has joined.");
    this.sendToAllClients(message);
    System.out.println(message);
  }

  @Override
  synchronized protected void clientDisconnected (ConnectionToClient client){
    String message = (client.getInfo("User ID ") +  ":" + " at port: " + getPort() + " has left.");
    this.sendToAllClients(message);
    System.out.println(message);
  }

  @Override
  synchronized protected void clientException(ConnectionToClient client, Throwable exception){
    String message = (client.getInfo("User ID ") +  ":" + " at port: " + getPort() + " has left.");
    this.sendToAllClients(message);
    System.out.println(message);
  }
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    //same setup as before for setting up in-line commands
    if (msg.toString().startsWith("#login")){
      String[] words = msg.toString().split(" ");
      client.setInfo("User ID ", words[1]); 
      System.out.println(msg);
      this.sendToAllClients(client.getInfo("User ID ") +  ":" + " has connected!");
    }
    else{
      System.out.println(client.getInfo("User ID ") + ": " + msg);
      this.sendToAllClients(client.getInfo("User ID ") + ": " + msg);
    }
  }
  public void handleMessageFromServerUI(String message) {

    //implementation for message interpretation for the server side
    if (message.startsWith("#")) {
      String[] words = message.split(" ");
      String input = words[0];
      switch (input) {
        case "#quit":
          System.out.print("Server quit.");
          System.exit(0);
          break;

        case "#stop":
          this.stopListening();
          System.out.print("Server stopped.");
          break;

        case "#close":
          try {
            this.sendToAllClients("Server disconnected.");
            this.close();
          } 
          catch (IOException e) {
            System.out.println("Error.");
          }
          break;

        case "#setport":
          if (!svOpen)
            this.setPort(Integer.parseInt(words[1])); //similar to ChatClient
          else
            System.out.println("Server already open.");
          break;

        case "#start":
          if (!svRunning) {
            try {
              this.listen();
            } catch (IOException e) {
              System.out.println("Error.");
            }
          } else
            System.out.println("Server already running.");
          break;

        case "#getport":
          System.out.println("Port: " + this.getPort());
          break;
      }
    } else {
      this.sendToAllClients("SERVER MSG> " + message);
    }
  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    svOpen = true;
    svRunning = true;
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    svOpen = false;
    svRunning = false;
    System.out.println
      ("Server has stopped listening for connections.");
  }

  protected void serverClosed(){
    svClosed = true;
  }
}
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
//   public static void main(String[] args) 
//   {
//     int port = 0; //Port to listen on

//     try
//     {
//       port = Integer.parseInt(args[0]); //Get port from command line
//     }
//     catch(Throwable t)
//     {
//       port = DEFAULT_PORT; //Set port to 5555
//     }
	
//     EchoServer sv = new EchoServer(port);
    
//     try 
//     {
//       sv.listen(); //Start listening for connections
//     } 
//     catch (Exception ex) 
//     {
//       System.out.println("ERROR - Could not listen for clients!");
//     }
//   }
// }
//End of EchoServer class
