package io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import config.Parameters.Communication;

public class LedController {

	private BufferedWriter writer;
	private Socket socket;
	private ServerSocket serverSocket;

	public LedController() throws UnknownHostException, IOException {

		serverSocket = new ServerSocket(Communication.RASPBERRY_SOCKET_PORT);

		socket = serverSocket.accept();

		socket.setKeepAlive(true);

		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void close() throws IOException {
		writer.close();
		socket.close();
		serverSocket.close();
	}

	public void on(LEDS led) throws IOException {
		set(led, true);
	}

	public void off(LEDS led) throws IOException {
		set(led, false);
	}

	private void set(LEDS led, boolean on) throws IOException {
		writer.write(led.toString() + ';' + on);
		writer.flush();
	}
}