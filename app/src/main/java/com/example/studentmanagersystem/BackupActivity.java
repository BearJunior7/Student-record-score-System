package com.example.studentmanagersystem;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.smailnet.emailkit.Draft;
import com.smailnet.emailkit.EmailKit;

import java.util.ArrayList;
import java.util.List;

public class BackupActivity extends Activity {

	private static final String TAG = "0";
	AutoCompleteTextView cardNumAuto;
	EditText passwordET;
	Button logBT;
	Button backupBt;

	CheckBox savePasswordCB;
	SharedPreferences sp;
	String cardNumStr;
	String passwordStr;
	private UserDbAdapter mDbHelper;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backup);
		cardNumAuto = (AutoCompleteTextView) findViewById(R.id.cardNumAuto);
		passwordET = (EditText) findViewById(R.id.passwordET);
		logBT = (Button) findViewById(R.id.logBT);
		backupBt = (Button) findViewById(R.id.resBT);
		backupBt.setOnClickListener(backup_button_listener);

		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
		savePasswordCB = (CheckBox) findViewById(R.id.savePasswordCB);
		savePasswordCB.setChecked(true);// 默认为记住密码
		cardNumAuto.setThreshold(1);// 输入1个字母就开始自动提示
		passwordET.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		// 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81
		// 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91

		cardNumAuto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
				allUserName = sp.getAll().keySet().toArray(new String[0]);
				// sp.getAll()返回一张hash map
				// keySet()得到的是a set of the keys.
				// hash map是由key-value组成的

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						BackupActivity.this,
						android.R.layout.simple_dropdown_item_1line,
						allUserName);

				cardNumAuto.setAdapter(adapter);// 设置数据适配器

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				passwordET.setText(sp.getString(cardNumAuto.getText()
						.toString(), ""));// 自动输入密码

			}
		});

		// 登陆
		logBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				cardNumStr = cardNumAuto.getText().toString();
				passwordStr = passwordET.getText().toString();
				if((cardNumStr == null||cardNumStr.equalsIgnoreCase("")) || (passwordStr == null||passwordStr.equalsIgnoreCase(""))){
					Toast.makeText(BackupActivity.this, "账户名和密码不能为空.",
							Toast.LENGTH_SHORT).show();
				}else{


					if (savePasswordCB.isChecked()) {// 登陆成功才保存密码
						sp.edit().putString(cardNumStr, passwordStr).commit();
					}

					boolean autoLogin = savePasswordCB.isChecked();
					if (autoLogin)
					{
						SharedPreferences.Editor editor = sp.edit();
						editor.putString("uname", cardNumStr);
						editor.putString("upswd", passwordStr);
						editor.putBoolean("auto", true);
						editor.commit();
					} else
					{
						SharedPreferences.Editor editor = sp.edit();
						editor.putString("uname", null);
						editor.putString("upswd", null);
						editor.putBoolean("auto", false);
						editor.commit();

					}

					loginEmail();


				}				
			}
		});
		
		mDbHelper = new UserDbAdapter(this);
		mDbHelper.open();

		if (sp.getBoolean("auto", false))
		{
			cardNumAuto.setText(sp.getString("uname", null));
			passwordET.setText(sp.getString("upswd", null));
			savePasswordCB.setChecked(true);
			cardNumStr = cardNumAuto.getText().toString();
			passwordStr = passwordET.getText().toString();
		}

	}

	private void loginEmail() {
		sendEmail("这是一封测试邮件");
	}

	private OnClickListener backup_button_listener = new OnClickListener() {
		public void onClick(View v) {
			String stujs = JSON.toJSONString(MainActivity.students,true);
			loadScores();
			String scoresjs = JSON.toJSONString(scores,true);
			String c = stujs + "\n\n\n" + scoresjs;
			Log.d("0", "BackupActivity onClick c=" + c);
			sendEmail(c);
		}
	};

	private void sendEmail(String content) {
		//初始化框架
		EmailKit.initialize(this);
//配置发件人邮件服务器参数
		EmailKit.Config config = new EmailKit.Config()
				.setMailType(EmailKit.MailType.FOXMAIL)     //选择邮箱类型，快速配置服务器参数
				.setAccount(cardNumStr)             //发件人邮箱
				.setPassword(passwordStr);                   //密码或授权码

//设置一封草稿邮件
		Draft draft = new Draft()
				.setNickname("小学生")                      //发件人昵称
				.setTo("2475867157@qq.com")                        //收件人邮箱
				.setSubject("备份")             //邮件主题
				.setText(content);                 //邮件正文

		new Thread(new Runnable() {
			@Override
			public void run() {

//使用SMTP服务发送邮件
				EmailKit.useSMTPService(config)
						.send(draft, new EmailKit.GetSendCallback() {
							@Override
							public void onSuccess() {
								Log.i(TAG, "发送成功！");


								Toast.makeText(BackupActivity.this, "成功", Toast.LENGTH_SHORT).show();


							}

							@Override
							public void onFailure(String errMsg) {
								Log.i(TAG, "发送失败，错误：" + errMsg);

								Toast.makeText(BackupActivity.this, "失败，错误：" + errMsg, Toast.LENGTH_LONG).show();

							}
						});
			}
		}).start();
	}

	private List<Score> scores = new ArrayList<Score>();
	private MySqlHelper mySqlHelper;
	private SQLiteDatabase db;

	private void loadScores() {


		mySqlHelper = new MySqlHelper(this,
				"student_inf.db", null, 2);
		db = mySqlHelper.getWritableDatabase();

		scores = new ArrayList<Score>();
		String sql ="select * from score;";
		Log.d("0", "CourseActivity loadData sql=" + sql);

		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			Score score = new Score(
					cursor.getString(0),
					cursor.getString(1),
					cursor.getString(2),
					cursor.getString(3),
					cursor.getString(4)
			);
			scores.add(score);
		}

	}


}