package com.audioant.io.raspberry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.audioant.config.Parameters.Communication;
import com.audioant.config.Parameters.PythonResources;

public class RaspberryConnection {

	private static Socket socket;
	private static BufferedWriter writer;
	private static BufferedReader reader;

	private static Socket getSocket() throws IOException {

		if (socket == null) {

			// Starting the counter side socket
			Runtime.getRuntime().exec(PythonResources.EXECUTE + PythonResources.CONNECTION_PY);

			socket = new Socket(Communication.RASPBERRY_HOST_NAME, Communication.RASPBERRY_SOCKET_PORT);
			socket.setKeepAlive(true);
		}
		return socket;
	}

	public static BufferedWriter getWriter() throws IOException {
		if (writer == null) {
			writer = new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream()));
		}
		return writer;
	}

	public static BufferedReader getReader() throws IOException {
		if (reader == null) {
			reader = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
		}
		return reader;
	}

}