package com.chatter.entity;

/**
 * Группы валидации
 * +
 * в модели данных необходимо указать
 * - @NotNull(message = "Id must be non null", groups = {
 *         Operation.OnUpdate.class, Operation.OnDelete.class
 * })
 * +
 * в методе Контроллера где используется это поле объекта указать:
 * - @PostMapping
 * - @Validated(Operation.OnCreate.class)
 */
public class Operation {

    public interface OnCreate { }

    public interface OnDelete { }

    public interface OnUpdate { }

}