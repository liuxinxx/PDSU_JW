package GetJW;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import httpclient.httpclientPost;

public class jw {

	static CookieStore cookieStore = null;
	public static StringBuilder sb = null;
	public static DefaultHttpClient client = new DefaultHttpClient();
	static String cookie = null;

	/***
	 * 字符到字节流
	 * 
	 * @param inputStream
	 *            字节流
	 * @param encode
	 *            字符串
	 */
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

	public static String sendHttpClientPost(String path, Map<String, String> map, String encode, String cookie) {

		List<NameValuePair> list = new ArrayList<NameValuePair>();

		if (map != null && !map.isEmpty()) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}

		}

		try {

			// 实现将请求的参数封装到表单中
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, encode);
			HttpPost httppost = new HttpPost(path);
			httppost.setHeader("Accept-Encoding", "gzip, deflate, sdch");
			httppost.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httppost.setHeader("Cookie", sb.toString());
			httppost.setHeader("Referer", "http://jiaowu.pdsu.edu.cn/_data/index_LOGIN.aspx");
			httppost.setHeader("Origin", "http://jiaowu.pdsu.edu.cn");
			httppost.setHeader("Host", "jiaowu.pdsu.edu.cn");
			httppost.setHeader("Connection", "keep-alive");

			httppost.setEntity(entity);
			HttpResponse httpResponse = client.execute(httppost);
			httppost.abort();
			Header headers[] = httppost.getAllHeaders();

			try {

				CookieStore set = client.getCookieStore();

				System.out.println("LOGIN cookie-->" + set.toString());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString() + "2");
			}

			sb = new StringBuilder();
			List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
			for (int j = 0; j < cookies.size(); j++) {
				sb.append(cookies.get(j).getName() + "=" + cookies.get(j).getValue() + ";");
			}

			System.out.print("成功后的Cookie---->" + sb.toString());

			HttpGet main = new HttpGet("http://jiaowu.pdsu.edu.cn/MAINFRM.aspx");
			HttpResponse mainhttpResponse = client.execute(main);
			main.abort();// 休眠

			HttpGet main1 = new HttpGet("http://jiaowu.pdsu.edu.cn/xsxj/Stu_MyInfo_RPT.aspx");
			HttpResponse main1httpResponse = client.execute(main1);

			int aa = main1httpResponse.getStatusLine().getStatusCode();
			String res = changInputStream(main1httpResponse.getEntity().getContent(), encode);

			if (aa == 200) {
				// 获取重定向之后跳转的url http://jiaowu.pdsu.edu.cn/MAINFRM.aspx
				// String
				// locationUrl=httpResponse.getLastHeader("Location").getValue();
				// System.out.println("重定向后的地址:http://jiaowu.pdsu.edu.cn"+locationUrl);
				// 跳转到重定向的url
				// HttpGet httpget = new
				// HttpGet("http://jiaowu.pdsu.edu.cn"+locationUrl);
				// 重定向之后cookie发生变化
				return "200\n\r" + res;

			} else {
				return "状态码:" + aa;
			}

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

		return "失败";
	}

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			System.out.println("MD5加密出现错误");
		}
		return "error";
	}

	/*
	 * 下载验证码到本地
	 */
	public static void dl(String Url) {

		HttpGet httpGet = new HttpGet(Url);
		httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
		httpGet.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
		httpGet.setHeader("Content-Type", "application/x-www-form-urlencoded");
		httpGet.setHeader("Accept", "image/webp,image/*,*/*;q=0.8");
		httpGet.setHeader("Cookie", sb.toString());
		httpGet.setHeader("Referer", "http://jiaowu.pdsu.edu.cn/_data/index_LOGIN.aspx");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Host", "jiaowu.pdsu.edu.cn");
		FileOutputStream fos;
		try {
			// 客户端开始向指定的网址发送请求
			HttpResponse response = client.execute(httpGet);

			CookieStore set = client.getCookieStore();
			System.out.println("验证码      cookie-->" + set.toString());

			InputStream inputStream = response.getEntity().getContent();

			File file = new File("j:\\jj");
			if (!file.exists()) {
				file.mkdirs();
			}

			fos = new FileOutputStream("j:\\jj\\test.jpg");
			byte[] data = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String LoginHtml = "";

		HttpResponse set1 = null;
		HttpResponse set2 = null;
		String set_cookie1 = "";

		// 对主页面发起GET请求
		HttpGet gg = new HttpGet("http://jiaowu.pdsu.edu.cn/");

		try {
			gg.setHeader("Accept-Encoding", "gzip, deflate, sdch");
			gg.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
			gg.setHeader("Content-Type", "application/x-www-form-urlencoded");
			gg.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			gg.setHeader("Connection", "keep-alive");
			gg.setHeader("Host", "jiaowu.pdsu.edu.cn");
			set1 = client.execute(gg);

			sb = new StringBuilder();
			List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				sb.append(cookies.get(i).getName() + "=" + cookies.get(i).getValue() + ";");
			}

			CookieStore set = client.getCookieStore();
			System.out.println("主页面      cookie-->" + set.toString());
			gg.abort();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 对LOGIN页面发起Get请求
		HttpGet kk = new HttpGet("http://jiaowu.pdsu.edu.cn/_data/index_LOGIN.aspx");

		try {
			// kk.setHeader("Cookie",sb.toString());
			kk.setHeader("Accept-Encoding", "gzip, deflate, sdch");
			kk.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
			kk.setHeader("Content-Type", "application/x-www-form-urlencoded");
			kk.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			kk.setHeader("Referer", "http://jiaowu.pdsu.edu.cn/");
			kk.setHeader("Connection", "keep-alive");
			kk.setHeader("Host", "jiaowu.pdsu.edu.cn");
			set2 = client.execute(kk);

			CookieStore set = client.getCookieStore();

			System.out.println("LOGIN cookie-->" + set.toString());
		} catch (IOException e) {

			e.printStackTrace();
		}

		try {
			LoginHtml = changInputStream(set2.getEntity().getContent(), "GB2312");
		} catch (UnsupportedOperationException | IOException e) {
			System.out.println(e.toString() + "1");
		}

//		String regEx = "name=\"__VIEWSTATE\" value=\"(.*?)\"";
//		Pattern pat = Pattern.compile(regEx);
//		Matcher mat = pat.matcher(LoginHtml);
//		boolean rs = mat.find();
//		String STR = mat.group(0);
		// String VTE = STR.substring(26, STR.length() - 1);
		// System.out.println("__VIEWSTATE-->" + VTE);

		Scanner sc = new Scanner(System.in);

		System.out.println("请输入账号:");
		String id = sc.next();

		System.out.println("请输入密码:");
		String pwd = sc.next();

		// 加密密码
		String Tmm = getMD5(id + getMD5(pwd).substring(0, 30).toUpperCase() + "10919").substring(0, 30).toUpperCase();
		System.out.println(Tmm);

		// 获取验证码
		String Url = "http://jiaowu.pdsu.edu.cn/sys/ValidateCode.aspx";
		jw.dl(Url);
		Thread time = new Thread();

		File file = new File("j:\\jj\\test.jpg");
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// java.lang.Runtime().exec();
		System.out.println("请输入验证码:");
		String yz = sc.next();
		// String yz ="w2dt";
		// 加密验证码
		String yzm = getMD5(getMD5(yz.toUpperCase()).substring(0, 30).toUpperCase() + "10919").substring(0, 30)
				.toUpperCase();
		System.out.println(yzm);
		// 设置登录POST内容
		String path = "http://jiaowu.pdsu.edu.cn/_data/index_LOGIN.aspx";
		String encode = "GBK";
		Map<String, String> map = new HashMap<String, String>();
		map.put("Bdl", "��¼");
		// map.put("__VIEWSTATE", VTE);
		map.put("pcInfo",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36undefined5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36 SN:NULL");
		map.put("typeName", "ѧ��");
		map.put("Sel_Type", "STU");
		map.put("txt_asmcdefsddsd", id);
		map.put("txt_pewerwedsdfsdff", "");
		map.put("txt_sdertfgsadscxcadsads", "");
		map.put("sbtState", "");
		map.put("dsdsdsdsdxcxdfgfg", Tmm);
		map.put("fgfggfdgtyuuyyuuckjg", yzm);

		String result = jw.sendHttpClientPost(path, map, encode, set_cookie1);
		System.out.println(result);

	}

}
