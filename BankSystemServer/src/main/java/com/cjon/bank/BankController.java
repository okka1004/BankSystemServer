package com.cjon.bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cjon.bank.dto.BankDTO;
import com.cjon.bank.service.BankDepositService;
import com.cjon.bank.service.BankSelectAllMemberService;
import com.cjon.bank.service.BankSelectHistoryService;
import com.cjon.bank.service.BankSelectMemberService;
import com.cjon.bank.service.BankService;
import com.cjon.bank.service.BankTransferService;
import com.cjon.bank.service.BankWithdrawService;

@Controller
public class BankController {

	private DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	private BankService service;

	@RequestMapping(value = "/selectAllMember")
	public void selectAllMember(HttpServletRequest request, HttpServletResponse response, Model model) {

		// 입력처리
		String callback = request.getParameter("callback");

		// 로직처리
		service = new BankSelectAllMemberService();
		model.addAttribute("dataSource", dataSource);
		service.execute(model);

		// 결과처리
		// model에서 결과를 꺼내면 됨
		ArrayList<BankDTO> list = (ArrayList<BankDTO>) model.asMap().get("RESULT"); // model을
																					// map형태로
																					// 바꿈

		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);
			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/selectMember")
	public void selectMember(HttpServletRequest request, HttpServletResponse response, Model model) {

		// 입력처리
		String callback = request.getParameter("callback");

		// 로직처리
		service = new BankSelectMemberService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);

		// 결과처리
		// model에서 결과를 꺼내면 됨
		ArrayList<BankDTO> list = (ArrayList<BankDTO>) model.asMap().get("RESULT"); // model을
																					// map형태로
																					// 바꿈

		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);
			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/deposit")
	public void deposit(HttpServletRequest request, HttpServletResponse response, Model model) {
		String callback = request.getParameter("callback");

		// 서비스 객체를 생성해서 로직처리
		service = new BankDepositService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);

		boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(callback + "(" + result + ")");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/withdraw")
	public void withdraw(HttpServletRequest request, HttpServletResponse response, Model model) {
		String callback = request.getParameter("callback");

		// 서비스 객체를 생성해서 로직처리
		service = new BankWithdrawService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);

		boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(callback + "(" + result + ")");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/transfer")
	public void transfer(HttpServletRequest request, HttpServletResponse response, Model model) {
		String callback = request.getParameter("callback");

		// 서비스 객체를 생성해서 로직처리
		service = new BankTransferService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);

		boolean result = (Boolean) model.asMap().get("RESULT");
		response.setContentType("text/plain; charset=utf8");
		PrintWriter out;
		try {
			out = response.getWriter();
			out.println(callback + "(" + result + ")");
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/selectHistory")
	public void selectHistory(HttpServletRequest request, HttpServletResponse response, Model model) {
		// 입력처리
		String callback = request.getParameter("callback");

		// 로직처리
		service = new BankSelectHistoryService();
		model.addAttribute("dataSource", dataSource);
		model.addAttribute("request", request);
		service.execute(model);

		// 결과처리
		// model에서 결과를 꺼내면 됨
		ArrayList<BankDTO> list = (ArrayList<BankDTO>) model.asMap().get("RESULT"); 

		String json = null;
		ObjectMapper om = new ObjectMapper();
		try {
			json = om.defaultPrettyPrintingWriter().writeValueAsString(list);
			response.setContentType("text/plain; charset=utf8");
			response.getWriter().println(callback + "(" + json + ")");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
