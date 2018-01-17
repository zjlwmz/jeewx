package cn.emay;

import weixin.popular.api.TokenAPI;
import weixin.popular.bean.token.Token;

public class WexinAPITest {

	
	public static void main(String[] args) {
//		Token token=TokenAPI.token("wx22b9825db4f8f33e", "343daca679959ab666dc43393eb54a41");
//		System.out.println(token.getAccess_token());
//		Ticket cicket=TicketAPI.ticketGetticket(token.getAccess_token());
//		System.out.println(cicket.getTicket());
		
		
		Token  token=TokenAPI.token("wxc9208f6de50bb023", "32511d530574dbbd415caa6f28dcd4ca");
		System.out.println(token.getAccess_token());
		
//		WxMpService mpService=new WxMpServiceImpl();
//		
//		mpService.getAccessToken()
		
	}
}
