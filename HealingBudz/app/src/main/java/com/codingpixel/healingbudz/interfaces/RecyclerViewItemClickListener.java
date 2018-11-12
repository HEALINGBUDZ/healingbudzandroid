package com.codingpixel.healingbudz.interfaces;
/*
 * Created by M_Muzammil Sharif on 06-Mar-18.
 */

public interface RecyclerViewItemClickListener {
    public void onItemClick(Object obj, int pos, int type);

    public boolean onItemLongClick(Object obj, int pos, int type);
}
