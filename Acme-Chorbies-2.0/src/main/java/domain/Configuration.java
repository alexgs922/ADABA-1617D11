
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	//Constructors ---------------------------------------------------------------------------

	public Configuration() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private int	hour;
	private int	minute;
	private int	second;
	private double managersFee;
	private double chorbiesFee;

	//Getters & Setters ----------------------------------------------------------------------

	public int getHour() {
		return this.hour;
	}

	public void setHour(final int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return this.minute;
	}

	public void setMinute(final int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return this.second;
	}

	public void setSecond(final int second) {
		this.second = second;
	}

	public double getManagersFee() {
		return managersFee;
	}

	public void setManagersFee(double managersFee) {
		this.managersFee = managersFee;
	}

	public double getChorbiesFee() {
		return chorbiesFee;
	}

	public void setChorbiesFee(double chorbiesFee) {
		this.chorbiesFee = chorbiesFee;
	}

	
	
}
