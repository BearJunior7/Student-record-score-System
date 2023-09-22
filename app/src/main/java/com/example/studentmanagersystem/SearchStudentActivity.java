package com.example.studentmanagersystem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchStudentActivity extends Activity {

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
		setContentView(R.layout.activity_search_student);

		mySqlHelper = new MySqlHelper(SearchStudentActivity.this,
				"student_inf.db", null, 2);
		db = mySqlHelper.getWritableDatabase();

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);



		stu_listview = (ListView) findViewById(R.id.stu_listview);
		stu_listview.setAdapter(stu_adapter);
		stu_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				StudentInformationActivity.student = students.get(arg2);
				Intent intent = new Intent(SearchStudentActivity.this,
						StudentInformationActivity.class);
				startActivity(intent);
//				showInformation(arg2);
//				System.out.println(arg2);
			}
		});



		ImageView search = (ImageView) findViewById(R.id.image_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(1111);
				// TODO Auto-generated method stub
				students = new ArrayList<Student>();
				Cursor cursor = db.rawQuery(
						"select * from student where name = '"
								+ serach_edit.getText().toString() + "'", null);
				while (cursor.moveToNext()) {
					Student student=new Student(	cursor.getString(0),
							cursor.getString(1),
							cursor.getString(2),
							cursor.getString(3),
							cursor.getString(4),
							cursor.getString(5),
							cursor.getString(6),
							cursor.getString(7));
					students.add(student);
					byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
					Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
							imageBytes.length);
					student.headimage = bitmap;
				}
				// stu_listview.setAdapter(stu_adapter);
				Toast.makeText(SearchStudentActivity.this, "搜索完毕！",
						Toast.LENGTH_SHORT).show();
				stu_adapter.notifyDataSetChanged();
			}
		});
		serach_edit = (AutoCompleteTextView) findViewById(R.id.search_edit);
		Cursor cursorstu = db.rawQuery("select name from student", null);
		while (cursorstu.moveToNext()) {
			lists.add(cursorstu.getString(0));
		}
		serach_edit.setThreshold(1);



	}

	public void addStudent() {
		Intent intent = new Intent(SearchStudentActivity.this,
				AddStudentActivity.class);
		startActivity(intent);

	}
	public void searchStudent() {
		Intent intent = new Intent(SearchStudentActivity.this,
				SearchStudentActivity.class);
		startActivity(intent);

	}

	public void delStudent() {

		for (Student student : students) {
			System.out.println(student.isChecked());
			if (student.isChecked()) {
				Cursor cursor = db.rawQuery("delete from student where _id = '"
						+ student.getId() + "'", null);
				cursor.moveToNext();
			}
		}

		// Cursor cursor =
		// db.rawQuery("delete from student where _id = '"+id+"'", null);
		// cursor.moveToNext();
		// chboxall = false;
	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		students = new ArrayList<Student>();
//		Cursor cursor = db.rawQuery("select * from student", null);
//		while (cursor.moveToNext()) {
//			Student student=new Student(cursor.getString(1), cursor.getString(2),
//					cursor.getString(3), cursor.getString(4), cursor
//					.getString(5), cursor.getString(6), cursor
//					.getString(7));
//			students.add(student);
//			byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
//			Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
//					imageBytes.length);
//			student.headimage = bitmap;
//		}
		// stu_listview.setAdapter(stu_adapter);
//		stu_adapter.notifyDataSetChanged();
		super.onResume();
	}

	BaseAdapter stu_adapter = new BaseAdapter() {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(
					SearchStudentActivity.this).inflate(
					R.layout.stulistviewitems, null);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent2 = new Intent(SearchStudentActivity.this,
							StudentInformationActivity.class);
					startActivity(intent2);
				}
			});
			TextView stu_list_name = (TextView) view
					.findViewById(R.id.stu_list_name);
			ImageView item_img = (ImageView) view
					.findViewById(R.id.item_img);
			TextView StudentMobilePhoneNumber = (TextView) view
					.findViewById(R.id.StudentMobilePhoneNumber);
			final int position = arg0;
			stu_list_name.setText(students.get(arg0).getName().toString());
			StudentMobilePhoneNumber.setText(students.get(arg0).getStudentNumber().toString());
			item_img.setImageBitmap(students.get(arg0).headimage);

			return view;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return students.size();
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = new MenuInflater(
				SearchStudentActivity.this);
		inflater.inflate(R.menu.search_student, menu);



		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.stu_add:
				addStudent();
				break;
			case R.id.search:
				searchStudent();
				break;

		}
		return super.onOptionsItemSelected(item);
	}


}
