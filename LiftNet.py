from time import sleep
import pifacedigitalio
import socket
 
TOP = 4
BOTTOM = 1
MIDDLE = 2
STOP = 3

 
pifacedigital = pifacedigitalio.PiFaceDigital()
 
def motorUp ():
    pifacedigital.leds[1].value = 0
    pifacedigital.leds[0].value = 0
    s.send ("+\n")
    f = s.recv (128)
    sleep (0.1)
    pifacedigital.leds[0].value = 1
    #s.send ("+\n")
    #floor = s.recv (128)
    sleep (0.1)

def motorDown ():
    pifacedigital.leds[1].value = 1
    pifacedigital.leds[0].value = 0
    s.send ("-\n")
    f = s.recv (128)
    sleep (0.1)
    pifacedigital.leds[0].value = 1
    #s.send ("-\n")
    #floor = s.recv (128)
    sleep (0.1)
 
def getFloor ():
    #raw = pifacedigital.input_port.value
    #floor = (pifacedigital.input_port.value & 0x38) >> 3
    s.send ("?\n")
    f = s.recv (128)
    print "in getFloor: floor=", f[0]
    if f[0] == "T":
        return TOP

    elif f[0] == "M":
        return MIDDLE

    elif f[0] == "B":
        return BOTTOM
    else:
        return 0
 
def getCallFromNet ():
    data, addr = sock.recvfrom (32)
    print "NET CALL: ", data
    try:
        call = int (data) & 7
    except ValueError:
        call =  0
    #call = int(data) & 0x07
    if call & TOP == TOP:
        return TOP

    if call & MIDDLE == MIDDLE:
        return MIDDLE

    if call & BOTTOM == BOTTOM:
        return BOTTOM
 
def getCall ():
    while True:
        call = pifacedigital.input_port.value & 0x07
        print "CALL: " + str (call)
        sleep (1)
        if call & TOP == TOP:
            return TOP

	if call & MIDDLE == MIDDLE:
            return MIDDLE

        if call & BOTTOM == BOTTOM:
            return BOTTOM
 
def gotoTop ():
    while getFloor() != TOP:
        motorUp ();

def gotoMiddleU ():
    while getFloor() != MIDDLE:
	motorUp ();

def gotoMiddleD ():
    while getFloor() != MIDDLE:
        motorDown ();
 
def gotoBottom ():
    while getFloor() != BOTTOM:
        motorDown ();
 
def runLoop ():
    floor = getFloor ()
    while True:
        call = getCall ()
        #call = getCallFromNet ()
        floor = getFloor ()
        print "floor:" + str(floor) + " call:" + str(call) + "\n"
	if floor == TOP:
            if call == BOTTOM:
                print "Going down"
                gotoBottom ();
            elif floor == STOP:
            	break
            	print "Emegency Stop Pressed" 
                
                
	if floor == TOP:
            if call == MIDDLE:
                print "Going down M"
                gotoMiddleD ()

	if floor == MIDDLE:
	    if call == TOP:
	        print "Going Up"
		gotoTop ()

	if floor == MIDDLE:
	    if call == BOTTOM:
	        print "Going Down"
       	        gotoBottom

	if floor == BOTTOM:
            if call == MIDDLE:
                print "Going up M"
                gotoMiddleU ();

        if floor == BOTTOM:
            if call == TOP:
                print "Going up"
                gotoTop ()


s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect (("192.168.1.70", 5678)) #set to your PC or Mac IP
 
sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
sock.bind (("192.168.1.103", 1234)) #set to your RPi IP
 
runLoop ()
 
#"192.168.1.70"
#"192.168.1.103"

#169.254.66.211", 5678
#10.136.96.67", 1234
