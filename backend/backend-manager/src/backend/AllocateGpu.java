package backend;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONObject;

public class AllocateGpu {

	public static String AllocateGpu(ArrayList<RenderRequest> queue) throws FileNotFoundException {
		String outString = JsonToString.JsonToString("GpuTasks.json");
		JSONObject GpuList = new JSONObject(outString);

		String Task = null;

		if (queue.size() != 0) {

			// Check gpu Rendering State. if not rendering, Mine
			for (int i = 0; i < queue.size(); i++) {
				GpuList.getJSONObject(queue.get(i).target);

				if (GpuList.getJSONObject(queue.get(i).target).get("task").equals("rendering")) {
					Task = "Gpu Rendering";

					System.out.println(Task +" "+ (queue.get(i).target));
				} else if (GpuList.getJSONObject(queue.get(i).target).get("task").equals("mining")) {
					Task = "Starting Render";

					System.out.println(Task +" "+ (queue.get(i).target));
					GpuList.put((queue.get(i).target), "rendering");
					
				} else if (GpuList.getJSONObject(queue.get(i).target).get("isFinished").equals("true")) {
					Task = "mining";
					GpuList.put((queue.get(i).target), "mining");
					System.out.println(Task +" "+ (queue.get(i).target));
				}
				

			}

		} else {
			// Set all gpu's to Mine if queue empty
			for (int i = 0; i < GpuList.length(); i++) {
				if ((GpuList.getJSONObject(queue.get(i).target).get("isFinished").equals("true"))
						&& !(GpuList.getJSONObject(queue.get(i).target).equals("750ti"))) {
					GpuList.put((queue.get(i).target), "mining");
				}
			}
		}

		
		return Task;

	}

}
