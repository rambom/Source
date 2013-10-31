package com.android.jetman.data.dao;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.widget.Toast;

import com.android.jetman.JetmanApp;
import com.android.jetman.entity.AddressNode;
import com.android.jetman.util.HtmlParse;

/*
 * Address Book Of Dcjet
 * @author FGSHU
 */
public class Address {
	/**
	 * 查询得到所有通讯记录列表
	 * 
	 * @return
	 */
	public List<AddressNode> getAllAddressNodes(String name, String pwd) {
		Document doc = HtmlParse.getHtmlDocument(name, pwd, "1");
		return parseDoc(doc);
	}

	/**
	 * 跟据查询条件得到查询数据
	 * 
	 * @return
	 */
	public List<AddressNode> searchNodes(String where, String name, String pwd) {
		Document doc = HtmlParse.getHtmlDocument(name, pwd, where);
		return parseDoc(doc);
	}

	/*
	 * 将Document解析为List<AddressNode>
	 */
	private List<AddressNode> parseDoc(Document doc) {
		List<AddressNode> listAddressNodes = new ArrayList<AddressNode>();
		if (null == doc)
			return null;
		if (doc.hasText() && doc.toString().contains("用户名密码错误")) {
			Toast.makeText(JetmanApp.getContext(), "用户名密码错误!",
					Toast.LENGTH_SHORT).show();
			return null;
		}
		Elements content = doc.getElementsByTag("table");
		Elements trs = content.get(0).getElementsByTag("tr");
		for (Element tr : trs) {
			AddressNode node = new AddressNode();
			node.setName(tr.child(0).text());
			node.setSex(tr.child(1).text());
			node.setPhone(tr.child(2).text());
			node.setMobile(tr.child(3).text());
			node.setEntphone(tr.child(4).text());
			node.setEmail(tr.child(5).text());
			listAddressNodes.add(node);
		}
		return listAddressNodes;
	}

}
