package com.arthur.web.antisamy.context;

/**
 * AntiSamy 过滤的内容
 *
 * @author <a href="mailto:545896770@qq.com">DearYang</a>
 * @date 2023-02-06
 * @since 1.0
 */
@SuppressWarnings("unused")
public class AntiSamyConfig {

	private String[] defenseHeaders;
	private String[] defenseCookies;

	/**
	 * 防御请求内容，比如：表单请求、JSON请求
	 */
	private boolean defenseQueryBody = true;

	public String[] getDefenseHeaders() {
		return defenseHeaders;
	}

	public void setDefenseHeaders(String[] defenseHeaders) {
		this.defenseHeaders = defenseHeaders;
	}

	public String[] getDefenseCookies() {
		return defenseCookies;
	}

	public void setDefenseCookies(String[] defenseCookies) {
		this.defenseCookies = defenseCookies;
	}

	public boolean isDefenseQueryBody() {
		return defenseQueryBody;
	}

	public void setDefenseQueryBody(boolean defenseQueryBody) {
		this.defenseQueryBody = defenseQueryBody;
	}

}
