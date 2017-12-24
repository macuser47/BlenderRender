package renderfarm.code;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorLogger {

	public static void logStack(Object error, String errorType) {
		PrintWriter writer = null;
		try {
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

			writer = new PrintWriter(timeStamp + "_" + errorType + ".txt", "UTF-8");

			writer.println(error);

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		writer.close();
	}

	public static void logString(String error, String errorType) {
		PrintWriter writer = null;
		try {
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

			writer = new PrintWriter(timeStamp + "_" + errorType + ".txt", "UTF-8");

			writer.println(error);

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		writer.close();
	}
}
