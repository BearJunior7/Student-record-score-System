package com.example.studentmanagersystem;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

public class StudentInformationActivity extends Activity {

	public static Student student;
	public static String course = "数据库";
	private int op_imageid[] = { R.drawable.addstudent_image,
			R.drawable.deletestudent_image, R.drawable.refreshstudent_image,
			R.drawable.user_image, R.drawable.sms_all };
	private String str[] = { "新增", "删除" };
	private ListView listView;
	private View visView;
	private boolean flag = true;
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private List<Student> students = new ArrayList<Student>();
	private ListView stu_listview;
	private CheckBox checkboxsum;
	private boolean chboxall = false;
	private ArrayList<CheckBox> cbs = new ArrayList<CheckBox>();
	private String updateName;
	private AutoCompleteTextView serach_edit;
	private List<String> lists = new ArrayList<String>();
	private ActionBar actionBar;
	private JSONArray jsonArray;
	private ImageView stu_headimage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_information);

		mySqlHelper = new MySqlHelper(StudentInformationActivity.this,
				"student_inf.db", null, 2);
		db = mySqlHelper.getWritableDatabase();

		actionBar = getActionBar();

		Log.d("0", "StudentInformationActivity onCreate d=" + JSON.toJSONString(student, true));


		init_Sxcj_Button();
		init_Tjcj_Button();
		init_Ywcj_Button();
		init_Yycj_Button();
		showInformation();


	}

	public android.widget.Button tjcjButton;

	public void tjcj_Button_click(){
		Intent intent = new Intent(this, AddScoreActivity.class);
		startActivity(intent);
	}

	public void tjcj_Button_long_click(){

	}


	public void init_Tjcj_Button(){

		tjcjButton = (android.widget.Button)findViewById(R.id.tjcj);

		tjcjButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				tjcj_Button_click();
			}
		} );
		tjcjButton.setOnLongClickListener( new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				tjcj_Button_long_click();
				return true;
			}
		} );


	}



	public android.widget.Button ywcjButton;

	public void ywcj_Button_click() {
		course = "数据库";
		Intent intent = new Intent(this, ScoreActivity.class);
		startActivity(intent);
	}

	public void ywcj_Button_long_click(){

	}


	public void init_Ywcj_Button(){

		ywcjButton = (android.widget.Button)findViewById(R.id.ywcj);

		ywcjButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ywcj_Button_click();
			}
		} );
		ywcjButton.setOnLongClickListener( new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				ywcj_Button_long_click();
				return true;
			}
		} );


	}



	public android.widget.Button sxcjButton;

	public void sxcj_Button_click(){
		course = "安卓";
		Intent intent = new Intent(this, ScoreActivity.class);

		startActivity(intent);
	}

	public void sxcj_Button_long_click(){

	}


	public void init_Sxcj_Button(){

		sxcjButton = (android.widget.Button)findViewById(R.id.sxcj);

		sxcjButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sxcj_Button_click();
			}
		} );
		sxcjButton.setOnLongClickListener( new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				sxcj_Button_long_click();
				return true;
			}
		} );


	}



	public android.widget.Button yycjButton;

	public void yycj_Button_click(){
		course = "c语言";
		Intent intent = new Intent(this, ScoreActivity.class);

		startActivity(intent);
	}

	public void yycj_Button_long_click(){

	}


	public void init_Yycj_Button(){

		yycjButton = (android.widget.Button)findViewById(R.id.yycj);

		yycjButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				yycj_Button_click();
			}
		} );
		yycjButton.setOnLongClickListener( new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				yycj_Button_long_click();
				return true;
			}
		} );


	}
	public void toHomePage() {
		Intent intent = new Intent(StudentInformationActivity.this,
				MainActivity.class);
		startActivity(intent);

	}
	public void searchStudent() {
		Intent intent = new Intent(StudentInformationActivity.this,
				SearchStudentActivity.class);
		startActivity(intent);

	}


	public void showInformation() {
		Log.d("0", "StudentInformationActivity showInformation student=" + JSON.toJSONString(student, true));

		final EditText sname = (EditText) findViewById(R.id.sname);
		final EditText ssex = (EditText) findViewById(R.id.ssex);
		final EditText StudentClass = (EditText) findViewById(R.id.StudentClass);
		final EditText StudentNumber = (EditText) findViewById(R.id.StudentNumber);
		final EditText sphone = (EditText) findViewById(R.id.sphone);
		final EditText StudentDormitory = (EditText) findViewById(R.id.StudentDormitory);
		stu_headimage = (ImageView) findViewById(R.id.stu_headimage);
		sname.setText(student.getName().toString());
		ssex.setText(student.getSex().toString());
		StudentClass.setText(student.getStudentClass().toString());
		StudentNumber.setText(student.getStudentNumber().toString());
		sphone.setText(student.getPhone().toString());
		StudentDormitory.setText(student.getStudentDormitory().toString());

		Cursor cursor = db.rawQuery(
				"select * from student where _id = '"
						+ student.getId().toString() + "'", null);
		// 知道只有一条记录 直接moveToNext
		cursor.moveToNext();
		// cursor.getColumnIndex("image")获取列的索引
		byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
				imageBytes.length);
		stu_headimage.setImageBitmap(bitmap);

		updateName = student.getName().toString();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = new MenuInflater(
				StudentInformationActivity.this);
		inflater.inflate(R.menu.student_information, menu);


		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.homePage:
				toHomePage();
				break;
			case R.id.search:
				searchStudent();
				break;

		}
		return super.onOptionsItemSelected(item);
	}

}
