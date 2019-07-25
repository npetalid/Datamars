package gr.petalidis.datamars.inspections.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.log4j.Logger;

import java.util.List;

import gr.petalidis.datamars.Log4jHelper;
import gr.petalidis.datamars.R;
import gr.petalidis.datamars.SessionViewer;
import gr.petalidis.datamars.inspections.domain.Inspection;
import gr.petalidis.datamars.inspections.dto.InspectionDateProducer;
import gr.petalidis.datamars.inspections.exceptions.PersistenceException;
import gr.petalidis.datamars.inspections.repository.DbHandler;
import gr.petalidis.datamars.inspections.service.InspectionService;
import gr.petalidis.datamars.rsglibrary.RsgSessionFiles;

public class CreateInspectionActivity extends Activity {

    private DbHandler dbHandler;
    private Context context;
    private RsgSessionFiles files = new RsgSessionFiles();

    private static final Logger log = Log4jHelper.getLogger(CreateInspectionActivity.class.getName());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            files = (RsgSessionFiles) savedInstanceState.getSerializable("dates");
            if (files == null) {
                files = new RsgSessionFiles();
            }
        }

        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        if (intent != null) {
            files = (RsgSessionFiles) intent.getSerializableExtra("dates");
            if (files == null) {
                files = new RsgSessionFiles();
            }
        }

        setContentView(R.layout.activity_create_inspection);

        dbHandler = new DbHandler(this.getApplicationContext());
        context = this;

        try {
            List<InspectionDateProducer> inspectionDateProducerList = InspectionService.findAllInspections(dbHandler);

            final ListView listview = (ListView) findViewById(R.id.inspectionList);

            final CreateInspectionAdapter adapter = new CreateInspectionAdapter(this,
                    android.R.layout.simple_list_item_1, inspectionDateProducerList);

//            LayoutInflater inflater = getLayoutInflater();
//
//            ViewGroup header = (ViewGroup) inflater.inflate(R.layout.listinspectionsheader, listview, false);
//            listview.addHeaderView(header);
//
//            ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.listinspectionsfooter, listview, false);
//            listview.addFooterView(footer);

            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, final View view,
                                        int position, long id) {
                    if (position >= 1 && position <= inspectionDateProducerList.size()) {
                        final InspectionDateProducer item = (InspectionDateProducer) parent.getItemAtPosition(position);
                        try {
                            Inspection inspection = InspectionService.findInspectionFor(dbHandler, item.getId());
                            Intent intent = new Intent(context, InspectionViewActivity.class);
                            intent.putExtra("inspection", inspection);
                            startActivity(intent);
                        } catch (PersistenceException e) {
                            log.error( e.getLocalizedMessage());
                            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }

            });

        } catch (PersistenceException e) {
            log.error( e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putSerializable("dates", files);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            files = (RsgSessionFiles) savedInstanceState.getSerializable("dates");
            if (files == null) {
                files = new RsgSessionFiles();
            }
        }
    }
    public void goToInspectionStepOneActivity(View view) {
        Intent intent = new Intent(this, InspectionStepOneActivity.class);
        intent.putExtra("files",files);
        startActivity(intent);
    }
}
