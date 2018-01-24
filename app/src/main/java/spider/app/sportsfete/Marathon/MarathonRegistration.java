package spider.app.sportsfete.Marathon;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spider.app.sportsfete.API.ApiInterface;
import spider.app.sportsfete.R;

/**
 * Created by AVINASH on 1/24/2018.
 */

public class MarathonRegistration extends android.support.v4.app.Fragment {

    EditText reg_user_name;
    EditText reg_user_rollno;
    Spinner department_spinner;
    Button register;
    ViewGroup viewGroup;

    int selectedPosition=0;

    String[] dept = {
            "Select Department",
            "Cse",
            "Ece",
            "Eee",
            "Mech",
            "Prod",
            "Ice",
            "Chemical",
            "Civil",
            "Metallurgy",
            "Architecture",
            "PhD+MSC",
            "DoMS",
            "MCA",
            "Mtech"};

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = container;
        return inflater.inflate(R.layout.marathon_reg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://us-central1-sportsfete-732bf.cloudfunctions.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        progressDialog  = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering user");

        reg_user_name = (EditText) getActivity().findViewById(R.id.user_name);
        reg_user_rollno = (EditText) getActivity().findViewById(R.id.user_rollno);
        department_spinner = (Spinner) getActivity().findViewById(R.id.department);
        register = (Button) getActivity().findViewById(R.id.register);

        reg_user_rollno.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        reg_user_name.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));
        register.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),  "fonts/HammersmithOneRegular.ttf"));

        CustomAdapter adapter = new CustomAdapter(getActivity(),R.layout.dept_spinner_element,dept);
        department_spinner.setAdapter(adapter);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    progressDialog.show();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("roll",reg_user_rollno.getText().toString().trim());
                    map.put("name",reg_user_name.getText().toString().trim());
                    map.put("dept",dept[selectedPosition]);

                    apiInterface.registerUserForMarathon(map).enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            progressDialog.dismiss();
                            if(response.isSuccessful()){
                                Log.e("response",response.body().toString());
                                Snackbar.make(viewGroup,"Registered successfully-:)", Snackbar.LENGTH_SHORT).show();
                            }else
                                try {
                                    String error = response.errorBody().string();
                                    Log.e("error response",error+"");
                                    Snackbar.make(viewGroup,""+error.split(":")[2].trim(), Snackbar.LENGTH_SHORT).show();
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

        return true;
    }
}
