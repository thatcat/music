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
	@Before(only={"music_cms","post_cms","deletePost","deleteComment","deal_refresh","auth_delete","addSeries","saveCarSeries"}) 
	static void checkAdmin() { 
		User user = connected();
		if(user == null)
			Application.login();
		if(user.isAdmin != true) {
			notFound();
        }
	}

    public static void logined() {
		Application.index();
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
       render();
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

		/**上传音乐,music为上传的音乐
		*/
		 public static void uploadMusic( File musicFile,String musicName,String player, String albums,String type,String publicDate) {
			String outputPath = Play.applicationPath.toString()+"\\public\\music\\";
			try
			{		
			File outputFile = new File(outputPath+musicFile.getName());
			FileOutputStream fos = new FileOutputStream(outputFile);
			FileInputStream fis = new FileInputStream(musicFile);

			byte[] buffer = new byte[10240];
			int len = 0;
			while ((len = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			fis.close();
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
			 
			 Music music = new Music();
			 music.musicName = musicName;
			 music.player = player;
			 music.albums = albums;
			 music.type = type;
			 //music.publicDate = 
			 music.uploadDate = new Date ();
			 music.path = "public\\music\\"+musicFile.getName();
			 music.save();
			 System.out.println("----------------------------music.id="+music.id);
			 System.out.println("----------------------------music.path="+music.path);
			 System.out.println("----------------------------music.playCount="+music.playCount);
			music_cms();
	 }
	 
	 
	/**上传音乐,music为上传的音乐
	
	public static void uploadMusic( File music) {
			String outputPath = Play.applicationPath.toString()+"\\public\\attachment\\";
			try
			{		
			File storeFile=new File(outputPath+music.getName());
			flash.success("上传成功" );
			
			File.copy(music, storeFile);
			
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
			music_customer();
	 }
	 */

	 	 

	public static void search() {
		
	}

	public static void deleteMusic() {
		List<Music> allMusicList=Music.findAll();
		render(allMusicList);
	}


}