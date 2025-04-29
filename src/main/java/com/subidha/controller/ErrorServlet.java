package com.subidha.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ErrorServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // forward the request to the login JSP page to display the form
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/error.jsp");
	    dispatcher.forward(request, response);
	}
}
