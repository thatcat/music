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
        		Logined.order_cms();
        }
		List<Post> postList = Post.find("order by id desc").from(0).fetch(3);	  	
		List<CarBrand> carBrandList = CarBrand.findAll();
       render(postList,carBrandList);
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

	public static void search(String carType,String lowPrice,String highPrice) {
		User user = Logined.connected();
		float tempLowPrice=0;
		float tempHighPrice=100000000;
		if( lowPrice.equals("") || lowPrice == "" )
			tempLowPrice=0;
		else
			tempLowPrice=Float.parseFloat(lowPrice)/10000;
		if( highPrice.equals("")||highPrice == "" )
			tempHighPrice=10000000;
		else
			tempHighPrice=Float.parseFloat(highPrice)/10000;
		System.out.println("-----------------------tempHighPrice="+tempHighPrice);
		List<CarSeries> carSeriesList = CarSeries.find("Select c from CarSeries c where carType=? and bottomPrice > ? and bottomPrice <?",carType,tempLowPrice,tempHighPrice).fetch();	
		render("Application/showSeries.html",carSeriesList,user);
	}

	/**点赞*/
	public static void addPraise(Long id,int num) {
		CarBrand carBrand = CarBrand.findById(id);
		carBrand.parise=num;
		carBrand.save();
	}

	public static void showSeries(Long id ) {
		User user = Logined.connected();
		CarBrand carBrand = CarBrand.find("byId",id).first();
		List<CarSeries> carSeriesList = CarSeries.find("byBrandName",carBrand).fetch();
		render(carSeriesList,user);
	}

	/**添加汽车系列点评
	 */
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

	public static void showModels(Long seriesId) {
		User user = Logined.connected();
		CarSeries carSeries = CarSeries.find("byId",seriesId).first();
		CarModel carModel = CarModel.find("bySeriesName",carSeries).first();
		String str;
		if(carModel == null)
			str = "<div align=\"center\"><h2>亲，还没有该系列型号的数据哦..可以先看一下其他系列</h2></div>";
		else {
		 str = carModel.allModelDataString;
		}
		render(str,user);
	}

	public static void notFound() {
		render();
	}

	public static void carNews(Long id) {
		User user = Logined.connected();
		render("Application/carNews"+id+".html",user);
	}

	public static void showAllNews() {
		User user = Logined.connected();
		render(user);
	}
	

}