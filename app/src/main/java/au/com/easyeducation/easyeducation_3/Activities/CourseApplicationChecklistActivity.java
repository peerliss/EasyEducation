package au.com.easyeducation.easyeducation_3.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import au.com.easyeducation.easyeducation_3.R;

public class CourseApplicationChecklistActivity extends AppCompatActivity {

    Button beginApplicationButton;
    private String businessTypeString;
    private String instituteRefString;
    private String courseRefString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_application_checklist);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable gradient = getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setBackgroundDrawable(gradient);

        Intent intent = getIntent();

        businessTypeString = intent.getExtras().getString("businessType");
        instituteRefString = intent.getExtras().getString("businessRef");
        courseRefString = intent.getExtras().getString("courseRef");

        beginApplicationButton = findViewById(R.id.courseApplicationChecklistBeginButton);

        beginApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), CourseApplicationActivity.class);
                Intent intent = new Intent(getApplicationContext(), CourseApplicationNewActivity.class);
                startActivity(intent);
            }
        });

        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
