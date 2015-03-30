package com.find1x.outsmart;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private ArrayAdapter<String> listAdapter;
    private List<EventModel> eventList = new ArrayList<EventModel>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle(R.string.search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_search, menu);
        MenuItem switchItem = menu.findItem(R.id.menu_search);
        listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(listAdapter);
        searchView = (SearchView) switchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateAdapter(newText);
//                Toast.makeText(SearchActivity.this, "改变: " + newText, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        updateAdapter("");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_search:
//                Toast.makeText(this, "点击搜索", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAdapter(String query) {
        Persistence calendarSet = new Persistence("CalendarSet.db");
        Cursor eventCursor = getContentResolver().query(Events.CONTENT_URI, null,
                null, null, null);
        listAdapter.clear();
        eventList.clear();
        eventCursor.moveToFirst();
        while (eventCursor.moveToNext()) {
            int calID = eventCursor.getInt(eventCursor.getColumnIndex(Events.CALENDAR_ID));
            String title = eventCursor.getString(eventCursor.getColumnIndex(Events.TITLE));
            String eventLocation = eventCursor.getString(eventCursor.getColumnIndex(Events.EVENT_LOCATION));
            long dtstart = eventCursor.getLong(eventCursor.getColumnIndex(Events.DTSTART));
            int hasAlarm = eventCursor.getInt(eventCursor.getColumnIndex(Events.HAS_ALARM));
            long eventId = eventCursor.getLong(eventCursor.getColumnIndex(Events.ORIGINAL_ID));
            if (title != null) {
                eventList.add(new EventModel(calID, title, eventLocation, dtstart, hasAlarm, eventId));
            }
        }

        for (EventModel event : eventList) {
            if (event.title != null && event.calendarId == calendarSet.getValue()) {
                if (!query.equals("")) {
                    if (event.title.contains(query) || event.eventLocation.contains(query)) {
                        listAdapter.add(event.title);
                    }
                } else {
                    listAdapter.add(event.title);
                }
            }
        }
    }
}