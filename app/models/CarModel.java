package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;

/**�����ͺ���
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

	public String Structure;//�����ܹ������磺5��5�����ᳵ

	public String lengthAndWidthAndHeight;

	public String wheelBase;

	public String maxSpeed;

	public String fuelConsumption;

	public String qualityGuarantee;

	//����Ϊ�������
	public String bodyStructure;//����ܹ������磺���ᳵ

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

	//����Ϊ����������
	public String engineModel;

	public String displacement;

	public String airInflowForm2;

	public String 

	*/
}
