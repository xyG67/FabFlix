package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchByGenres
 */
@WebServlet("/SearchByGenres")
public class SearchByGenres extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchByGenres() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet: SearchByGenres");
		response.setContentType("text/plain");
		String genre = request.getParameter("genre");
		System.out.println("trans data: "+ genre);
		String[] searching = new String[2];
		searching[0] = "genre";
		searching[1] = genre;
		request.getSession().setAttribute("searching", searching);
		PrintWriter out = response.getWriter();
		out.print("Hello"+ genre);
	}

}
