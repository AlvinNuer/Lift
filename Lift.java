import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import javax.swing.event.*;

import java.net.*;

import java.io.*;

 

public class Lift extends JFrame

{

    public JButton top;

    public JButton middle;

    public JButton bottom;

    public Container container;

    ButtonHandler bHandler;

 

    public Lift (String title)

    {

        super (title);

 

        bHandler = new ButtonHandler ();

 

        container = getContentPane ();

        container.setLayout (new FlowLayout ());

 

        top = new JButton ("Top");

        container.add (top);

        top.addActionListener (bHandler);

 

        middle = new JButton ("Middle");

        container.add (middle);

        middle.addActionListener (bHandler);

 

        bottom = new JButton ("Bottom");

        container.add (bottom);

        bottom.addActionListener (bHandler);

    }

 

    private class ButtonHandler implements ActionListener

    {

        public void actionPerformed (ActionEvent event)

        {

            try

            {

                DatagramSocket socket = new DatagramSocket ();

                byte[] buf = new byte[256];

                String data = event.getActionCommand();

 

                //System.out.println (data.charAt(4) + ":" + data.charAt(0));

 

                if (data.charAt(0) == 'T')

                {

                    buf = "4".getBytes ();

                    InetAddress address = InetAddress.getByName ("192.168.1.103");

                    DatagramPacket packet = new DatagramPacket (buf, buf.length, address, 1234);

                    socket.send (packet);

                }

                if (data.charAt(0) == 'M')

                {

                    buf = "2".getBytes ();

                    InetAddress address = InetAddress.getByName ("192.168.1.103");

                    DatagramPacket packet = new DatagramPacket (buf, buf.length, address, 1234);

                    socket.send (packet);

                }

                if (data.charAt(0) == 'B')

                {

                    buf = "1".getBytes ();

                    InetAddress address = InetAddress.getByName ("192.168.1.103");

                    DatagramPacket packet = new DatagramPacket (buf, buf.length, address, 1234);

                    socket.send (packet);

                }

            }

            catch (IOException e)

            {

            }

        }

    }

 

    public static void main (String[] args)

    {

        Lift led = new Lift ("JAVA Lift Client");

 

        led.pack ();

        led.setVisible (true);

    }

}