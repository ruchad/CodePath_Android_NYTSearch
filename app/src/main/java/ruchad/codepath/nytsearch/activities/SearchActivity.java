package ruchad.codepath.nytsearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import ruchad.codepath.nytsearch.R;
import ruchad.codepath.nytsearch.fragment.FilterFragment;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* Custom Action Bar */
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_view);

        //ToDo: Show search history/suggestions in dropdown
        EditText tvSearch = ButterKnife.findById(actionBar.getCustomView(), R.id.tvSearch);
        tvSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    String searchText = v.getText().toString();
                    if(searchText.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(SearchActivity.this, SearchResultsActivity.class);
                        intent.putExtra(getApplicationContext().getResources().getString(R.string.search_string), v.getText().toString());
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mnuFilter:
                FragmentManager fm = getSupportFragmentManager();
                FilterFragment filterFragment = FilterFragment.getInstance("Search Filter");
                filterFragment.show(fm, "Fragment_Search_Filter");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
