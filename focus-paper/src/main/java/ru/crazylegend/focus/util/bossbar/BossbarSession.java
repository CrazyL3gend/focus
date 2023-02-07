package ru.crazylegend.focus.util.bossbar;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import ru.crazylegend.focus.util.color.ColorUtils;


public class BossbarSession {

    BossBar bukkitBossBar;
    Bossbar bossbar;

    public static BossbarSession session(Bossbar bossbar) {
        return new BossbarSession(bossbar);
    }

    private BossbarSession(Bossbar bossbar) {
        this.bossbar = bossbar;
        this.bukkitBossBar = Bukkit.createBossBar(bossbar.getText(), bossbar.getColor(), bossbar.getStyle(), bossbar.getFlags());
        bukkitBossBar.setProgress(bossbar.getProgress());
    }

    BossbarSession(BossbarSession session) {
        this.bossbar = session.getBossbar();
        this.bukkitBossBar = session.getBukkitBossBar();

        bukkitBossBar.setTitle(bossbar.getText());
        bukkitBossBar.setColor(bossbar.getColor());
        bukkitBossBar.setStyle(bossbar.getStyle());
        bukkitBossBar.setProgress(bossbar.getProgress());
    }

    public void setProgress(double progress) {
        bukkitBossBar.setProgress(progress);
    }

    public void setColor(BarColor color) {
        bukkitBossBar.setColor(color);
    }

    public void setStyle(BarStyle style) {
        bukkitBossBar.setStyle(style);
    }

    public void setTitle(String title) {
        bukkitBossBar.setTitle(ColorUtils.color(title));
    }

    public Bossbar getBossbar() {
        return bossbar;
    }

    public BossBar getBukkitBossBar() {
        return bukkitBossBar;
    }

    public void hide(Player player) {
        bukkitBossBar.removePlayer(player);
    }

    public void show(Player player) {
        bukkitBossBar.addPlayer(player);
    }

    public void hideAll() {
        bukkitBossBar.removeAll();
    }

    public void showAll() {
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    public AdaptiveBossbarSession toAdaptive() {
        return AdaptiveBossbarSession.adapt(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BossbarSession that = (BossbarSession) o;
        return new EqualsBuilder().append(bukkitBossBar, that.bukkitBossBar).append(bossbar, that.bossbar).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(bukkitBossBar).append(bossbar).toHashCode();
    }
}
