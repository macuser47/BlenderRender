package backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws InterruptedException, IOException {

		boolean RunServerManager = true;

		while (RunServerManager) {

			// check NodeList.json for new nodes
			// check for Node Status example; failed gpu's or handing render
			Map<String, String> IPGPU = ParseNodesjson.ParseNodesjson();

			// Check RenderQueue.json for new entries
			ArrayList<RenderRequest> Queue = CheckRenderQueue.CheckQueue();

			// determine gpu plan to render with
			// determine what gpu's status
			// add file to respective gpu queue
			// run .py script for respective gpu
			AllocateGpu.AllocateGpu(Queue);

			Thread.sleep(5000);
		}

	}

}
