package ru.crazylegend.focus.menu.filling;

import ru.crazylegend.focus.api.Creatable;
import ru.crazylegend.focus.menu.MenuSession;
import ru.crazylegend.focus.menu.button.MenuButton;

import java.util.HashMap;
import java.util.Map;

@FunctionalInterface
public interface FillingStrategy {

    void generate(ResultBuilder builder, MenuSession session);

    class ResultBuilder implements Creatable<Map<Integer, MenuButton>> {

        private Map<Integer, MenuButton> buttonMap = new HashMap<>();

        private ResultBuilder() {
        }

        public static ResultBuilder newBuilder() {
            return new ResultBuilder();
        }

        public ResultBuilder addButton(MenuButton button, int... slots) {
            for (int slot : slots) {
                buttonMap.put(slot, button);
            }
            return this;
        }

        public Map<Integer, MenuButton> create() {
            return buttonMap;
        }
    }

}
