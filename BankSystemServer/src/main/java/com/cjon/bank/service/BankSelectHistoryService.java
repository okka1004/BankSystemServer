package com.cjon.bank.service;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;

public class BankSelectHistoryService implements BankService {

	@Override
	public void execute(Model model) {
		
		HttpServletRequest request=(HttpServletRequest) model.asMap().get("request");
		String memberId=request.getParameter("memberId");
		Connection con=null;
		DataSource dataSource=(DataSource) model.asMap().get("dataSource");
		
		
		try{
			con=dataSource.getConnection();
			con.setAutoCommit(false);
			
			BankDAO dao=new BankDAO(con);
			ArrayList<BankDTO> list = dao.selectHistory(memberId);
			ArrayList<BankDTO> list2 = dao.selectTransferHistory(memberId);
			
			
			
			ArrayList<BankDTO> myList= new ArrayList<BankDTO>();
			myList.addAll(list);
			myList.addAll(list2);
			
			for(BankDTO temp: list2){
	            System.out.println("??"+temp.getKind());
	        }
			
			if(list !=null){
				con.commit();
			}else{
				con.rollback();
			}
			model.addAttribute("RESULT", myList);

			
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
