package com.audioant.io.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.audioant.config.Config;

public class AndroidConnection extends Observable {

	private ServerSocket serverSocket;

	private List<Socket> sockets;

	private static AndroidConnection androidConnection;

	public AndroidConnection() throws IOException {

		sockets = new ArrayList<Socket>();

		serverSocket = new ServerSocket(Config.ANDROID_PORT);

		Runnable waitForConnections = () -> {
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					socket.setKeepAlive(true);
					sockets.add(socket);

					new AndroidInputListener(new BufferedReader(new InputStreamReader(socket.getInputStream())));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		Thread waitThread = new Thread(waitForConnections);
		waitThread.start();

	}

	private void writeText(String text) throws IOException {
		for (int i = 0; i < sockets.size(); i++) {

			try {
				if (!sockets.get(i).isConnected() || sockets.get(i).isClosed() || !sockets.get(i).isBound()) {
					throw new SocketException();
				}

				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sockets.get(i).getOutputStream()));
				writer.write(text);
				writer.flush();

			} catch (SocketException e) {
				sockets.remove(i);
			}
		}
	}

	public static void write(String text) {
		if (androidConnection == null) {
			open();
		}
		try {
			androidConnection.writeText(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void open() {
		if (androidConnection == null) {
			try {
				androidConnection = new AndroidConnection();
				androidConnection.addObserver(AndroidListener.getInstance());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private class AndroidInputListener {

		private AndroidInputListener(BufferedReader reader) throws IOException {

			Thread thread = new Thread(() -> {

				String inputLine;

				try {
					while ((inputLine = reader.readLine()) != null) {
						setChanged();
						notifyObservers(inputLine);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			});
			thread.start();
		}
	}
}
