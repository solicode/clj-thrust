package clj_thrust;

public class RpcEvent {
	private String json;

	public RpcEvent(String json) {
		this.json = json;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
