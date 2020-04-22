package com.kopeyka.android.photoreport;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.kopeyka.android.photoreport.http.API;
import com.kopeyka.android.photoreport.http.APIClient;
import com.kopeyka.android.photoreport.http.DocRequest;
import com.kopeyka.android.photoreport.http.DocRequestN;
import com.kopeyka.android.photoreport.http.TaskResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteFragment extends Fragment {

    public Note mNote;
    public PhotoAdapter adapter;
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
    public static final String EXTRA_NOTE_ID = "com.android.notes.note_id";
    private static final String DIALOG_IMAGE = "image";
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;
    final int STATUS_NONE_LINK = 999;
    final int STATUS_ERROR = 1000;
    final int STATUS_COMPETE = 1001;
    final int STATUS_SENDING = 1002;
    final String D_TAG_SENDING = "SendingFromRetrofit";

    Handler h;
    ProgressDialog pd;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (!mNote.isComplete()){
            inflater.inflate(R.menu.fragment_note, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean selectionHandled;
        API apiboss;


        switch (item.getItemId()) {
//            case R.id.menu_item_upload_note_new:
//                if (mNote.isComplete()) {
//                    Toast.makeText(NoteFragment.super.requireContext(), "Отчет уже отправлен ранее", Toast.LENGTH_SHORT).show();
//                    //selectionHandled = super.onOptionsItemSelected(item);
//                    //break;
//                }
//                item.setEnabled(false);
//                apiboss = APIClient.putDoc().create(API.class);
//                Call<DocRequest> call = apiboss.postJson(new DocRequest(mNote, this));
//                call.enqueue(new Callback<DocRequest>() {
//                    @Override
//                    public void onResponse(Call<DocRequest> call, Response<DocRequest> response) {
//                        if (response.code() == 200) {
//                            h.sendEmptyMessage(STATUS_COMPETE);
//
//                        } else {
//                            h.sendEmptyMessage(STATUS_NONE_LINK);
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<DocRequest> call, Throwable t) {
//                        h.sendEmptyMessage(STATUS_NONE_LINK);
//
//                    }
//                });
//                selectionHandled = super.onOptionsItemSelected(item);
//                item.setEnabled(true);
//                break;
//

            case R.id.menu_item_upload_note:
                if (mNote.isComplete()) {
                    Toast.makeText(NoteFragment.super.requireContext(), "Отчет уже отправлен ранее", Toast.LENGTH_SHORT).show();
                    selectionHandled = super.onOptionsItemSelected(item);
                    //break;
                }

                pd = new ProgressDialog(getActivity());
                pd.setTitle("Отправка отчета: ");
                pd.setMessage(mNote.getTitle());
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setIndeterminate(false);


                SendingMsg runnable = new SendingMsg(mNote,this);
                new Thread(runnable).start();

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

        h = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case STATUS_NONE_LINK:
                        Toast.makeText(getContext(), "Нет связи с офисом попробуйте позже", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        break;
                    case STATUS_ERROR:
                        Toast.makeText(getContext(), "Проверьте Ваше интерет соединение", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        break;
                    case STATUS_COMPETE:
                        Toast.makeText(getContext(), "Фотоотчет отправлен успешно", Toast.LENGTH_SHORT).show();
                        mNote.setComplete(true);
                        pd.dismiss();
                        getActivity().onBackPressed();
                        break;
                    case STATUS_SENDING:
                        if(!pd.isShowing()){
                            pd.show();
                        }
                        pd.setMax(msg.arg2);
                        pd.setProgress(msg.arg1);
                        //Toast.makeText(NoteFragment.super.requireContext(), "Отправка фото №"+ msg.arg1+" из "+ msg.arg2 , Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };


        UUID noteId = (UUID) getArguments().getSerializable(EXTRA_NOTE_ID);
        mNote = Notebook.getInstance(getActivity()).getNote(noteId);

    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent,Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_note,parent,false);
        setHasOptionsMenu(true);

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
                    Intent intent = new Intent(getActivity(),NoteCameraActivity.class);
                    startActivityForResult(intent, REQUEST_PHOTO);
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_no_camera),Toast.LENGTH_LONG).show();
                }
            }
        });


        if (mNote.isComplete()) {
            mPhotoButton.setVisibility(View.GONE);
        }

        ListView listView = (ListView) view.findViewById(R.id.note_listView);
        mPhotos = mNote.getPhotoArray();
        adapter = new PhotoAdapter(mPhotos);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

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
    public void onCreateContextMenu(ContextMenu menu,View view,ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.note_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean selectionHandled;

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()) {
            case R.id.menu_item_delete_note:
                mPhotos = mNote.getPhotoArray();
                ArrayList<Photo> tempList = new ArrayList<>();
                tempList.add(mPhotos.get(position));
                mNote.dellPhotos(tempList);
                tempList.clear();
                mPhotos = mNote.getPhotoArray();
                adapter.notifyDataSetChanged();

                selectionHandled = true;
                break;
            default:
                selectionHandled = super.onContextItemSelected(item);
                break;
        }

        return selectionHandled;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {
                String fileName = data
                        .getStringExtra(NoteCameraFragment.EXTRA_PHOTO_FILENAME);
                if (fileName != null) {
                    Photo photo = new Photo(fileName);
                    mNote.setPhoto(photo);
                    mPhotos = mNote.getPhotoArray();
                    adapter.notifyDataSetChanged();
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.detach(this).attach(this).commit();


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
                TitleIMG.setText(String.valueOf(position + 1));

            }

            return convertView;
        }


    }

    public class SendingMsg implements Runnable {
        private Note mNote;
        Fragment fragment;

        public SendingMsg(Note mNote,Fragment fragment){
            this.fragment = fragment;
            this.mNote = mNote;
        }

        public void run() {
            API apiboss;

            boolean SuccessfulSending;
            apiboss = APIClient.putDoc().create(API.class);

            Integer countPhoto = mNote.getPhotoCount() - 1;
            for (int i = 0; i < countPhoto; i++) {
                h.sendMessage(h.obtainMessage(STATUS_SENDING, i+1, countPhoto));
                Call<DocRequestN> callN = apiboss.postJsonN(new DocRequestN(mNote,fragment, i, countPhoto));
                Log.d(D_TAG_SENDING, "create Call object - successful");

                try {
                    Response<DocRequestN> response = callN.execute();
                    SuccessfulSending = response.isSuccessful();
                    if (SuccessfulSending){
                        Log.d(D_TAG_SENDING, "successful "+(i+1)+"  "+countPhoto);
                    }else{
                        Log.d(D_TAG_SENDING, "fail sending");

                    }

                } catch (IOException e) {
                    h.sendMessage(h.obtainMessage(STATUS_ERROR, 0, 0));
                    e.printStackTrace();
                    break;
                }
            }
            h.sendMessage(h.obtainMessage(STATUS_COMPETE, 0, 0));


        }
    }


}






