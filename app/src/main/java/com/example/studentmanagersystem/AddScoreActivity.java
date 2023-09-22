package com.example.studentmanagersystem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class AddScoreActivity extends Activity {

	private Spinner spinner;
	private String arrs[];
	private EditText StudentNumber;
	private Calendar calendar;
	private DatePickerDialog datePickerDialog;
	private Button StudentNumber_choose;
	private Button add_submit;
	private Button add_cancel;
	private EditText score;
	private EditText StudentClass;
	private EditText stu_phone;
	private EditText StudentDormitory;
	private RadioGroup lx_rg;
	private RadioButton lx_dm;
	private RadioButton lx_pszy;
	private RadioButton lx_sj;
	private RadioGroup kc_rg;
	private RadioButton kc_yw;
	private RadioButton kc_sx;
	private RadioButton kc_yy;
	private String type = "点名";
	private String mingzu = "";
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private int headimageid = R.drawable.image;
	private ImageView headimage;
	private final int CODE = 1;
	private String course = "数据库";

	private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// TODO Auto-generated method stub
			StudentNumber.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_score);

		mySqlHelper = new MySqlHelper(AddScoreActivity.this,
				"student_inf.db", null, 2);
		db = mySqlHelper.getWritableDatabase();
	
		score = (EditText) findViewById(R.id.score);

		lx_rg = (RadioGroup) findViewById(R.id.lx_rg);
		lx_dm = (RadioButton) findViewById(R.id.lx_dm);
		lx_pszy = (RadioButton) findViewById(R.id.lx_pszy);
		lx_sj = (RadioButton) findViewById(R.id.lx_sj);
		lx_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				// 
				Log.d("0", "AddScoreActivity onCheckedChanged arg1=" + arg1);
				
				if (arg1 == lx_dm.getId()) {
					type = "点名";
				} else if (arg1 == lx_pszy.getId()) {
					type = "平时作业";
				}else if (arg1 == lx_sj.getId()) {
					type = "上机";
				}
			}
		});
		
		kc_rg = (RadioGroup) findViewById(R.id.kc_rg);
		kc_yw = (RadioButton) findViewById(R.id.kc_yw);
		kc_sx = (RadioButton) findViewById(R.id.kc_sx);
		kc_yy = (RadioButton) findViewById(R.id.kc_yy);
		kc_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				if (arg1 == kc_yw.getId()) {
					course = "数据库";
				} else if (arg1 == kc_sx.getId()) {
					course = "安卓";
				}else if (arg1 == kc_yy.getId()) {
					course = "c语言";
				}
			}
		});

		add_submit = (Button) findViewById(R.id.add_submit);
		add_cancel = (Button) findViewById(R.id.add_cancel);

		add_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				addStudent();
			}
		});
		add_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}


	public void addStudent() {

		if (score.getText().toString().equals("")) {
			Toast.makeText(AddScoreActivity.this, "您输入的信息不完整！",
					Toast.LENGTH_SHORT).show();
		} else {
			Log.d("0", "AddScoreActivity addStudent type=" + type);
			Log.d("0", "AddScoreActivity addStudent course=" + course);

			ContentValues values = new ContentValues();
			values.put("score", score.getText().toString());
			values.put("type", type);
			values.put("course", course);
			values.put("uid", StudentInformationActivity.student.getId());
			db.insert("score", null, values);
			Toast.makeText(AddScoreActivity.this, "添加成功！", Toast.LENGTH_SHORT)
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
					Toast.makeText(AddScoreActivity.this, "cursor == null",
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
