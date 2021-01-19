package ru.mertsalovda.core_impl.calculator.translator.expressions;

/**
 * Класс, описывающий операнд математического выражения
 */
public class Operand implements Expression {
    private double value = 0;

    public Operand(String oValue) {
        value = Double.parseDouble(oValue);
    }

    public Operand(Double oValue) {
        value = oValue;
    }

    @Override
    public Type getType() {
        return Type.OPERAND;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
