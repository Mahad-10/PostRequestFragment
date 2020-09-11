package pk.codebase.postrequestfragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class FragmentSignup extends Fragment {
    EditText userTxt, firstNameTxt, lastNameTxt, mobileNumTxt, emailTxt, passTxt, confirmPassTxt;
    Button registerBtn;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";

    public FragmentSignup() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        userTxt = rootView.findViewById(R.id.userNameTxt);
        firstNameTxt = rootView.findViewById(R.id.firstNameTxt);
        lastNameTxt = rootView.findViewById(R.id.lastNameTxt);
        mobileNumTxt = rootView.findViewById(R.id.mobileTxt);
        emailTxt = rootView.findViewById(R.id.emailTxt);
        passTxt = rootView.findViewById(R.id.passTxt);
        confirmPassTxt = rootView.findViewById(R.id.confirmPassTxt);
        registerBtn = rootView.findViewById(R.id.btnRegister);


        userTxt.setText("Maddy");
        firstNameTxt.setText("Mahad");
        lastNameTxt.setText("Munir");
        mobileNumTxt.setText("0123455");
        emailTxt.setText("mahadmunir10@gmail.com");
        passTxt.setText("123456");
        confirmPassTxt.setText("123456");


        sharedPreferences = rootView.getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userTxt.getText().toString().trim();
                String firstName = firstNameTxt.getText().toString().trim();
                String lastName = lastNameTxt.getText().toString().trim();
                String mobileNum = mobileNumTxt.getText().toString().trim();
                String email = emailTxt.getText().toString().trim();
                String pass = passTxt.getText().toString().trim();
                String confirmPass = confirmPassTxt.getText().toString().trim();

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getActivity(), "Enter your User Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(firstName)) {
                    Toast.makeText(getActivity(), "Enter your First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    Toast.makeText(getActivity(), "Enter your last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mobileNum)) {
                    Toast.makeText(getActivity(), "Please Enter your Mobile Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Please Enter your Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getActivity(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (TextUtils.isEmpty(confirmPass)) {
                    Toast.makeText(getActivity(), "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 6) {
                    Toast.makeText(getActivity(), "Password too short.Length of password must be greater than 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.equals(confirmPass)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Name, userName);
                    editor.putString(Email, email);
                    editor.putString(Phone, mobileNum);
                    editor.commit();

                    HttpRequest request = new HttpRequest();
                    request.setOnResponseListener(new HttpRequest.OnResponseListener() {
                        @Override
                        public void onResponse(HttpResponse response) {
                            if (response.code == HttpResponse.HTTP_OK) {
                                FragmentVerify fragmentVerify = new FragmentVerify();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container_view,fragmentVerify)
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(), response.text, Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    request.setOnErrorListener(new HttpRequest.OnErrorListener() {
                        @Override
                        public void onError(HttpError error) {
                            Toast.makeText(getActivity(), "Unexpected Error.Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                    JSONObject json;
                    try {
                        json = new JSONObject();
                        json.put("username", userName);
                        json.put("firstname", firstName);
                        json.put("lastname", lastName);
                        json.put("email", email);
                        json.put("password", pass);
                        json.put("mobile", mobileNum);

                    } catch (JSONException ignore) {
                        return;
                    }
                    request.post("http://192.168.100.25:5000/api/users/", json);
                }

            }


        });

        return rootView;
    }

}
