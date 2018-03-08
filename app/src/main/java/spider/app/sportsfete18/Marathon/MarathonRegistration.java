package spider.app.sportsfete18.Marathon;

import android.app.ProgressDialog;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spider.app.sportsfete18.API.ApiInterface;
import spider.app.sportsfete18.R;

/**
 * Created by AVINASH on 1/24/2018.
 */

public class MarathonRegistration extends android.support.v4.app.Fragment {

    EditText reg_user_name;
    EditText reg_user_rollno;
    EditText user_password;
    Spinner department_spinner;
    Button register;
    ViewGroup viewGroup;
    RadioGroup radioGroup;
    RadioButton radio_male;
    RadioButton radio_female;

    int selectedPosition=0;

    String[] dept = {
            "Select Department",
            "CSE",
            "ECE",
            "Eee",
            "Mech",
            "Prod",
            "Ice",
            "Chem",
            "Civil",
            "Meta",
            "Archi",
            "PhD_MSC_MS",
            "DoMS",
            "MCA",
            "M_tech"};

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = container;

        final View view = inflater.inflate(R.layout.marathon_reg, container, false);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                view.getWindowVisibleDisplayFrame(r);

                int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
                Log.d("height",""+heightDiff);
                if (heightDiff > 300) { // if more than 100 pixels, its probably a keyboard...

                }else{
                    Log.d("close","");
                    reg_user_name.clearFocus();
                    reg_user_rollno.clearFocus();
                    user_password.clearFocus();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sportsfete-732bf.firebaseapp.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        progressDialog  = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering user");

        reg_user_name = (EditText) getActivity().findViewById(R.id.user_name);
        user_password = (EditText) getActivity().findViewById(R.id.user_password);
        reg_user_rollno = (EditText) getActivity().findViewById(R.id.user_rollno);
        department_spinner = (Spinner) getActivity().findViewById(R.id.department);
        register = (Button) getActivity().findViewById(R.id.register);
        radioGroup = (RadioGroup) getActivity().findViewById(R.id.radio_group);
        radio_female = (RadioButton) getActivity().findViewById(R.id.female_radio);
        radio_male = (RadioButton) getActivity().findViewById(R.id.male_radio);

        radio_female.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        radio_male.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        reg_user_rollno.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        reg_user_name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        register.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        user_password.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));

        CustomAdapter adapter = new CustomAdapter(getActivity(),R.layout.dept_spinner_element,dept);
        department_spinner.setAdapter(adapter);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg_user_name.clearFocus();
                reg_user_rollno.clearFocus();
                user_password.clearFocus();
                if(validate()){
                    progressDialog.show();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("roll",reg_user_rollno.getText().toString().trim());
                    map.put("name",reg_user_name.getText().toString().trim());
                    map.put("dept",dept[selectedPosition].toUpperCase());
                    map.put("password",user_password.getText().toString().trim());

                    if(radioGroup.getCheckedRadioButtonId()==R.id.male_radio){
                        map.put("gender","M");
                    }
                    if(radioGroup.getCheckedRadioButtonId()==R.id.female_radio){
                        map.put("gender","F");
                    }

                    apiInterface.registerUserForMarathon(map).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressDialog.dismiss();
                            if(response.isSuccessful()){
                                Log.e("response",response.body().toString());
                                if(response.body().get("response code").getAsInt()==1){
                                    Snackbar.make(viewGroup,"Registered successfully-:)", Snackbar.LENGTH_SHORT).show();
                                }
                                if(response.body().get("response code").getAsInt()==-1){
                                    Snackbar.make(viewGroup,"User already registered!!", Snackbar.LENGTH_SHORT).show();
                                }
                                if(response.body().get("response code").getAsInt()==-2){
                                    Snackbar.make(viewGroup,"Invalid Roll Number or password!!", Snackbar.LENGTH_SHORT).show();
                                }
                                if(response.body().get("response code").getAsInt()==-3){
                                    Snackbar.make(viewGroup,"Internal Server Error!!", Snackbar.LENGTH_SHORT).show();
                                }
                            }else
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("error response",error+"");
                                    Snackbar.make(viewGroup,""+error, Snackbar.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            progressDialog.dismiss();
                            t.printStackTrace();
                        }
                    });
                }
            }
        });

        department_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        department_spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                reg_user_name.clearFocus();
                reg_user_rollno.clearFocus();
                user_password.clearFocus();
                return false;
            }
        });

        radio_male.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                reg_user_name.clearFocus();
                reg_user_rollno.clearFocus();
                user_password.clearFocus();
                return false;
            }
        });

        radio_female.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                reg_user_name.clearFocus();
                reg_user_rollno.clearFocus();
                user_password.clearFocus();
                return false;
            }
        });

        reg_user_rollno.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("focus",""+b);
                if(b){
                    Animation anim = new ScaleAnimation(
                            1f, 1.07f, // Start and end values for the X axis scaling
                            1f, 1.1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(200);
                    reg_user_rollno.startAnimation(anim);
                }else{
                    Animation anim = new ScaleAnimation(
                            1.07f, 1f, // Start and end values for the X axis scaling
                            1.1f, 1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(200);
                    reg_user_rollno.startAnimation(anim);
                }
            }
        });

        user_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Animation anim = new ScaleAnimation(
                            1f, 1.07f, // Start and end values for the X axis scaling
                            1f, 1.1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(200);
                    user_password.startAnimation(anim);
                }else{
                    Animation anim = new ScaleAnimation(
                            1.07f, 1f, // Start and end values for the X axis scaling
                            1.1f, 1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(200);
                    user_password.startAnimation(anim);
                }
            }
        });

        reg_user_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    Animation anim = new ScaleAnimation(
                            1f, 1.07f, // Start and end values for the X axis scaling
                            1f, 1.1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(200);
                    reg_user_name.startAnimation(anim);
                }else{
                    Animation anim = new ScaleAnimation(
                            1.07f, 1f, // Start and end values for the X axis scaling
                            1.1f, 1f, // Start and end values for the Y axis scaling
                            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                            Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
                    anim.setFillAfter(true); // Needed to keep the result of the animation
                    anim.setDuration(200);
                    reg_user_name.startAnimation(anim);
                }
            }
        });

    }

    public boolean validate(){
        if(reg_user_rollno.getText().toString().length()!=9){
            reg_user_rollno.setError(null);
            reg_user_rollno.setError("Enter 9 digit Roll No");
            return false;
        }

        if(reg_user_name.getText().toString().isEmpty()){
            reg_user_name.setError(null);
            reg_user_rollno.setError(null);
            reg_user_name.setError("Enter your Name");
            return false;
        }

        if(selectedPosition == 0){
            Snackbar.make(viewGroup,"Select department", Snackbar.LENGTH_SHORT).show();
            reg_user_rollno.setError(null);
            reg_user_name.setError(null);
            return false;
        }

        if(radioGroup.getCheckedRadioButtonId()!=R.id.female_radio&&radioGroup.getCheckedRadioButtonId()!=R.id.male_radio){
            Snackbar.make(viewGroup,"Choose Gender!!", Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onDestroyView(){
        Runtime.getRuntime().gc();
        super.onDestroyView();
    }
}
