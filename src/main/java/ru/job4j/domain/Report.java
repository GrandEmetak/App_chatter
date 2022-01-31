package ru.job4j.domain;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Добавим Bean в класс запуска приложения:
 * -@Bean
 * public RestTemplate getTemplate() {
 * return new RestTemplate(); }
 */
@Data
public class Report {
    private int id;

    private String name;

    private Timestamp created;

    private Person person;

    /**
     * r.created = new Timestamp(System.currentTimeMillis());
     * т.е. при каждом запросе на формирование отчета у нас будет генерироваться новый отчет с новой датой.
     *
     * @param id
     * @param name
     * @param person
     * @return
     */
    public static Report of(int id, String name, Person person) {
        Report r = new Report();
        r.id = id;
        r.name = name;
        r.person = person;
        r.created = new Timestamp(System.currentTimeMillis());
        return r;
    }
}
