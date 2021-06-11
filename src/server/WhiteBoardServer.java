package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class WhiteBoardServer {
	
	private static int port = 3002;
	
	private static ArrayList<Socket> clientSockets = new ArrayList<>();
	private static ArrayList<InputStream> inputs = new ArrayList<>();
	private static ArrayList<OutputStream> outputs = new ArrayList<>();
	private static ArrayList<String> onlineClients = new ArrayList<>();
	
	public static void main(String[] args)
	{

		try(ServerSocket server = new ServerSocket(port)){
			System.out.println("Waiting for client connection..");
			
			// Wait for connections.
			while(true){
				Socket client = server.accept();

				clientSockets.add(client);
				
				InputStream input = client.getInputStream();
				inputs.add(input);
				OutputStream output = client.getOutputStream();
				outputs.add(output);
				
				int countClient = clientSockets.size()-1;
				System.out.println(countClient);
				
				ClientConnect c = new ClientConnect(countClient, input, outputs);
				c.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static class ClientConnect extends Thread{
		private int clientsocket;
		private InputStream input;
		private ArrayList<OutputStream> outputs;
		
		
		public ClientConnect(int clientsocket, InputStream input, ArrayList<OutputStream> outputs) {
			this.clientsocket = clientsocket;
			this.input = input;
			this.outputs = outputs;
		}
		
		public void run() {
			while(true) {
				try {
					byte[] OP = new byte[4];
					input.read(OP);
					String msg = new String(OP);
					
					switch(new String(OP)){
					case"line":
						System.out.println(msg);
						upDatePaintBoard("line");
						break;
					case"circ":
						System.out.println(msg);
						upDatePaintBoard("circ");
						break;
					case"oval":
						System.out.println(msg);
						upDatePaintBoard("oval");
						break;
					case"rect":
						System.out.println(msg);
						upDatePaintBoard("rect");
						break;
					case"penc":
						upDatePaintBoard("penc");
						break;
					case"colo":
						changeColor("colo");
						break;
					case"chat":
						chat("chat");
						break;
						
					case"clea":
						cleanPaintBoard("clea");
						break;
						
					case"exit":
						clientExit("exit");
						break;
					case"join":
						clientJoin("join");
						break;
						
					case"text":
						insertText("text");
						break;
//					default:
//						System.out.println(msg);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void cleanPaintBoard(String OP) {
			byte[] msg = OP.getBytes();
			for( int i = 0; i< outputs.size(); i++) {
				if(i == this.clientsocket) {
					continue;
				}
				try {
					outputs.get(i).write(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		private void upDatePaintBoard(String OP) {
			try {
				int curSize = outputs.size();
				byte[] msg = OP.getBytes();
				
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(msg);
				}
				
				for (int i = 0; i < 4; i++) {
					byte[] curByte = new byte[4];
					input.read(curByte);
					for (int j = 0; j < curSize; j++) {
						if(j == this.clientsocket) {
							continue;
						}
						outputs.get(j).write(curByte);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		//TODO: chat
		private void chat(String OP) {
			try {
				byte[] operation = OP.getBytes();
				int curSize = outputs.size();
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(operation);
				}
				
				// get and send cliend name who send the message
				byte[] nameLen = new byte[4];
				input.read(nameLen);
				int nameLenInt = getInt(nameLen);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(nameLen);
				}
				
				byte[] name = new byte[nameLenInt];
				input.read(name);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(name);
				}
				
				// get and send the message
				byte[] messageLen = new byte[4];
				input.read(messageLen);
				int messageLenInt = getInt(messageLen);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(messageLen);
				}
				
				byte[] message = new byte[messageLenInt];
				input.read(message);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(message);
				}
				System.out.println( new String(name) + "said: " + new String(message));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		public int getInt(byte[]bt){
			int number=(bt[3]& 0xff)<<24|
					(bt[2]& 0xff)<<16|
					(bt[1]& 0xff)<<8|
					(bt[0]& 0xff)<<0;
			return number;
		}
		
		//TODO: change color
		private void changeColor(String OP) {
			try {
				byte[]msg = OP.getBytes();
				for(int i = 0;i < outputs.size(); i++){
					if(i == this.clientsocket){
						continue;
					}
					outputs.get(i).write(msg);
				}
				
				byte[]color = new byte[4];
				input.read(color);

				for(int i = 0;i < outputs.size(); i++){
					if(i==this.clientsocket){
						continue;
					}
					outputs.get(i).write(color);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private void clientExit(String OP) {
			try {

				byte[] operation = OP.getBytes();
				int curSize = outputs.size();
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						outputs.get(i).write(operation);
					}
				}
				
				byte[] nameLen = new byte[4];
				input.read(nameLen);
				int nameLenInt = getInt(nameLen);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						outputs.get(i).write(nameLen);
					}
				}
				
				byte[] name = new byte[nameLenInt];
				input.read(name);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						outputs.get(i).write(name);
					}
				}
				
				String exitClient = new String(name);
				onlineClients.remove(exitClient);
				
				String totalClient = "";
				
				for(String curClient: onlineClients) {
					totalClient += curClient + ",";
				}
				
				tellClientExit(totalClient, exitClient);
				
				clientSockets.remove(this.clientsocket);
				outputs.remove(this.clientsocket);
				inputs.remove(this.clientsocket);
				System.out.println(exitClient + " exit!" );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		private void tellClientExit(String totalClient, String exitClient) {
			try {
				byte[] operation = "sbex".getBytes();
				int curSize = outputs.size();
				for( int i = 0; i< curSize; i++) {
					if(i==this.clientsocket){
						continue;
					}
					outputs.get(i).write(operation);

				}
	
				int numClient = onlineClients.size();
				int nameLenTotal = totalClient.getBytes().length;
				int exitClientLen = exitClient.getBytes().length;
				
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(getByte(numClient));
					outputs.get(i).write(getByte(nameLenTotal));
					outputs.get(i).write(totalClient.getBytes());
					outputs.get(i).write(getByte(exitClientLen));
					outputs.get(i).write(exitClient.getBytes());
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		private void clientJoin(String OP) {
			try {
				byte[] operation = OP.getBytes();
				int curSize = outputs.size();
				for( int i = 0; i< curSize; i++) {
					outputs.get(i).write(operation);
				}
				
				byte[] nameLen = new byte[4];
				input.read(nameLen);
				int nameLenInt = getInt(nameLen);
				
				byte[] name = new byte[nameLenInt];
				input.read(name);
				String joinName = new String(name);

				String clientName = new String(name);
				onlineClients.add(clientName);
				String totalClient = "";
				
				for(String curClient: onlineClients) {
					totalClient += curClient + ",";
				}
				
				int numClient = onlineClients.size();
				int nameLenTotal = totalClient.getBytes().length;

				
				for( int i = 0; i< curSize; i++) {
					outputs.get(i).write(getByte(numClient));
					outputs.get(i).write(getByte(nameLenTotal));
					outputs.get(i).write(totalClient.getBytes());
					outputs.get(i).write(getByte(nameLenInt));
					outputs.get(i).write(joinName.getBytes());
				}
				
				System.out.println(clientName + " join!" );
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		private void insertText(String OP) {
			try {
				byte[] operation = OP.getBytes();
				int curSize = outputs.size();
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(operation);
				}
				
				
				byte[] txtLen = new byte[4];
				input.read(txtLen);
				int txtLenInt = getInt(txtLen);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(txtLen);
				}
				
				byte[] txt = new byte[txtLenInt];
				input.read(txt);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(txt);
				}
				
				// get and send the message
				byte[] xLen = new byte[4];
				input.read(xLen);
				int xLenInt = getInt(xLen);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(xLen);
				}
				
				byte[] yLen = new byte[4];
				input.read(yLen);
				int yLenInt = getInt(yLen);
				for( int i = 0; i< curSize; i++) {
					if(i == this.clientsocket) {
						continue;
					}
					outputs.get(i).write(yLen);
				}
				

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		public byte[] getByte(int number){
			byte[]bt=new byte[4];
			bt[0]=(byte) ((number>>0) & 0xff);
			bt[1]=(byte) ((number>>8) & 0xff);
			bt[2]=(byte) ((number>>16) & 0xff);
			bt[3]=(byte) ((number>>24) & 0xff);
			return bt;
		}
		

		
	}
}
