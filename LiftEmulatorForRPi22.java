import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import java.util.Timer;

import java.util.TimerTask;

import java.io.*;

import java.io.BufferedReader;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.net.*;

 

public class LiftEmulatorForRPi extends JFrame //implements ActionListener

{

    private JButton buttonB, buttonM, buttonT;

    static private JPanel panel;

    static int h = 780;//hh = 780;

    static boolean start = false;

    static int sign = 1;

    static int tick = 0;

    static int floor = 0;

    static boolean up = true;

    static PrintWriter output;

    static BufferedReader input;

 

    class TcpServer

    {

        ServerSocket serverSocket;

        Socket socket;

 

        TcpServer (int port) throws IOException

        {

            serverSocket = new ServerSocket (port);

            socket = serverSocket.accept ();

            //output = socket.getOutputStream();

            output = new PrintWriter (socket.getOutputStream(), true);

            input = new BufferedReader (new InputStreamReader (socket.getInputStream ()));

        }

    }

 

    public LiftEmulatorForRPi()

    {

        try {TcpServer t = new TcpServer (5678);} catch (IOException e) {}

    }

 

    public void createGUI ()

    {

        setDefaultCloseOperation (EXIT_ON_CLOSE);

 

        Container window = getContentPane ();

 

        window.setLayout (new FlowLayout());

 

        panel = new JPanel ();

        //panel.setDoubleBuffered (true);

        panel.setPreferredSize (new Dimension(480, 900));

        panel.setBackground (Color.white);

 

        window.add(panel);

 

        panel.addMouseListener(new MouseAdapter()

                               {

            @Override

            public void mouseClicked(MouseEvent e)

            {

                int x = e.getX();

                int y = e.getY();

 

                System.out.println("(" + x + "," + y + ")");

 

                if (x >= 20 && x <= 30 && y >= 40 && y <= 50)

                {

                    System.out.println ("RED");

                    floor = 1;

                }

 

                if (x >= 20 && x <= 30 && y >= 400 && y <= 410)

                {

                    System.out.println ("YELLOW");

                    floor = 2;

                }

 

                if (x >= 20 && x <= 30 && y >= 800 && y <= 810)

                {

                    System.out.println ("GREEN");

                    floor = 4;

                }

 

                if (h == 780)

                {

                    if (x >= 30 && x < 40 && y >= 800 && y <= 810)

                    {

                        System.out.println("Cage Call: Red");

                        floor = 1;

                    }

 

                    if (x >= 40 && x <= 50 && y >= 800 && y <= 810)

                    {

                        System.out.println("Cage Call: Yellow");

                        floor = 2;

                    }

 

                    if (x >= 50 && x <= 60 && y >= 800 && y <= 810)

                    {

                        System.out.println("Cage Call: Green");

                        floor = 4;

                    }

                }

 

                if (h == 380)

                {

                    if (x >= 30 && x < 40 && y >= 400 && y <= 410)

                    {

                        System.out.println("Cage Call: Red");

                        floor = 1;

                    }

 

                    if (x >= 40 && x <= 50 && y >= 400 && y <= 410)

                    {

                        System.out.println("Cage Call: Yellow");

                        floor = 2;

                    }

 

                    if (x >= 50 && x <= 60 && y >= 400 && y <= 410)

                    {

                        System.out.println("Cage Call: Green");

                        floor = 4;

                    }

                }

 

                if (h == 20)

                {

                    if (x >= 30 && x < 40 && y >= 40 && y <= 50)

                    {

                        System.out.println("Cage Call: Red");

                        floor = 1;

                    }

 

                    if (x >= 40 && x <= 50 && y >= 40 && y <= 50)

                    {

                        System.out.println("Cage Call: Yellow");

                        floor = 2;

                    }

 

                    if (x >= 50 && x <= 60 && y >= 40 && y <= 50)

                    {

                        System.out.println("Cage Call: Green");

                        floor = 4;

                    }

                }

 

            }

        });

    }

 

    public static String sensor (int h)

    {

        if (h == 20)       return "T"; //(floor + 8) ^ 0x3F;
        
        else if (h == 375) return "M";

        else if (h == 380) return "M"; //(floor + 16) ^ 0x3F;

 		else if (h == 385) return "M";
 		
        else if (h == 780) return "B"; //(floor + 32) ^ 0x3F;
        

        else               return "X";

    }

 

    public static void main (String[] args) throws IOException

    {

        LiftEmulatorForRPi frame = new LiftEmulatorForRPi ();

 

        frame.createGUI ();

        frame.setSize (new Dimension (500,1000));

        frame.setVisible (true);

 

        String s = "+";

        Graphics paper = panel.getGraphics();

 

        output.println (sensor (h));

        System.out.println ("Init:" + sensor (h) + " " + h);

 

        // LIFT SHAFT

        paper.setColor (Color.gray);

        paper.fillRect (40, 10, 10, 880);

        // LIFT BASE

        paper.fillRect (10, 890, 70, 10);

        paper.setColor (Color.white);

 

        // FLOOR BUTTON

        paper.setColor (Color.red);

        paper.fillRect (20, 40, 10, 10);

        paper.setColor (Color.gray);

        paper.fillRect (30, 40, 10, 10);

        paper.setColor (new Color(0, 120, 0));

        paper.fillRect (50, 40, 20, 10);

 

        paper.setColor (Color.yellow);

        paper.fillRect (20, 400, 10, 10);

        paper.setColor (Color.gray);

        paper.fillRect (30, 400, 10, 10);

        paper.setColor (new Color(0, 120, 0));

        paper.fillRect (50, 400, 20, 10);

 

        paper.setColor (Color.green);

        paper.fillRect (20, 800, 10, 10);

        paper.setColor (Color.gray);

        paper.fillRect (30, 800, 10, 10);

        paper.setColor (new Color(0, 120, 0));

        paper.fillRect (50, 800, 20, 10);

 

        // LIFT CAGE

        paper.setColor (Color.cyan);

        paper.fillRect (30, h, 30, 30);

        paper.setColor (Color.red);

        paper.fillRect (30, h+20, 10, 10);

        paper.setColor (Color.yellow);

        paper.fillRect (40, h+20, 10, 10);

        paper.setColor (Color.green);

        paper.fillRect (50, h+20, 10, 10);

 

        //boolean firstTime = true;

 

        while (true)

        {

            System.out.println ("Waiting...");

            s = input.readLine();

 

            if (s.equals ("?")) {output.println (sensor (h));}

 

            if (s.equals("-"))

            {

                up = true;

                if (h < 780) h = h + 5; //40;

            }

 

            if (s.equals("+"))

            {

                up = false;

                if (h > 20) h = h - 5; //40;

            }
            
            if (s.equals("="))

            {

                up = true;
                
                if (h < 780) h = -h ;

                
//                 if(h < 780) return;
                


            }
        
       
            
            
            

            // LIFT SHAFT

            paper.setColor (Color.gray);

            paper.fillRect (40, 10, 10, 880);

            // LIFT BASE

            paper.fillRect (10, 890, 70, 10);

            paper.setColor (Color.white);

 

            // ERASE CAGE WHEN MOVED

            if (up)

            {

                paper.fillRect (30, h-5, 10, 5);

                paper.fillRect (50, h-5, 10, 5);

            }

            else

            {

                paper.fillRect (30, h+30, 10, 5);

                paper.fillRect (50, h+30, 10, 5);

            }

 

            // FLOOR BUTTON

            paper.setColor (Color.red);

            paper.fillRect (20, 40, 10, 10);

            paper.setColor (Color.gray);

            paper.fillRect (30, 40, 10, 10);

            paper.setColor (new Color(0, 120, 0));

            paper.fillRect (50, 40, 20, 10);

 

            paper.setColor (Color.yellow);

            paper.fillRect (20, 400, 10, 10);

            paper.setColor (Color.gray);

            paper.fillRect (30, 400, 10, 10);

            paper.setColor (new Color(0, 120, 0));

            paper.fillRect (50, 400, 20, 10);

 

            paper.setColor (Color.green);

            paper.fillRect (20, 800, 10, 10);

            paper.setColor (Color.gray);

            paper.fillRect (30, 800, 10, 10);

            paper.setColor (new Color(0, 120, 0));

            paper.fillRect (50, 800, 20, 10);

 

            // LIFT CAGE

            paper.setColor (Color.cyan);

            paper.fillRect (30, h, 30, 30);

            paper.setColor (Color.red);

            paper.fillRect (30, h+20, 10, 10);

            paper.setColor (Color.yellow);

            paper.fillRect (40, h+20, 10, 10);

            paper.setColor (Color.green);

            paper.fillRect (50, h+20, 10, 10);

 

            output.println (sensor (h));

            System.out.println (s + " " + sensor (h) + " " + h);

            if (floor != 0 || s.equals("=")) floor = 0; // Reset

        }

    }

}