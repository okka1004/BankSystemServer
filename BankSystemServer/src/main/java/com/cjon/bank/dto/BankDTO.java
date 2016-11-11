package com.cjon.bank.dto;

public class BankDTO {

	private String memberId;
	private String memberName;
	private String memberAccount;
	private int memberBalance;
	private String kind;
	private String date;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public BankDTO() {
		super();
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getMemberAccount() {
		return memberAccount;
	}
	public void setMemberAccount(String memberAccount) {
		this.memberAccount = memberAccount;
	}
	public int getMemberBalance() {
		return memberBalance;
	}
	public void setMemberBalance(int memberBalance) {
		this.memberBalance = memberBalance;
	}
	
	
	
	
}
