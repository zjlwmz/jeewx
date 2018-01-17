package cn.emay.framework.common.poi.excel.entity.params;

public class ExcelVerifyEntity {
	private boolean interHandler;
	private boolean notNull;
	private boolean isMobile;
	private boolean isTel;
	private boolean isEmail;
	private int minLength;
	private int maxLength;
	private String regex;
	private String regexTip;

	public int getMaxLength() {
		return this.maxLength;
	}

	public int getMinLength() {
		return this.minLength;
	}

	public String getRegex() {
		return this.regex;
	}

	public String getRegexTip() {
		return this.regexTip;
	}

	public boolean isEmail() {
		return this.isEmail;
	}

	public boolean isInterHandler() {
		return this.interHandler;
	}

	public boolean isMobile() {
		return this.isMobile;
	}

	public boolean isNotNull() {
		return this.notNull;
	}

	public boolean isTel() {
		return this.isTel;
	}

	public void setEmail(boolean isEmail) {
		this.isEmail = isEmail;
	}

	public void setInterHandler(boolean interHandler) {
		this.interHandler = interHandler;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setRegexTip(String regexTip) {
		this.regexTip = regexTip;
	}

	public void setTel(boolean isTel) {
		this.isTel = isTel;
	}
}