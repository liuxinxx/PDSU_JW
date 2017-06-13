package jiexi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;

import com.google.gson.JsonObject;

import net.sf.json.JSONObject;

public class jx {

	public static String changInputStream(InputStream inputStream, String encode) {
		ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					OutputStream.write(data, 0, len);
				}
				result = new String(OutputStream.toByteArray(), encode);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return result;

	}

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {

		DefaultHttpClient client1 = new DefaultHttpClient();
		String api = "http://api.url2io.com/article?";
		String uu = "http://dxy.pdsu.edu.cn/wlx/html/educate/major/2015-6-23/190.html";
		String Url = "&url=" + uu;//
		String token = "token=ZqdXt_UHTmWx9irL1L8wgA";
		String encode = "utf-8";
		Map<String,String> map = new HashMap<String,String>();
		System.out.println(api + token + Url);
		HttpGet get = new HttpGet(api + token + Url );
		HttpResponse httpResponse = null;
		try {
			httpResponse = client1.execute(get);
		} catch (IOException e1) {

			e1.printStackTrace();
		}
		String res = null;
		try {
			res = changInputStream(httpResponse.getEntity().getContent(), encode);
		} catch (UnsupportedOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		JSONObject jsonObject = JSONObject.fromObject(res);
		Iterator iterator = jsonObject.keys();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = jsonObject.getString(key);
			map.put(key, value);
			System.out.println(key+"----->"+value+"\r\n");
		}
		System.out.println("新闻来源："+map.get("title"));
		System.out.println("发表时间："+map.get("date"));
		System.out.println("正文："+map.get("content").trim().replace(Jsoup.parse("&nbsp;").text(), ""));
		
		// JSONObject map = new JSONObject();.replace("\n", "")
		// JSONObject json = JSONObject.fromObject(res);
		// JSONObject jsmap = (JSONObject)res;.fromObject(res);
		// Map<String ,String> map = new Map<String,String>(res);

	}
}
