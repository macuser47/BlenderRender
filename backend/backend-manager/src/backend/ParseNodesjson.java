package backend;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.*;

public class ParseNodesjson {

	static Map<String, String> ParseNodesjson() throws FileNotFoundException  {

		String outString = JsonToString.JsonToString("NodeList.json");

		// Dumps the Json array "nodelist" into the JSONArray "array"
		JSONArray array = (new JSONObject(outString)).getJSONArray("NodeList");

		// creates a JSONObject array element for each object in the Nodelist
		// JSONArray
		
		//dont do this
		JSONObject[] Objects = new JSONObject[array.length()];

		for (int i = 0; i < Objects.length; i++) {

			Objects[i] = array.getJSONObject(i);

		}

		Map<String, String> IPGPU = new HashMap<String, String>();
		//String[] nodeRoot = new String[Objects.length];

		for (int i = 0; i < Objects.length; i++) {

			// Associates gpu with an IP address
			for(int j = 0; j <Objects[i].getJSONArray("gpus").length(); j++){
				IPGPU.put(Objects[i].getJSONArray("gpus").getJSONObject(j).getString("name"), Objects[i].getString("ip"));
			}
			
			

		}
		System.out.println(IPGPU);
		return IPGPU;
	}
}
