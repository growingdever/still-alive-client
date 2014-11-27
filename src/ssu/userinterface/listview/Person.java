package ssu.userinterface.listview;

public class Person {
	
	private int _id= 0 ;
	private String _name = "";
	private String _time = "";
	
	public Person(){
		
	}
	
	public void setId(int id){
		_id = id;
	}
	
	public void setName(String name){
		_name = name;
	}

	public void setTime(String time){
		_time = time;
	}

	public int getId(){
		return _id;
	}
	
	public String getName(){
		return _name;
	}
	
	public String getTime(){
		return _time;
	}
}
