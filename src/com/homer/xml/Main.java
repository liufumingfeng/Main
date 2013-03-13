package com.homer.xml;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.homer.model.PostValue;
import com.homer.model.Question;
import com.homer.model.Surveys;
import com.homer.util.PullxmlParser;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



import java.util.Enumeration;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;


public class Main extends Activity {

	private static final String TAG = null;
	private TextView tvXMLCreate;
	private TextView tvXMLResolve;
	
	
	//������ʾ��List��ر���  
    ListView list;  
    ArrayAdapter<Surveys> adapter;  
    ArrayList<Surveys> surveysEntryList; 
	
	StringBuffer stringBuffer;
	
	/** 
     *  
     * @param urlPath ����·�� 
     * @param params Map��keyΪ���������valueΪ���������ֵ 
     * @param encoding  ���뷽ʽ 
     * @return 
     * @throws Exception 
     */  
      
    //ͨ��post��������˷������ݣ�����÷������������  
    public static InputStream getInputStreamByPost(String urlPath,Map<String,PostValue> params,String encoding) throws Exception{  
        StringBuffer sb = new StringBuffer(); 
        
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy��MM��dd��   HH:mm:ss     ");     
        Date   curDate   =   new   Date(System.currentTimeMillis());//��ȡ��ǰʱ��     
        String   DateStr   =   formatter.format(curDate);    
       
        String userId = "<UserId>";
        String UserName = "<UserName>";
        String Surveyid = "<Surveyid>";
        String IPAddress = "<<IPAddress>::>";
        
        sb.append("xml=<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("<Answers>").append("<IPAddress>::%@").append("</IPAddress>")
        .append("<VoteTime>").append("</VoteTime>").append("<UserId>%@").append("</UserId>").append("<UserName>%@").append("</UserName>")
        .append("<SourceType>2").append("</SourceType>").append("<Surveyid>").append("</Surveyid>");
        
        for(Map.Entry<String,PostValue> entry:params.entrySet()){
        	PostValue tempPostValue = entry.getValue();
        	if (tempPostValue.getOptions().equals("0")) {
        		sb.append("<Question ").append("Questionid=\"%@\">").append("<Answer>").append(tempPostValue.getAnswers().indexOf(0)).append("</Answer>").append("</Question>");
        	} else if (tempPostValue.getOptions().equals("1")) {
        		sb.append("<Question ").append("Questionid=\"%@\">");
				for (int i = 0; i < tempPostValue.getAnswers().size(); i ++) {
					sb.append("<Answer>").append(tempPostValue.getAnswers().indexOf(i)).append("</Answer>");
				}
				sb.append("<Question ").append("</Question>");
			} else if (tempPostValue.getOptions().equals("2")) {
				sb.append("<Question ").append("Questionid=\"%@\">").append("<Answer ").append("type=\"text\">%@").append("</Answer>").append("</Question>");
			}
        }
        sb.append("</Answers>");
        String data = sb.deleteCharAt(sb.length()).toString();  
        URL url = new URL(urlPath);  
        //������  
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
        //�����ύ��ʽ  
        conn.setDoOutput(true);  
        conn.setDoInput(true);  
        conn.setRequestMethod("POST");  
        //post��ʽ����ʹ�û���  
        conn.setUseCaches(false);  
        conn.setInstanceFollowRedirects(true);  
        //�������ӳ�ʱʱ��  
        conn.setConnectTimeout(6*1000);  
        //���ñ������ӵ�Content-Type������Ϊapplication/x-www-form-urlencoded  
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
        //ά�ֳ�����  
//        conn.setRequestProperty("Connection", "Keep-Alive");  
        //�������������  
        conn.setRequestProperty("Charset", "UTF-8");  
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());  
        //���������������������˷���  
        dos.writeBytes(data);  
        dos.flush();  
        dos.close();  
        if(conn.getResponseCode() == 200){  
            //��÷������������  
            return conn.getInputStream();  
        }  
        return null;  
    }  
      
    //ͨ������������ֽ�����  
    public static byte[] readStream(InputStream is) throws Exception {  
        byte[] buffer = new byte[1024];  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        int len = 0;  
        while((len=is.read(buffer)) != -1){  
            bos.write(buffer, 0, len);  
        }   
        is.close();  
        return bos.toByteArray();  
    }
    
	
	//��ȡ������IP��ַ
	public String getLocalIpAddress() {  
        try {  
            for (Enumeration<NetworkInterface> en = NetworkInterface  
                    .getNetworkInterfaces(); en.hasMoreElements();) {  
                NetworkInterface intf = en.nextElement();  
                for (Enumeration<InetAddress> enumIpAddr = intf  
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
                    InetAddress inetAddress = enumIpAddr.nextElement();  
                    if (!inetAddress.isLoopbackAddress()) {  
                        return inetAddress.getHostAddress().toString();  
                    }  
                }  
            }  
        } catch (SocketException ex) {  
            Log.e("WifiPreference IpAddress", ex.toString());  
        }  
        return null;  
    } 
	
	//�������ϻ�ȡʵʱ��������
	private InputStream readSurveysDataFromInternet()  
    {  
        //�������ϻ�ȡʵʱ��������  
        URL infoUrl = null;  
        InputStream inStream = null;  
        try {  
            infoUrl = new URL("http://earthquake.usgs.gov/earthquakes/catalogs/1day-M2.5.xml");  
            URLConnection connection = infoUrl.openConnection();  
            HttpURLConnection httpConnection = (HttpURLConnection)connection;  
            int responseCode = httpConnection.getResponseCode();  
            if(responseCode == HttpURLConnection.HTTP_OK)  
            {  
                inStream = httpConnection.getInputStream();  
            }  
        } catch (MalformedURLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return inStream;  
    }  
	
	//�ӱ��ػ�ȡ��������  
	private InputStream readSurveysDataFromFile()  
    {  
        //�ӱ��ػ�ȡ��������  
        InputStream inStream = null;  
        try {  
            inStream = this.getAssets().open("test.xml");  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        return inStream;  
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//1����������ʾ���������б���ʾ������б�ĳһ�н���ĳһ���ʾ�Ȼ�����ѡ��
		//��ȡ����������  
        InputStream surveysStream = readSurveysDataFromFile();  
        //Pull��ʽ����xml����  
        PullxmlParser pullHandler = new PullxmlParser();  
        surveysEntryList = pullHandler.SurveysXmlParser(surveysStream);  
        
        
        for (int i = 0; i < surveysEntryList.size(); i ++) {
        	Surveys temp = surveysEntryList.get(i);
        	String tttString =  temp.getSurveyID();
        	String aaaString =  temp.getSurveyName();
        }
        //������ֶ����������
        
        
        //��ListView������ʾ  
        list = (ListView)this.findViewById(R.id.list);  
        adapter = new ArrayAdapter<Surveys>(this, android.R.layout.simple_list_item_1, surveysEntryList);  
        list.setAdapter(adapter);  
	    //ʵ����ʾ����
	    
		//2������ʾ���е�ѡ�⡣Options  Ϊ0����ѡ��
		
		
		//3������ʾ���ж�ѡ�⡣Options  Ϊ1����ѡ��
		
		
		//4������ʾ�����⡣Options  Ϊ2���ı����ԣ�
		
		
		//5��ѡ��һ����л�����һ�⣬�����һ�����ʾ�ύ�𰸣��л����ύ��ť��
		
		
		//6�����û�ѡ��õĴ�ID�������������ϴ�������������xml�ļ���ʽ����Ҫ��֯Ϊxml��ʽ�����ύ��ͷ����ʾ��б�
//		Map<String, PostValue> map= new HashMap<String, PostValue>();
//		//������forѭ���õ��������������Ԫ��
//		PostValue quetion = new PostValue();
//		map.put("question", quetion);
//		//����������·��  
//        String urlPath = "http://ip��ַ:8080";  
//        InputStream is = null;
//		try {
//			is = getInputStreamByPost(urlPath, map, "UTF-8");
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}  
//        byte[] data = null;
//		try {
//			data = readStream(is);
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}  
//        Log.i(TAG, new String(data));
        //������Ҫ�ж��Ƿ��ϴ��ɹ�
		//6
        
        
        
        
//		tvXMLCreate = (TextView) findViewById(R.id.tvXMLCreate);
//		tvXMLResolve = (TextView) findViewById(R.id.tvXMLResolve);
//		
//		
//		
//		PullxmlParser pp = new PullxmlParser();
//		InputStream mInputStream = null;
//		ArrayList<Surveys> mArrayList = null;
//		Surveys surveys = null;
//		
//		ArrayList<Question> mArrayList2 = null;
//		Question qt = null;
//		
//		StringBuffer sb = new StringBuffer();
//		try {
//			mInputStream = this.getAssets().open("test.xml");
//			mArrayList2 = pp.XmlParser(mInputStream);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		PullxmlParser pp = new PullxmlParser();
//		InputStream mInputStream = null;
//		ArrayList<Question> mArrayList = null;
//		Question qt = null;
//		StringBuffer sb = new StringBuffer();
//		try {
//			mInputStream = this.getAssets().open("test.xml");
//			mArrayList = pp.XmlParser(mInputStream);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		for (int i = 0; i < mArrayList2.size(); i++) {
//			qt = mArrayList2.get(i);
//			sb.append(qt.getQuestionID()).append(qt.getQuestionContent())
//			.append(qt.getOptions());
//		}

//		String xml = "";
//		 XMLDom xmlDom = new XMLDom(this);
//		 xml = xmlDom.domCreateXML();
//		 tvXMLCreate.setText(xml);
		//
		// xml = xmlDom.domResolveXML();
		// tvXMLResolve.setText(xml);

		// XMLSax xmlSax = new XMLSax(this);
		// xml = xmlSax.saxCreateXML();
		// tvXMLCreate.setText(xml);
		//
		// xml = xmlSax.saxResolveXML();
		// tvXMLResolve.setText(xml);

//		XMLPull xmlPull = new XMLPull(this);
//		xml = xmlPull.pullXMLCreate();
//		tvXMLCreate.setText(xml);
//
//		xml = xmlPull.pullXMLResolve();
//		tvXMLResolve.setText(sb);
	}
}