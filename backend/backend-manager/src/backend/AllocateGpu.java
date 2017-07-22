package backend;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

public class AllocateGpu {

	public static String AllocateGpu(ArrayList<RenderRequest> queue) throws IOException {
		String outString = JsonToString.JsonToString("F:\\RenderStorage\\GpuTasks.json");
		JSONObject GpuList = new JSONObject(outString);

		String Task = null;

		System.out.println(queue.size());
		if (queue.size() != 0) {

			// Check gpu Rendering State. if not rendering, Mine
			for (int i = 0; i < queue.size(); i++) {
				

				System.out.println(GpuList.getJSONObject(queue.get(i).target).get("task").equals("rendering"));
				System.out.println((queue.get(i).isFinished).equals("true"));
				if (GpuList.getJSONObject(queue.get(i).target).get("task").equals("rendering") && (queue.get(i).isFinished).equals("true")) {
					
					//ToDo send email with download link
					
					
					//This stuff be broke
					queue.remove((queue.get(i).target));
					System.out.println("delete THIS "+(queue.get(i).target));
					
						
					GpuList.getJSONObject((queue.get(i).target)).put("task", "rendering");
					GpuList.getJSONObject((queue.get(i).target)).put("args", (queue.get(i).args));
					GpuList.getJSONObject((queue.get(i).target)).put("target", (queue.get(i).file));
					
					System.out.println((queue.get(i).target + " is now rendering"));
					
					
				} else if (GpuList.getJSONObject(queue.get(i).target).get("task").equals("mining")) {

					GpuList.getJSONObject((queue.get(i).target)).put("task", "mining");

				}
			}

		} else {
			// Set all gpu's to Mine if queue empty
			for (int i = 0; i < GpuList.length(); i++) {
				if ((GpuList.getJSONObject(queue.get(i).target).get("isFinished").equals("true"))) {
					GpuList.put((queue.get(i).target), "mining");
					System.out.println((queue.get(i).target + " is now mining"));
				}
			}
		}
		try (FileWriter file = new FileWriter("F:\\RenderStorage\\GpuTasks.json")) {
			file.write(GpuList.toString());
			//System.out.println("Successfully Copied JSON Object to File...");
		
		}

		
		Task = "mining";
		return Task;

	}

}
