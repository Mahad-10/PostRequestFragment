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

import java.util.zip.Inflater;

import pk.codebase.requests.HttpError;
import pk.codebase.requests.HttpRequest;
import pk.codebase.requests.HttpResponse;

public class FragmentVerify extends Fragment {
    EditText emailTxt,otpTxt;
    Button submitBtn;

    public FragmentVerify() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify, container, false);

        emailTxt = rootView.findViewById(R.id.email2Txt);
        otpTxt = rootView.findViewById(R.id.otpText);
        submitBtn = rootView.findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTxt.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                String otp = otpTxt.getText().toString().trim();
                if (TextUtils.isEmpty(otp)) {
                    Toast.makeText(getActivity(), "Please Enter OTP Code", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    HttpRequest request = new HttpRequest();
                    request.setOnResponseListener(new HttpRequest.OnResponseListener() {
                        @Override
                        public void onResponse(HttpResponse response) {
                            if (response.code == HttpResponse.HTTP_OK) {
                                FragmentMain fragmentMain = new FragmentMain();
                                getFragmentManager().beginTransaction()
                                        .replace(R.id.container_view,fragmentMain)
                                        .commit();
                            } else {
                                Toast.makeText(getActivity(), response.text, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    request.setOnErrorListener(new HttpRequest.OnErrorListener() {
                        @Override
                        public void onError(HttpError error) {
                            Toast.makeText(getActivity(), "Unexpected Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    JSONObject json;
                    try {
                        json = new JSONObject();
                        json.put("email", email);
                        json.put("otp", otp);
                    } catch (JSONException ignore) {
                        return;
                    }
                    request.post("http://192.168.100.25:5000/api/verifyuser/", json);
                }
            }
        });
        return rootView;
    }
}