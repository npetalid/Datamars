package gr.petalidis.datamars.inspections.ui;

import android.app.Activity;
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
import java.util.UUID;
import java.util.stream.Collectors;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.SessionViewer;
import gr.petalidis.datamars.activities.StartActivity;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.FileService;
import gr.petalidis.datamars.inspections.service.InspectionService;

public class CreateInspectionAdapter extends ArrayAdapter<InspectionDateProducer> {
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
        rowView = inflater.inflate(R.layout.listinspections, parent, false);
     }
        final InspectionDateProducer item = objects.get(position);

        TextView inspectionDateText = (TextView) rowView.findViewById(R.id.inspectionDate);
        TextView inspectionProducer = (TextView) rowView.findViewById(R.id.inspectionProducer);
        Button exportCsvButton = (Button) rowView.findViewById(R.id.exportCsv);


        exportCsvButton.setOnClickListener(x->
        { try {
            File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            Inspection inspection = InspectionService.findInspectionFor(dbHandler,item.getId());
            FileService.export(inspection.toStrings(),downloads.getAbsolutePath(),inspection.getProducer1Tin()+"-"+simpleDateFormat.format(inspection.getDate())+".csv");
            FileService.copyFilesToDirectory(inspection.getScannedDocuments().stream().map(y->y.getImagePath()).collect(Collectors.toList()), downloads.getAbsolutePath()+"/"+inspection.getProducer1Tin()+"-"+UUID.randomUUID().toString());
            AlertDialog.Builder preSaveBuilder = new AlertDialog.Builder(this.context);

            String msg = "Τα στοιχεία αποθηκεύθηκαν στη θέση " + downloads.getCanonicalPath();
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
                ((Activity)context).startActivity(intent);
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
                ((Activity)context).startActivity(intent);
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
