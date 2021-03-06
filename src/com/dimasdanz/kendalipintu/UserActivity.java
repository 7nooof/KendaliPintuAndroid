/*******************************************************************************
 * Copyright (c) 2014 Dimas Rullyan Danu
 * 
 * Kendali Pintu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Kendali Pintu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Kendali Pintu. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.dimasdanz.kendalipintu;

import java.util.ArrayList;
import java.util.List;

import com.dimasdanz.kendalipintu.R;
import com.dimasdanz.kendalipintu.usermodel.UserAdapter;
import com.dimasdanz.kendalipintu.usermodel.UserDialogManager;
import com.dimasdanz.kendalipintu.usermodel.UserListView;
import com.dimasdanz.kendalipintu.usermodel.UserLoadData;
import com.dimasdanz.kendalipintu.usermodel.UserModel;
import com.dimasdanz.kendalipintu.usermodel.UserSendData;
import com.dimasdanz.kendalipintu.usermodel.UserDialogManager.UserDialogManagerListener;
import com.dimasdanz.kendalipintu.usermodel.UserLoadData.UserLoadDataListener;
import com.dimasdanz.kendalipintu.usermodel.UserSendData.UserSendDataListener;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class UserActivity extends FragmentActivity implements UserDialogManagerListener, UserListView.EndlessListener, OnItemClickListener, UserLoadDataListener, UserSendDataListener{
	UserListView userLv;
	UserAdapter userAdp;
	Integer page = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		List<UserModel> initData = new ArrayList<UserModel>();
		initData.add(new UserModel("0", "null", "0"));
		
		userAdp = new UserAdapter(UserActivity.this, initData, R.layout.list_item_user);
		
		userLv = (UserListView)findViewById(R.id.userListView);
		userLv.setOnItemClickListener(this);
		
		userLv.setLoadingView(R.layout.loading_layout);
		userLv.setAdapter(userAdp);
		
		userLv.setListener(UserActivity.this);
		
		userAdp.clear();
		new UserLoadData(this, page).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		} else if (itemId == R.id.action_add_account) {
			new UserDialogManager();
			UserDialogManager.newInstance(null).show(getSupportFragmentManager(), "UserForm");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public void loadData() {
		new UserLoadData(this, page).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ArrayList<String> userData = new ArrayList<String>();
		userData.add(userAdp.getItem(position).getUserID());
		userData.add(userAdp.getItem(position).getName());
		userData.add(userAdp.getItem(position).getPassword());
		new UserDialogManager();
		UserDialogManager.newInstance(userData).show(getSupportFragmentManager(), "UserForm");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, ArrayList<String> data) {
		new UserSendData(this).execute(data.get(0), data.get(1), data.get(2));
	}
	
	@Override
	public void onDialogNegativeClick(DialogFragment dialog, ArrayList<String> data) {
		new UserSendData(this).execute("delete", data.get(0));
	}

	@Override
	public void onTaskProgress(){
		if(page == 1){
			findViewById(R.id.layout_progress_bar).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
		}
	}

	@Override
	public void onTaskComplete(List<UserModel> userModel){
		if(userModel != null){
			userLv.addNewData(userModel);
			page += 1;
		}else{
			userLv.addNewData(null);
		}
	}

	@Override
	public void onSendDataComplete(Boolean result) {
		Log.d("Result", result.toString());
		if(result){
			userAdp.clear();
			page = 1;
			new UserLoadData(this, page).execute();
		}else{
			
		}
	}
}
