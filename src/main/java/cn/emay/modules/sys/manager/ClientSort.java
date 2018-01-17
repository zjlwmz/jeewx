package cn.emay.modules.sys.manager;

import java.util.Comparator;

import cn.emay.modules.sys.entity.Client;


public class ClientSort implements Comparator<Client> {

	
	public int compare(Client prev, Client now) {
		return (int) (now.getLogindatetime().getTime()-prev.getLogindatetime().getTime());
	}

}
