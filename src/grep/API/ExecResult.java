package grep.API;

import com.google.gson.Gson;

public class ExecResult {
	String status = "OK";
	// String fileName;
	String cmd;
	String msg = "not implemented";

	public ExecResult(String _cmd) {
		cmd = _cmd;
	}

	public void setMsg(String _msg) {
		msg = _msg;
	}

	public String toJason() {
		Gson jsonObj = new Gson();
		return jsonObj.toJson(this);
	}
}
