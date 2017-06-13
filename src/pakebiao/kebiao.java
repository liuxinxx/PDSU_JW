package pakebiao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.jdbc.Statement;
import java.sql.*;
import SZHGoJW_laoshi.laoshi;
import httpclient.SetMapZY;

public class kebiao {
	static DefaultHttpClient client = new DefaultHttpClient();
	static StringBuilder sb = null;
	static String Name = "";
	static int sum = 0;
	static int sbb = 0;
	public kebiao() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 将流数据转换为指定编码的字符串
	 * 
	 * @param inputStream
	 * @param encode
	 * @return
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

	/**
	 * 发起请求
	 * 
	 * @param path
	 * @param map
	 * @param encode
	 */
	public static String sendHttpClientPost(String path, Map<String, String> map, String encode) {

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

			httppost.setHeader("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httppost.setEntity(entity);

			// client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httppost);
			// 获取cookie

			String res = changInputStream(httpResponse.getEntity().getContent(), encode);
			int aa = httpResponse.getStatusLine().getStatusCode();
			
			if (aa == 200) {
				return res;

			} else if (aa == 302) {

				HttpGet get = new HttpGet("http://szhpt.pdsu.edu.cn/xs/default_xs.aspx");
				HttpResponse set = client.execute(get);
				sb = new StringBuilder();
				List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
				// System.out.println(set_cookie.substring(0,set_cookie.indexOf(";")));
				for (int j = 0; j < cookies.size(); j++) {
					sb.append(cookies.get(j).getName() + "=" + cookies.get(j).getValue() + ";");
				}
				String res1 = changInputStream(set.getEntity().getContent(), encode);
				
				
				Document doc1 = Jsoup.parse(res1);
				Elements name = doc1.select("#Lxm");
				for (Element nam : name) {
					Name = nam.text();
					System.out.println("登录数字平台成功！\n\n名字："+Name);
				}
				
				Document doc = Jsoup.parse(res1);
				String JWUrl = "";
				// String URL = doc.select("table.thumbnail").attr("href");
				Elements links = doc.select("table.thumbnail");
				int i = 0;
				for (Element l : links) {
					i++;
					if (i == 8)

					{
						JWUrl = l.select("a").attr("href");// 相对路径
						System.out.println("获取教务处URL成功！："+JWUrl);
					}
				}
				get.abort();
				try {

					HttpGet gg = new HttpGet(JWUrl);
					HttpResponse set1 = client.execute(gg);

					gg.abort();
					HttpGet main = new HttpGet(JWUrl);
					main.setHeader("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					main.setHeader("Accept-Encoding", "gzip, deflate, sdch");
					main.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
					main.setHeader("User-Agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
					main.setHeader("Referer", "http://szhpt.pdsu.edu.cn/xs/default_xs.aspx");
					main.setHeader("Host", "jiaowu.pdsu.edu.cn");
					main.setHeader("Cookie", sb.toString());
					HttpResponse set2 = client.execute(main);

					main.abort();

					HttpGet Main = new HttpGet("http://jiaowu.pdsu.edu.cn/MAINFRM.aspx");
					Main.setHeader("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					Main.setHeader("Accept-Encoding", "gzip, deflate, sdch");
					Main.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
					Main.setHeader("User-Agent",
							"Mozilla/5.0 (Windows NT10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/52.0.2743.116 Safari/537.36");
					Main.setHeader("Referer", JWUrl);
					Main.setHeader("Host", "jiaowu.pdsu.edu.cn");
					Main.setHeader("Connection", "keep-alive");
					Main.setHeader("Cookie", sb.toString());
					HttpResponse set3 = client.execute(Main);
					Main.abort();
					System.out.println("登录教务处成功！");

					HttpGet main1 = new HttpGet("http://jiaowu.pdsu.edu.cn/wsxk/stu_zxjg_rpt.aspx");
					main1.setHeader("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					main1.setHeader("Accept-Encoding", "gzip, deflate,sdch");
					main1.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
					main1.setHeader("User-Agent",
							"Mozilla/5.0 (Windows NT10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/52.0.2743.116 Safari/537.36");
					main1.setHeader("Referer", "http://jiaowu.pdsu.edu.cn/wsxk/stu_zxjg.aspx");
					main1.setHeader("Host", "jiaowu.pdsu.edu.cn");
					main1.setHeader("Upgrade-Insecure-Requests", "1");
					// main1.setHeader("Cookie", sb.toString());
					main1.setHeader("Connection", "keep-alive");
					HttpResponse main1httpResponse = client.execute(main1);
					System.out.println("获取数据成功！");
					// HttpGet main1 = new
					// HttpGet("http://jiaowu.pdsu.edu.cn/xsxj/Stu_MyInfo_RPT.aspx");
					// HttpResponse main1httpResponse = client.execute(main1);
					return changInputStream(main1httpResponse.getEntity().getContent(), encode);
					// //return res;
				} catch (Exception ee) {
					System.out.println(ee.toString());
				}

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String Mysql = "com.mysql.jdbc.Driver";
		String Sqlstr = "jdbc:mysql://139.129.46.146:3306/pdsu_Student?characterEncoding=utf-8";
		java.sql.Statement stat = null;
		String xh = "";
		Connection conn = null;
		try {
			Class.forName(Mysql);
			conn = DriverManager.getConnection(Sqlstr, "root", "0806");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("数据库链接出错！");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> zymap = new HashMap<String, String>();
		zymap = SetMapZY.SetMapyy(zymap);
		String path = "http://szhpt.pdsu.edu.cn/login_net1.aspx";
		String encode = "GBK";
		int nj = 13;
		
		//for (nj = 13; nj < 17; nj++)
		{
			for (Map.Entry<String, String> entry : zymap.entrySet()) {
				for (int x = 1; x < 6; x++) {

					for (int k = 0; k < 80; k++) {

						Map<String, String> tUrl = new HashMap<String, String>();
						Map<String, String> map = new HashMap<String, String>();
						map.put("Bdl", "��¼");
						map.put("__VIEWSTATE", "/wEPDwULLTExNTc2NTI3OTlkZFizXxmxM2I3lLeYmJvVeuCfnIjc");
						map.put("__EVENTVALIDATION",
								"/wEWBAKpmZSiCAKeq5+XAgKlueiABQL8z67rChPBkREEBLHd8IJxbYGJ3itgkLlZ");
						map.put("__VIEWSTATEGENERATOR", "5E26995F");
						 if (k < 10) {
						 map.put("Tzh", nj + entry.getValue() + x + "0" + k);
						 xh = nj + entry.getValue() + x + "0" + k;
						 } else {
						 map.put("Tzh", nj + entry.getValue() + x + k);
						 xh = nj + entry.getValue() + x + k;
						 }
//						map.put("Tzh", "141210135");
//						xh = "141210135";
						map.put("Tmm", "123456");
						Name = "";
						String result = kebiao.sendHttpClientPost(path, map, encode);
						String text = "";
						
						Document doc = Jsoup.parse(result);
						Elements url = doc.select("table[bgcolor]");
						int i = 0;
						for (Element link : url) {
							if (i == 1) 
							{
								text = link.html();
								//System.out.println("i= "+i+text + "\n\n" + text.length());
							}
							i++;
						}
						Document doc1 = Jsoup.parse(result);
						Elements url1 = doc1.select("td");
						int ii = 0;
						String kcxx = "";
						for (Element link1 : url1) {
							String text1 = link1.text();
							if (ii > 64) {
								kcxx = kcxx +" "+ text1;
							}
							ii++;
						}
						System.out.println("字符长度：" + text.length());
						// System.out.println(T);
						if (kcxx.length() > 20) {
							System.out.println("字符长度：" + text.length());
							try {
								
								stat = conn.createStatement();
								System.out.println("数据库链接成功！");
								//System.out.println(text);
								String ins = "insert into student_kb values('" + xh + "','" + Name + "','" + kcxx
										+ "','" + text.replace("'", "$") + "')";
								int kn = stat.executeUpdate(ins);
								System.out.println("添加成功！" + kn + "行！");
								sum++;
								stat.close();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								System.out.println("添加信息出错");		
								sbb++;
							}
						}
					}

				}

			}
		}
		try {
			conn.close();
			System.out.print("成功添加："+sum+"数据！\n"+"添加出错："+sbb+"数据！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
