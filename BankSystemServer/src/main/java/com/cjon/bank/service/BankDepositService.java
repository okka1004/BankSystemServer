package com.cjon.bank.service;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;

public class BankDepositService implements BankService {

	@Override
	public void execute(Model model) {
		
		HttpServletRequest request=(HttpServletRequest) model.asMap().get("request");
		String memberId=request.getParameter("memberId");
		String memberBalance=request.getParameter("memberBalance");
		DataSource dataSource=(DataSource) model.asMap().get("dataSource");
		Connection con=null;
		
		try{
			con=dataSource.getConnection();
			con.setAutoCommit(false);
			
			BankDAO dao=new BankDAO(con);
			
			boolean result = dao.updateDeposit(memberId, memberBalance, false);
			
			if(result){
				con.commit();

			}else{
				con.rollback();
			}
			model.addAttribute("RESULT", result);
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {
				con.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

		
	}

}
