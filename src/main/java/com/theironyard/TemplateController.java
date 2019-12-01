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

    /*
    Лучше сделать класс-сервис с бизнес логикой, который в свою очередь будет тянуть репозиторий
     */
    @Autowired
    WidgetRepository widgetRepository;

    /*
    Не уверен, зачем здесь это.. это же контроллер, он принимает запросы и отдает их
    Если ты хочешь реализовать stateful, то это делается иначе
    смотри, все контроллеры синглтоны,
    это значит что есть только один объект этого типа инициализируемый при создании проекта
    Представь что подключается пользователь Ваня, для него сохраняюстя инструменты
    окей.. потом подключается Петя, а ты по прежнему хранишь инструменты Вани
    но по идее Петя не должен знать инструменты Вани, ну.. типа это как-будто я бы мог иметь доступ к твоим перепискам
    поэтому контроллер никогда ничего не хранит, только обрабатывает запросы
     */
    ArrayList<Request> requests = new ArrayList<>();
    ArrayList<Instrument> instruments = new ArrayList<>();
    //ArrayList<User> users = new ArrayList<>();

    //есть гет и пост запросы, нужно их разделять
    //не может быть несколько методов с одинаковым маппингом
    //иначе как сервер поймет какой метод запустить?
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

            for(RequestPart p: currentTasks){
                if (t>=p.getStartTime()+p.getTime()) { //next task if current is finished
                    if (p.getStartTime() != 0){
                        availability.set(instruments.indexOf(p.getInstrument()), availability.get(instruments.indexOf(p.getInstrument()))+1); //instrument is available
                    }
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

        Instrument i1 = new Instrument("Instrument 1", 3);
        instruments.add(i1);

        Instrument i2 = new Instrument("Instrument 2", 1);
        instruments.add(i2);

        Instrument i3 = new Instrument("Instrument 3", 2);
        instruments.add(i3);

        Request r1 = new Request();
        r1.addPart(i1, 2);
        r1.addPart(i2, 2);
        requests.add(r1);

        Request r2 = new Request();
        r2.addPart(i1, 3);
        r2.addPart(i2, 1);
        requests.add(r2);

        Request r3 = new Request();
        r3.addPart(i1, 1);
        r3.addPart(i2, 1);
        r3.addPart(i3, 1);
        requests.add(r3);

        this.processRequest();

        ArrayList<Widget> widgets = new ArrayList<>();

        for (Request req : requests){
            for (RequestPart rp : req.getParts()){
                widgets.add(new Widget(""+requests.indexOf(req), rp.getInstrument().getName()+", requested time: " + rp.getTime(), rp.getStartTime()));
            }
        }

        widgetRepository.save(widgets);

        model.addAttribute("widgets", widgetRepository.findAll());

        return "home";
    }
}
