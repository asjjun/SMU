package com.SW_FINAL.mp_proj;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;
    public FragmentManager fm;
    public FragmentTransaction ft;
    public MatchingFragment matchingFragment;
    public DeptBoardFragment deptBoardFragment;
    public SchoolBoardFragment schoolBoardFragment;
    //public MoreInfoFragment moreInfoFragment;
    public MarketFragment marketActivity;
    DatabaseReference nameRef;

    FirebaseUser user;
    DatabaseReference matchingRef;
    private long backKeyPressedTime = 0;
    private Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        bottomNavigationView = findViewById(R.id.bottomNavi);
        deptBoardFragment = new DeptBoardFragment();
        user= FirebaseAuth.getInstance().getCurrentUser();



        setFrag(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_matching: {
                        setFrag(0);
                        break;
                    }
                    case R.id.action_dept: {
                        setFrag(1);
                        break;
                    }
                    case R.id.action_school: {
                        setFrag(2);
                        break;
                    }
                    case R.id.action_store:{
                        setFrag(3);
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //???????????? ?????? ?????? ????????? ??????
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //????????? ?????? ?????? ?????????
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.member_btn:
                Intent intent = new Intent(this, MemberInfoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'??????\' ????????? ?????? ??? ???????????? ???????????????.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            removeMatching();
            finish();
            toast.cancel();
        }
    }

    public void setFrag(int n){
        switch (n){
            case 0: {
                if(matchingFragment == null) {
                    matchingFragment = new MatchingFragment();
                    fm.beginTransaction().add(R.id.main_frame, matchingFragment).commit();
                }
                if(matchingFragment != null) fm.beginTransaction().show(matchingFragment).commit();
                if(deptBoardFragment != null) fm.beginTransaction().hide(deptBoardFragment).commit();
                if(schoolBoardFragment != null) fm.beginTransaction().hide(schoolBoardFragment).commit();
                if(marketActivity != null) fm.beginTransaction().hide(marketActivity).commit();
                this.invalidateOptionsMenu();
                break;
            }

            case 1: {
                if(deptBoardFragment != null){
                    fm.beginTransaction().remove(deptBoardFragment).commit();
                    deptBoardFragment = null;
                }

                if(deptBoardFragment == null){
                    deptBoardFragment = new DeptBoardFragment();
                    fm.beginTransaction().add(R.id.main_frame,deptBoardFragment).commit();
                }
                if(matchingFragment != null) fm.beginTransaction().hide(matchingFragment).commit();
                if(deptBoardFragment != null) fm.beginTransaction().show(deptBoardFragment).commit();
                if(schoolBoardFragment != null) fm.beginTransaction().hide(schoolBoardFragment).commit();
                if(marketActivity != null) fm.beginTransaction().hide(marketActivity).commit();
                this.invalidateOptionsMenu();
                break;
            }

            case 2: {
                if(schoolBoardFragment != null){
                    fm.beginTransaction().remove(schoolBoardFragment).commit();
                    schoolBoardFragment = null;
                }
                if(schoolBoardFragment == null){
                    schoolBoardFragment = new SchoolBoardFragment();
                    fm.beginTransaction().add(R.id.main_frame,schoolBoardFragment).commit();
                }
                if(matchingFragment != null) fm.beginTransaction().hide(matchingFragment).commit();
                if(deptBoardFragment != null) fm.beginTransaction().hide(deptBoardFragment).commit();
                if(schoolBoardFragment != null) fm.beginTransaction().show(schoolBoardFragment).commit();
                if(marketActivity != null) fm.beginTransaction().hide(marketActivity).commit();
                break;
            }

            case 3: {
                if(marketActivity != null){
                    fm.beginTransaction().remove(marketActivity).commit();
                    marketActivity =null;
                }
                if(marketActivity == null) {
                    marketActivity = new MarketFragment();
                    fm.beginTransaction().add(R.id.main_frame, marketActivity).commit();
                }
                if(matchingFragment != null) fm.beginTransaction().hide(matchingFragment).commit();
                if(deptBoardFragment != null) fm.beginTransaction().hide(deptBoardFragment).commit();
                if(schoolBoardFragment != null) fm.beginTransaction().hide(schoolBoardFragment).commit();
                if(marketActivity != null) fm.beginTransaction().show(marketActivity).commit();
                this.invalidateOptionsMenu();
                break;
            }
        }
    }
    void removeMatching(){
        /**
         *  ???????????????????????? ????????? ???????????? ???????????????  DB ??????
         */
        matchingRef = FirebaseDatabase.getInstance().getReference("matching");
        matchingRef.child(Storage.MyInterest).child(Storage.MyName).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}