#!/usr/bin/python3
# -*- coding: utf-8 -*-

import time
import sys
import socket

print("Started Example Socket Script")

HOST = "localhost"
PORT = 9999

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

try:
    s.bind((HOST, PORT))
except socket.error as err:
    print('Bind failed. Error Code : ' .format(err))

s.listen(10)

conn, addr = s.accept()

while(True):
    data = conn.recv(1024)
    filename = data.decode(encoding='UTF-8')
    data_len = len(filename)
    if data_len>0:
        print("Filename set to ", filename)   
        conn.send(bytes("Success"+"\r\n",'UTF-8'))
    time.sleep(1)