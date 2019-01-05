package com.u3xj.collegebar;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class StudentsFragment extends Fragment {


    RecyclerView recyclerView;
    StudentsAdapter adapter;

    String userId;

    List<Students> studentsList;

    Bitmap profileName;

    Boolean isScrolling = false;

    public StudentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_students, container,false
        );

        studentsList = new ArrayList<>();

        userId =  Integer.toString(SharedPrefManager.getInstance(getActivity()).getUserID());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        prepareAlbums();
        return  view;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void loadStudentList()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL_STUDENT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray student = new JSONArray(response);
                            for (int i = 0; i < student.length(); i++)
                            {
                                JSONObject studentObject = student.getJSONObject(i);

                                String user_id = studentObject.getString("user_id");

                                if (user_id.equals(userId))
                                    continue;

                                profileName = stringToImage(studentObject.getString("profile_name"));
                                Students student1 = new Students(studentObject.getString("nick_name"),studentObject.getString("gender")
                                        ,profileName,studentObject.getString("status"),studentObject.getString("collegename"));
                                studentsList.add(student1);
                            }
                            adapter = new StudentsAdapter(getActivity(),studentsList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }
        );
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private Bitmap stringToImage(String image)
    {
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void prepareAlbums() {
        Bitmap profileName = BitmapFactory.decodeResource(getResources(),
                R.drawable.collegeimage);

        Students a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

         a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

        a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

        a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

        a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);

        a = new Students("Maroon5", "Male",profileName,"1","DSB, Campus");
        studentsList.add(a);



        adapter = new StudentsAdapter(getActivity(),studentsList);
        recyclerView.setAdapter(adapter);
    }
}
