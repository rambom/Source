package com.ethan.mlife.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView.OnEditorActionListener;

import com.ethan.mlife.R;

public class EditTextDelete extends LinearLayout {

	private EditText edt_input;
	private Button btn_delete;

	public EditTextDelete(Context context) {
		super(context);
	}

	public EditTextDelete(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = LayoutInflater.from(context).inflate(R.layout.edit_delete,
				this, true);
		edt_input = (EditText) view.findViewById(R.id.edtInput);
		btn_delete = (Button) this.findViewById(R.id.btnClear);
		edt_input.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					btn_delete.setVisibility(View.VISIBLE);
				} else {
					btn_delete.setVisibility(View.GONE);
				}
			}
		});

		btn_delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				edt_input.setText("");
			}
		});
	}

	public void setHint(int resid) {
		edt_input.setHint(resid);
	}

	/**
	 * @return 获取输入框中的内容
	 */
	public String getText() {
		return edt_input.getText().toString();
	}

	/**
	 * 文本动作事件
	 * @param e
	 */
	public void setOnEditorActionListener(OnEditorActionListener e) {
		edt_input.setOnEditorActionListener(e);
	}
}
