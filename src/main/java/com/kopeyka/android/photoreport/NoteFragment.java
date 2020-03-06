package com.kopeyka.android.photoreport;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.kopeyka.android.photoreport.http.API;
import com.kopeyka.android.photoreport.http.APIClient;
import com.kopeyka.android.photoreport.http.DocRequest;
import com.kopeyka.android.photoreport.http.TaskResponse;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteFragment extends Fragment   {

    private Note mNote;



    private EditText mTitleField;
    private EditText mContentField;
    private EditText mDocNoFiled;
    private TextView mTitle;
    private TextView mDate;
    private Button mDateButton;
    private CheckBox mCompleteCheckBox;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private ArrayList<Photo> mPhotos;

    private static final String TAG = "NoteFragment";
    public static final String EXTRA_NOTE_ID = "com.android.notes.note_id";
    private static final String DIALOG_IMAGE = "image";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;

    public static NoteFragment newInstance(UUID noteId) {
        // Attaching arguments to a fragment must be done after the fragment
        // is created but before it is added to an activity.
        // This function uses the standard convention, call this function
        // instead of the constructor directly.
        // TODO: Should the constructor be marked as private?
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NOTE_ID, noteId);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void setFormattedDateButton(FragmentActivity activity) {
        if (activity != null) {
            Date date = mNote.getDate();
            DateFormat dateFormat = android.text.format.DateFormat
                    .getDateFormat(activity.getApplicationContext());
            DateFormat timeFormat = android.text.format.DateFormat
                    .getTimeFormat(activity.getApplicationContext());
            mDateButton.setText(dateFormat.format(date) +
                    " " +
                    timeFormat.format(date));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_note, menu);
    }


    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selectionHandled;
        API apiboss;

        switch (item.getItemId()) {
            case R.id.menu_item_download_note:
                Toast.makeText(super.getContext(), "11111", Toast.LENGTH_SHORT).show();



                apiboss = APIClient.putDoc().create(API.class);
                Call<DocRequest> call = apiboss.postJson(new DocRequest(mNote,this));
                call.enqueue(new Callback<DocRequest>(){
                    @Override
                    public void onResponse(Call<DocRequest> call, Response<DocRequest> response) {
                     }

                    @Override
                    public void onFailure(Call<DocRequest> call, Throwable t) {

                    }

                });



                selectionHandled = super.onOptionsItemSelected(item);
                break;
            default:
                selectionHandled = super.onOptionsItemSelected(item);
                break;
        }
        return selectionHandled;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        UUID noteId = (UUID) getArguments().getSerializable(EXTRA_NOTE_ID);
        mNote = Notebook.getInstance(getActivity()).getNote(noteId);
    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater,
                            ViewGroup parent,
                             Bundle savedInstanceState) {


          View view = inflater.inflate(R.layout.fragment_note,
                parent,
                false);
        setHasOptionsMenu(true);
//
//        mTitle = (TextView) view.findViewById(R.id.noteTitleText);
//        mTitle.setText(mNote.getTitle());
//
//
//        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
//        String strDt = simpleDate.format(mNote.getDate());
//
//        mDate = (TextView) view.findViewById(R.id.noteDateText);
//        mDate.setText(strDt);
//        mDate.setGravity(1);

        mDocNoFiled = (EditText) view.findViewById(R.id.note_docNo);
        mDocNoFiled.setText(mNote.getDocNo());


        mTitleField = (EditText) view.findViewById(R.id.note_title);
        mTitleField.setText(mNote.getTitle());

        mTitleField.setFocusable(true);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        mContentField = (EditText) view.findViewById(R.id.note_content);
        mContentField.setText(mNote.getContent());
        mContentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start,
                                          int count,
                                          int after) {
                // This space intentionally left blank
            }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start,
                                      int before,
                                      int count) {
                mNote.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // This space intentionally left blank
            }
        });

        mDateButton = (Button) view.findViewById(R.id.note_date);
        setFormattedDateButton(getActivity());
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mNote.getDate());

                // We want to get the selected date back from the dialog
                dialog.setTargetFragment(NoteFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });




        mCompleteCheckBox = (CheckBox) view.findViewById(R.id.note_complete);
        mCompleteCheckBox.setChecked(mNote.isComplete());
        mCompleteCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        mNote.setComplete(isChecked);
                    }
                });

//
        mPhotoButton = (ImageButton) view.findViewById(R.id.note_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager pm = getActivity().getPackageManager();

                boolean hasCamera =
                        pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
                                pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
                                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD &&
                                        Camera.getNumberOfCameras() > 0);

                if (hasCamera) {
                    Intent intent = new Intent(getActivity(),
                            NoteCameraActivity.class);
                    startActivityForResult(intent, REQUEST_PHOTO);

                } else {
                    Toast.makeText(getActivity(),
                            getResources()
                                    .getString(R.string.error_no_camera),
                            Toast.LENGTH_LONG).show();
                }
            }
        });



        ListView listView = (ListView) view.findViewById(R.id.note_listView);
        mPhotos = mNote.getPhotoArray();
        PhotoAdapter adapter = new PhotoAdapter(mPhotos);
        listView.setAdapter(adapter);


        mPhotoView = (ImageView) view.findViewById(R.id.note_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Photo photo = mNote.getPhoto();

                if (photo != null) {
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    String path = getActivity().getFileStreamPath(
                            photo.getFileName()).getAbsolutePath();
                    ImageFragment.newInstance(path)
                            .show(fm, DIALOG_IMAGE);
                }
            }
        });


        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_DATE) {
                Date date = (Date) data
                        .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mNote.setDate(date);
                setFormattedDateButton(getActivity());
            } else if (requestCode == REQUEST_PHOTO) {
                String fileName = data
                        .getStringExtra(NoteCameraFragment.EXTRA_PHOTO_FILENAME);
                if (fileName != null) {
                    Photo photo = new Photo(fileName);
                    mNote.setPhoto(photo);
                    Log.d("onActivityResult","showPhoto");
                    showPhoto();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(this).attach(this).commit();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();

        // onPause() is the safest choice to save notes.
        // onStop() or onDestroy() might not work.
        // A paused activity will be destroyed if the OS needs to
        // reclaim memory, where you cannot count on onStop() or onDestroy()
        boolean success = Notebook.getInstance(getActivity()).saveNotes();

        if (!success) {
            Toast.makeText(getActivity(),
                    getResources()
                            .getString(R.string.error_saving),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();

    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    private void showPhoto() {
        // Reset the image button's image based o our photo
        Photo photo = mNote.getPhoto();
        BitmapDrawable bitmapDrawable = null;
        if (photo != null) {
            String path = getActivity()
                    .getFileStreamPath(photo.getFileName()).getAbsolutePath();
            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(),
                    path);
        }
        mPhotoView.setImageDrawable(bitmapDrawable);
    }

    private class PhotoAdapter extends ArrayAdapter<Photo> {
       public PhotoAdapter(ArrayList<Photo> photo) {
           super(getActivity(), 0, photo);
       }
       @Override
       public View getView(int position, View convertView, ViewGroup parent) {
           if (convertView == null) {
               convertView = getActivity().getLayoutInflater()
                       .inflate(R.layout.list_item_photo, null);
           }
           Photo photo = getItem(position);
           BitmapDrawable bitmapDrawable = null;
           if (photo != null) {
               String path = getActivity()
                       .getFileStreamPath(photo.getFileName()).getAbsolutePath();
               bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(),
                       path);
               ImageView photoItem = (ImageView) convertView.findViewById(R.id.list_item_photo_Image);
               photoItem.setImageDrawable(bitmapDrawable);

               TextView TitleIMG = (TextView) convertView.findViewById(R.id.list_item_photo_Image_text);
               TitleIMG.setText(String.valueOf(position+1));

           }

           return convertView;
       }


    }
}




