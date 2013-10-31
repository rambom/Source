package com.android.jetman.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.widget.Toast;

import com.android.jetman.JetmanApp;
import com.android.jetman.R;

public class HtmlParse {
	public static Document getHtmlDocument(String svnUser, String svnPwd,
			String where) {
		Document doc = null;
		String url = JetmanApp.getContext()
				.getString(R.string.jetAddressBookUrl);
		try {
			doc = Jsoup.connect(url).data("txtSvnUser", svnUser)
					.data("txtSvnPwd", svnPwd).data("txtWhere", where).post();
		} catch (IOException e) {
			Toast.makeText(JetmanApp.getContext(), "²éÑ¯Ê§°Ü£¬Çë¼ì²éÍøÂç!",
					Toast.LENGTH_SHORT).show();
		}
		return doc;
	}
}
