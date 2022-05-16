package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManager em = DBUtil.createEntityManager();

		//여는 페이지수를 습득(디폴트는 1페지)
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
		} catch(NumberFormatException e) {}

		//최대건수와 시작위치를 지정해서 메세지를 습득
		List<Message> messages = em.createNamedQuery("getAllMessages", Message.class)
						.setFirstResult(15* (page - 1))
						.setMaxResults(15)
						.getResultList();

		//전건수를 습득
		long messages_count = (long)em.createNamedQuery("getMessagesCount", Long.class)
				.getSingleResult();

		em.close();

		request.setAttribute("message", messages);
		request.setAttribute("messages_count", messages_count);
		request.setAttribute("page", page);

		if(request.getSession().getAttribute("flush") != null) {
			request.setAttribute("flush", request.getSession().getAttribute("flush"));
			request.getSession().removeAttribute("flush");
		}

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/index.jsp");
		rd.forward(request, response);
		/*response.getWriter().append(Integer.valueOf(messages.size()).toString());
		//http://localhost:8080/message_board/index 실행했을때 반응하는 코드↑
		em.close();*/
	}

}
