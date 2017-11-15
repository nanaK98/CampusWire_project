package com.cpen200.campuswire;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mCampusWirelist;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        mCampusWirelist = (RecyclerView) findViewById(R.id.CampusWire_list);
        mCampusWirelist.setHasFixedSize(true);
        mCampusWirelist.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CampusWire");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);
                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Insta, InstaViewHolder> FBRA = new FirebaseRecyclerAdapter<Insta, InstaViewHolder>(

                Insta.class,
                R.layout.insta_row,
                InstaViewHolder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(InstaViewHolder viewHolder, Insta model, int position) {

                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.setUserName(model.getUsername());
                viewHolder.setTimestamp(model.getTimestamp());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(MainActivity.this, "Reader Mode", Toast.LENGTH_LONG).show();

                        Intent singleInstaActivity = new Intent(MainActivity.this, SingleInstaActivity.class);
                        singleInstaActivity.putExtra("PostId", post_key);
                        startActivity(singleInstaActivity);
                    }
                });

            }
        };
        mCampusWirelist.setAdapter(FBRA);
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
        if (id == R.id.addIcon) {

            Intent intent = new Intent(MainActivity.this, PostActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            mAuth.signOut();
        }
        if (id == R.id.emailicon) {
            Intent intent = new Intent(MainActivity.this, EmailActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public static class InstaViewHolder extends RecyclerView.ViewHolder implements Comparable<Insta> {

        View mView;
        private String userName;
        private long timestamp;

        public InstaViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            TextView postUserName = mView.findViewById(R.id.textUsername);
            postUserName.setText(userName);
            this.userName = userName;
        }

        public void setTitle(String title) {
            TextView post_title = mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = mView.findViewById(R.id.textDescription);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }

        @Override
        public int compareTo(@NonNull Insta o) {
            if (o.getTimestamp() > getTimestamp()) {
                return 1;
            } else if ((o.getTimestamp() == getTimestamp())) {
                return 0;
            } else {
                return -1;
            }
        }
    }

}
