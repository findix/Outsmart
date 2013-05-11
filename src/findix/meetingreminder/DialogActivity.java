package findix.meetingreminder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import findix.meetingreminder.analysis.GetUserLocation;
import findix.meetingreminder.analysis.GetUserTime;
import findix.meetingreminder.db.DatabaseHelper;
import findix.meetingreminder.segmentation.Persistence;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.*;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class DialogActivity extends Activity implements OnClickListener {

	private TextView smstextView = null;
	private TextView timetextView = null;
	private TextView datetextView = null;
	private EditText editText_location = null;
	private EditText editText = null;
	private Button btn_ok = null;
	private Button btn_cancel = null;
	private Button btn_reply = null;
	private Button btn_changeLocation = null;
	private Button btn_changeEvent = null;
	private Button btn_changeTime = null;
	private Button btn_changeDate = null;
	private String[] location;
	private Calendar time = Calendar.getInstance();
	private String sender = new String();
	private AutoCompleteTextView autoCompletetextView = null;

	private boolean isClear_Event = false;
	private boolean isClear_Location = false;

	// private static String calanderURL = "";
	private static String calanderEventURL = "";
	private static String calanderRemiderURL = "";
	// Ϊ�˼��ݲ�ͬ�汾������,2.2�Ժ�url�����ı�
	static {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			// calanderURL = "content://com.android.calendar/calendars";
			calanderEventURL = "content://com.android.calendar/events";
			calanderRemiderURL = "content://com.android.calendar/reminders";
		} else {
			// calanderURL = "content://calendar/calendars";
			calanderEventURL = "content://calendar/events";
			calanderRemiderURL = "content://calendar/reminders";

		}

	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dialog);
		setTheme(R.style.translucent);
		datetextView = (TextView) findViewById(R.id.datetextView);
		timetextView = (TextView) findViewById(R.id.timetextView);
		editText_location = (EditText) findViewById(R.id.locationedittext);
		smstextView = (TextView) findViewById(R.id.smstextView);
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
		autoCompletetextView = (AutoCompleteTextView) findViewById(R.id.AutoCompleteTextView);
		autoCompletetextView.setOnClickListener(this);

		// ����intent
		Intent intent = getIntent();
		String content = intent.getStringExtra("content");
		String sender = intent.getStringExtra("sender");
		Log.i("content", content);
		Log.i("sender", sender);

		GetUserTime getUserTime = new GetUserTime(content);
		time = getUserTime.getTime();
		GetUserLocation getUserLocation = new GetUserLocation(
				getUserTime.getNoDateMsg());
		location = getUserLocation.getLocation();
		this.sender = sender;
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTime = new SimpleDateFormat("H:mm");
		datetextView.setText(formatDate.format(time.getTime()));
		timetextView.setText(formatTime.format(time.getTime()));
		editText_location.setText(getUserLocation.getUserLocation(this));
		editText_location.clearFocus();
		smstextView.setText(content);

		// TipHelper.PlaySound(this);// ����
		// long ring[]={1000,500,1000};
		// TipHelper.Vibrate(this, ring, false);//��
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
					.setTitle("���õص�")
					// ���öԻ������
					.setMultiChoiceItems(location, defaultSelectedStatus,
							new OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									// �����ظ�ѡ��ȡ��������Ӧȥ�ı�item��Ӧ��boolֵ�����ȷ��ʱ���������bool[],�õ�ѡ�������
									defaultSelectedStatus[which] = isChecked;
								}
							}) // ���öԻ���[�϶�]��ť
					.setPositiveButton("ȷ��",
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
							}).setNegativeButton("ȡ��", null)// ���öԻ���[��]��ť
					.show();
			break;
		}
		case R.id.changeEventButton: {
			final boolean[] defaultSelectedStatus = new boolean[location.length];
			for (int i = 0; i < location.length; i++)
				defaultSelectedStatus[i] = false;
			new AlertDialog.Builder(this)
					.setTitle("���õص�")
					// ���öԻ������
					.setMultiChoiceItems(location, defaultSelectedStatus,
							new OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									// �����ظ�ѡ��ȡ��������Ӧȥ�ı�item��Ӧ��boolֵ�����ȷ��ʱ���������bool[],�õ�ѡ�������
									defaultSelectedStatus[which] = isChecked;
								}
							}) // ���öԻ���[�϶�]��ť
					.setPositiveButton("ȷ��",
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
									autoCompletetextView.setText(locationSet);
								}
							}).setNegativeButton("ȡ��", null)// ���öԻ���[��]��ť
					.show();
			break;
		}
		case R.id.btn_ok: {

			// �����¼�
			String calId = "";
			Persistence setCalendar = new Persistence("CalendarSet.db");
			calId = (setCalendar.getValue()) + "";
			ContentValues event = new ContentValues();
			event.put("title", autoCompletetextView.getText().toString());
			event.put("description", autoCompletetextView.getText().toString());
			// �����˻�
			if (!editText_location.equals("")
					&& !editText_location.equals("��ѡ��ص�")) {
				event.put("eventLocation", editText_location.getText()
						.toString());
			}
			event.put("calendar_id", calId);
			Calendar mCalendar = Calendar.getInstance();
			mCalendar.set(Calendar.HOUR_OF_DAY, 10);
			long start = time.getTimeInMillis();
			mCalendar.set(Calendar.HOUR_OF_DAY, 11);
			long end = time.getTimeInMillis() + 3600000;
			event.put("dtstart", start);
			event.put("dtend", end);
			TimeZone tz = TimeZone.getDefault();
			event.put("eventTimezone", tz.getID());
			event.put("hasAlarm", 1);
			Uri newEvent = getContentResolver().insert(
					Uri.parse(calanderEventURL), event);
			long id = Long.parseLong(newEvent.getLastPathSegment());
			ContentValues values = new ContentValues();
			values.put("event_id", id);
			values.put("minutes", 10);
			// ��������
			getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
			Toast.makeText(DialogActivity.this, "������ѳɹ�!!!", Toast.LENGTH_SHORT)
					.show();

			// finish();

			// ��ӵص㵽���ݿ�
			// �������ݿ�
			DatabaseHelper dbHelper = new DatabaseHelper(this, "user.db3");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			String location_Temp = editText_location.getText().toString();
			String raw = "select location from user where location=\'"
					+ location_Temp + "\'";
			Cursor cursor = db.rawQuery(raw, null);
			if (!cursor.moveToNext() && !location_Temp.equals("")
					&& !location_Temp.equals("��ѡ��ص�")) {
				String sql = "insert or ignore into user(location) values('"
						+ location_Temp + "');";
				System.out.println(sql);
				db.execSQL(sql);
				Toast.makeText(this,
						"������֪��" + "\"" + location_Temp + "\"" + "����ط���",
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
					.setTitle("ȷ�ϻظ�")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									editText = (EditText) myLoginView
											.findViewById(R.id.EditText);
									String reply = editText.getText()
											.toString();
									// System.out.println(reply);
									PendingIntent sentIntent = PendingIntent
											.getBroadcast(DialogActivity.this,
													0, new Intent(), 0);
									if (PhoneNumberUtils
											.isGlobalPhoneNumber(sender)
											&& sender.length() > 0
											&& reply.length() > 0) {
										SmsManager sms = SmsManager
												.getDefault();
										sms.sendTextMessage(sender, null,
												reply, sentIntent, null);
										Toast.makeText(
												DialogActivity.this,
												"����� \"" + sender + "\" ���Ͷ��� \""
														+ reply + "\" �ɹ�",
												Toast.LENGTH_LONG).show();
										 finish();
									} else {
										if (sender.length() == 0) {
											Toast.makeText(DialogActivity.this,
													"������Ϣû�з����ˣ�������û���ظ���Ŷ~",
													Toast.LENGTH_LONG).show();
										} else if (reply.length() == 0) {
											Toast.makeText(DialogActivity.this,
													"������Ϣʲô��ûдŶ����Ӧ�ûظ�ʲô�أ�",
													Toast.LENGTH_LONG).show();
										} else {
											Toast.makeText(
													DialogActivity.this,
													"����� \"" + sender
															+ "\" ���Ͷ��� \""
															+ reply
															+ "\" ʧ�ܣ������³���",
													Toast.LENGTH_LONG).show();
										}
									}
									//finish();
								}
							}).setNegativeButton("ȡ��", null).create();
			alertDialog.show();
			break;
		}

		case R.id.btn_cancel: {
			finish();
			break;
		}
		case R.id.AutoCompleteTextView: {
			if (isClear_Event == false) {
				autoCompletetextView.setText("");
				isClear_Event = true;
			}
			break;
		}
		case R.id.locationedittext: {
			if (isClear_Location == false) {
				editText_location.setText("");
				isClear_Location = true;
			}
			break;
		}
		}
	}

	protected Dialog onCreateDialog(int id) {
		// ������ȡ���ں�ʱ���
		Calendar calendar = Calendar.getInstance();

		Dialog dialog = null;
		switch (id) {
		case R.id.changeDateButton:
			DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker datePicker, int year,
						int month, int dayOfMonth) {
					// Calendar�·��Ǵ�0��ʼ,����monthҪ��1
					time.set(year, month, dayOfMonth);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					datetextView.setText(format.format(time.getTime()));
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
					timetextView.setText(format.format(time.getTime()));
				}
			};
			dialog = new TimePickerDialog(this, timeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE), true); // �Ƿ�Ϊ��ʮ����
			break;
		default:
			break;
		}
		return dialog;
	}

	/*
	 * ��Ա�ڲ���,�˴�Ϊ��߿������ԣ�Ҳ���Ի��������ڲ���
	 */
	private class BtnOnClickListener implements View.OnClickListener {

		private int dialogId = 0; // Ĭ��Ϊ0����ʾ�Ի���

		public BtnOnClickListener(int dialogId) {
			this.dialogId = dialogId;
		}

		@Override
		public void onClick(View view) {
			showDialog(dialogId);
		}

	}
}