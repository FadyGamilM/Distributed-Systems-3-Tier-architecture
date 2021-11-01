package com.socketprog;
import java.net.*;
import java.io.*;

/******* client handler class must inherit from Thread class so we can provide multi-threaded paradigm ********/
class ClientHandler extends Thread{

    //###################### [1] Define a client socket ######################### //
    Socket client;
    //##############################################################################

    //########## [2] Define input/output streams between server and sensor nodes ##########//
    DataInputStream sensor_is;
    DataOutputStream sensor_os;
    // ####################################################################################


    //################ [3] constructor to set the client socket, input stream for sensor, and output stream for sensor ############//
    public ClientHandler(Socket s,DataInputStream sensor_is, DataOutputStream sensor_os)
    {
        this.client=s;
        this.sensor_is=sensor_is;
        this.sensor_os=sensor_os;
    }
    //##############################################################################################################################


    //###################### [4] we have to implement our own version of run function to be able to start the client's thread  ##################//
    @Override
    public void run(){
        try
        {
            /** Define the client-server input/output streams to deal with clients */
            DataInputStream is = new DataInputStream(client.getInputStream());
            DataOutputStream os = new DataOutputStream(client.getOutputStream());
            /*********************************************************************/


            while (true)
            {
                os.writeUTF("Connection Is Established Successfully, How Can I Help You ? ");
                String Request=is.readUTF();
                System.out.println("[CLIENT] : "+Request);

                os.writeUTF("Got It, But We Need More Info Before Sending You The Best Route.\n[1] Enter Your Location: ");
                os.flush();
                String location = is.readUTF();
                System.out.println("[CLIENT] : "+location);

                os.writeUTF("[2] Enter Your Destination: ");
                os.flush();
                String destination = is.readUTF();
                System.out.println("[CLIENT] : "+destination);

                sensor_os.writeUTF("Please Send Me The Best Recommendation for this user, The required time for the trip, and the state of streets right now.");
                sensor_os.flush();
                String readings = sensor_is.readUTF();
                System.out.println("[SENSOR] : "+readings);


                os.writeUTF("recommendation for the best route is: "+readings+"\nFor New Request Enter [YES]\n To End This Session Enter [NO]");
                os.flush();
                String decision = is.readUTF();
                System.out.println("[CLIENT] : "+decision);

                if (decision.equalsIgnoreCase("NO")){
                    os.writeUTF("[SERVER] : Session Is End, See You Later..");
                    break;
                }
            }

            is.close();
            os.close();
            client.close();

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

}

public class Server_Threaded {
    public static void main(String[] args)
    {
        try
        {
            ServerSocket server = new ServerSocket(22000);

            //############# Connection Setup between server and socket nodes ####################/
            Socket sensor_client= new Socket("127.0.0.1",33000);
            DataInputStream sensor_is = new DataInputStream(sensor_client.getInputStream());
            DataOutputStream sensor_os = new DataOutputStream(sensor_client.getOutputStream());
            // ###################################################################################

            System.out.println("Server Running up on port 22000 ...");
            while (true)
            {
                Socket client = server.accept();

                System.out.println("A New Client Is Connected !! ...");

                // create a thread for this client
                ClientHandler thr1 = new ClientHandler(client,sensor_is,sensor_os );

                // start the thread of the current connected client
                thr1.start();

            }

        } catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
