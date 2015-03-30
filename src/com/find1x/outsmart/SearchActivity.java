package com.find1x.outsmart;

import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TwoLineListItem;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.find1x.outsmart.pojos.EventModel;
import com.find1x.outsmart.segmentation.Persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 2015/3/27.
 */
public class SearchActivity extends SherlockActivity {
    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter<EventModel> adapter;
    private List<EventModel> eventList = new ArrayList<EventModel>();
    private List<EventModel> events = new ArrayList<EventModel>();
    private int calendarId = new Persistence("CalendarSet.db").getValue();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle(R.string.search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initEvents();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventModel eventModel = getEventModel(position);
                Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, eventModel.eventId);
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri);
                startActivity(intent);
            }
        });
        adapter = new ArrayAdapter<EventModel>(this, android.R.layout.simple_list_item_2, eventList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TwoLineListItem row;
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = (TwoLineListItem) inflater.inflate(android.R.layout.simple_list_item_2, null);
                } else {
                    row = (TwoLineListItem) convertView;
                }
                EventModel data = eventList.get(position);
                row.getText1().setTextColor(Color.BLACK);
                row.getText2().setTextColor(Color.BLACK);
                row.getText1().setText(data.title);
                row.getText2().setText(data.eventLocation);

                return row;
            }
        };
        listView.setAdapter(adapter);
    }

    private void initEvents() {
        //数据库异步查询
        new AsyncQueryHandler(getContentResolver()) {
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    int calID = cursor.getInt(cursor.getColumnIndex(Events.CALENDAR_ID));
                    String title = cursor.getString(cursor.getColumnIndex(Events.TITLE));
                    String eventLocation = cursor.getString(cursor.getColumnIndex(Events.EVENT_LOCATION));
                    long dtstart = cursor.getLong(cursor.getColumnIndex(Events.DTSTART));
                    int hasAlarm = cursor.getInt(cursor.getColumnIndex(Events.HAS_ALARM));
                    long eventId = cursor.getLong(cursor.getColumnIndex(Events._ID));
                    if (title != null) {
                        events.add(new EventModel(calID, title, eventLocation, dtstart, hasAlarm, eventId));
                    }
                }
                updateAdapter("");
            }

        }.startQuery(0, null, Events.CONTENT_URI, null, null, null, "_id desc");
    }

    private void updateAdapter(String query) {
        if (events == null) return;
        adapter.clear();
        for (EventModel event : events) {
            if (event.title != null && event.calendarId == calendarId) {
                if (query.equals("")) {
                    event.positionInList = adapter.getCount();
                    adapter.add(event);
                } else {
                    if (event.title.contains(query) || event.eventLocation.contains(query)) {
                        event.positionInList = adapter.getCount();
                        adapter.add(event);
                    }
                }
            }
        }
    }

    private EventModel getEventModel(int position) {
        for (EventModel eventModel : eventList) {
            if (eventModel.positionInList == position) {
                return eventModel;
            }
        }
        return null;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_search, menu);
        MenuItem switchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) switchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateAdapter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_search:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}