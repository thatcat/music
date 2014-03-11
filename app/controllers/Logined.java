package controllers;

import java.util.*;
import play.mvc.*;
import play.data.validation.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.io.*;
//import java.nio.file.Files;
import java.text.SimpleDateFormat;
import play.*;
import models.*;

public class Logined extends Controller {
    @Before
    static void addUser() {
        User user = connected();
        if(user != null) {
            renderArgs.put("user", user);
        }
    }
    
    static User connected() {
        if(renderArgs.get("user") != null) {
            return renderArgs.get("user", User.class);
        }
        String username = session.get("user");
        if(username != null) {
            return User.find("byUsername", username).first();
        } 
        return null;
    }

	@Before
	static void checkLogined() { 
		    if(connected() == null) {
            //跳转到登录画面
            Application.login();
        }
	}
	
	/**添加管理员权限拦截，不是管理员，不能执行以下字符串数组内的方法*/
	@Before(only={"order_cms","post_cms","deletePost","deleteComment","deal_refresh","auth_delete","addSeries","saveCarSeries"}) 
	static void checkAdmin() { 
		User user = connected();
		if(user.isAdmin != true) {
			notFound();
        }
	}

    public static void logined() {
		List<Post> postList = Post.find("order by id desc").from(0).fetch(3);	
       render(postList);
    }
    
    public static void logout() {
        session.clear();
        Application.index();
    }
    
    public static void email(String message) {
    	render(message);
    }

    public static void sendEmail(@Valid @Email String fromEmail,String title, String contentTexts){
		try
		{
		SendEmail sendEmailInfo = new SendEmail(fromEmail, title, contentTexts);
        sendEmailInfo.send();
        email("发送成功！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		email("发送失败，请登录你的邮箱发送到xiusonchan@gmail.com 谢谢！");
    }
    
    public static void music_cms(){
		 //还没有处理的车主认证
    	List<User> userUndealList = User.find("byIsDealAndAuthPictiurePathIsNotNull",false).fetch();
    	//已经处理的车主认证
    	List<User> userDealedList = User.find("byIsDealAndAuthPictiurePathIsNotNull",true).fetch();
       render(userUndealList,userDealedList);
    }

	public static void savePostOrigin(Post post) {

	}


/**
  *在“更多留言”保存留言的方法
  */
    public static void savePost( Post post) {
		User author=null;
        String userName = session.get("user");  
		
		 if(userName != null) {
            author=User.find("byUsername", userName).first();
        } 
	
        post.author = author;  
		post.postedAt= new Date();
        // Save
        post.save();     
		flash.success("Thanks for posting %s", author);
		words_board();
    }

/**
  *在首页保存留言的方法
  */
	public static void savePost2( Post post) {
		User author=null;
        String userName = session.get("user");  
		
		 if(userName != null) {
            author=User.find("byUsername", userName).first();
        } 
	
        post.author = author;  
		post.postedAt= new Date();
        // Save
        post.save();     
		flash.success("Thanks for posting %s", author);
		logined();
    }

/**
  *管理员页面保存留言的方法
  */
	 public static void savePostForCMS( Post post) {
		User author=null;
        String userName = session.get("user");  
		
		 if(userName != null) {
            author=User.find("byUsername", userName).first();
        } 
	
        post.author = author;  
		post.postedAt= new Date();
        // Save
        post.save();     
		flash.success("Thanks for posting %s", author);
		post_cms();
		
    }

	public static void words_board(){
    	//从form+1开始返回后面的fetch条记录
    	List<Post> postList = Post.find("order by id desc").fetch();  	
        render(postList);
    }


/**
  *在首页的留言评论
  */
	 public static void postComment(Long postId, String content ) {
        Post post = Post.findById(postId);
		User author = null;
		String userName = session.get("user");
		 if(userName != null) {
			 author=User.find("byUsername", userName).first();
        } 
		Comment comment = new Comment(post,author,content);
		comment.save();
        flash.success("Thanks for posting %s", author);
        words_board();
    }

/**
  *在留言板的留言评论
  */
	 public static void postComment2(Long postId, String content) {
        Post post = Post.findById(postId);
		User author = null;
		String userName = session.get("user");
		 if(userName != null) {
			 author=User.find("byUsername", userName).first();
        } 
		Comment comment = new Comment(post,author,content);
		comment.save();
        flash.success("Thanks for posting %s", author);
		logined();
    }

/**
  *管理员的留言评论
  */
	 public static void postCommentForCMS(Long postId, String content) {
        Post post = Post.findById(postId);
		User author = null;
		String userName = session.get("user");
		 if(userName != null) {
			 author=User.find("byUsername", userName).first();
        } 
		Comment comment = new Comment(post,author,content);
		comment.save();
        flash.success("Thanks for posting %s", author);
        post_cms();
    }

	public static void post_cms( ) {
		List<Post> postList = Post.find("order by id desc").fetch();
       render(postList);
	}

	 public static void deletePost( Long postId) {
        Post post = Post.findById(postId);
		post.delete();
        post_cms();
    }

	 public static void deleteComment(Long commentId) {
		Comment comment=Comment.findById(commentId);
		comment.delete();
        post_cms();
    }

	 public static void music_customer(){
       render();
    }

	public static void changePassword(String newPassword, String verifyNewPassword) {
		validation.required(verifyNewPassword);
        validation.equals(verifyNewPassword, newPassword).message("Your password doesn't match");
        if(validation.hasErrors()) {
            render("@Logined.music_customer", verifyNewPassword);
         }
		 User user = connected();
		 newPassword=Md5Util.getMD5Str(newPassword);				//MD5加密
		 user.password=newPassword;
		 user.save();
		 flash.success("修改成功" );
		 music_customer();
		}

		/**上传图片,photo为车辆证明材料照片文件，idcard为身份证照片文件
	
		 public static void uploadPhoto( File photo, File idcard) {
			String outputPah = Play.applicationPath.toString()+"\\public\\attachment\\";
			try
			{		
			File outputFile = new File(outputPah+photo.getName());
			FileOutputStream fos = new FileOutputStream(outputFile);
			FileInputStream fis = new FileInputStream(photo);

			File outputIdCard = new File(outputPah+idcard.getName());
			FileOutputStream fos2 = new FileOutputStream(outputIdCard);
			FileInputStream fis2 = new FileInputStream(idcard);

			byte[] buffer = new byte[10240];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();

			len=0;
			while((len=fis2.read(buffer))>0) {
				fos2.write(buffer,0,len);
			}
			fis2.close();
			fos2.close();

			 flash.success("上传成功" );
			}
			catch (FileNotFoundException  e )
			{
				e.printStackTrace(); 
			}
			catch (IOException  e )
			{
				e.printStackTrace(); 
			}
			catch(NullPointerException e)
			 {
				e.printStackTrace(); 
			 }

			User user = connected();
			if(photo!=null)
				user.authPictiurePath="public\\attachment\\"+photo.getName();
			if(idcard!=null)
				user.idcardPictiurePath="public\\attachment\\"+idcard.getName();
			user.isDeal=false;
			user.applyDate=new Date();
			user.save();	
			music_customer();
	 }

	 	  */

	public static void search() {
		
	}


}