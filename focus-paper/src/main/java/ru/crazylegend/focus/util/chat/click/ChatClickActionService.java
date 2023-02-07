package ru.crazylegend.focus.util.chat.click;


import ru.crazylegend.focus.api.service.FocusServiceApi;

public interface ChatClickActionService extends FocusServiceApi {

    ChatClickAction createAction(ChatClickAction action);

    ChatClickAction createAction(ChatClickAction.Builder builder);

}
