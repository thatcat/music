package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;

/**汽车系列类，一个品牌有多条点评和多个型号
  */
@Entity
@Table(name="CarSeries")
public class CarSeries extends Model {
    
    @Required
    @MaxSize(15)
    public String seriesName;
    
    @Required
    public String path1;//汽车前右图片路径

	@Required
    public String path2;//汽车左视图片路径

	@Required
    public String path3;//汽车后视图片路径

	@Required
    public String path4;//汽车内部图片路径

    public float bottomPrice;//最低价

	public String fuelConsumption;//油耗

	public float startLevel;//星级

	@Required
	public String carType;//车型

	@Required
    @ManyToOne
    public CarBrand brandName;

	@OneToMany(mappedBy="seriesName", cascade=CascadeType.ALL)
    public List<CarModel> carModels;

	@OneToMany(mappedBy="seriesName", cascade=CascadeType.ALL)
    public List<CarComment> carComments;

    public CarSeries(String seriesName,String path1,String path2,String path3,String path4, float bottomPrice,String fuelConsumption,float startLevel, String carType) {
	this.seriesName=seriesName;
	this.path1=path1;
	this.path2=path2;
	this.path3=path3;
	this.path4=path4;
	this.bottomPrice=bottomPrice;
	this.fuelConsumption=fuelConsumption;
	this.startLevel=startLevel;
	this.carType=carType;
    }

    public String toString()  {
        return seriesName ;
    }
}
