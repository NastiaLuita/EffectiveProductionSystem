package com.theironyard;

import com.theironyard.entities.*;
import com.theironyard.repositories.InstrumentRepository;
import com.theironyard.repositories.RequestPartRepository;
import com.theironyard.repositories.RequestRepository;
import com.theironyard.repositories.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Random;

@Controller
public class EffectiveProductionSystem {

    /*
    Лучше сделать класс-сервис с бизнес логикой, который в свою очередь будет тянуть репозиторий
     */
    @Autowired
    WidgetRepository widgetRepository;

    @Autowired
    InstrumentRepository instrumentRepository;

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    RequestPartRepository requestPartRepository;

    private ArrayList<Request> requests = new ArrayList<>();
    private ArrayList<Instrument> instruments = new ArrayList<>();
    //ArrayList<User> users = new ArrayList<>();

    //есть гет и пост запросы, нужно их разделять
    //не может быть несколько методов с одинаковым маппингом
    @RequestMapping(path = "/nextTask")
    public RequestPart nextTask(Request request){
        RequestPart next = null;
        for(RequestPart p: request.getParts()) {
            if (p.getStartTime() == 0) {
                next = p;
                break;
            }
        }
        return next;
    }

    //можно указать method = RequestMethod.POST
    //можно просто писать не @RequestMapping, а @GetMapping и @PostMapping
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public void processRequest(){
        ArrayList<Integer> availability = new ArrayList<>(instruments.size());
        for (Instrument i: instruments)
            availability.add(i.getCount());

        ArrayList<RequestPart> currentTasks = new ArrayList<>(requests.size());
        for (Request r: requests)
            currentTasks.add(r.getParts().get(0));

        int t = 0; //time
        while(currentTasks.size() != 0)
        {
            t += 1;
            int taskToRemove = -1;

            boolean change = true;
            int count = 0;

            while (change && count <= currentTasks.size()) {
                change = false;
                count += 1;

                for (RequestPart p : currentTasks) {
                    if (t >= p.getStartTime() + p.getTime()) { //next task if current is finished
                        change = true;

                        if (p.getStartTime() != 0) {
                            availability.set(instruments.indexOf(p.getInstrument()), availability.get(instruments.indexOf(p.getInstrument())) + 1); //instrument is available
                        }

                        RequestPart next = nextTask(requests.get(currentTasks.indexOf(p)));
                        if (next != null) {
                            currentTasks.set(currentTasks.indexOf(p), next); //replace task
                            p = next;
                        } else
                            taskToRemove = currentTasks.indexOf(p); //mark for delete  if all tasks done
                    }

                    int curAvailability = availability.get(instruments.indexOf(p.getInstrument()));
                    if (curAvailability > 0) { //instrument for p is available
                        if (p.getStartTime() == 0) { //if p has not started yet
                            p.setStartTime(t);
                            availability.set(instruments.indexOf(p.getInstrument()), curAvailability - 1); //instrument is  unavailable
                        }
                    }
                }
                if (taskToRemove >= 0 && count == 1)
                    currentTasks.remove(taskToRemove); //delete
            }
        }
    }

    @RequestMapping(path = "/instruments")
    public void InitInstruments() {
        instrumentRepository.deleteAll();

        instruments.add(new Instrument("Instrument 1", 3));
        instruments.add(new Instrument("Instrument 2", 1));
        instruments.add(new Instrument("Instrument 3", 2));

        instrumentRepository.save(instruments);
    }

    @RequestMapping(path = "/requests")
    public void InitRequests() {
        ArrayList<RequestPart> rp = new ArrayList<>();

        requestPartRepository.deleteAll();
        requestRepository.deleteAll();

        Request r1 = new Request();
        r1.addPart(instruments.get(0), 2);
        r1.addPart(instruments.get(1), 2);
        requests.add(r1);
        rp.addAll(r1.getParts());

        Request r2 = new Request();
        r2.addPart(instruments.get(0), 3);
        r2.addPart(instruments.get(1), 1);
        requests.add(r2);
        rp.addAll(r2.getParts());

        Request r3 = new Request();
        r3.addPart(instruments.get(0), 1);
        r3.addPart(instruments.get(1), 1);
        r3.addPart(instruments.get(2), 1);
        requests.add(r3);
        rp.addAll(r3.getParts());

        requestRepository.save(requests);
        requestPartRepository.save(rp);
    }

    @RequestMapping(path = "/")
    public String home(Model model){

        widgetRepository.deleteAll();

        this.InitInstruments();

        this.InitRequests();

        this.processRequest();

        ArrayList<Widget> widgets = new ArrayList<>();
        ArrayList<RequestPart> reqParts = new ArrayList<>();

        for (Request req : requests){
            for (RequestPart rp : req.getParts()){
                widgets.add(new Widget(""+requests.indexOf(req), rp.getInstrument().getName()+", requested time: " + rp.getTime(), rp.getStartTime()));
                reqParts.add(rp);
            }
        }

        widgetRepository.save(widgets);

        model.addAttribute("widgets", widgetRepository.findAll());
        model.addAttribute("instruments", instrumentRepository.findAll());
        model.addAttribute("requests", requestRepository.findAll());
        model.addAttribute("requestParts", requestPartRepository.findAll());

        return "home";
    }
}
