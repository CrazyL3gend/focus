package ru.crazylegend.focus.menu.filling;

import ru.crazylegend.focus.menu.MenuSession;
import ru.crazylegend.focus.menu.button.MenuButton;

import java.util.HashMap;
import java.util.Map;

public class PatternFillingStrategy<S extends MenuSession> implements FillingStrategy {

    private String[] matrix;
    private Map<Character, MenuButton> buttonMap = new HashMap<>();

    public PatternFillingStrategy() {
    }

    public PatternFillingStrategy(String... matrix) {
        setPattern(matrix);
    }

    public PatternFillingStrategy<S> setPattern(String... matrix) {
        this.matrix = matrix;
        return this;
    }

    public PatternFillingStrategy<S> withButton(char itemChar, MenuButton button) {
        buttonMap.put(itemChar, button);
        return this;
    }

    @Override
    public void generate(ResultBuilder builder, MenuSession session) {
        int slot = 0;
        for (String line : matrix) {
            for (char item : line.toCharArray()) {
                MenuButton button = buttonMap.get(item);
                if (button != null) {
                    builder.addButton(button, slot);
                }
                slot++;
            }
        }
    }
}
