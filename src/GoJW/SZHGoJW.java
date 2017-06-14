package GoJW;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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

import httpclient.ImageUtil;
import httpclient.SetMapZY;
import httpclient.httpclientPost;

public class SZHGoJW {

	public SZHGoJW() {
		// TODO Auto-generated constructor stub
	}

	static DefaultHttpClient client = new DefaultHttpClient();
	static StringBuilder sb = null;

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
				System.out.println("登录数字平台成功！");
				HttpGet get = new HttpGet("http://szhpt.pdsu.edu.cn/xs/default_xs.aspx");
				HttpResponse set = client.execute(get);
				sb = new StringBuilder();
				List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
				// System.out.println(set_cookie.substring(0,set_cookie.indexOf(";")));
				for (int j = 0; j < cookies.size(); j++) {
					sb.append(cookies.get(j).getName() + "=" + cookies.get(j).getValue() + ";");
				}
				String res1 = changInputStream(set.getEntity().getContent(), encode);
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
						System.out.println(JWUrl);
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

					HttpGet main1 = new HttpGet("http://jiaowu.pdsu.edu.cn/xsxj/Stu_MyInfo_RPT.aspx");
					main1.setHeader("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
					main1.setHeader("Accept-Encoding", "gzip, deflate,sdch");
					main1.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
					main1.setHeader("User-Agent",
							"Mozilla/5.0 (Windows NT10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)Chrome/52.0.2743.116 Safari/537.36");
					main1.setHeader("Referer", "http://jiaowu.pdsu.edu.cn/xsxj/Stu_MyInfo.aspx");
					main1.setHeader("Host", "jiaowu.pdsu.edu.cn");
					main1.setHeader("Upgrade-Insecure-Requests", "1");
					main1.setHeader("Cookie", sb.toString());
					main1.setHeader("Connection", "keep-alive");

					HttpResponse main1httpResponse = client.execute(main1);
					System.out.println("登录教务系统平台成功！");

					return changInputStream(main1httpResponse.getEntity().getContent(), encode);
					
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

	/**
	 * 下载图片到本地
	 * 
	 * @param Url
	 *            图片地址
	 * @param flieName
	 * @param Num
	 * @param Name
	 */
	public static void dl(String Url, String flieName, String Num, String Name) {
		DefaultHttpClient client1 = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Url);
		System.out.println("url" + Url);
		FileOutputStream fos;
		try {
			// 客户端开始向指定的网址发送请求
			HttpResponse response = client1.execute(httpGet);
			InputStream inputStream = response.getEntity().getContent();
			File file = new File(flieName);
			if (!file.exists()) {
				file.mkdirs();
			}

			fos = new FileOutputStream(flieName + Num + "_" + Name + ".jpg");
			byte[] data = new byte[1024];
			int len = 0;
			while ((len = inputStream.read(data)) != -1) {
				fos.write(data, 0, len);
			}
			fos.close();
			System.out.println("获取"+Name+"高考照片成功！");
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		String path = flieName + Num + "_" + Name + ".jpg";
		FileInputStream in = null;
		try {
			in = ImageUtil.readImage(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String ins = "insert into student_Sinfo values(?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(ins);
			ps.setString(1, Num);
			ps.setString(2, Name);
			ps.setString(3, xb);
			ps.setString(4, csrq);
			ps.setString(5, mz);
			ps.setString(6, ksh);
			ps.setString(7, len);
			ps.setString(8, cc);
			ps.setBinaryStream(9, in, in.available());
			// ps.setString(9, Num);

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
		try {

			int count = ps.executeUpdate();
			if (count > 0) {
				System.out.println("插入成功！" + count + "行！");

			} else {
				System.out.println("插入失败！");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 将字符串写入指定路径的文件
	 */
	public static void xrstr(String flieName, String str) {
		File txt = new File(flieName);
		if (!txt.exists()) {
			try {
				txt.createNewFile();
				byte bytes[] = new byte[1024];
				bytes = str.getBytes(); // 新加的
				int b = str.length(); // 改
				FileOutputStream foss = new FileOutputStream(txt);
				foss.write(str.getBytes("UTF-8"));
				foss.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	static Connection conn = null;
	static PreparedStatement ps = null;
	static java.sql.Statement stat = null;

	public static void db() {
		String Mysql = "com.mysql.jdbc.Driver";
		String Sqlstr = "jdbc:mysql://139.129.46.146:3306/pdsu_Student?characterEncoding=utf-8";
		try {
			java.lang.Class.forName(Mysql);
			conn = DriverManager.getConnection(Sqlstr, "root", "0806");
			// stat = conn.createStatement();
			System.out.println("数据库链接成功！");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("数据库链接出错！");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String YX = "";
	static String ZY = "";
	static String Class = "";
	static String XM = "";
	static String XH = "";
	static String len = "";
	static String cc = "";
	static String csrq = "";
	static String ksh = "";
	static String xb = "";
	static String mz = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		db();
		Map<String, String> zymap = new HashMap<String, String>();
		zymap = SetMapZY.SetMapyy(zymap);
		String path = "http://szhpt.pdsu.edu.cn/login_net1.aspx";
		String encode = "GBK";
		String NJ = "";
		int flag = 0;
		int nj = 0;
		for (nj = 12; nj < 17; nj++) {
			for (Map.Entry<String, String> entry : zymap.entrySet()) {
				for (int x = 1; x < 6; x++) {
					for (int k = 0; k < 80; k++) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("Bdl", "��¼");
						map.put("__VIEWSTATE", "/wEPDwULLTExNTc2NTI3OTlkZFizXxmxM2I3lLeYmJvVeuCfnIjc");
						map.put("__EVENTVALIDATION",
								"/wEWBAKpmZSiCAKeq5+XAgKlueiABQL8z67rChPBkREEBLHd8IJxbYGJ3itgkLlZ");
						map.put("__VIEWSTATEGENERATOR", "5E26995F");
						if (k < 10) {
							map.put("Tzh", nj + entry.getValue() + x + "0" + k);
						} else {
							map.put("Tzh", nj + entry.getValue() + x + k);
						}
						map.put("Tmm", "*****");//我们学校数字平台的默认密码
						String result = SZHGoJW.sendHttpClientPost(path, map, encode);
						Document doc = Jsoup.parse(result);
						String phoURL = doc.select("img").attr("src").toString();
						if (phoURL.length() > 0) {
							System.out.println("获取学生信息成功！");

							Elements content = doc.select("td ");

							int i = 0;
							for (Element link : content) {
								i++;
								String linkText = link.text();
								// 学号
								if (i == 3) {
									XH = linkText;
								}
								// 姓名
								if (i == 5) {
									XM = linkText;
								}
								// 性别
								if (i == 12) {
									xb = linkText;
								}
								// 名族
								if (i == 18) {
									mz = linkText;
								}
								// 出生日期
								if (i == 16) {
									csrq = linkText;
								}
								// 考生号
								if (i == 56) {
									ksh = linkText;
								}
								// 培养层次
								if (i == 98) {
									cc = linkText;
								}
								// 学习年限
								if (i == 104) {
									len = linkText;
								}
								// 专业
								if (i == 114) {
									ZY = linkText;
								}
								// 院系
								if (i == 112) {
									YX = linkText;
								}
								if (i == 116) {
									Class = linkText;
								}
							}
							String Filename = "j:\\平院学生test\\" + nj + "\\" + YX + "\\" + ZY + "\\" + Class + "\\" + XH
									+ "_" + XM + "\\";
							System.out.println(Filename);
							
							phoURL = "http://jiaowu.pdsu.edu.cn" + phoURL.substring(2, phoURL.length());
							System.out.println("\r\n图片链接---->" + phoURL);
							System.out.println("解析学生信息成功！");
							SZHGoJW.dl(phoURL, Filename, XH, XM);
							System.out.println(XM+"信息完成存入数据库操作！\n\n");
							flag = 0;
						} else {
							flag++;
							if (flag == 10) {
								flag = 0;
								break;
							}
							System.out.println("登录失败！");
						}

					}
				}
			}
		}
	}

}
