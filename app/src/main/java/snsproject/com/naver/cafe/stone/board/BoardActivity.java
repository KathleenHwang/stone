package snsproject.com.naver.cafe.stone.board;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import snsproject.com.naver.cafe.stone.R;


public class BoardActivity extends ActionBarActivity {

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btnPrev:
                    cBoard.goPrev();
                    break;
                default:
                    break;
            }
        }
    };

    private BoardView cBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_board);

        initUi();
    }

    private void initUi() {
        this.cBoard = (BoardView) findViewById(R.id.cBoard);
        Button btnBegin = (Button) findViewById(R.id.btnBegin);
        btnBegin.setOnClickListener(onClickListener);
        Button btnPrev = (Button) findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(onClickListener);
        Button btnPrevBranch = (Button) findViewById(R.id.btnPrevBranch);
        btnPrevBranch.setOnClickListener(onClickListener);
        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(onClickListener);
        Button btnNextBranch = (Button) findViewById(R.id.btnNextBranch);
        btnNextBranch.setOnClickListener(onClickListener);
        Button btnEnd = (Button) findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(onClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
