package ru.crazylegend.focus.util.bossbar;


import ru.crazylegend.focus.util.color.ColorUtils;

public class AdaptiveBossbarSession extends BossbarSession {

    private AdaptiveBossbarSession(BossbarSession bossbar) {
        super(bossbar);
    }

    public static AdaptiveBossbarSession adapt(BossbarSession session) {
        return new AdaptiveBossbarSession(session);
    }

    public AdaptiveBossbarSession placeholder(String holder, Object to) {
        bukkitBossBar.setTitle(bossbar.getText().replaceAll(holder, ColorUtils.color(to.toString())));
        return this;
    }
}
