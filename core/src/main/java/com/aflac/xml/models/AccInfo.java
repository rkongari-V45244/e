package com.aflac.xml.models;

public class AccInfo {
	    public String groupNumber;
	    public String accountName;
	    public String situsState;
		public String getGroupNumber() {
			return groupNumber;
		}
		public void setGroupNumber(String groupNumber) {
			this.groupNumber = groupNumber;
		}
		public String getAccountName() {
			return accountName;
		}
		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}
		public String getSitusState() {
			return situsState;
		}
		public void setSitusState(String situsState) {
			this.situsState = situsState;
		}
		@Override
		public String toString() {
			return "AccInfo [groupNumber=" + groupNumber + ", accountName=" + accountName + ", situsState=" + situsState
					+ "]";
		}
	    
	    
}
