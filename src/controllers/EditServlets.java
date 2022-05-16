package controllers;

import java.io.IOException;

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
 * Servlet implementation class EditServlets
 */
@WebServlet("/edit")
public class EditServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlets() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManager em = DBUtil.createEntityManager();

			//대상아이디의메세지 1건만 데이터베이스로부터 습득
		Message m = em.find(Message.class, Integer.parseInt(request.getParameter("id")));

		em.close();

		//메세지정보와 세션아이디를 리퀘스트스콥에 등록
		request.setAttribute("message", m);
		request.setAttribute("_token", request.getSession().getId());

		//메세지아이디를 세션스코프에 등록
		if(m != null) {
		request.getSession().setAttribute("message_id", m.getId());
		}

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/edit.jsp");
		rd.forward(request, response);

	}
}
