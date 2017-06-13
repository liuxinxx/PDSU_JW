package SZHGoJW_laoshi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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

import GoJW.SZHGoJW;
import httpclient.SetMapZY;

public class laoshi {

	public laoshi() {
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

			System.out.println(aa + "123123123");
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
					System.out.println("登录成功！");

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
					System.out.println("http://jiaowu.pdsu.edu.cn/wsxk/stu_zxjg_rpt.aspx");
					HttpResponse main1httpResponse = client.execute(main1);

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

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map<String, String> zymap = new HashMap<String, String>();
		zymap = SetMapZY.SetMapyy(zymap);
		String path = "http://szhpt.pdsu.edu.cn/login_net1.aspx";
		String encode = "GBK";
		String XM = "";
		String XB = "";
		String CSRQ = "";
		String XL = "";
		String XW = "";
		String ZC = "";
		String RXNF = "";
		String MZ = "";
		String ID = "";
		String JG = "";
		String GW = "";
		String YN_GW = "";
		String Mail = "";
		String JL = "";
		int nj = 0;
		for (nj = 13; nj < 17; nj++) {
			for (Map.Entry<String, String> entry : zymap.entrySet()) {
				for (int x = 1; x < 6; x++) {
					int ffff = 0;
					for (int k = 0; k < 80; k++) {
						if (ffff == 1)
							continue;
						else {
							Map<String, String> tUrl = new HashMap<String, String>();
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
							map.put("Tmm", "123456");
							String result = laoshi.sendHttpClientPost(path, map, encode);
							Document doc = Jsoup.parse(result);
							Elements url = doc.select("#showD");
							System.out.println("^^^^^^^^^^---------------------^^^^^^^^^^");
							for (Element link : url) {
								if (link.val().toString().length() > 8) {
									String key = link.text();
									String value = link.val();
									tUrl.put(key, value);
									System.out.println(value + "\t老师名字：" + link.text());
								} else {
									String urll = link.val();
									System.out.println("专业代码：" + urll + "\t课程：" + link.text());
								}
							}
							System.out.println("^^^^^^^^^^---------------------^^^^^^^^^^");
							for (Map.Entry<String, String> tt : tUrl.entrySet()) {
								String gg = "http://jiaowu.pdsu.edu.cn/JXZY/INFO_Teacher.aspx?id=";
								System.out.println(gg + tt.getValue());
								HttpGet Gett = new HttpGet(gg + tt.getValue());
								HttpResponse ter = null;
								try {
									ter = client.execute(Gett);
								} catch (IOException e) {
									e.printStackTrace();
								}
								try {
									String laoshiinfo = changInputStream(ter.getEntity().getContent(), encode);
									System.out.print(laoshiinfo);
									Document doc1 = Jsoup.parse(laoshiinfo);
									Elements url1 = doc1.select("td");
									System.out.println("老师信息开始---------------------");
									int k1 = 0;
									for (Element link1 : url1) {

										k1++;
										String text = link1.text();
										if (k1 == 4) {
											XM = text;
										}
										if (k1 == 7) {
											XB = text;
										}
										if (k1 == 9) {
											CSRQ = text;
										}
										if (k1 == 11) {
											XL = text;
										}
										if (k1 == 13) {
											XW = text;
										}
										if (k1 == 15) {
											ZC = text;
										}
										if (k1 == 17) {
											RXNF = text;
										}
										if (k1 == 19) {
											MZ = text;
										}
										if (k1 == 23) {
											JG = text;
										}
										if (k1 == 27) {
											YN_GW = text;
										}
										if (k1 == 33) {
											Mail = text;
										}
										if (k1 == 36) {
											JL = text;
										}

										System.out.println(k1 + "   " + text);
									}
									String tInfo = "姓    名:" + XM + "\r\n" + "\r\n" + "性    别:" + XB + "\r\n" + "\r\n"
											+ "出生年月:" + CSRQ + "\r\n" + "\r\n" + "民    族:" + MZ + "\r\n" + "\r\n"
											+ "学    历:" + XL + "\r\n" + "\r\n" + "学    位:" + XW + "\r\n" + "\r\n"
											+ "职    称:" + ZC + "\r\n" + "\r\n" + "入学年份:" + RXNF + "\r\n" + "\r\n"
											+ "身份证号:" + ID + "\r\n" + "\r\n" + "籍    贯:" + JG + "\r\n" + "\r\n"
											+ "岗    位:" + GW + "\r\n" + "\r\n" + "是否在岗:" + YN_GW + "\r\n" + "\r\n"
											+ "电子邮箱:" + Mail + "\r\n" + "\r\n" + "简    历:" + JL + "\r\n";
									String flieName = "j:\\平院老师\\";
									File file = new File(flieName);
									if (!file.exists()) {
										file.mkdirs();
									}
									xrstr(flieName + XM + ".txt", tInfo);
									ffff = 1;
									System.out.println("老师信息结束---------------------");
								} catch (UnsupportedOperationException | IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
}
