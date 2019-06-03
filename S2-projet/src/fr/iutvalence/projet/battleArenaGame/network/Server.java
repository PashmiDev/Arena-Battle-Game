package fr.iutvalence.projet.battleArenaGame.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import fr.iutvalence.projet.battleArenaGame.Game;
import fr.iutvalence.projet.battleArenaGame.pawn.TeamId;

/**
 * Server class represent one of the player which receive and send data to other player and manage the game
 * In the future, other player can be added easily by just editing the MAXPLAYERS constant.
 * 
 * @author pashmi
 *
 */
public class Server implements Communication
{
    //The port of the server
    private int port;
    //Where we write/send things
    private ObjectInputStream in;

    //Where we read send
    private ObjectOutputStream out;

    //Socket for the current client
    private Socket socketClient;

    //Socket of the server
    private ServerSocket socketServ;

    //Number of connected player
    private int playersConnected;

    //table which contains Socket, ObjectInputStream & ObjectOutputStream of a customer
    private Object[][] clients;
    
    //Network of the system: Used to translate received object into methods calls
    private Network myNetwork;
 
    
    /**
     * Constructor for the server
     * When the server is started there is no player connected
     * @param port chosen in the Game class
     */
    public Server(int port, Network pNetwork)
    {
        this.port = port;
        this.playersConnected = 0;
        this.clients = new Object[Game.MAXPLAYERS][3];
        this.myNetwork = pNetwork;

    }


    /**
     * This method basically launch the server
     * It first listen for udp search (in order to give ip adress of the server to client)
     * Then it wait for every players to connect and it creates thread to listen to what client are sending
     * It also saves players socket, inputstream and outputstream in the "clients" array
     * in order to send information to the right player
     */
    public void init()
    {

        //TODO remove debug
        System.out.println("Launching the server...");

        socketServ = null;

        try {
            socketServ = new ServerSocket(port);
        }
        catch (IOException e) {
        	//TODO replace this
            e.printStackTrace();
        }
        System.out.println("Server launched !");



        //The server starts to emit or send when every player are connected
        while(this.playersConnected < Game.MAXPLAYERS)
        {
            try{

                // The server wait for a connection from the client

                socketClient = socketServ.accept();

                System.out.println("Connected with : " + socketClient.getInetAddress().getHostName());


                //It get the outputStream and inputStream from the client socket
                out = new ObjectOutputStream(socketClient.getOutputStream());

                in = new ObjectInputStream(socketClient.getInputStream());

                //It saves the socket, the input stream and output stream in the clients array
                clients[this.playersConnected][0] = socketClient;
                clients[this.playersConnected][1] = in;
                clients[this.playersConnected][2] = out;


                /*
                Create a new thread to handle the connection of the right and new player connected
                 */
                //TODO remove debug
                //System.out.println("Nb joueurs courants:" + this.playersConnected + "Socket" + this.clients[this.playersConnected][0] + " OutputStream " + this.clients[this.playersConnected][1]);

                new Thread(() -> receive(((Socket)clients[playersConnected-1][0]), ((ObjectInputStream)clients[playersConnected-1][1]))).start();

                this.Send(this.playersConnected, new TeamId(this.playersConnected));

                //Increase the number of connected player
                this.playersConnected++;

            } catch(Exception e)
            {
            	//TODO replace this
                e.printStackTrace();
            }
        }

    }

   
    /**
     * Receive method, this method is called by a thread which always listen to a client Socket 
     * It read the object in the stream sent by the client.
     * If the client sent a quit message, it disconnect him and remove him from the array.
     * @param pSocket socket of the client
     * @param pInput ObjectInputStream of the client
     */
    private void receive(Socket pSocket, ObjectInputStream pInput)
    {

        while(socketClient.isConnected())
        {
            Object msg = null;
            try {
                msg = pInput.readObject();
                myNetwork.Transform(msg);

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(socketClient.getInetAddress().getHostName() + "(client) :" + msg);

            if (msg.equals(Game.QUIT))
            {
                System.out.println("Deconnexion de " + pSocket.getInetAddress());

                try {
                    pSocket.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                //Searching the ID for the leaving player
                for (int playerId = 0; playerId < this.playersConnected; playerId++) {}

            //If clients[i][0] == pSocket: the disconnected user is found

                //Copy the Socket existing after the removed one just after the previous of the removed one.
                if (this.playersConnected - 1 >= 0)
                    System.arraycopy(clients, 1, clients, 0, this.playersConnected - 1);

                //Then reduce the array size
                this.playersConnected--;

            }
        }
    }
    


    /**
     * Send to everyone an object
     * Write in every outputStream of connected people the object passed in parameters
     * 
     * @param o: the object sent
     */
    public void sendToOther(Object o)
    {
        System.out.println("Sending to all : " +o);
        for (int playerID = 0; playerID < this.playersConnected; ++playerID)
        {
            try {
                ((ObjectOutputStream)clients[playerID][2]).writeObject(o);
            }
            catch (Exception e) {
            	//TODO Replace this
                e.printStackTrace();
            }
        }

    }

    /**
     * Send to only one player
     * First id = first person connected
     * Check if the id of the player is correct
     * Write in the selected outputStream the object passed in parameters
     * @param playerId id of the player (to whom we are talking)
     * @param o Object that we are sending
     */
    public void Send(int playerId, Object o)
    {
        System.out.println("Sending to " + ((Socket)this.clients[playerId][0]).getInetAddress().getHostName());
        
        if(playerId >= 0 && playerId < this.playersConnected)
        {
            try {
                ((ObjectOutputStream)clients[playerId][2]).writeObject(o);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
            System.out.println("Id non reconnu... Soon tm une exception...");
        //Exception : IdClientException() TODO
    }


    /**
     * Send to everyone except the emitter
     * (to prevent negative effect of double movement)
     * @param id id of the emitter
     * @param o Object sended
     */
    public void Broadcast(int id, Object o)
    {
        System.out.println("Sending to everyone except " + ((Socket)this.clients[id][0]).getInetAddress().getHostName());

        for (int playerIndex = 0; playerIndex < this.playersConnected; playerIndex++)
        {
            if(playerIndex != id)
            {
                try {
                    ((ObjectOutputStream)clients[playerIndex][2]).writeObject(o);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Close every socket of connected players in order to disconnect them
     */
    public void DisconnectAll()
    {
        for(int playerIndex = 0; playerIndex < this.playersConnected; playerIndex++)
        {
            try {
                ((Socket)this.clients[playerIndex][0]).close();
                System.out.println( ((Socket)this.clients[playerIndex][0]).getInetAddress().getHostName() + " has left");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }






}
