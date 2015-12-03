package finalProject.main;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;

@WebServlet("/transcoder")
@MultipartConfig
public class TranscodeAPI extends HttpServlet{
	private static final long serialVersionUID = -3760409529344238423L;

	public TranscodeAPI() {
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response){
		//Resource: https://stackoverflow.com/a/2424824
		//Get form content
		String outputType = request.getParameter("output");
	}
}
