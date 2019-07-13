import socket

 

def sendToLift (f):

    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    sock.sendto(f, ("192.168.1.103", 1234))

 

n = 0

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

s.bind (("192.168.1.103", 8888))

s.listen (1)

while True:

    conn, addr = s.accept ()

    data = conn.recv (2048)

    print "FLOOR:", data[5]

    sendToLift (data[5])

    str = '''HTTP/1.1 200 OK

 

<html>

<h1>Lift Web Server</h1>

<form action='http://192.168.1.103:8888/4' method='get'><input type=submit value='Goto Top'></form>

<form action='http://192.168.1.103:8888/2' method='get'><input type=submit value='Goto Middle'></form>

<form action='http://192.168.1.103:8888/1' method='get'><input type=submit value='Goto Bottom'></form>

</html>

'''

    conn.send (str)

    conn.close ()

 
