package com.example.studentmanagersystem;

import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

import com.alibaba.fastjson.JSON;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends Activity {

	private int op_imageid[] = { R.drawable.addstudent_image,
			R.drawable.deletestudent_image, R.drawable.refreshstudent_image,
			R.drawable.user_image, R.drawable.sms_all };
	private String str[] = { "新增", "删除" };
	private ListView listView;
	private View visView;
	private boolean flag = true;
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;
	private List<Score> scores = new ArrayList<Score>();
	private ListView course_listview;
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
		setContentView(R.layout.activity_score);

		mySqlHelper = new MySqlHelper(ScoreActivity.this,
				"student_inf.db", null, 2);
		db = mySqlHelper.getWritableDatabase();

		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);


		csh_kj();
		csh_kj2();
		course_listview = (ListView) findViewById(R.id.course_listview);
		course_listview.setAdapter(score_adapter);
		course_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});

	}

	public android.widget.TextView dmzf_TextView;
	public android.widget.TextView pszyzf_TextView;
	public android.widget.TextView sjzf_TextView;

	private void csh_kj() {



		dmzf_TextView = (android.widget.TextView)findViewById(R.id.dmzf);

		dmzf_TextView.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View view) {
				dmzf_TextView_dian_ji();
			}
		} );

		dmzf_TextView.setOnLongClickListener( new android.view.View.OnLongClickListener() {
			@Override
			public boolean onLongClick(android.view.View view) {
				dmzf_TextView_cang_an();
				return true;
			}
		} );



		pszyzf_TextView = (android.widget.TextView)findViewById(R.id.pszyzf);

		pszyzf_TextView.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View view) {
				pszyzf_TextView_dian_ji();
			}
		} );

		pszyzf_TextView.setOnLongClickListener( new android.view.View.OnLongClickListener() {
			@Override
			public boolean onLongClick(android.view.View view) {
				pszyzf_TextView_cang_an();
				return true;
			}
		} );



		sjzf_TextView = (android.widget.TextView)findViewById(R.id.sjzf);

		sjzf_TextView.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View view) {
				sjzf_TextView_dian_ji();
			}
		} );

		sjzf_TextView.setOnLongClickListener( new android.view.View.OnLongClickListener() {
			@Override
			public boolean onLongClick(android.view.View view) {
				sjzf_TextView_cang_an();
				return true;
			}
		} );


	}


	public void dmzf_TextView_dian_ji(){

	}

	public void dmzf_TextView_cang_an(){

	}



	public String dmzf_TextView_wen_zi(){
		return dmzf_TextView.getText().toString();
	}

	public void dmzf_TextView_wen_zi(String text){
		dmzf_TextView.setText(text);
	}



	public void pszyzf_TextView_dian_ji(){

	}

	public void pszyzf_TextView_cang_an(){

	}



	public String pszyzf_TextView_wen_zi(){
		return pszyzf_TextView.getText().toString();
	}

	public void pszyzf_TextView_wen_zi(String text){
		pszyzf_TextView.setText(text);
	}



	public void sjzf_TextView_dian_ji(){

	}

	public void sjzf_TextView_cang_an(){

	}



	public String sjzf_TextView_wen_zi(){
		return sjzf_TextView.getText().toString();
	}

	public void sjzf_TextView_wen_zi(String text){
		sjzf_TextView.setText(text);
	}


	public android.widget.TextView zf_TextView;

	private void csh_kj2() {



		zf_TextView = (android.widget.TextView)findViewById(R.id.zf);

		zf_TextView.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(android.view.View view) {
				zf_TextView_dian_ji();
			}
		} );

		zf_TextView.setOnLongClickListener( new android.view.View.OnLongClickListener() {
			@Override
			public boolean onLongClick(android.view.View view) {
				zf_TextView_cang_an();
				return true;
			}
		} );


	}


	public void zf_TextView_dian_ji(){

	}

	public void zf_TextView_cang_an(){

	}



	public String zf_TextView_wen_zi(){
		return zf_TextView.getText().toString();
	}

	public void zf_TextView_wen_zi(String text){
		zf_TextView.setText(text);
	}







	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	private void loadData() {
		scores = new ArrayList<Score>();
		String sql = String.format("select * from score where uid = '%s' and course = '%s' ;", StudentInformationActivity.student.getId(),
				StudentInformationActivity.course
		);
		Log.d("0", "CourseActivity loadData sql=" + sql);

		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Score score=new Score(
					cursor.getString(0),
					cursor.getString(1),
					cursor.getString(2),
					cursor.getString(3),
					cursor.getString(4)
			);
			scores.add(score);
	}
		score_adapter.notifyDataSetChanged();
		calc_total_score();

	}

	ArrayList<Score> dmScores =new ArrayList<>();
	ArrayList<Score> pszyScores =new ArrayList<>();
	ArrayList<Score> sjScores =new ArrayList<>();

	private void calc_total_score() {
		double dmzf = 0;
		double pszyzf = 0;
		double sjzf = 0;

		for (Score score : scores) {
			if (score.type.equals("点名")) {
				dmzf += Double.valueOf(score.score);
				dmScores.add(score);
			}else if (score.type.equals("平时作业")) {
				pszyScores.add(score);
				pszyzf += Double.valueOf(score.score);
			} else if (score.type.equals("上机")) {
				sjScores.add(score);
				sjzf += Double.valueOf(score.score);
			}
		}

		scores.clear();
		scores.addAll(dmScores);
		scores.addAll(pszyScores);
		scores.addAll(sjScores);

		for (int i = 0; i < dmScores.size(); i++) {
			Score item = dmScores.get(i);
			item.no = i + 1;

		}
		for (int i = 0; i < pszyScores.size(); i++) {
			Score item = pszyScores.get(i);
			item.no = i + 1;

		}
		for (int i = 0; i < sjScores.size(); i++) {
			Score item = sjScores.get(i);
			item.no = i + 1;
		}
		dmzf = dmzf * 0.4;
		pszyzf = pszyzf * 0.6;

		dmzf_TextView_wen_zi(dmzf+"");

		pszyzf_TextView_wen_zi(pszyzf+"");

		sjzf_TextView_wen_zi(sjzf+"");

		double zf =  dmzf  + pszyzf + sjzf ;
		Log.d("0", "ScoreActivity calc_total_score zf=" + zf);

		zf_TextView_wen_zi(zf+"");



	}
	BaseAdapter score_adapter = new BaseAdapter() {


		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(
					ScoreActivity.this).inflate(
					R.layout.score_list_item, null);

			Log.d("0", "ScoreActivity getView scores=" + JSON.toJSONString(scores, true));

			



			TextView score = (TextView) view
					.findViewById(R.id.score);
			TextView type = (TextView) view
					.findViewById(R.id.type);
			score.setText("得分:"+ ScoreActivity.this.scores.get(arg0).score.toString() );
			type.setText(ScoreActivity.this.scores.get(arg0).type.toString() + (ScoreActivity.this.scores.get(arg0).no));



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
			return scores.size();
		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = new MenuInflater(
				ScoreActivity.this);
		inflater.inflate(R.menu.contextmenu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		}
		return super.onOptionsItemSelected(item);
	}
	

}
