package com.audioant.io.raspberry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.audioant.config.Parameters.Communication;
import com.audioant.config.Parameters.PythonResources;

public class LedController {

	static private BufferedWriter writer;
	static private Socket socket;

	public LedController() throws UnknownHostException, IOException {

		if (socket == null || writer == null) {

			// Starting the counter side socket
			Runtime.getRuntime().exec(PythonResources.EXECUTE + PythonResources.CONNECTION_PY);

			socket = new Socket(Communication.RASPBERRY_HOST_NAME, Communication.RASPBERRY_SOCKET_PORT);
			socket.setKeepAlive(true);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}
	}

	public void close() throws IOException {
		writer.close();
		socket.close();
	}

	public void on(LEDS led) throws IOException {
		set(led, true);
	}

	public void off(LEDS led) throws IOException {
		set(led, false);
	}

	private void set(LEDS led, boolean on) throws IOException {
		writer.write(led.toString() + Communication.VALUE_SEPERATOR + on);
		writer.flush();
	}
}