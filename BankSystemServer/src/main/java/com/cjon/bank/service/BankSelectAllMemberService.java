package com.cjon.bank.service;

import java.sql.Connection;
import java.util.ArrayList;

import javax.sql.DataSource;

import org.springframework.ui.Model;

import com.cjon.bank.dao.BankDAO;
import com.cjon.bank.dto.BankDTO;

public class BankSelectAllMemberService implements BankService {

	@Override
	public void execute(Model model){
		//실질적인 로직 구현
		

			DataSource dataSource= (DataSource) model.asMap().get("dataSource");
					Connection con;
					try {
						con=dataSource.getConnection();
						con.setAutoCommit(false);
						BankDAO dao=new BankDAO(con);
						ArrayList<BankDTO> list=dao.selectAll();
						if(list !=null){
							con.commit();
						}else{
							con.rollback();
						}
						model.addAttribute("RESULT", list);
						con.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
							 

		
	}
}
