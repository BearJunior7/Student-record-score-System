package com.example.studentmanagersystem;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

public class AddStudentActivity extends Activity {

	public static boolean edit;
    private Spinner spinner;
	private String arrs[];
	private EditText StudentNumber;
	private Calendar calendar;
	private DatePickerDialog datePickerDialog;
	private Button StudentNumber_choose;
	private Button add_submit;
	private Button add_cancel;
	private EditText stu_name;
	private EditText StudentClass;
	private EditText stu_phone;
	private EditText StudentDormitory;
	private RadioGroup stu_rg;
	private RadioButton stu_rb1;
	private RadioButton stu_rb2;
	private String sex = "男";
	private String mingzu = "";
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private int headimageid = R.drawable.image;
	private ImageView headimage;
	private final int CODE = 1;

	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			StudentNumber.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
		}
	};
	private Student student;
	private ImageView stu_headimage;
	private String updateName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_student);

		mySqlHelper = new MySqlHelper(AddStudentActivity.this,
				"student_inf.db", null, 2);
		db = mySqlHelper.getWritableDatabase();
		headimage = (ImageView) findViewById(R.id.headImage);
		headimage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//selectImage();
				chooseImage();
			}
		});
		stu_name = (EditText) findViewById(R.id.stu_name);
		StudentClass = (EditText) findViewById(R.id.StudentClass);
		stu_phone = (EditText) findViewById(R.id.stu_phone);
		StudentDormitory = (EditText) findViewById(R.id.StudentDormitory);



		stu_rg = (RadioGroup) findViewById(R.id.stu_rg);
		stu_rb1 = (RadioButton) findViewById(R.id.stu_rb1);
		stu_rb2 = (RadioButton) findViewById(R.id.stu_rb2);
		stu_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == stu_rb1.getId()) {
					sex = "男";
				} else {
					sex = "女";
				}
			}
		});

		StudentNumber = (EditText) findViewById(R.id.StudentNumber);
		calendar = Calendar.getInstance();
		int year = calendar.get(calendar.YEAR);
		int month = calendar.get(calendar.MONTH);
		int day = calendar.get(calendar.DAY_OF_MONTH);
		datePickerDialog = new DatePickerDialog(AddStudentActivity.this,
				listener, year, month, day);
//		StudentNumber_choose = (Button) findViewById(R.id.StudentNumber_choose);
		add_submit = (Button) findViewById(R.id.add_submit);
		add_cancel = (Button) findViewById(R.id.add_cancel);
//		StudentNumber_choose.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				datePickerDialog.show();
//			}
//		});
		add_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addStudentInfo();
			}
		});
		add_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		if (edit) {
			getActionBar().setTitle("编辑");
			showInformation();
		}
	}

	public void showInformation() {
		student = StudentInformationActivity.student;

		final EditText sname = (EditText) findViewById(R.id.stu_name);

		final EditText StudentClass = (EditText) findViewById(R.id.StudentClass);
		final EditText StudentNumber = (EditText) findViewById(R.id.StudentNumber);
		final EditText sphone = (EditText) findViewById(R.id.stu_phone);
		final EditText StudentDormitory = (EditText) findViewById(R.id.StudentDormitory);
		stu_headimage = (ImageView) findViewById(R.id.headImage);
		sname.setText(student.getName().toString());
		StudentClass.setText(student.getStudentClass().toString());
		StudentNumber.setText(student.getStudentNumber().toString());
		sphone.setText(student.getPhone().toString());
		StudentDormitory.setText(student.getStudentDormitory().toString());
		if (student.getSex().equals("男")) {
			stu_rb1.setChecked(true);
		}else if (student.getSex().equals("女")) {
			stu_rb1.setChecked(true);
		}
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



	public void addStudentInfo() {

		if (stu_name.getText().toString().equals("")
				|| StudentClass.getText().toString().equals("")
				|| StudentNumber.getText().toString().equals("")
				|| stu_phone.getText().toString().equals("")) {
			Toast.makeText(AddStudentActivity.this, "您输入的信息不完整！",
					Toast.LENGTH_SHORT).show();
		} else {
			Bitmap bitmap = ((BitmapDrawable)headimage.getDrawable()).getBitmap();
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			// 压缩bitmap到ByteArrayOutputStream
			bitmap.compress(CompressFormat.PNG, 100, byteOut);
			
			ContentValues values = new ContentValues();
			values.put("name", stu_name.getText().toString());
			values.put("sex", sex);
			values.put("mingzu", mingzu);
			values.put("StudentClass", StudentClass.getText().toString());
			values.put("StudentNumber", StudentNumber.getText().toString());
			values.put("phone", stu_phone.getText().toString());
			values.put("StudentDormitory", StudentDormitory.getText().toString());

			values.put("image", byteOut.toByteArray());
			if (edit) {
				db.update("student", values, "_id = ?", new String[] { student.getId() });
			} else {
				db.insert("student", null, values);
			}

			Toast.makeText(AddStudentActivity.this, "成功！", Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	private void chooseImage() {
		// 选择相册
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, CODE);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = new MenuInflater(
				this);
		inflater.inflate(R.menu.add_student, menu);


		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.homePage:
				Intent intent = new Intent(this,
						MainActivity.class);
				startActivity(intent);
				break;
			case R.id.search:
				Intent intent2 = new Intent(this,
						SearchStudentActivity.class);
				startActivity(intent2);
				break;


		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CODE) {
			if (resultCode == Activity.RESULT_OK && null != data) {
				Uri selectedImagePath = data.getData();
				Cursor cursor = getContentResolver().query(selectedImagePath,
						null, null, null, null);

				if (cursor == null) {
					Toast.makeText(AddStudentActivity.this, "cursor == null",
							Toast.LENGTH_SHORT).show();
				} else {
					cursor.moveToFirst();
					String img = cursor.getString(1);
					// System.out.println("img:" + img);
					//
					// student.setImgPath(img);
					cursor.close();
					Bitmap bitmap = BitmapFactory.decodeFile(img);

					headimage.setImageBitmap(bitmap);
				}
			}
		}
	}
}
