package backend;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class JsonToString {

	public static String JsonToString(String fileToRead) throws FileNotFoundException {
		Scanner NodeJson = new Scanner(new FileReader(fileToRead));

		StringBuilder sb = new StringBuilder();
		while (NodeJson.hasNext()) {
			sb.append(NodeJson.next());
		}
		NodeJson.close();
		String outString = sb.toString();
		return outString;
	}

}
