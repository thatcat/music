package models;

public class SendEmail {
	MailSenderInfo mailInfo = null;

	public SendEmail(String fromEmail, String title, String contentTexts) {
		 mailInfo = new MailSenderInfo();    
	     mailInfo.setMailServerHost("smtp.163.com");    //设置服务器
	     mailInfo.setMailServerPort("25");    //设置端口
	     mailInfo.setValidate(true);    
	     mailInfo.setUserName("cat19890603@163.com");    
	     mailInfo.setPassword("7497304");//您的邮箱密码 
	     mailInfo.setFromAddress("cat19890603@163.com");		    
	     mailInfo.setToAddress("xiusonchan@gmail.com");    
	     mailInfo.setSubject(title + "----from:" + fromEmail);    //设置邮箱标题
	     mailInfo.setContent(contentTexts); //设置邮箱内容
	}
	public void send() {		    
		 //这个类主要来发送邮件 
	     SimpleMailSender sms = new SimpleMailSender();   
	  //       sms.sendTextMail(mailInfo);//发送文体格式  
	         sms.sendHtmlMail(mailInfo);//发送html格式  
	}
}