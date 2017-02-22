package cn.ac.iie.hy.areamonitor.data;

public class SMetaData {

	private int C_CDRtype;
	private long C_BeginTime;
	private long C_EndTime;
	private long C_TimeStamp;
	private long C_SMSCStamp;
	private String C_UserNum;
	private String C_RelateNum;
	private String C_Imsi;
	private String C_Imei;
	private int C_SpCode;
	private int C_AsCode;
	private int C_LAC;
	private int C_CI;
	private int C_RAC;
	private String c_uli;
	private String C_AreaCode;
	private String C_HomeCode;
	private String C_Content;
	
	

	public int getC_CDRtype() {
		return C_CDRtype;
	}

	public void setC_CDRtype(int c_CDRtype) {
		C_CDRtype = c_CDRtype;
	}

	public long getC_BeginTime() {
		return C_BeginTime;
	}

	public void setC_BeginTime(long c_BeginTime) {
		C_BeginTime = c_BeginTime;
	}

	public long getC_EndTime() {
		return C_EndTime;
	}

	public void setC_EndTime(long c_EndTime) {
		C_EndTime = c_EndTime;
	}

	public long getC_TimeStamp() {
		return C_TimeStamp;
	}

	public void setC_TimeStamp(long c_TimeStamp) {
		C_TimeStamp = c_TimeStamp;
	}

	public long getC_SMSCStamp() {
		return C_SMSCStamp;
	}

	public void setC_SMSCStamp(long c_SMSCStamp) {
		C_SMSCStamp = c_SMSCStamp;
	}

	public String getC_UserNum() {
		return C_UserNum;
	}

	public void setC_UserNum(String c_UserNum) {
		C_UserNum = c_UserNum;
	}

	public String getC_RelateNum() {
		return C_RelateNum;
	}

	public void setC_RelateNum(String c_RelateNum) {
		C_RelateNum = c_RelateNum;
	}

	public String getC_Imsi() {
		return C_Imsi;
	}

	public void setC_Imsi(String c_Imsi) {
		C_Imsi = c_Imsi;
	}

	public String getC_Imei() {
		return C_Imei;
	}

	public void setC_Imei(String c_Imei) {
		C_Imei = c_Imei;
	}

	public int getC_SpCode() {
		return C_SpCode;
	}

	public void setC_SpCode(int c_SpCode) {
		C_SpCode = c_SpCode;
	}

	public int getC_AsCode() {
		return C_AsCode;
	}

	public void setC_AsCode(int c_AsCode) {
		C_AsCode = c_AsCode;
	}

	public int getC_LAC() {
		return C_LAC;
	}

	public void setC_LAC(int c_LAC) {
		C_LAC = c_LAC;
	}

	public int getC_CI() {
		return C_CI;
	}

	public void setC_CI(int c_CI) {
		C_CI = c_CI;
	}

	public int getC_RAC() {
		return C_RAC;
	}

	public void setC_RAC(int c_RAC) {
		C_RAC = c_RAC;
	}

	public String getC_uli() {
		return c_uli;
	}

	public void setC_uli(String c_uli) {
		this.c_uli = c_uli;
	}
	
	public String getC_AreaCode() {
		return C_AreaCode;
	}

	public void setC_AreaCode(String c_AreaCode) {
		C_AreaCode = c_AreaCode;
	}

	public String getC_HomeCode() {
		return C_HomeCode;
	}

	public void setC_HomeCode(String c_HomeCode) {
		C_HomeCode = c_HomeCode;
	}

	public String getC_Content() {
		return C_Content;
	}

	public void setC_Content(String c_Content) {
		C_Content = c_Content;
	}

	@Override
	public String toString() {
		return "SMetaData{" +
				"C_CDRtype=" + C_CDRtype +
				", C_BeginTime=" + C_BeginTime +
				", C_EndTime=" + C_EndTime +
				", C_TimeStamp=" + C_TimeStamp +
				", C_SMSCStamp=" + C_SMSCStamp +
				", C_UserNum='" + C_UserNum + '\'' +
				", C_RelateNum='" + C_RelateNum + '\'' +
				", C_Imsi='" + C_Imsi + '\'' +
				", C_Imei='" + C_Imei + '\'' +
				", C_SpCode=" + C_SpCode +
				", C_AsCode=" + C_AsCode +
				", C_LAC=" + C_LAC +
				", C_CI=" + C_CI +
				", C_RAC=" + C_RAC +
				", c_uli='" + c_uli + '\'' +
				", C_AreaCode='" + C_AreaCode + '\'' +
				", C_HomeCode='" + C_HomeCode + '\'' +
				", C_Content='" + C_Content + '\'' +
				'}';
	}
}
