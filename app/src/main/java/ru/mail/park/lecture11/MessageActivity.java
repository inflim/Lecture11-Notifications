package ru.mail.park.lecture11;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends AppCompatActivity{
    public static final String EXTRA_TEXT = "extra_text";

    @BindView(R.id.text_message) TextView messageText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        ButterKnife.bind(this);

        String message = getIntent().getStringExtra(EXTRA_TEXT);
        messageText.setText(message);
    }
}
