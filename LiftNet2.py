from time import sleep
import pifacedigitalio
import socket
import thread
import os

 
TOP = 4
BOTTOM = 1
MIDDLE = 2
nextFloor=0
speed = 0.05
 
pifacedigital = pifacedigitalio.PiFaceDigital()
 
def motorUp ():
    global speed
    #pifacedigital.leds[1].value = 0
    #pifacedigital.leds[0].value = 0
    s.send ("+\n")
    f = s.recv (128)
    sleep (speed)
    pifacedigital.leds[0].value = 1
    #s.send ("+\n")
    #floor = s.recv (128)
   

def motorDown ():
    global speed
   # pifacedigital.leds[1].value = 1
   # pifacedigital.leds[0].value = 0
    s.send ("-\n")
    f = s.recv (128)
  
    pifacedigital.leds[0].value = 1
    #s.send ("-\n")
    #floor = s.recv (128)
    sleep (speed)
    
def motorStop ():
    global speed
   # pifacedigital.leds[1].value = 1
   # pifacedigital.leds[0].value = 0
    s.send ("=\n")
    f = s.recv (128)
  
    pifacedigital.leds[0].value = 0
    #s.send ("-\n")
    #floor = s.recv (128)
#     sleep (speed)
 
def motorReset ():
    global speed
   # pifacedigital.leds[1].value = 1
   # pifacedigital.leds[0].value = 0
    s.send ("$\n")
    f = s.recv (128)
  
    pifacedigital.leds[0].value = 1
    #s.send ("-\n")
    #floor = s.recv (128)
    sleep (speed)
 
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
        
        
def beep():
    print ("\a")
 
def getCallFromNet ():
    global nextFloor
    global speed
    while True:   
        data, addr = sock.recvfrom (32)
        print "NET CALL: ", data
        try:
           call = int (data) & 7
        except ValueError:
           call =  0
    #call = int(data) & 0x07
        if call & TOP == TOP:
            nextFloor= TOP
        if call & MIDDLE == MIDDLE:
            nextFloor= MIDDLE
        if call & BOTTOM == BOTTOM:
            nextFloor= BOTTOM
            
        if call == 3:
            motorStop()
            beep()                    
       
        if call == 6:
            nextFloor = -nextFloor
            speed = 0.01
        if call == 7:
            nextFloor = -nextFloor
            speed = 0.05
            
        # if call == 8:
#             nextFloor = 0
#             motorReset()
                
 
def getCallFromSwitch ():

    global nextFloor

    while True:

        call = pifacedigital.input_port.value & 0x07

        print "SWITCH: " + str (call)


        if call & TOP == TOP:
            nextFloor= TOP
        if call & MIDDLE == MIDDLE:
            nextFloor= MIDDLE
        if call & BOTTOM == BOTTOM:
            nextFloor= BOTTOM
            
def getCall ():

    global nextFloor

    while nextFloor == 0:

        pass

    floor = nextFloor

    nextFloor = 0

    print "Call to floor ", floor

    return floor
    
    
 
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
                gotoBottom ()

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
 
try:
     thread.start_new_thread(getCallFromSwitch,())
     
except:
     print "Error: unable to start thread"
     
try:

         thread.start_new_thread(getCallFromNet,())
except:
     print "Error: unable to start thread"


runLoop()



# https://stackoverflow.com/questions/20340430/python-thread-using-start-new-thread-not-working

 
#"192.168.1.70"
#"192.168.1.103"

#169.254.66.211", 5678
#10.136.96.67", 1234
