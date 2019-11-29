package com.theironyard;

import com.theironyard.entities.*;
import com.theironyard.repositories.WidgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Random;

@Controller
public class TemplateController {

    @Autowired
    WidgetRepository widgetRepository;

    @Resource
    ArrayList<Request> requests = new ArrayList<>();
    @Resource
    ArrayList<Instrument> instruments = new ArrayList<>();
    @Resource
    ArrayList<User> users = new ArrayList<>();

    @RequestMapping(path = "/")
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

    @RequestMapping(path = "/")
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

            for(RequestPart p: currentTasks){
                if (t>=p.getStartTime()+p.getTime()) { //next task if current is finished
                    availability.set(instruments.indexOf(p.getInstrument()), availability.get(instruments.indexOf(p.getInstrument()))+1); //instrument is available
                    RequestPart next = this.nextTask(requests.get(currentTasks.indexOf(p)));
                    if (next != null) {
                        currentTasks.set(currentTasks.indexOf(p), next); //replace task
                        p = next;
                    }
                    else
                        taskToRemove = currentTasks.indexOf(p); //mark for delete  if all tasks done
                }

                int curAvailability = availability.get(instruments.indexOf(p.getInstrument()));
                if(curAvailability > 0){ //instrument for p is available
                    if (p.getStartTime() == 0) { //if p has not started yet
                        p.setStartTime(t);
                        availability.set(instruments.indexOf(p.getInstrument()), curAvailability - 1); //instrument is  unavailable
                    }
                }
            }
            if(taskToRemove >= 0)
                currentTasks.remove(taskToRemove); //delete
        }
    }

    @RequestMapping(path = "/")
    public String home(Model model){

        widgetRepository.deleteAll();

        Instrument i1 = new Instrument("stanok1", 3);
        instruments.add(i1);

        Instrument i2 = new Instrument("stanok2", 1);
        instruments.add(i2);

        Request r1 = new Request();
        r1.addPart(i1, 2);
        r1.addPart(i2, 2);
        requests.add(r1);

        Request r2 = new Request();
        r2.addPart(i1, 3);
        r2.addPart(i2, 1);
        requests.add(r2);

        this.processRequest();

        ArrayList<Widget> widgets = new ArrayList<>();


        for (RequestPart rp : r1.getParts()) {
            widgets.add(new Widget(rp.getInstrument().getName(), String.valueOf(rp.getTime()), rp.getStartTime()));
        }
        for (RequestPart rp : r2.getParts()) {
            widgets.add(new Widget(rp.getInstrument().getName(), String.valueOf(rp.getTime()), rp.getStartTime()));
        }

        widgetRepository.save(widgets);

        model.addAttribute("widgets", widgetRepository.findAll());

        return "home";
    }
}
