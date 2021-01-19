package ru.mertsalovda.core_impl.calculator.translator.expressions;

/**
 * Класс, описывающий константу математического выражения
 */
public class Constant implements Expression {
    enum ConstantType {NONE, E, PI}

    ConstantType type = ConstantType.NONE;

    public Constant(String literal) {
        type = ExpressionsSet.getConstantTypeByLiteral(literal);
    }

    @Override
    public Type getType() {
        return Type.CONSTANT;
    }

    @Override
    public Double getValue() {
        switch (type) {
            case E:
                return StrictMath.E;
            case PI:
                return StrictMath.PI;
        }

        return 0.0;
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + type;
    }
}
