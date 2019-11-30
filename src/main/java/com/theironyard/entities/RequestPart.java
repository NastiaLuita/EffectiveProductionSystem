package com.theironyard.entities;

import javax.persistence.*;

@Entity
public class RequestPart {
    /*
    Каждый Entity должен иметь поле id
     */
    @Id
    @GeneratedValue
    int id;

    /*
    А тут уже не просто колонка в таблице, а связь между двумя таблицами, нужно утточнить что за связь
    много к одному (
        fetch type -
            то как мы подгружаем эти данные при загрузке объекта этого класса,
            сразу или при обращении
    )
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instrument_id")
    Instrument instrument;
    int time;
    int startTime = 0;

    public RequestPart() {}

    public RequestPart(Instrument instrument, int time){
        this.instrument = instrument;
        this.time = time;
    }

    public Instrument getInstrument(){
        return instrument;
    }

    public int getTime(){
        return time;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }
}
