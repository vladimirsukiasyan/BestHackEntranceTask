package dsow.besthackentrancetask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dsow.besthackentrancetask.Database.App;
import dsow.besthackentrancetask.Database.AppDatabase;
import dsow.besthackentrancetask.Database.DbBitmapUtility;
import dsow.besthackentrancetask.Database.MemberDao;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_MEMBER ="member";

    private RecyclerView recyclerView;
    private Menu collapsedMenu;
    private ImageView photoView;
    private boolean appBarExpanded = true;
    private DetailAdapter adapter;
    private Member member;
    private Bitmap photo=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Getting our views for Parallax Scrolling Tabs with header image
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout=findViewById(R.id.appbar);

        recyclerView=findViewById(R.id.scrollableview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Import photoView(photo of member) for photo change possibility
        photoView = findViewById(R.id.header);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we use external library to get image from gallery or another external source and further to crop him
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(DetailActivity.this);
            }
        });

        //Getting serializable member from intent
        member = (Member) getIntent().getSerializableExtra(DetailActivity.EXTRA_MEMBER);
        //Separating our member on the string to send them to DetailAdapter for recycleView
        final List<String> memberInfo = new ArrayList<>();
        memberInfo.add(member.surname + " " + member.name + " " + member.patronymic);
        memberInfo.add(member.role);
        memberInfo.add(member.group);
        memberInfo.add(member.about_me);
        memberInfo.add(member.link);

        photo=DbBitmapUtility.getImage(member.image); //Getting out photo's bitmap from byte
        photoView.setImageBitmap(photo);
        adapter=new DetailAdapter(this,memberInfo);
        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position==4) startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(memberInfo.get(position))));
            }
        });
        recyclerView.setAdapter(adapter);

//        int id=getIntent().getIntExtra(DetailActivity.EXTRA_ID,0);
//        String fio=getIntent().getStringExtra(DetailActivity.EXTRA_FIO);
//        String role=getIntent().getStringExtra(DetailActivity.EXTRA_ROLE);
//        String group=getIntent().getStringExtra(DetailActivity.EXTRA_GROUP);
//        String aboutMe=getIntent().getStringExtra(DetailActivity.EXTRA_ABOUT_ME);
//        String link=getIntent().getStringExtra(DetailActivity.EXTRA_LINK);
//        byte [] image=getIntent().getByteArrayExtra(DetailActivity.EXTRA_IMAGE);
        //Code for appBar animation
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //  Vertical offset == 0 indicates appBar is fully expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Here we get out image from external storage
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //We use Picasso for compress the image and get Bitmap after. So we should do this in not main thread;
                new MyAsyncTask().execute(result.getUri());
                photoView.setImageURI(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        collapsedMenu=menu;
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
        if (item.getTitle() == "Edit") {
            Toast.makeText(this, "clicked edit", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId()==android.R.id.home) {
            //If user clicked "Go back" icon on toolbar, we update out database here
            AppDatabase db = App.getInstance().getDatabase();
            MemberDao memberDao=db.memberDao();
            member.image=DbBitmapUtility.getBytes(photo); //getting byte from Bitmap
            memberDao.update(member);
            //Animation for come back to card in MainActivity
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Code for determining a state of appBar and adding edit icon to Toolbar menu
        if (collapsedMenu != null
                && (!appBarExpanded || collapsedMenu.size() != 1)) {
            //collapsed
            collapsedMenu.add(R.string.action_edit)
                    .setIcon(R.drawable.ic_edit)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            //expanded

        }
        return super.onPrepareOptionsMenu(collapsedMenu);
    }

    @Override
    public void onBackPressed() {
        //If user clicked "Go back" icon on telephone control menu, we update out database here
        super.onBackPressed();
        AppDatabase db = App.getInstance().getDatabase();
        MemberDao memberDao=db.memberDao();
        member.image=DbBitmapUtility.getBytes(photo);
        memberDao.update(member);
    }

    private class MyAsyncTask extends AsyncTask<Uri,Uri,Void>{
        @Override
        protected Void doInBackground(Uri[] uri) {
            try {
                //resing out photo for normal saving in database, else we will get memory crash
                photo=Picasso.get()
                        .load(uri[0])
                        .centerCrop()
                        .resize(350, 256)
                        .get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
