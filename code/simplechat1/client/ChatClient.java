// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String clientID;
  ConnectionToClient ctcID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String clientID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); 
    this.clientID = clientID;
    this.clientUI = clientUI;
    openConnection();
    sendToServer("#login " + "<" + clientID + ">");
  }
  
  //Instance methods ************************************************
/**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  public void setClientID(String ID){
    this.clientID = ID;
  }

  public String getClientID(){
    return clientID;
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    if(message.startsWith("#")){
      //Now you have to filter the command, why splitting the whole command word by word
      String[] words = message.split(" ");  //Puts the words into an array
      String input = words[0];

      switch (input){
        case "#quit":
        System.out.println("Connection has been disconnected.");
        System.exit(0);
          break;
        case "#logoff":
          try {
            closeConnection();
          } catch (IOException e) { // required to catch the exception as it is thrown in AbstractClient
            System.out.println("Cannot close connection, try again." );
          }
          break;
        case "#sethost":
          // it is necessary to check if the client is already connected or not.
          if (this.isConnected()){
            System.out.println("Already Connected to: " + getHost());
          }else { 
            this.setHost(words[1]);
            System.out.println("Host is now set to: " + words[1]);
          }
          break;
        case "#setport":
          if (this.isConnected()){
            System.out.println("Already connected to: " + getPort());
          }else {
            this.setPort(Integer.parseInt(words[1]));
          }
          break;
        case "#login":
          if (this.isConnected() == false){
            try {
              this.openConnection();
            }catch (IOException e){
              System.out.println("Error.");
            }
            }else {
              System.out.println("Already connected to: " + getHost() + " at: " + getPort());
            }
          break;
        case "#gethost":
          System.out.println("The host is: " + this.getHost());
          break;
        case "#getport":
            System.out.println("The port is: " + this.getPort());
            break;
          }
        }else {
        try
        {
          sendToServer(message);
        }
        catch(IOException e)
        {
          clientUI.display
            ("Could not send message to server.  Terminating client.");
          quit();
        }
      }
    }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  public void connectionClosed(){
    System.out.println("Server has shut down.");
}
}
//End of ChatClient class
