package com.example.viniciusoliveira.image;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<String> filePaths=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    Context context;
    CRUD crud=new CRUD();
    Dialog d;
    Button buttonAdd;
    public final int SELECT_PHOTO = 1;
    public final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        gridView = (GridView) findViewById(R.id.gv);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                    ImageView imageView = (ImageView) view.findViewById(R.id.imageviewgrid);

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displayInputDialog(i);
                        }
                    });
//                if(d != null) {
//                    if(!d.isShowing())
//                    {
                        System.out.println("ASUSAHASUUHASSAHUASHUUHSAUSAH " + i);

//                    }else
//                    {
//                        d.dismiss();
//                    }
//                }
            }
        });

        buttonAdd = (Button) findViewById(R.id.buttonadd);
        adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, R.id.buttonadd);
        gridView.setAdapter(adapter);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog(-1);
            }
        });
    }


    public void displayInputDialog(final int pos)
    {
        d=new Dialog(context);
        d.setTitle("GRIDVIEW CRUD");
        d.setContentView(R.layout.input_dialog);

        final EditText nameEditTxt= (EditText) d.findViewById(R.id.nameEditText);
        Button addBtn= (Button) d.findViewById(R.id.addBtn);
        Button updateBtn= (Button) d.findViewById(R.id.updateBtn);
        Button deleteBtn= (Button) d.findViewById(R.id.deleteBtn);

        if(pos== -1)
        {
            addBtn.setEnabled(true);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }else
        {
            addBtn.setEnabled(true);
            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
            nameEditTxt.setText(crud.getNames().get(pos));
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=nameEditTxt.getText().toString();
//                    crud.save(name);
//                    nameEditTxt.setText("");
//                    adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,crud.getNames());
//                    gridView.setAdapter(adapter);s
                    d.dismiss();
                    openGallery();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName=nameEditTxt.getText().toString();

                if(newName.length()>0 && newName != null)
                {
                    if(crud.update(pos,newName))
                    {
                        nameEditTxt.setText(newName);
                        adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,crud.getNames());
                        gridView.setAdapter(adapter);
                        d.dismiss();
                    }

                }else
                {
                    Toast.makeText(MainActivity.this, "Preencha", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( crud.delete(pos))
                {

                    filePaths.remove(pos);
                    nameEditTxt.setText("");
                    adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,crud.getNames());
                    gridView.setAdapter(adapter);
                    d.dismiss();
                }
            }
        });

        d.show();
    }

    private void openGallery() {
        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {

            filePaths.clear();

            FilePickerBuilder.getInstance().setMaxCount(5)
                    .setSelectedFiles(filePaths)
                    .setActivityTheme(R.style.AppTheme)
                    .pickPhoto(MainActivity.this);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        switch (requestCode) {
//            case SELECT_PHOTO:
//                if (resultCode == RESULT_OK) {
//                    Uri selectedImage = imageReturnedIntent.getData();
//                    InputStream imageStream = null;
//
//                    try {
//                        imageStream = getContentResolver().openInputStream(selectedImage);
//                    }
//                    catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                    View grid;
//                    LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                        grid = inflater.inflate(R.layout.gridviewitem, null);
//                        ImageView imageView = (ImageView)grid.findViewById(R.id.imageViewgrid);
//
//                        imageView.setImageURI(selectedImage);// To display selected image in image view
//                    }
//                }
//
//
//        }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    FilePickerBuilder.getInstance().setMaxCount(10)
                            .setSelectedFiles(filePaths)
                            .setActivityTheme(R.style.AppTheme)
                            .pickPhoto(MainActivity.this);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {case FilePickerConst.REQUEST_CODE:

                if(resultCode==RESULT_OK && data!=null)
                {
                    filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    Spacecraft s;
                    ArrayList<Spacecraft> spacecrafts=new ArrayList<>();

                    try
                    {
                        for (String path:filePaths) {
                            s=new Spacecraft();
                            s.setName(path.substring(path.lastIndexOf("/")+1));

                            s.setUri(Uri.fromFile(new File(path)));
                            spacecrafts.add(s);
                        }

                        gridView.setAdapter(new CustomAdapter(this,spacecrafts));
                        Toast.makeText(MainActivity.this, "Total = "+String.valueOf(spacecrafts.size()), Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
        }
    }
    }





//        xButton = (Button) findViewById(R.id.xbutton);
//        xButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RemoveImage(v);
//            }
//        });
//        buttonLoadImage = (Button) findViewById(R.id.button_add);
//        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChooseImage(v);
//            }
//        });
//    }
//
//    public void ChooseImage(View v) {
//        openGallery();
//    }
//
//    public void RemoveImage(View v){
//        image1.clearFocus();
//    }
//
//    private void openGallery() {
//        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.setType("image/*");
//        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//    }

