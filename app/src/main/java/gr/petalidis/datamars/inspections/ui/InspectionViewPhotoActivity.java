
/*
 * Copyright 2017-2019 Nikolaos Petalidis
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gr.petalidis.datamars.inspections.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.domain.ScannedDocument;
import gr.petalidis.datamars.inspections.dto.ThumbnailDto;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;

public class InspectionViewPhotoActivity extends AppCompatActivity {
    private String pictureFilePath = "";
    private static final int REQUEST_PICTURE_CAPTURE = 1;
    private ImageView image;
    private List<ThumbnailDto> thumbnails = new ArrayList<>();
    private final List<ThumbnailDto> newThumbnails = new ArrayList<>();

    private Inspection inspection;
    private InspectionStepPhotoAdapter adapter;
    private DbHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_view_photo);
        image = findViewById(R.id.picture);
        dbHandler = new DbHandler(this.getApplicationContext());

        Button captureButton = findViewById(R.id.capture);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            captureButton.setEnabled(false);
        }
        captureButton.setOnClickListener(view -> this.sendTakePictureIntent());

        if (savedInstanceState != null) {
            inspection = (Inspection)savedInstanceState.getSerializable("inspection");
            pictureFilePath = savedInstanceState.getString("pictureFilePath");
            if (pictureFilePath != null && !pictureFilePath.equals("")) {
                File file = new File(pictureFilePath);
                image.setImageURI(Uri.fromFile(file));
            }
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }

        thumbnails = inspection.getScannedDocuments().stream().map(x->new ThumbnailDto(new File(x.getImagePath()).getName(),x.getImagePath())).collect(Collectors.toList());

        adapter = new InspectionStepPhotoAdapter(this,
                android.R.layout.simple_list_item_1, thumbnails);
        final ListView listview = findViewById(R.id.imageslist);

        listview.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("inspection", inspection);
        outState.putSerializable("pictureFilePath", pictureFilePath);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            thumbnails = (ArrayList<ThumbnailDto>) savedInstanceState.getSerializable("thumbnails");
            pictureFilePath = savedInstanceState.getString("pictureFilePath");
            if (pictureFilePath != null && !pictureFilePath.equals("")) {
                File file = new File(pictureFilePath);
                image.setImageURI(Uri.fromFile(file));
            }
        } else {
            inspection = (Inspection) getIntent().getExtras().getSerializable("inspection");
        }
    }

    private void sendTakePictureIntent() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File pictureFile;
            try {
                pictureFile = getPictureFile();
                Uri photoURI = FileProvider.getUriForFile(this,
                        "gr.petalidis.datamars.fileprovider",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File getPictureFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pictureFile = "BOVSCANNER_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(pictureFile, ".jpg", storageDir);
        pictureFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()) {

                Uri uri = Uri.fromFile(imgFile);
                image.setImageURI(Uri.fromFile(imgFile));


                //image.setImageBitmap(imageBitmap);
                thumbnails.add(new ThumbnailDto(imgFile.getName(), pictureFilePath));
                newThumbnails.add(new ThumbnailDto(imgFile.getName(), pictureFilePath));

                adapter.notifyDataSetChanged();
            }
        }
    }

    public void savePhotos(View view) {
        List<ScannedDocument> scannedDocuments = newThumbnails.stream().map(x -> new ScannedDocument(inspection.getId(), x.getImagePath())).collect(Collectors.toList());

        InspectionService.updatePhotos(dbHandler, inspection, scannedDocuments);
        newThumbnails.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.success).setMessage(R.string.success);
        builder.show();

    }

    private class InspectionStepPhotoAdapter extends ArrayAdapter<ThumbnailDto> {


        private final Context context;
        private final List<ThumbnailDto> bitmaps;

        InspectionStepPhotoAdapter(Context context, int resource, List<ThumbnailDto> bitmaps) {
            super(context, resource, bitmaps);
            this.context = context;
            this.bitmaps = bitmaps;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listimages, parent, false);
            final ThumbnailDto item = bitmaps.get(position);

            ImageView imageView = rowView.findViewById(R.id.img_preview);
            final int THUMBSIZE = 512;

            Bitmap imageBitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(item.getImagePath()),
                    THUMBSIZE, THUMBSIZE);
            imageView.setImageBitmap(imageBitmap);

            imageView.setOnTouchListener((view, motionEvent) -> {
                File file = new File(item.getImagePath());
                image.setImageURI(Uri.fromFile(file));
                pictureFilePath = item.getImagePath();
                return true;
            });
            TextView textView = rowView.findViewById(R.id.img_label);
            textView.setText(item.getName());
            return rowView;
        }

    }
}