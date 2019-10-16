#!/usr/bin/env python2

import socket
import sys
import thread
import time
import re

default_message = """we were here again!"""

def on_new_client(clientsocket,addr):
    print 'Got connection from', addr
    while True:
        msg = clientsocket.recv(2024)
        print '>>' + msg
        clientsocket.send(default_message)
    clientsocket.close()
    print 'Closed connection from', addr

s = socket.socket()         # Create a socket object
host = '0.0.0.0' # Get local machine name
port = 9500   # Reserve a port for your service.

print 'Server started!'
print '0.0.0.0'

s.bind((host, port))        # Bind to the port
print 'Waiting for clients...'
s.listen(5)                 # Now wait for client connection.


while True:
    c, addr = s.accept()     # Establish connection with client.
    thread.start_new_thread(on_new_client,(c,addr))
s.close()
