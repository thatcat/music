package controllers;

import play.mvc.*;
import play.data.validation.*;
import java.util.*;
import models.*;

public class Application extends Controller {

    public static void index() {
    	User user = Logined.connected();
        if(user != null) {
            //跳转到登录画面
        	if(user.isAdmin == false)
        		Logined.logined();
        	else
        		Logined.music_cms();
        }
		List<Post> postList = Post.find("order by id desc").from(0).fetch(3);
		List<Music> yueYuList=Music.find("byTypeOrderByPlayCount","国语歌").from(0).fetch(15);
		List<Music> guoYuList=Music.find("byTypeOrderByPlayCount","粤语歌").from(0).fetch(15);
		List<Music> yingWenList=Music.find("byTypeOrderByPlayCount","英文歌").from(0).fetch(15);
		List<Music> hanWenList=Music.find("byTypeOrderByPlayCount","韩语歌").from(0).fetch(15);
		
       render(postList);
    }

    public static void register(String message) {
        render(message);
    }
    
    public static void saveUser(@Valid User user, String verifyPassword) {
		verifyPassword=Md5Util.getMD5Str(verifyPassword);		// MD5加密
		user.password=Md5Util.getMD5Str(user.password);
        User userExisted = User.find("byUsername", user.username).first(); 	
        if(userExisted != null){
            validation.required(user.username);
            validation.equals(user.username,"1").message("This user is existed!");
            if(validation.hasErrors()) {
                String message = "用户已经存在！";
                register(message);
            }
        }
        validation.required(verifyPassword);
        validation.equals(verifyPassword, user.password).message("密码不一致");
        if(validation.hasErrors()) {
            render("@register", user, verifyPassword);
         }
        user.isAdmin = false;
		user.isCarOwner=false;
        user.create();
        session.put("user", user.username);
        flash.success("Welcome, " + user.username);
        index();
    }
    
    public static void login(){
        render();
    }

    public static void logined(String username, String password) {
		password=Md5Util.getMD5Str(password);				//MD5加密
        User user = User.find("byUsernameAndPassword", username, password).first();
        if(user != null) {
            session.put("user", user.username);
            flash.success("Welcome, " + user.username);
            index();         
        }
        // Oops
        flash.put("username", username);
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
		User author=null;
        String userName = session.get("user");  
		 if(userName != null) {
            author=User.find("byUsername", userName).first();
        } 
		CarSeries carSeries = CarSeries.find("byId",id).first();
		Date carCommentDate = new Date();
		CarComment carComment = new CarComment(carCommentType);
		carComment.carCommentTime=carCommentDate;
		carComment.seriesName=carSeries;
		carComment.user=author;
		if(author.isCarOwner == true)
			author.integration=author.integration+2;
		else
			author.integration=author.integration+1;
		author.save();
		carComment.save();
		showSeries(id);
	}
 */

}