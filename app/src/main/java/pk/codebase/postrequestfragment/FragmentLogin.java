package pk.codebase.postrequestfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

public class FragmentLogin extends Fragment {
    EditText emailTxt, passTxt;
    Button btnLogin, btnReg;

    public FragmentLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        emailTxt = rootView.findViewById(R.id.email_txt);
        passTxt = rootView.findViewById(R.id.pass_txt);
        btnLogin = rootView.findViewById(R.id.btn_login);
        btnReg = rootView.findViewById(R.id.btn_register);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentSignup fragmentSignup = new FragmentSignup();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container_view,fragmentSignup)
                        .commit();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTxt.getText().toString().trim();
                String pass = passTxt.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(),"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    Toast.makeText(getActivity(),"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                HttpRequest request = new HttpRequest();
                request.setOnResponseListener(new HttpRequest.OnResponseListener() {
                    @Override
                    public void onResponse(HttpResponse response) {
                        if (response.code == HttpResponse.HTTP_OK) {

                            FragmentMain fragmentMain = new FragmentMain();
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.container_view,fragmentMain)
                                    .commit();
                        }else {
                            Toast.makeText(getActivity(),response.text,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                request.setOnErrorListener(new HttpRequest.OnErrorListener() {
                    @Override
                    public void onError(HttpError error) {
                        Toast.makeText(getActivity(),"Unexpected Error",Toast.LENGTH_SHORT).show();
                    }
                });

                JSONObject json;
                try {
                    json = new JSONObject();
                    json.put("email", email);
                    json.put("password",pass);

                } catch (JSONException ignore) {
                    return;
                }
                request.post("http://192.168.100.25:5000/api/login/", json);
            }
        });
        return rootView;
    }
}