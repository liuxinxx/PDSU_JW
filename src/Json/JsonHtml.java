package Json;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Document;

import org.apache.http.Header;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsonHtml {

	public JsonHtml() {
		// TODO Auto-generated constructor stub
	}
	

	public static String changInputStream(InputStream inputStream,String encode) {
		ByteArrayOutputStream OutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if(inputStream != null)
		{
			try
			{
				while((len = inputStream.read(data)) != -1)
				{
				   OutputStream.write(data,0,len);
				}
				result = new String(OutputStream.toByteArray(),encode);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
			
		}
		return result;
		
	}
	
	public static String JsendHttpClientPost(String path,Map<String,String> map,String encode){
		
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		if(map!=null&&!map.isEmpty())
		{
			for(Map.Entry<String, String> entry:map.entrySet())
			{
				list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
			}
			
		}

		try {
			//实现将请求的参数封装到表单中
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,encode);
			HttpPost httppost = new HttpPost(path);
			
			httppost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
			httppost.setHeader("Content-Type","application/x-www-form-urlencoded");
			httppost.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httppost.setEntity(entity);
			 DefaultHttpClient client = new DefaultHttpClient();
			 //client = new DefaultHttpClient();
			HttpResponse httpResponse = client.execute(httppost);
			//获取cookie
			StringBuilder sb = new StringBuilder();
			String set_cookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
			
			//System.out.println(set_cookie);
			
			
//			try{
//	        HttpGet httpget = new HttpGet("http://yyxt.pdsu.edu.cn/pdsunews/writenews.aspx?typeid=b131a131a13&n=5&lettern=24&lstr=15&more=no&hstr=170%&cstr=1px&shownew=yes&showrq=no");  
//            httpget.setHeader("Cookie", set_cookie.substring(0,set_cookie.indexOf(";")));  
//            HttpResponse response = client.execute(httpget);  }
//			catch(Exception ee)
//			{
//				ee.printStackTrace();
//			}
	        
	        
			int aa = httpResponse.getStatusLine().getStatusCode();
			String res = changInputStream(httpResponse.getEntity().getContent(), encode);
			//String ss = changInputStream(response.getEntity().getContent(), encode);
			if(aa== 200)
			{
				return res;
				
			}
			else if(aa==302)
			{

				//获取重定向之后跳转的url
				//String locationUrl=httpResponse.getLastHeader("Location").getValue();  
				//System.out.println("重定向后的地址:http://szhpt.pdsu.edu.cn"+locationUrl);
			    //跳转到重定向的url  
				//HttpGet httpget = new HttpGet("http://szhpt.pdsu.edu.cn/"+locationUrl);
				//重定向之后cookie发生变化
				
				//HttpResponse set  = client.execute(httpget);
			    
			    
			    //set_cookie = se.getFirstHeader("Set-Cookie").getValue();
		        //System.out.println(set_cookie.substring(0,set_cookie.indexOf(";")));
		        
		        //System.out.println(changInputStream(se.getEntity().getContent(), encode));
		        //HttpGet get = new HttpGet("http://jiaowu.pdsu.edu.cn/cas_pdsxy.aspx");
		        HttpGet get = new HttpGet("http://yyxt.pdsu.edu.cn/cet/default0.aspx");
		        
		        HttpResponse set = client.execute(get);        
		        
		        List<Cookie> cookies = ((AbstractHttpClient) client).getCookieStore().getCookies();   
		        
				for(int i = 0 ;i<cookies.size();i++)
				{
					sb.append(cookies.get(i).getName()+"="+cookies.get(i).getValue()+";");
				}
				get.abort();
				try
				{
					//DefaultHttpClient client1 = new DefaultHttpClient();
					HttpGet gg = new HttpGet("http://yyxt.pdsu.edu.cn/cet/entry1.aspx");
					//HttpGet gg = new HttpGet("http://jiaowu.pdsu.edu.cn/xsxj/Stu_MyInfo_RPT.aspx");
					//gg.setHeader("Cookie", sb.toString()); //设置get请求的cookie			 
					HttpResponse  set1 = client.execute(gg);
					return changInputStream(set1.getEntity().getContent(), encode);  
					
				}
				catch(Exception ee)
				{
					System.out.println(ee.toString());
				}
				      			
			}
			else
			{
				return "状态码:"+aa;
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
				String path = "http://szhpt.pdsu.edu.cn/login_net1.aspx";
				String encode = "utf-8";
				Map<String ,String > map = new HashMap<String,String>();
				map.put("Bdl", "��¼");
				map.put("__VIEWSTATE", "/wEPDwULLTExNTc2NTI3OTlkZFizXxmxM2I3lLeYmJvVeuCfnIjc");
				map.put("__EVENTVALIDATION", "/wEWBAKpmZSiCAKeq5+XAgKlueiABQL8z67rChPBkREEBLHd8IJxbYGJ3itgkLlZ");
				map.put("__VIEWSTATEGENERATOR", "5E26995F");
				map.put("Tzh", "141210135");
				map.put("Tmm", "123456");
				String result = JsonHtml.JsendHttpClientPost(path, map, encode);
				
//				String regEx = "_GB2312(.*?)color=\"LightSeaGreen\">(.*?)</f";
//				Pattern pat = Pattern.compile(regEx);
//				Matcher mat = pat.matcher(result);
//				boolean rs = mat.find();
//				String STR = mat.group(0);
//				
//				String regEX1 = "[\u4e00-\u9fa5]+";
//				Pattern pat1 = Pattern.compile(regEX1);
//				Matcher mat1 = pat1.matcher(STR);
//				boolean rs1 = mat1.find();
//				System.out.println(mat1.group(0));
				org.jsoup.nodes.Document doc = Jsoup.parse(result);
//				Element content = doc.getElementById("ctl00_ContentPlaceHolder1_TextBox1");  
//				Elements links = content.getElementsByTag("value");
				//Elements ll = doc
				Elements lin = doc.select("td");
				//System.out.print(lin.toString());
				int i = 0;
				//for (Element link : lin) {  
				  //String linkHref = link.attr("href");  
				  //String linkText = link.text(); 
					//String linkText = link.html().toString();
					
					String regEx = "<input(.*?)readonly";
					Pattern pat = Pattern.compile(regEx);
					Matcher mat = pat.matcher(result);
					boolean rs = mat.find();
					if(rs)
					{String STR = mat.group(i);i++;}
					
					
//					String regEX1 = "[\u4e00-\u9fa5]+";
//					Pattern pat1 = Pattern.compile(STR);
//					Matcher mat1 = pat1.matcher(linkText);
//					boolean rs1 = mat1.find();
					
//					try{System.out.println(STR);
//						}
//					
//					catch (Exception ee){}
//					if(i%2 == 0)
//					{
//						System.out.println("");
//					}
					
				  //System.out.println(linkText);
				//}  
				//System.out.println(result);
				
	}



}
