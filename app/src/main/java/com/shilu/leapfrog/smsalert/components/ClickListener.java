package com.shilu.leapfrog.smsalert.components;


import android.view.View;

/**
 * Interface to implement onClickListener for RecyclerAdapter
 *
 * @author: Shilu Shrestha, shilushrestha@lftechnology.com
 * @date: 5/11/15
 */
public interface ClickListener {
    void itemClicked(View view, int position);
}