package ru.mertsalovda.core_impl.calculator.translator.expressions;

/**
 * Математическое выражение
 */
public interface Expression {
    enum Type {CONSTANT, OPERAND, OPERATION, FUNCTION, BRACKET}

    /**
     * Возвращает тип данного математического выражения
     *
     * @return тип выражения
     */
    Type getType();

    /**
     * Вычисляет значение математического выражения
     *
     * @return результат вычисления
     */
    Double getValue();

    @Override
    String toString();
}
