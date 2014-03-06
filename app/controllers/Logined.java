package controllers;

import java.util.*;
import play.mvc.*;
import play.data.validation.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.io.*;
import java.nio.file.Files;
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
		List<CarBrand> carBrandList = CarBrand.findAll();
       render(postList,carBrandList);
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
    
    public static void order_cms(){
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

	/**各个页面添加评论的通用代码
	*/
	public static void originComment (Long postId, String content ) {
        Post post = Post.findById(postId);
		User author = null;
		String userName = session.get("user");
		 if(userName != null) {
			 author=User.find("byUsername", userName).first();
        } 
		Comment comment = new Comment(post,author,content);
		comment.save();
        flash.success("Thanks for posting %s", author);

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

	 public static void order_customer(){
       render();
    }

	public static void changePassword(String newPassword, String verifyNewPassword) {
		validation.required(verifyNewPassword);
        validation.equals(verifyNewPassword, newPassword).message("Your password doesn't match");
        if(validation.hasErrors()) {
            render("@Logined.order_customer", verifyNewPassword);
         }
		 User user = connected();
		 newPassword=Md5Util.getMD5Str(newPassword);				//MD5加密
		 user.password=newPassword;
		 user.save();
		  flash.success("修改成功" );
		  order_customer();
		}

		/**上传图片,photo为车辆证明材料照片文件，idcard为身份证照片文件
		  */
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
			order_customer();
	 }

	/**处理认证通不通过，isCarOwner为true表示通过，为false表示不通过
	  */
	public static void deal_refresh(long itemId, String authType,boolean isCarOwner){
    	//找到对应数据项
    	User user = User.findById(itemId);
		// 是否已经被处理设为true
		user.isDeal=true; 
		//这次认证通过
		if(isCarOwner == true) {
			user.authType=authType;
			user.isCarOwner=true;
			user.integration=user.integration+20;
		user.authDate=new Date();
		}
    	user.save();
    	Map map = new HashMap();
    	int status = 200;
    	map.put("status", status);
    	renderJSON(map);
    }

	/**取消认证
	  */
	public static void auth_delete(long itemId) {
    	//找到对应数据项
		int status=0;
    	User user = User.findById(itemId);
		if(user != null) {
		user.isDeal=true;
		user.isCarOwner=false;
		user.authType=null;
		user.save();
		status=200;
		}
		Map map = new HashMap();    	
   		map.put("status", status);
    	renderJSON(map); 	
	}

	public static void search(String carType,String lowPrice,String highPrice) {
		Application.search( carType, lowPrice, highPrice);
	}

	public static void addSeries(String message,Long seriesId) {
		render(message,seriesId);	
	}

	public static void saveCarSeries(CarSeries carSeries, Long carBrandId) {
		CarSeries carSeriesExisted = CarSeries.find("bySeriesName", carSeries.seriesName).first(); 
		String message = "";
		System.out.println("------------------------------------carSeries.path1:"+carSeries.path1);
		System.out.println("------------------------------------carSeries.path1:"+carSeries.path2);
		System.out.println("------------------------------------carSeries.path1:"+carSeries.path3);
		System.out.println("------------------------------------carSeries.path1:"+carSeries.path4);
		if(carSeriesExisted != null)  {
			message = "系列已经存在！添加失败";
			addSeries(message,carSeries.id);
		}
		CarBrand carBrand = CarBrand.find("byId", carBrandId).first();
		carSeries.brandName=carBrand;
		
		carSeries.save();
		message = "添加成功！";
		addSeries(message,carSeries.id);			
	}

	public static void addModel(String message,Long seriesId) {
		render(message,seriesId);	
	}

	public static void addCarModel(String seriesName , String carModelDataString) {
		String insert = "";
		int car_len;
		int para_len;
		String [] parameterName={"型号","官方价","销售商最低报价","厂商","级别","上市时间","发动机","进气形式","最大马力(PS)","最大扭矩(N·m)","变速箱","车身结构","长x宽x高(mm)" ,"轴距(mm)","最高车速(km/h)","工信部综合油耗(L/100km)","整车质保","车身结构","长度(mm)","宽度(mm)","高度(mm)","轴距(mm)","前轮距(mm)" 
														,"后轮距(mm)","最小离地间隙(mm)","车重(kg)","车门数","座位数","油箱容量","行李箱容积","发动机型号","排量(ml)","进气形式" ,"最大马力","最大功率(kw)","最大功率转速(rpm)","最大扭矩(N·m)","最大扭矩转速(rpm)","气缸排列形式","气缸数","每缸气门数","压缩比","配气机构","燃料形式"
														,"燃料标号","供油方式","缸盖材料","缸体材料","排放标准","变速箱简称","档位个数","变速箱类型","驱动方式","前悬挂类型" ,"后悬挂类型","转向助力类型","车体结构","前制动器类型","后制动器类型","驻车制动类型","前轮胎规格","后轮胎规格","备胎规格"};

		String [] car = carModelDataString.split("@");
		String [] tmp = car[0].split(",");//某个型号的参数数组
		para_len = tmp.length;
		car_len = car.length;
		String [][] list1 =new String  [car_len][para_len];
		String [][] list2 = new String  [para_len][car_len];
		for (int i=0;i<car.length;i++) {
			String [] para = car[i].split(",");
			for (int j=0; j<para.length; j++) {
				list1[i][j] = para[j];
			}
		}
		for (int i=0;i<para_len;i++) {
			for (int j=0; j<car_len; j++) {
				list2[i][j] = list1[j][i];
			}
		}
		for (int i=0;i<para_len; i++) {
			insert += "<tr>";
			insert += "<th>"+parameterName[i]+"</th>";
			for (int j=0; j<car_len;j++) {
				insert += "<td>" + list2[i][j] + "</td>";
			}
			insert += "</tr>";
		}

		CarSeries carSeries = CarSeries.find("bySeriesName",seriesName).first();
		CarModel carModel = new CarModel();
		carModel.seriesName = carSeries;
		carModel.allModelDataString = insert;
		carModel.save();
		String message="添加成功";
		addModel(message,carSeries.id);	
	}

	public static void carNews() {
		render();
	}

}