package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String _token = request.getParameter("_token");
		if(_token != null  && _token.equals(request.getSession().getId())) {
			EntityManager em = DBUtil.createEntityManager();

			//세션스코프에서 메서지의 아이디를수집
			//대상아이디의 메세지 1건만을 데이터베이스로부터 수집
			Message m = em.find(Message.class, (Integer)(request.getSession().getAttribute("message_id")));

			//폼의내용을 각필드에 덮어쓰기
			String title = request.getParameter("title");
			m.setTitle(title);

			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			m.setUpdated_at(currentTime);

			//데이터베이스를갱신
			em.getTransaction().begin();
			em.getTransaction().commit();
			em.close();

			//세션스코프상의 불필요한 데이터를 삭제
			request.getSession().removeAttribute("message_id");

			//index페이지에리다이렉트
			response.sendRedirect(request.getContextPath()+"/index");


		}
	}

}
