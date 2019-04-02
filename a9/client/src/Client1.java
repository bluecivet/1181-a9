import java.io.*;
import java.net.*;

public class Client1
{
    public static void main(String[] args)
    {
        Socket server = null;
        try
        {
            server = new Socket("localhost", 10081);
            GameWindow window = new GameWindow(server);

        }
        catch(UnknownHostException e)
        {
            System.out.println("cannot find the server");
        }
        catch(IOException e)
        {
            System.out.println("cannot get the message");
        }


    }
}
