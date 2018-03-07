package spider.app.sportsfete;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import net.cachapa.expandablelayout.ExpandableLayout;


public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
/*
        final RelativeLayout relativeLayout1, relativeLayout2, relativeLayout3, relativeLayout4, relativeLayout5,
                relativeLayout6, relativeLayout7;
        final ExpandableLayout expandableLayout1, expandableLayout2, expandableLayout3, expandableLayout4, expandableLayout5, expandableLayout6;
        expandableLayout1 = (ExpandableLayout) findViewById(R.id.exp1);
        expandableLayout2 = (ExpandableLayout) findViewById(R.id.exp2);
        expandableLayout3 = (ExpandableLayout) findViewById(R.id.exp3);
        expandableLayout4 = (ExpandableLayout) findViewById(R.id.exp4);
        expandableLayout5 = (ExpandableLayout) findViewById(R.id.exp5);
        expandableLayout6 = (ExpandableLayout) findViewById(R.id.exp6);

        relativeLayout1 = (RelativeLayout) findViewById(R.id.rel1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.rel2);
        relativeLayout3 = (RelativeLayout) findViewById(R.id.rel3);
        relativeLayout4 = (RelativeLayout) findViewById(R.id.rel4);
        relativeLayout5 = (RelativeLayout) findViewById(R.id.rel5);
        relativeLayout6 = (RelativeLayout) findViewById(R.id.rel6);
        relativeLayout7 = (RelativeLayout) findViewById(R.id.rel7);

/*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = new ScaleAnimation(
                        1f, 0f, // Start and end values for the X axis scaling
                        1f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(1000);
                relativeLayout1.startAnimation(anim);
            }
        },0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = new ScaleAnimation(
                        1f, 1f, // Start and end values for the X axis scaling
                        1f, 0f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(3000);
                //Animation anim = new AlphaAnimation(1, 0);
                //anim.setFillAfter(true); // Needed to keep the result of the animation
                //anim.setDuration(5000);
                relativeLayout2.startAnimation(anim);
            }
        },10);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Animation anim = new ScaleAnimation(
                        1f, 0f, // Start and end values for the X axis scaling
                        1f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(5000);*/
  /*             Animation anim = new AlphaAnimation(1,0);
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(500);
                relativeLayout3.startAnimation(anim);
 /*           }
        },2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = new ScaleAnimation(
                        1f, 0f, // Start and end values for the X axis scaling
                        1f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(500);
                relativeLayout4.startAnimation(anim);
            }
        },1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = new ScaleAnimation(
                        1f, 0f, // Start and end values for the X axis scaling
                        1f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(500);
                relativeLayout5.startAnimation(anim);
            }
        },1500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = new ScaleAnimation(
                        1f, 0f, // Start and end values for the X axis scaling
                        1f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(500);
                relativeLayout6.startAnimation(anim);
            }
        },2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation anim = new ScaleAnimation(
                        1f, 0f, // Start and end values for the X axis scaling
                        1f, 1f, // Start and end values for the Y axis scaling
                        Animation.RELATIVE_TO_SELF, 1f, // Pivot point of X scaling
                        Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                anim.setFillAfter(true); // Needed to keep the result of the animation
                anim.setDuration(500);
                relativeLayout6.startAnimation(anim);
            }
        },3000);
*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);

    }

    @Override
    public void onDestroy(){
        Runtime.getRuntime().gc();
        super.onDestroy();
    }
}
