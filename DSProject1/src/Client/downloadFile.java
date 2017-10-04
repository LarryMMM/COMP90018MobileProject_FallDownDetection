package Client;
/*The EZshare client package implements an application that
* client end can use command line to communicate with server.
*
* @author Duan Lian, Sisi Li, Chuanxi Fu, Jianqiang Zhang
* @version 2.9
* @since   2014-04-25
*/

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

import org.json.JSONObject;
//download file from server
public class downloadFile {
	public static void run(String message, DataInputStream input) {
		try {
			JSONObject command = new JSONObject(message);
			if (command.has("resourceSize")) {
				FileOutputStream out = null;
				String fileName = "downloadFile/" + "defalut";
				if (!command.get("name").equals("")) {
					fileName = "downloadFile/" + command.get("name");
				}
				BufferedInputStream in = new BufferedInputStream(input);
				out = new FileOutputStream(fileName);
				long fileSizeRemaining = command.getLong("resourceSize");
				int chunkSize = setChunkSize(fileSizeRemaining);
				byte[] receiveBuffer = new byte[chunkSize];
				int num;
				System.out.println("Downloading " + fileName + " of size " + fileSizeRemaining);
				Client.LOGGER.log(Level.FINE, "Downloading: " + fileName + " of size " + fileSizeRemaining);

				while ((num = in.read(receiveBuffer)) > 0) {
					out.write(receiveBuffer, 0, num);
					fileSizeRemaining -= num;
					chunkSize = setChunkSize(fileSizeRemaining);
					receiveBuffer = new byte[chunkSize];

					if (fileSizeRemaining == 0) {
						System.out.println("File received!");
						break;
					} else {
						System.out.println("Downloading " + fileName + " of size " + fileSizeRemaining);
					}
				}

				out.close();
			}
		} catch (IOException e) {
			System.out.println("Download failed,please check connection");
		}

	}

	// Determine the chunkSize
	public static int setChunkSize(long fileSizeRemaining) {
		int chunkSize = 1024 * 1024;
		// If the file size remaining is less than the chunk size
		// then set the chunk size to be equal to the file size.
		if (fileSizeRemaining < chunkSize) {
			chunkSize = (int) fileSizeRemaining;
		}

		return chunkSize;
	}
}
