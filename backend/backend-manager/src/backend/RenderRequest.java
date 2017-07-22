package backend;

public class RenderRequest {

	public String file;
	public String args;
	public String target;
	public String rendering;
	public String isFinished;

	public RenderRequest(String file, String args, String target, String rendering, String isFinished) {
		this.file = file;
		this.args = args;
		this.target = target;
		this.rendering = rendering;
		this.isFinished = isFinished;

	}

}
