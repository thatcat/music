package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;

/**汽车型号类
  */
@Entity
@Table(name="CarModel")
public class CarModel extends Model {

	@Required
    @ManyToOne
    public CarSeries seriesName;

	public String allModelDataString;
    
	/**
    public float officialPrice;

	public float buttomPrice;

	public String carBrand;

	public String carType;

	public String timeToMarket;

	public String engine;

	public String airInflowForm;

	public String maxHorsePower;

	public String maxTorque;

	public String gearbox;

	public String Structure;//整车架构，例如：5门5座两厢车

	public String lengthAndWidthAndHeight;

	public String wheelBase;

	public String maxSpeed;

	public String fuelConsumption;

	public String qualityGuarantee;

	//以下为车身参数
	public String bodyStructure;//车身架构，例如：两厢车

	public String length;

	public String width;

	public String height;

	public String wheelBase2;

	public String  frontWheelThread;

	public String backWheelThread;

	public String minGroupClearance;

	public String weight;

	public String doorCount;

	public String seatCount;

	public String oilTankCapacity;

	public String trunk;

	//以下为发动机参数
	public String engineModel;

	public String displacement;

	public String airInflowForm2;

	public String 

	*/
}
