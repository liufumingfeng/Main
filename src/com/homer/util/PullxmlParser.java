package com.homer.util;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

import com.homer.model.Question;
import com.homer.model.Surveys;
import com.homer.model.Answer;

public class PullxmlParser {

	//xml�����õ���Tag  
    private String kSurveyElementName = "Survey";  
    private String kSurveyNameElementName = "SurveyName"; 
    private String kSurveyIDElementName = "SurveyID"; 
    private String kQuestionListElementName = "QuestionList";
    private String kQuestionElementName = "Question";  
    private String kQuestionIDElementName = "QuestionID";  
    private String kQuestionContentElementName = "QuestionContent";  
    private String kOptionsElementName = "Options"; 
    private String kAnswerListElementName = "AnswerList";
    private String kAnswerElementName = "Answer";
    private String kAnswerIDElementName = "AnswerID";  
    private String kAnswerContentElementName = "AnswerContent";  
    
    //���ڱ���xml������ȡ�Ľ��
    private Answer mAnswer = null;
    
	private ArrayList<Question> mQuestionList = null;
	private Question mQuestion = null;
	
	private ArrayList<Surveys> mSurveyList = null;
	private Surveys mSurvey = null;
	private Boolean startEntryElementFlag = false;
	
	
	public ArrayList<Surveys> SurveysXmlParser(InputStream mInputStream) {
		XmlPullParserFactory pullFactory;
		XmlPullParser xmlPullParser;
		try {
			pullFactory = XmlPullParserFactory.newInstance();
			xmlPullParser = pullFactory.newPullParser();
			xmlPullParser.setInput(mInputStream, "UTF-8");

			int mEvtentType = xmlPullParser.getEventType();
			Boolean isDone = false;
			
			while ((mEvtentType != XmlPullParser.END_DOCUMENT)&&(isDone != true)) {
				String DocumentCode = null;
				switch (mEvtentType) {

				case XmlPullParser.START_DOCUMENT:
					mSurveyList = new ArrayList<Surveys>();
					break;
				case XmlPullParser.START_TAG:
					
					DocumentCode = xmlPullParser.getName();
					System.out.println("DocumentCode:" + DocumentCode);

					if (DocumentCode.equalsIgnoreCase(kSurveyElementName)) {
						Log.e("Survey", "Survey");
						mSurvey = new Surveys();
						startEntryElementFlag = true;  
					} else if (startEntryElementFlag == true) {
						String currentData = null;  
                        if(DocumentCode.equalsIgnoreCase(kSurveyNameElementName))  
                        {  
                            currentData = xmlPullParser.nextText();  
//                          Log.v("Pull", currentData);  
                            //��ȡ�ʾ�id
                            mSurvey.setSurveyName(currentData);
                        } else if (DocumentCode.equalsIgnoreCase(kSurveyIDElementName)) {
                        	currentData = xmlPullParser.nextText();
							mSurvey.setSurveyID(currentData);
						} else if (DocumentCode.equalsIgnoreCase(kQuestionListElementName)) {
							mSurvey.QuestionList = new ArrayList<Question>();
							break;
						} else if (DocumentCode.equalsIgnoreCase(kQuestionElementName)) {
							mQuestion = new Question();
							break;
						} else if (DocumentCode.equalsIgnoreCase(kQuestionIDElementName)) {
							currentData = xmlPullParser.nextText();
							mQuestion.setQuestionID(currentData);
						} else if (DocumentCode.equalsIgnoreCase(kQuestionContentElementName)) {
							currentData = xmlPullParser.nextText();
							mQuestion.setQuestionContent(currentData);
						} else if (DocumentCode.equalsIgnoreCase(kOptionsElementName)) {
							currentData = xmlPullParser.nextText();
							mQuestion.setOptions(currentData);
						} else if (DocumentCode.equalsIgnoreCase(kAnswerListElementName)) {
							mQuestion.answerList = new ArrayList<Answer>();
							break;
						} else if (DocumentCode.equalsIgnoreCase(kAnswerElementName)) {
							mAnswer = new Answer();
							break;
						} else if (DocumentCode.equalsIgnoreCase(kAnswerIDElementName)) {
							currentData = xmlPullParser.nextText();
							mAnswer.setAnswerID(currentData);
						} else if (DocumentCode.equalsIgnoreCase(kAnswerContentElementName)) {
							currentData = xmlPullParser.nextText();
							mAnswer.setAnswerContent(currentData);
						}
					}
				case XmlPullParser.END_TAG:
					if (xmlPullParser.getName().equalsIgnoreCase(kSurveyElementName)) {
						Log.e("End", "End    End   End");
						System.out.println("End");
						mSurveyList.add(mSurvey);
					}
					if (xmlPullParser.getName().equalsIgnoreCase(kQuestionListElementName)) {
						mSurvey.QuestionList.add(mQuestion);
					}
					if (xmlPullParser.getName().equalsIgnoreCase(kAnswerListElementName)) {
						mQuestion.answerList.add(mAnswer);
					}
					break;
				}
				mEvtentType = xmlPullParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mSurveyList;
	}
	
	public ArrayList<Question> XmlParser(InputStream mInputStream) {

		XmlPullParserFactory pullFactory;
		XmlPullParser xmlPullParser;
		try {
			pullFactory = XmlPullParserFactory.newInstance();
			xmlPullParser = pullFactory.newPullParser();

			xmlPullParser.setInput(mInputStream, "UTF-8");

			int mEvtentType = xmlPullParser.getEventType();

			while (mEvtentType != XmlPullParser.END_DOCUMENT) {
				String DocumentCode = null;
				switch (mEvtentType) {

				case XmlPullParser.START_DOCUMENT:
					mQuestionList = new ArrayList<Question>();
					break;
				case XmlPullParser.START_TAG:

					DocumentCode = xmlPullParser.getName();
					System.out.println("DocumentCode:" + DocumentCode);

					if (DocumentCode.equals("Question")) {
						Log.e("Question", "Question");
						mQuestion = new Question();
					}
					
					if (DocumentCode.equals("QuestionID")) {
						mQuestion.setQuestionID(xmlPullParser.nextText());
					}
					
					if (DocumentCode.equals("QuestionContent")) {
						mQuestion.setQuestionContent(xmlPullParser.nextText());
					}
					if (DocumentCode.equals("Options")) {
						mQuestion.setOptions(xmlPullParser.nextText());
					}

//					if (DocumentCode.equals("AnswerID")) {
//						mQuestion.setAnswerID(xmlPullParser.nextText());
//					}
//
//					if (DocumentCode.equals("AnswerContent")) {
//						mQuestion.setAnswerContent(xmlPullParser.nextText());
//					}
					break;

				case XmlPullParser.END_TAG:

					if (xmlPullParser.getName().equals("Question")) {
						Log.e("End", "End    End   End");
						System.out.println("End");
						mQuestionList.add(mQuestion);
					}
					break;
				}
				mEvtentType = xmlPullParser.next();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mQuestionList;
	}

}
