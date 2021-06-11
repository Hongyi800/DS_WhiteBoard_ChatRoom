1.	Introduction 
This is a multi-threaded shared painting whiteboard which provides accessibility for multiple users to operate, draw and chat with other online users simultaneously. This whiteboard server provides a branch of basic functionality including synchronous painting board, multiplayer text chat room, online user display list and export, import of files. 
2.	Architecture
SYSTEM ARCHITECTURE
COMMUNICATION PROTOCOLS 
The system is based on java socket, used transfer control protocol (TCP) as the communication protocol since TCP is connection-oriented, all synchronization operations need to be communicated through the exclusive connection established between each client and server, while UDP is connectionless, the information cannot be exchange between client and server.
IMPLEMENTATION DETAILS
The server application for this project is established by ServerSocket on specific port number 3002. After the server object is created, it will wait for the socket connection request and continually called accept() method to receive client socket application. At the same time, four arraylists will be created to store all client socket, its input, output stream and the sockets of online clients. After the server get connection request, it will obtain the input and output stream for this client socket object to send and receive message, add these objects into corresponding arraylists and create a thread to listen this client socket. 
 
To realized the synchronization between multiple clients, I decided to use server socket as the third part transfer station. The main idea is, based on the connection-oriented communication between one single client and server, the client will send its operation to the server, after the server read the information, it will send this information to all other online clients except the client who send current information by output in output stream arraylist, then the clients will read and run the same operation. For this reason, when a client socket is created, a thread will be established to listen information from server.
MESSAGE FORMATS 
Since the outputstream has the limitation that it can only transfer byte array, the data to be transferred will be process based on their type, and the receiver will get the original data with the same method. For the start of each transfer message, there will be a 4-bits string to show this what operation is. For example, “line” means one client draw a line on the board, “text” means one client insert a string on the board, “chat” means one client send message on chatting room, “exit” means someone leave the server and so on. To send a string, first the length of string will be take and transfer into byte with a getByte method, and turn it back through getInt method. 
This method can ensure the integrity of int by shift int step by step and store the lower eight bits into byte, for the reason that the length of an int is 4 bytes, when int is forcibly converted to byte, the data with 24 bits higher than int is possible to get loss. The int transform method is referred from an Anonymous on CSDN blog. then write the transformed int into output and write string into output. For the receiver, first create a byte array with length 4 to read the operation message, then create another byte array with length 4 to read the length of receiving string. After this, create a byte array of this length to read the string.
To send a shape, such as oval, first the position of this shape will be stored as a 4-bit int array[x position, y position, height, weight], then write the operation string “oval” and each element in int array into output. To send a color, write the operation string “colo” into output, then get and send the hash code of this color. To send a message in chatting room, send the length of this client name, client name, length of message and the message one by one. To update the online client list, each join or exit of a client will send operation “join” and “exit” separately, then on the server side, loop through the online client array list, take all the client name and write them into a single string, separated by “,”, then send the length of total client string and the string.
 
3.	GUI
There are five window applications. 
A WhiteBoardClient window for client to connect, the client needs to enter correct IP address and port number of whiteboard server, and they are required to provide a username to show identity to other clients. 

A WhiteBoard window, which provide a dashboard, this window would take most of the operations. It provides an interactive paint panel for users to draw in the center of the window, the content on the panel will be synchronized to other online users. On the top left, there is a tool panel and a color panel. Tool panel provides four shape painting tools including line, circle, oval and rectangle available for users to choose. Users can choose different colors from color panel, there are 16 colors in color panel can be chosen from. The chatting room is located on bottom left of the window which allows users to communicate with other online users by typing in text panel. The chatting panel will display the previous chat history in format of client name + message send time + message, the notification of new user joins in and some users exit from the server will also be shown on the chatting room. On the right, there is a client list, it shows the username of all online client and will be automatically updated when someone join or leave. The system also provides some file function on the bottom right, which includes image export and import, users can save the current whiteboard content at any time or import images into current whiteboard, and they can insert text on the whiteboard, the color of text can also be changed according to the brush color. All content of whiteboard, chat room and client list will be synchronized to all online clients.
 

A TextShape window(left) for clients to insert text into the painting panel, the clients would be asked to enter the text they want to insert, as well the position coordinate as (x position, y position)



An ImportFileName window(right) for user to enter the picture name they want to insert into paint board. A FileName window for user to enter the file name they want to save current paint board as. The system supports to open image or save file in PNG format.
4.	Message Dialog and Failure Model
For this project, TCP socket is applied as the communication mechanism. The tips on client side which can help clients to get operation information will be shown as a pop-up dialog window. These prompt windows include: 
	The warning message for client when they enter an invalid port or IP of the server or the server on input port has not been started yet.
	The warning message for client when they forget to enter an invalid port or IP of the server or forget to enter the username.
	The warning message for all online client when a new client joins in the server

	The warning message for all online client when a client exits the server

	The warning message for client when they tried to insert the text but forget to enter the text or the x and y positions which they want this text to be insert.

	The warning message for client when they tried to import an image from system but forget to enter the file name (left) and the image does not exist (right).

	The warning message for client when they tried to export current paint panel as an image but forget to enter the file name (left) and the image is successfully saved (right).


5.	Discussion
ADVANTAGES
1.	The system is easy to operate, the structure and functions are simple and clear.
2.	The system responds efficiently and quickly to the increasing client queries quickly.
3.	There are sufficient notifications will be shown in the form of pop dialogs, in order to guide the clients who has performed the improper operation, fully interact with clients.

WHAT CAN BE IMPROVED IN THE FRETURE
The data communication between client and server is very fragile. It is necessary to read and send data strictly according to the length, and the types of data that can be sent are very limited, such as HashMap and ArrayList. More consideration should be given to the design. Now it is hard for this system to be reuse since too many operations are carried by a single class. There should be more notification for some special circumstances. 
