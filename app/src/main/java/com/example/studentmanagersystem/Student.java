package com.example.studentmanagersystem;

import android.graphics.Bitmap;

import com.alibaba.fastjson.annotation.JSONField;

public class Student {

	@JSONField(serialize = false)
	public Bitmap headimage;

	private String name;
	private String sex;
	private String mingZu;
	private String id;
	private String StudentNumber;
	private String StudentClass;
	private String phone;
	private String StudentDormitory;

	@JSONField(serialize = false)
	private boolean checked;

	public String getStudentClass() {
		return StudentClass;
	}

	public void setStudentClass(String studentClass) {
		StudentClass = studentClass;
	}

	public Student(String id, String name, String sex, String mingZu,
				 String StudentClass, String StudentNumber, String phone, String StudentDormitory) {
		super();
		this.name = name;
		this.sex = sex;
		this.mingZu = mingZu;
		this.id = id;
		this.StudentNumber = StudentNumber;
		this.StudentClass = StudentClass;
		this.phone = phone;
		this.StudentDormitory = StudentDormitory;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMingZu() {
		return mingZu;
	}

	public void setMingZu(String mingZu) {
		this.mingZu = mingZu;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStudentNumber() {
		return StudentNumber;
	}

	public void setStudentNumber(String StudentNumber) {
		this.StudentNumber = StudentNumber;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStudentDormitory() {
		return StudentDormitory;
	}

	public void setStudentDormitory(String StudentDormitory) {
		this.StudentDormitory = StudentDormitory;
	}
}
