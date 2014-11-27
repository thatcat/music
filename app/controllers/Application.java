package controllers;

import play.mvc.*;
import play.data.validation.*;
import java.util.*;
import models.*;
import javax.persistence.*;
import play.db.jpa.*;

public class Application extends Controller {

    public static void index() {
    	Author author = Logined.connected();
        if(author != null && author.isAdmin == true) {
            //转跳到管理员页面
        		Logined.music_cms();
        }
	//	Query query = JPA.em().createQuery("select id,content,postedAt,author_id from post order by id desc");
	//	List<Post> postList = query.getResultList();
	//	String sql = "select id,content,postedAt,author_id from post order by id desc";
		String sql = "select * from post order by id desc";
	//	String sql = "select authorName,password,email, isAdmin,registerDate from `author`";
		Query query = Model.em().createNativeQuery(sql,Post.class);
		List<Post> postList = query.getResultList();
		
	//	List<Post> postList = Post.find("order by id desc").from(0).fetch(3);
		List<Music> guoYuList=Music.find("byTypeOrderByPlayCount",1).from(0).fetch(15);
		List<Music> yueYuList=Music.find("byTypeOrderByPlayCount",2).from(0).fetch(15);
		List<Music> yingWenList=Music.find("byTypeOrderByPlayCount",3).from(0).fetch(15);
		List<Music> hanWenList=Music.find("byTypeOrderByPlayCount",4).from(0).fetch(15);


       render(author,postList,yueYuList,guoYuList,yingWenList,hanWenList);
		render();
    }

    public static void register(String message) {
        render(message);
    }
    
    public static void saveAuthor(@Valid Author author, String verifyPassword) {
		verifyPassword=Md5Util.getMD5Str(verifyPassword);		// MD5加密
		author.password=Md5Util.getMD5Str(author.password);
        Author authorExisted = Author.find("byAuthorName", author.authorName).first(); 	
        if(authorExisted != null){
            validation.required(author.authorName);
            validation.equals(author.authorName,"1").message("This author is existed!");
            if(validation.hasErrors()) {
                String message = "用户已经存在！";
                register(message);
            }

        }
        validation.required(verifyPassword);
        validation.equals(verifyPassword, author.password).message("密码不一致");
        if(validation.hasErrors()) {
            render("@register", author, verifyPassword);
         }
        author.isAdmin = false;
		author.registerDate = new Date();
        author.create();
        session.put("author", author.authorName);
        flash.success("Welcome, " + author.authorName);
        index();
    }
    
    public static void login(){
        render();
    }

    public static void logined(String authorName, String password) {
		password=Md5Util.getMD5Str(password);				//MD5加密
        Author author = Author.find("byAuthornameAndPassword", authorName, password).first();
        if(author != null) {
            session.put("author", author.authorName);
            flash.success("Welcome, " + author.authorName);
            index();         
        }
        // Oops
        flash.put("authorName", authorName);
        flash.error("用户不存在或者密码错误！");
        login();
    }


	/**点赞
	public static void addPraise(Long id,int num) {
		CarBrand carBrand = CarBrand.findById(id);
		carBrand.parise=num;
		carBrand.save();
	}
	*/

	/**添加汽车系列点评
	
	public static void addCarComment(Long id, String carCommentType ) {
		if(Logined.connected()==null)
		{
			 flash.error("请先登录！");
            //跳转到登录画面
            Application.login();
        }		   
		Author author=null;
        String authorName = session.get("author");  
		 if(authorName != null) {
            author=Author.find("byAuthorname", authorName).first();
        } 
		CarSeries carSeries = CarSeries.find("byId",id).first();
		Date carCommentDate = new Date();
		CarComment carComment = new CarComment(carCommentType);
		carComment.carCommentTime=carCommentDate;
		carComment.seriesName=carSeries;
		carComment.author=author;
		if(author.isCarOwner == true)
			author.integration=author.integration+2;
		else
			author.integration=author.integration+1;
		author.save();
		carComment.save();
		showSeries(id);
	}
 */

 	/**添加音乐点评
	 */
	public static void addMusicComment(Long id, String musicCommentType ) {  
			if(Logined.connected()==null)
		{
			 flash.error("请先登录！");
            //跳转到登录画面
            Application.login();
        }		   
		
		Author author=null;
        String authorName = session.get("author");  
		 if(authorName != null) {
            author=Author.find("byAuthorname", authorName).first();
        } 
		Music music = Music.find("byId",id).first();	 
		MusicComment musicComment = new MusicComment();
		musicComment.commentContent = musicCommentType;
		musicComment.commentTime=new Date();
		musicComment.music=music;
		musicComment.author=author;
		author.integration=author.integration+2;
		author.save();
		musicComment.save();
		
	}
	
	/**歌曲搜索
	 */
	public static void search(String name, String player, String lyric ) { 

	}	

}