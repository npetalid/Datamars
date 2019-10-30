
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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.ExcelService;
import gr.petalidis.datamars.inspections.service.FileService;
import gr.petalidis.datamars.inspections.service.InspectionService;

class CreateInspectionAdapter extends ArrayAdapter<InspectionDateProducer> {
    private final Context context;
    private final List<InspectionDateProducer> objects;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final DbHandler dbHandler;
    private static final Logger log = Log4jHelper.getLogger(CreateInspectionAdapter.class.getName());

    public CreateInspectionAdapter(Context context, int resource, List<InspectionDateProducer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        dbHandler = new DbHandler(context.getApplicationContext());
    }

    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = Objects.requireNonNull(inflater).inflate(R.layout.listinspections, parent, false);
     }
        final InspectionDateProducer item = objects.get(position);

        TextView inspectionDateText = rowView.findViewById(R.id.inspectionDate);
        TextView inspectionProducer = rowView.findViewById(R.id.inspectionProducer);
        Button exportCsvButton = rowView.findViewById(R.id.exportCsv);


        exportCsvButton.setOnClickListener(x->
        { try {
            File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Inspection inspection = InspectionService.findInspectionFor(dbHandler,item.getId());
            String randomString = inspection.getProducer1Tin()+"-"+UUID.randomUUID().toString();
            String directory = downloads.getAbsolutePath()+File.separator+randomString;
           // FileService.export(inspection.toStrings(),downloads.getAbsolutePath(),inspection.getProducer1Tin()+"-"+simpleDateFormat.format(inspection.getDate())+".csv");
            FileService.copyFilesToDirectory(inspection.getScannedDocuments().stream().map(y->y.getImagePath()).collect(Collectors.toList()), directory);
            ExcelService.export(inspection,directory,inspection.getProducer1Tin()+"-"+simpleDateFormat.format(inspection.getDate())+".xls");
            AlertDialog.Builder preSaveBuilder = new AlertDialog.Builder(this.context);

            String msg = "Τα στοιχεία αποθηκεύθηκαν στη θέση " + downloads.getName()+File.separator+randomString;
            preSaveBuilder.setTitle("Αποθήκευση ελέγχου").setMessage(msg)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            preSaveBuilder.show();
        } catch (IOException | PersistenceException e) {
            Log.e("CreateInspectionActivity",e.getLocalizedMessage());
        }});


        inspectionProducer.setText(item.getMainProducer());
        inspectionDateText.setText(item.getInspectionDate());
        inspectionDateText.setOnTouchListener((x,y)->{
            try {
                Inspection inspection = InspectionService.findInspectionFor(dbHandler, item.getId());
                Intent intent = new Intent(context, InspectionViewActivity.class);
                intent.putExtra("inspection", inspection);
                context.startActivity(intent);
                return true;
            } catch (PersistenceException e) {
                log.error( e.getLocalizedMessage());
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        inspectionProducer.setOnTouchListener((x,y)->{
            try {
                Inspection inspection = InspectionService.findInspectionFor(dbHandler, item.getId());
                Intent intent = new Intent(context, InspectionViewActivity.class);
                intent.putExtra("inspection", inspection);
                context.startActivity(intent);
                return true;
            } catch (PersistenceException e) {
                log.error( e.getLocalizedMessage());
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
        return rowView;
    }


}
