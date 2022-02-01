package ru.job4j.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * отчет для стороннего сервиса
 */
@Data
public class ReportMessage {
    private int id;

    private String name;

    private Timestamp created;

    private Message message;

    /**
     * r.created = new Timestamp(System.currentTimeMillis());
     * т.е. при каждом запросе на формирование отчета у нас будет генерироваться новый отчет с новой датой.
     *
     * @param id
     * @param name
     * @param
     * @return
     */
    public static ReportMessage of(int id, String name, Message message) {
        ReportMessage r = new ReportMessage();
        r.id = id;
        r.name = name;
        r.message = message;
        r.created = new Timestamp(System.currentTimeMillis());
        return r;
    }
}
