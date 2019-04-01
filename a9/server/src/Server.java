import java.net.*;
import java.io.*;
import java.util.*;

public class Server
{
    final static int PORT = 10080;

    public static void main(String[] args)
    {
        //ArrayList<Thread> tasks = new ArrayList<Thread>();
        try
        {
            ServerSocket server = new ServerSocket(PORT);

            while(true)
            {
                try
                {
                    System.out.println("waiting for user1");
                    Socket client1 = server.accept();
                    if(client1.isConnected())
                        System.out.println("user1 is connect");

                    System.out.println("waiting for other user");
                    Socket client2 = server.accept();
                    if(client2.isConnected())
                        System.out.println("two users are connected");

                    int randomNum = (int) (Math.random() * 20);

                    Thread task = new Thread(new ClientTask(server,client1,client2,randomNum,true));
                    Thread task2 = new Thread(new ClientTask(server,client2,client1,randomNum,false));
                    task.start();
                    task2.start();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                    //System.out.println("cannot read or write to the client");
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }





    }
}