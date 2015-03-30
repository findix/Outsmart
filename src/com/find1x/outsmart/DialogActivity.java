package com.find1x.outsmart;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.*;
import com.find1x.outsmart.analysis.GetUserLocation;
import com.find1x.outsmart.analysis.GetUserTime;
import com.find1x.outsmart.db.DatabaseHelper;
import com.find1x.outsmart.segmentation.Persistence;
import com.find1x.outsmart.sms.Contact;
import com.find1x.outsmart.sms.SendSMS;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DialogActivity extends Activity implements OnClickListener {

	private TextView senderTextView = null;
	private TextView timeSMSTextView = null;
	private TextView smsTextView = null;
	private TextView timeTextView = null;
	private TextView dateTextView = null;
	private EditText editText_location = null;
	private EditText editText_event = null;
	private EditText editText = null;
	private Button btn_ok = null;
	private Button btn_cancel = null;
	private Button btn_reply = null;
	private Button btn_changeLocation = null;
	private Button btn_changeEvent = null;
	private Button btn_changeTime = null;
	private Button btn_changeDate = null;
	private ImageView contact_imageView = null;
	private String[] location;
	private Calendar time = Calendar.getInstance();
	private String address = new String();
	private String replyText = new String();

	private boolean isClear_Event = false;
	private boolean isClear_Location = false;

	/** 发送与接收的广播 **/
	String SENT_SMS_ACTION = "SENT_SMS_ACTION";
	String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dialog);
		setTheme(R.style.DialogTheme);
		senderTextView = (TextView) findViewById(R.id.sendertextView);
		timeSMSTextView = (TextView) findViewById(R.id.timeSMStextView);
		dateTextView = (TextView) findViewById(R.id.datetextView);
		timeTextView = (TextView) findViewById(R.id.timetextView);
		smsTextView = (TextView) findViewById(R.id.smstextView);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(this);
		btn_reply = (Button) findViewById(R.id.btn_reply);
		btn_reply.setOnClickListener(this);
		btn_changeDate = (Button) findViewById(R.id.changeDateButton);
		btn_changeDate.setOnClickListener(new BtnOnClickListener(
				R.id.changeDateButton));
		btn_changeTime = (Button) findViewById(R.id.changeTimeButton);
		btn_changeTime.setOnClickListener(new BtnOnClickListener(
				R.id.changeTimeButton));
		btn_changeLocation = (Button) findViewById(R.id.changeLocationButton);
		btn_changeLocation.setOnClickListener(this);
		btn_changeEvent = (Button) findViewById(R.id.changeEventButton);
		btn_changeEvent.setOnClickListener(this);
		editText_location = (EditText) findViewById(R.id.locationEditText);
		editText_location.setOnClickListener(this);
		editText_event = (EditText) findViewById(R.id.eventEditText);
		editText_event.setOnClickListener(this);
		contact_imageView = (ImageView) findViewById(R.id.contact_imageView);

		// 芒果广告
		// adsMogoLayout = ((AdsMogoLayout)
		// this.findViewById(R.id.adsMogoView));
		// adsMogoLayout.setAdsMogoListener(this);
		// adsMogoLayout.downloadIsShowDialog = true;

		// 接受intent
		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		String address = intent.getStringExtra("address");
		// String person = intent.getStringExtra("person");
		Long date = intent.getLongExtra("date", 0);
		String id = Contact.getContactId(this, address);
		GetUserTime getUserTime = new GetUserTime(content);
		time = getUserTime.getTime();
		GetUserLocation getUserLocation = new GetUserLocation(
				getUserTime.getNoDateMsg());
		location = getUserLocation.getLocation();
		this.address = address;
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-M-d");
		SimpleDateFormat formatTime = new SimpleDateFormat("H:mm");
		SimpleDateFormat formatSMSTime = new SimpleDateFormat("M月d日 H:mm");
		if (id != null) {
			senderTextView.setText(Contact.getDisplayName(this, id));
			if (Contact.getContactsPhoto(this, id) != null)
				contact_imageView.setImageBitmap(Contact.getContactsPhoto(this,
						id));
		} else
			senderTextView.setText(address);
		if (date != 0)
			timeSMSTextView.setText(formatSMSTime.format(date));
		dateTextView.setText(formatDate.format(time.getTime()));
		timeTextView.setText(formatTime.format(time.getTime()));

		editText_location.setText(getUserLocation.getUserLocation(this));
		editText_location.clearFocus();
		smsTextView.setText(content);

		// 注册广播
		// registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
		// registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));

		// TipHelper.PlaySound(this);// 响铃
		// long ring[]={1000,500,1000};
		// TipHelper.Vibrate(this, ring, false);//震动
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MobclickAgent.onPause(this);
		// 解除注册广播
		// unregisterReceiver(sendMessage);
		// unregisterReceiver(receiver);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.changeLocationButton: {
			final boolean[] defaultSelectedStatus = new boolean[location.length];
			for (int i = 0; i < location.length; i++)
				defaultSelectedStatus[i] = false;
			new AlertDialog.Builder(this)
					.setTitle("设置地点")
					// 设置对话框标题
					.setMultiChoiceItems(location, defaultSelectedStatus,
							new OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									// 来回重复选择取消，得相应去改变item对应的bool值，点击确定时，根据这个bool[],得到选择的内容
									defaultSelectedStatus[which] = isChecked;
								}
							}) // 设置对话框[肯定]按钮
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									StringBuffer locationSet = new StringBuffer();
									for (int i = 0; i < defaultSelectedStatus.length; i++) {
										if (defaultSelectedStatus[i]) {
											locationSet.append(location[i]);
										}
									}
									editText_location.setText(locationSet);
								}
							}).setNegativeButton("取消", null)// 设置对话框[否定]按钮
					.show();
			break;
		}
		case R.id.changeEventButton: {
			final boolean[] defaultSelectedStatus = new boolean[location.length];
			for (int i = 0; i < location.length; i++)
				defaultSelectedStatus[i] = false;
			new AlertDialog.Builder(this)
					.setTitle("设置事件")
					// 设置对话框标题
					.setMultiChoiceItems(location, defaultSelectedStatus,
							new OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									// 来回重复选择取消，得相应去改变item对应的bool值，点击确定时，根据这个bool[],得到选择的内容
									defaultSelectedStatus[which] = isChecked;
								}
							}) // 设置对话框[肯定]按钮
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									StringBuffer locationSet = new StringBuffer();
									for (int i = 0; i < defaultSelectedStatus.length; i++) {
										if (defaultSelectedStatus[i]) {
											locationSet.append(location[i]);
										}
									}
									editText_event.setText(locationSet);
								}
							}).setNegativeButton("取消", null)// 设置对话框[否定]按钮
					.show();
			break;
		}
		case R.id.btn_ok: {

			// 插入事件
			String calId = "";
			Persistence setCalendar = new Persistence("CalendarSet.db");
			calId = (setCalendar.getValue()) + "";
			ContentValues event = new ContentValues();
			event.put(Events.TITLE, editText_event.getText().toString());
			event.put(Events.DESCRIPTION, editText_event.getText().toString());
			// 插入账户
			if (!editText_location.equals("")
					&& !editText_location.equals("请选择地点")) {
				event.put(Events.EVENT_LOCATION, editText_location.getText()
						.toString());
			}
			event.put(Events.CALENDAR_ID, calId);
			Calendar mCalendar = Calendar.getInstance();
			mCalendar.set(Calendar.HOUR_OF_DAY, 10);
			long start = time.getTimeInMillis();
			mCalendar.set(Calendar.HOUR_OF_DAY, 11);
			long end = time.getTimeInMillis() + 3600000;
			event.put(Events.DTSTART, start);
			event.put(Events.DTEND, end);
			TimeZone tz = TimeZone.getDefault();
			event.put(Events.EVENT_TIMEZONE, tz.getID());
			event.put(Events.HAS_ALARM, 1);
			Uri newEvent = getContentResolver().insert(
					Events.CONTENT_URI, event);
			long eventID = Long.parseLong(newEvent.getLastPathSegment());

			ContentValues values = new ContentValues();
			values.put(Reminders.MINUTES, 10);
			values.put(Reminders.EVENT_ID, eventID);
			values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
			Uri uri = getContentResolver().insert(Reminders.CONTENT_URI, values);

			Toast.makeText(DialogActivity.this, "添加提醒成功!!!", Toast.LENGTH_SHORT)
					.show();

			// finish();

			// 添加地点到数据库
			// 建立数据库
			DatabaseHelper dbHelper = new DatabaseHelper(this, "user.db3");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String location_Temp = editText_location.getText().toString();
			String raw = "select location from user where location=\'"
					+ location_Temp + "\'";
			Cursor cursor = db.rawQuery(raw, null);
			if (cursor != null && !cursor.moveToNext()
					&& !location_Temp.equals("")
					&& !location_Temp.equals("请选择地点")) {
				String sql = "insert or ignore into user(location) values('"
						+ location_Temp + "');";
				// System.out.println(sql);
				db.execSQL(sql);
				Toast.makeText(this,
						"我现在知道" + "\"" + location_Temp + "\"" + "这个地方啦",
						Toast.LENGTH_LONG).show();
			}
			db.close();
			break;
		}
		case R.id.btn_reply: {
			LayoutInflater layoutInflater = LayoutInflater.from(this);
			final View myLoginView = layoutInflater.inflate(
					R.layout.activity_reply, null);
			Dialog alertDialog = new AlertDialog.Builder(this)
					.setView(myLoginView)
					.setTitle("确认回复")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									editText = (EditText) myLoginView
											.findViewById(R.id.EditText);
									String reply = editText.getText()
											.toString();
									replyText = reply;
									if (PhoneNumberUtils
											.isGlobalPhoneNumber(address)
											&& address.length() > 0
											&& reply.length() > 0) {
										new SendSMS(DialogActivity.this,
												address, reply);
										/** 将发送的短信插入数据库 **/
										ContentValues values = new ContentValues();
										// 发送时间
										values.put("date",
												System.currentTimeMillis());
										// 阅读状态
										values.put("read", 0);
										// 1为收 2为发
										values.put("type", 2);
										// 送达号码
										values.put("address", address);
										// 送达内容
										values.put("body", replyText);
										// 插入短信库
										getContentResolver().insert(
												Uri.parse("content://sms"),
												values);
										// finish();
									} else {
										if (address.length() == 0) {
											Toast.makeText(DialogActivity.this,
													"这条信息没有发件人，所以是没法回复的哦~",
													Toast.LENGTH_LONG).show();
										} else if (reply.length() == 0) {
											Toast.makeText(DialogActivity.this,
													"这条信息什么都没写哦，我应该回复什么呢？",
													Toast.LENGTH_LONG).show();
										} else {
											Toast.makeText(
													DialogActivity.this,
													"向号码 \"" + address
															+ "\" 发送短信 \""
															+ reply
															+ "\" 失败，请重新尝试",
													Toast.LENGTH_LONG).show();
										}
									}
									// finish();
								}
							}).setNegativeButton("取消", null).create();
			alertDialog.show();
			break;
		}

		case R.id.btn_cancel: {
			finish();
			break;
		}
		case R.id.eventEditText: {
			if (isClear_Event == false) {
				editText_event.setText("");
				isClear_Event = true;
			}
			break;
		}
		case R.id.locationEditText: {
			if (isClear_Location == false) {
				editText_location.setText("");
				isClear_Location = true;
			}
			break;
		}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// 用来获取日期和时间的
		// Calendar calendar = Calendar.getInstance();
		Calendar calendar = time;
		Dialog dialog = null;
		switch (id) {
		case R.id.changeDateButton:
			DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker datePicker, int year,
						int month, int dayOfMonth) {
					// Calendar月份是从0开始,所以month要加1
					time.set(year, month, dayOfMonth);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					dateTextView.setText(format.format(time.getTime()));
				}
			};
			dialog = new DatePickerDialog(this, dateListener,
					calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
			break;
		case R.id.changeTimeButton:
			TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker timerPicker, int hourOfDay,
						int minute) {
					time.set(Calendar.HOUR_OF_DAY, hourOfDay);
					time.set(Calendar.MINUTE, minute);
					SimpleDateFormat format = new SimpleDateFormat("H:mm");
					timeTextView.setText(format.format(time.getTime()));
				}
			};
			dialog = new TimePickerDialog(this, timeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), false); // 是否为二十四制
			break;
		default:
			break;
		}
		return dialog;
	}

	/*
	 * 成员内部类,此处为提高可重用性，也可以换成匿名内部类
	 */
	private class BtnOnClickListener implements View.OnClickListener {

		private int dialogId = 0; // 默认为0则不显示对话框

		public BtnOnClickListener(int dialogId) {
			this.dialogId = dialogId;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View view) {
			showDialog(dialogId);
		}

	}

	/**
	 * 参数说明 destinationAddress:收信人的手机号码 scAddress:发信人的手机号码 text:发送信息的内容
	 * sentIntent:发送是否成功的回执，用于监听短信是否发送成功。
	 * DeliveryIntent:接收是否成功的回执，用于监听短信对方是否接收成功。
	 */
	public BroadcastReceiver sendMessage = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 判断短信是否发送成功
			switch (getResultCode()) {
			case Activity.RESULT_OK:
				Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
				Log.i("短信发送", "成功！");
				break;
			default:
				Toast.makeText(DialogActivity.this, "发送失败", Toast.LENGTH_LONG)
						.show();
				Log.i("短信发送", "失败！");
				break;
			}
		}
	};

	public BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 表示对方成功收到短信
			Toast.makeText(DialogActivity.this, "对方接收成功", Toast.LENGTH_LONG)
					.show();
			Log.i("短信接收", "成功！");
		}
	};
}