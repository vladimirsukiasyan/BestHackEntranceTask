package dsow.besthackentrancetask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dsow.besthackentrancetask.Database.App;
import dsow.besthackentrancetask.Database.AppDatabase;
import dsow.besthackentrancetask.Database.DbBitmapUtility;
import dsow.besthackentrancetask.Database.MemberDao;


public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CardAdapter adapter;
    private List<Member> members = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        AppDatabase db = App.getInstance().getDatabase(); //getting Room database
        MemberDao memberDao=db.memberDao(); //getting database API for the table "Member"

//        !!!This comment block shows how we inserted our team's member into table
//        List<Member> membersList=new ArrayList<Member>();
//        Member member1=new Member();
//        member1.name="Владимир";
//        member1.surname="Сукиасян";
//        member1.patronymic="Мартунович";
//        member1.role="Android разработчик, логика";
//        member1.group="ИУ5-21Б";
//        member1.about_me="About me ... bla bla bla ...";
//        member1.link="https://vk.com/notkpss";
//        membersList.add(member1);
//        membersList.add(member2);
//        memberDao.insertAll(membersList);

        members=memberDao.getAll(); //getting all members from Table by Room query
        adapter = new CardAdapter(this, members);
        //Here we add custom listener for RecyclerView in order to open DetailActivity for clicked member
        //For the full information about them you can open CardApater
        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_MEMBER,members.get(position)); //put our Serializable member into Intent for sending to DetailActivity
                //This is code for wonderful transition animation between cardView and DetailActivity
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this, view.findViewById(R.id.photo_view), "profile");
                startActivity(intent, options.toBundle());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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

    @Override
    protected void onResume() {
        //Here we update the list of our recyclerView when we come back from DetailActivity
        AppDatabase db = App.getInstance().getDatabase();
        MemberDao memberDao=db.memberDao();
        members=memberDao.getAll();
        adapter.setItems(members);
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}
