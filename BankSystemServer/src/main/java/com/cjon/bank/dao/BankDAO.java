package com.cjon.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.cjon.bank.dto.BankDTO;

public class BankDAO {

	private Connection con;

	public BankDAO(Connection con) {
		this.con = con;
	}

	public ArrayList<BankDTO> selectAll() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BankDTO> list = new ArrayList<BankDTO>();

		try {

			String sql = "select * from bank_member_tb";
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setMemberName(rs.getString("member_name"));
				dto.setMemberAccount(rs.getString("member_account"));
				dto.setMemberBalance(rs.getInt("member_balance"));
				list.add(dto);
			}

		} catch (Exception e) {
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public boolean updateDeposit(String memberId, String memberBalance, boolean transfer) {

		PreparedStatement pstmt = null;
		boolean result = false;
		boolean total_result = false;
		try {

			String sql = "update bank_member_tb set member_balance=member_balance+? where member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(memberBalance));
			pstmt.setString(2, memberId);

			int count = pstmt.executeUpdate();
			if (count == 1) {
				result = true;
				// TODO
				if (!transfer) {
					total_result = saveHistory(memberId, "deposit", memberBalance);
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (transfer) {
			return result;
		} else {
			return total_result;
		}
	}

	public boolean updateWithdraw(String memberId, String memberBalance, boolean transfer) {

		PreparedStatement pstmt = null;
		boolean result = false;
		boolean total_result = false;
		BankDTO dto = new BankDTO();

		try {

			String sql = "update bank_member_tb set member_balance=member_balance-? where member_id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(memberBalance));
			pstmt.setString(2, memberId);

			int count = pstmt.executeUpdate();
			if (count == 1) {
				
				String sql1 = "select member_id, member_balance from bank_member_tb where member_id=?";
				PreparedStatement pstmt1 = con.prepareStatement(sql1);
				pstmt1.setString(1, memberId);
				ResultSet rs = pstmt1.executeQuery();
				if (rs.next()) {
					dto.setMemberBalance(rs.getInt("member_balance"));
				}
				if (dto.getMemberBalance() < 0) {
					result=false;
					con.rollback();
				} else {
					con.commit();
					result = true;
				}
				
				if (!transfer) {
					total_result = saveHistory(memberId, "withdraw", memberBalance);
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (transfer) {
			return result;
		} else {
			return total_result;
		}
	}

	public ArrayList<BankDTO> selectMember(String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BankDTO> list = new ArrayList<BankDTO>();

		try {

			String sql = "select * from bank_member_tb where MEMBER_ID=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			System.out.println(memberId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setMemberName(rs.getString("member_name"));
				dto.setMemberAccount(rs.getString("member_account"));
				dto.setMemberBalance(rs.getInt("member_balance"));
				list.add(dto);
			}

		} catch (Exception e) {
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public boolean saveHistory(String memberId, String kind, String balance) {

		PreparedStatement pstmt = null;
		boolean result = false;
		try {

			String sql = "insert into bank_statement_tb(MEMBER_ID, KIND, MONEY, date) values (?, ?, ?, sysdate())";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			pstmt.setString(2, kind);
			pstmt.setInt(3, Integer.parseInt(balance));

			int count = pstmt.executeUpdate();
			if (count == 1) {
				result = true;
			}
		} catch (Exception e) {
		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	public boolean updateTransfer(String sendId, String receiveId, String memberBalance) {

		boolean result1 = false;
		boolean result2 = false;
		boolean total_result = false;
		boolean save_result = false;

		result1 = updateWithdraw(sendId, memberBalance, true);
		result2 = updateDeposit(receiveId, memberBalance, true);

		save_result = saveTransferHistory(sendId, receiveId, memberBalance);

		if (result1 && result2 && save_result) {
			total_result = true;
		}

		return total_result;
	}

	private boolean saveTransferHistory(String sendId, String receiveId, String memberBalance) {
		PreparedStatement pstmt = null;
		boolean result = false;
		try {

			String sql = "insert into bank_transfer_history_tb(SEND_MEMBER_ID, RECEIVE_MEMBER_ID, TRANSFER_MONEY, date) values (?, ?, ?, sysdate())";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, sendId);
			pstmt.setString(2, receiveId);
			pstmt.setInt(3, Integer.parseInt(memberBalance));

			int count = pstmt.executeUpdate();
			if (count == 1) {
				result = true;
			}
		} catch (Exception e) {

		} finally {
			try {
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;

	}

	public ArrayList<BankDTO> selectHistory(String memberId) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BankDTO> list = new ArrayList<BankDTO>();

		try {
			String sql = "select * from bank_statement_tb where MEMBER_ID=? order by date DESC";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			System.out.println(memberId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("member_id"));
				dto.setKind(rs.getString("kind"));
				dto.setMemberBalance(rs.getInt("money"));
				dto.setDate(rs.getString("date"));
				list.add(dto);
			}

			// list.addAll(selectTransferHistory(memberId));

			System.out.println(list);

		} catch (Exception e) {
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public ArrayList<BankDTO> selectTransferHistory(String memberId) {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<BankDTO> list = new ArrayList<BankDTO>();

		try {
			String sql = "select * from bank_transfer_history_tb where MEMBER_ID=? order by date DESC";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, memberId);
			System.out.println(memberId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BankDTO dto = new BankDTO();
				dto.setMemberId(rs.getString("send_member_id"));
				dto.setKind(rs.getString("receive_member_id"));
				dto.setMemberBalance(rs.getInt("transfer_money"));
				dto.setDate(rs.getString("date"));
				list.add(dto);
			}

		} catch (Exception e) {
		} finally {
			try {
				// rs.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}