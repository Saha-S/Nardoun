package com.appmagazine.nardoon.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appmagazine.nardoon.R;
import com.appmagazine.nardoon.activities.*;
import com.appmagazine.nardoon.activities.Main;


public class test extends Fragment {


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
   View view = inflater.inflate(R.layout.fragment_test, container, false);

  // com.appmagazine.nardoon.activities.Main main = new Main();
   //main.getActionBar().hide();
   return view;
  }
 }
