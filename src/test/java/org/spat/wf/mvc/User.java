package org.spat.wf.mvc;

public class User {
	
	private String name;
	private int age;
	private String company;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	@Override
	public String toString() {
		return "User [name=" + name + ", age=" + age + ", company=" + company
				+ "]";
	}

}

