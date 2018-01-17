package cn.emay;

import java.io.IOException;

import cn.emay.modules.wx.socket.WebSocket;

public class WebSocketMainTest {

	public static void main(String[] args) throws IOException {
		WebSocket webSocketTest=new WebSocket();
		webSocketTest.sendMessage("13131");
	}
}
