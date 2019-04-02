import java.net.*;
import java.io.*;

public class ClientTask implements Runnable
{
    private Socket client;
    private Socket other;
    private ServerSocket server;
    private int randomNum;        // this is the random number of the card  which is loose
    private boolean startFirst;

    ClientTask(ServerSocket server, Socket client, Socket other,int number,boolean start)
    {
        this.client = client;
        this.server = server;
        this.other = other;
        randomNum = number;
        this.startFirst = start;
    }

    public void run()
    {
        InputStream input = null;
        OutputStream output = null;
        InputStream otherInput = null;
        OutputStream otherOutput = null;

        InputStreamReader reader = null;
        OutputStreamWriter writer = null;
        InputStreamReader otherReader = null;
        OutputStreamWriter otherWriter = null;
        char[] message = new char[20];
        int readNum = 0;


        try
        {
            input = client.getInputStream();
            output = client.getOutputStream();
            otherInput = client.getInputStream();
            otherOutput = other.getOutputStream();

            reader = new InputStreamReader(input);
            writer = new OutputStreamWriter(output);
            otherReader = new InputStreamReader(otherInput);
            otherWriter = new OutputStreamWriter(otherOutput);
        }
        catch(IOException e)
        {
            System.out.println("cannot get input or output");
        }

        try
        {
            // if the client should start fist tell them


            while(true)
            {
                try
                {
                    String str = null;

                    readNum = reader.read(message);
                    System.out.println("the readNum is " + readNum + " the char is " + new String(message));
                    str = new String(message,0,readNum);
                    System.out.println("the String is " +  str);

                    control(str,writer,otherWriter,otherReader);
                }
                catch(IOException e)
                {
                    throw e;
                }
            }
        }
        catch (IOException e)
        {
            System.out.println("exiting");
            try
            {
                otherWriter.write("WIN");
                otherWriter.flush();
            }
            catch(IOException ex)
            {
                System.out.println("other socket close");
            }

        }
        finally
        {
            try
            {
                closeAll(input,output,reader,writer);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

    }



    ///////////////////////////////////////////////////////////////////////


    public void closeAll(InputStream input, OutputStream output, InputStreamReader reader, OutputStreamWriter writer)
            throws IOException
    {
        if(output != null)
            input.close();

        if(output != null)
            output.close();

        if(reader != null)
            input.close();

        if(writer != null)
            input.close();

    }


    ///////////////////////////////////////////////////////////////////////


    public void control(String str, OutputStreamWriter writer, OutputStreamWriter otherWriter, InputStreamReader otherReader) throws IOException
    {
        String[] mgs = str.split(" ");

        try
        {
            switch(mgs[0])
            {
                case "RANDOM" : writer.write("RANDOM " + randomNum);
                                break;

                case "WHOTERM" : otherWriter.write("YOURTERM");
                                 otherWriter.flush();
                                 //whoterm(writer,otherWriter,otherReader);
                                 break;

                case "MYTERM" : otherWriter.write("NOTSTART");
                                break;

                case "NOTMINE" : otherWriter.write("START");
                                 break;

                case "CARD" : otherWriter.write(str); Thread.sleep(100);
                              break;

                case "END" : otherWriter.write("START");
                             break;

                case "LOOSE" : otherWriter.write("WIN");
                               break;

                case "READY" : writer.write("CONNECT");  // tell the player are connect
                              writer.flush();
                              Thread.sleep(100);
                              System.out.println("flushed");

                                if(startFirst)
                                    writer.write("START");
                                 else
                                     writer.write("NOTSTART");

                                writer.flush();
                                break;

                default : System.out.println("cannot read this message: " + str); break;
            }
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();

        }
        finally
        {
            writer.flush();
            otherWriter.flush();
        }

    }


    ////////////////////////////////////////////////////////////////////
//
//    public void whoterm(OutputStreamWriter writer, OutputStreamWriter otherWriter, InputStreamReader otherReader) throws IOException
//    {
//        char[] message = new char[1024];
//        int numOfRead = otherReader.read(message);
//        String str = new String(message, 0, numOfRead);
//        control(str,writer,otherWriter,otherReader);
//    }
}
