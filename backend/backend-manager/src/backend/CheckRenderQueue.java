package backend;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class CheckRenderQueue {

	public static ArrayList<RenderRequest> CheckQueue() throws FileNotFoundException {
		String outString = JsonToString.JsonToString("F:\\RenderStorage\\RenderQueue.json");
		JSONArray array = (new JSONObject(outString)).getJSONArray("Queue");

		ArrayList<RenderRequest> QueueList = new ArrayList<RenderRequest>();

		for (int i = 0; i < array.length(); i++) {
			String filename = array.getJSONObject(i).getString("file");
			RenderRequest Queue = new RenderRequest(array.getJSONObject(i).getString("file"),
					array.getJSONObject(i).getString("args"), array.getJSONObject(i).getString("target"),
					String.valueOf(array.getJSONObject(i).getBoolean("rendering")),
					String.valueOf(array.getJSONObject(i).getBoolean("isFinished")));

			QueueList.add(Queue);
		}
		return QueueList;
	}
}
