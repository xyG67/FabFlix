package cs122B.FabFlix.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SetSortAndPageSession
 */
@WebServlet("/SetSortAndPageSession")
public class SetSortAndPageSession extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetSortAndPageSession() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet: SetSortAndPageSession");
		response.setContentType("text/plain");
		
		String showList = request.getParameter("showList");
		
		if(showList.equals("1")) {
			String range = (String) request.getSession().getAttribute("range");
			if((Integer.parseInt(range)-1)>0) {
				int newrange = Integer.parseInt(range)-1;
				request.getSession().setAttribute("range", String.valueOf(newrange));
			}
		}else if(showList.equals("2")){
			String range = (String) request.getSession().getAttribute("range");
			int newrange = Integer.parseInt(range)+1;
			request.getSession().setAttribute("range", String.valueOf(newrange));
		}else if(showList.equals("3")){
			//change range 'newPageNum'
			String newPageNum = request.getParameter("newPageNum");
			System.out.println("how many number in one page: "+ newPageNum);
			request.getSession().setAttribute("page", newPageNum);
		}else if(showList.equals("4")){
			//sort by title
			request.getSession().setAttribute("sort", "title");
		}else if(showList.equals("5")) {
			//sort by genre
			request.getSession().setAttribute("sort", "year");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
