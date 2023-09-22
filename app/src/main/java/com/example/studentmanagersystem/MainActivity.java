package com.example.studentmanagersystem;

import java.io.File;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

public class MainActivity extends Activity {

	private int op_imageid[] = { R.drawable.addstudent_image,
			R.drawable.deletestudent_image, R.drawable.refreshstudent_image,
			R.drawable.user_image, R.drawable.sms_all };
	private String str[] = { "新增", "删除" };
	private ListView listView;
	private View visView;
	private boolean flag = true;
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	public static List<Student> students = new ArrayList<Student>();
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
		setContentView(R.layout.activity_main);

		mySqlHelper = new MySqlHelper(MainActivity.this,
				"student_inf.db", null, 2);
		db = mySqlHelper.getWritableDatabase();

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);


		stu_listview = (ListView) findViewById(R.id.stu_listview);
		this.registerForContextMenu(stu_listview);
		stu_listview.setAdapter(stu_adapter);
		stu_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				StudentInformationActivity.student = students.get(arg2);
				Log.d("0", "students.get(arg2);: "+students.get(arg2));

				Intent intent = new Intent(MainActivity.this,
						StudentInformationActivity.class);
				startActivity(intent);
//				showInformation(arg2);
//				System.out.println(arg2);
			}
		});
		refreshGallery();

	}


	/**
	 * 添加上下文菜单
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		//设置Menu显示内容
		menu.setHeaderTitle("操作");
		menu.setHeaderIcon(R.drawable.sms_all);
		menu.add(1, 2, 1, "删除");
		menu.add(1, 1, 1, "编辑");
	}


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo  menuInfo=
				(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		//position就是获取到的当前listview的位置
		int position=menuInfo.position;
		Log.d("0", "MainActivity onContextItemSelected position=" + position);

		switch (item.getItemId()) {
			case 1:
				Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
				StudentInformationActivity.student = students.get(position);

				AddStudentActivity.edit = true;
				startActivity(intent);
				break;
			case 2:

			    delStudent(students.get(position).getId());
				break;
	}
		return super.onContextItemSelected(item);
	}


	public void addStudent() {
		Intent intent = new Intent(MainActivity.this,
				AddStudentActivity.class);
		startActivity(intent);

	}
	public void searchStudent() {
		Intent intent = new Intent(MainActivity.this,
				SearchStudentActivity.class);
		startActivity(intent);

	}

	public void delStudent(String id) {
		Log.d("0", "MainActivity delStudent id=" + id);
//		String sql = "delete from student where _id = " + id + " ;";
		db.execSQL("delete from student where _id = ?", new String[] { id });
		loadData();

		// Cursor cursor =
		// db.rawQuery("delete from student where _id = '"+id+"'", null);
		// cursor.moveToNext();
		// chboxall = false;
	}




	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	private void loadData() {
		students = new ArrayList<Student>();
		Cursor cursor = db.rawQuery("select * from student", null);
		while (cursor.moveToNext()) {
			Student student=new Student(
					cursor.getString(0),
					cursor.getString(1),
					cursor.getString(2),
					cursor.getString(3),
					cursor.getString(4),
					cursor.getString(5),
					cursor.getString(6),
					cursor.getString(7)
			);
			students.add(student);
			byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
			Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0,
					imageBytes.length);
			student.headimage = bitmap;
		}


		stu_adapter.notifyDataSetChanged();
	}

	BaseAdapter stu_adapter = new BaseAdapter() {

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(
					MainActivity.this).inflate(
					R.layout.stulistviewitems, null);

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
				MainActivity.this);
		inflater.inflate(R.menu.contextmenu, menu);

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
			case R.id.backup:
				Intent intent = new Intent(this, BackupActivity.class);
				startActivity(intent);
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	//刷新图库
	private void refreshGallery() {
		String p = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";
		Log.d("0", "MainActivity refreshGallery p=" + p);
		File[] fl = new File(p).listFiles();
		Log.d("0", "MainActivity refreshGallery fl=" + fl);

		if (fl != null) {
			for (File file : fl) {
				Log.d("0", "MainActivity refreshGallery file.getAbsolutePath()=" + file.getAbsolutePath());
				
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
			}
		}
	}

}
