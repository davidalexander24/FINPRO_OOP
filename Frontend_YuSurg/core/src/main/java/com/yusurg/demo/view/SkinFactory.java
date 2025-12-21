package com.yusurg.demo.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;

public class SkinFactory {

    // Modern Surgical Interface Color Palette
    public static final Color PRIMARY_BG = new Color(0.1f, 0.1f, 0.2f, 0.85f);
    public static final Color ACCENT_COLOR = new Color(0.0f, 0.8f, 0.8f, 1f);
    public static final Color DANGER_COLOR = new Color(0.9f, 0.2f, 0.2f, 1f);
    public static final Color SUCCESS_COLOR = new Color(0.2f, 0.9f, 0.2f, 1f);
    public static final Color WARNING_COLOR = new Color(1.0f, 0.8f, 0.0f, 1f);
    public static final Color DIGITAL_GRAY = new Color(0.5f, 0.5f, 0.5f, 1f);

    public static Skin createDefaultSkin() {
        Skin skin = new Skin();

        // Create fonts
        BitmapFont defaultFont = new BitmapFont();
        BitmapFont smallFont = new BitmapFont();
        smallFont.getData().setScale(0.8f);
        BitmapFont largeFont = new BitmapFont();
        largeFont.getData().setScale(1.5f);

        skin.add("default-font", defaultFont);
        skin.add("small-font", smallFont);
        skin.add("large-font", largeFont);

        // Create colors
        skin.add("white", Color.WHITE);
        skin.add("black", Color.BLACK);
        skin.add("gray", Color.GRAY);
        skin.add("light-gray", new Color(0.7f, 0.7f, 0.7f, 1f));
        skin.add("dark-gray", new Color(0.3f, 0.3f, 0.3f, 1f));
        skin.add("cyan", ACCENT_COLOR);
        skin.add("danger", DANGER_COLOR);
        skin.add("success", SUCCESS_COLOR);
        skin.add("warning", WARNING_COLOR);

        // Create textures
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        // Panel background
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(PRIMARY_BG);
        pixmap.fill();
        skin.add("panel-bg", new Texture(pixmap));
        pixmap.dispose();

        // Button textures
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.25f, 0.3f, 0.4f, 1f));
        pixmap.fill();
        skin.add("button", new Texture(pixmap));
        pixmap.dispose();

        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.15f, 0.2f, 0.3f, 1f));
        pixmap.fill();
        skin.add("button-pressed", new Texture(pixmap));
        pixmap.dispose();

        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.3f, 0.4f, 0.5f, 1f));
        pixmap.fill();
        skin.add("button-over", new Texture(pixmap));
        pixmap.dispose();

        // Tooltip background
        pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.05f, 0.05f, 0.1f, 0.95f));
        pixmap.fill();
        skin.add("tooltip-bg", new Texture(pixmap));
        pixmap.dispose();

        // Create button style
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = defaultFont;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = new Color(0.8f, 0.8f, 0.8f, 1f);
        textButtonStyle.up = skin.newDrawable("button");
        textButtonStyle.down = skin.newDrawable("button-pressed");
        textButtonStyle.over = skin.newDrawable("button-over");
        skin.add("default", textButtonStyle);

        // Create label styles
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = defaultFont;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);

        // Title style
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = largeFont;
        titleStyle.fontColor = WARNING_COLOR;
        skin.add("title", titleStyle);

        // Vital style
        Label.LabelStyle vitalStyle = new Label.LabelStyle();
        vitalStyle.font = defaultFont;
        vitalStyle.fontColor = ACCENT_COLOR;
        skin.add("vital", vitalStyle);

        // Header style
        Label.LabelStyle headerStyle = new Label.LabelStyle();
        headerStyle.font = defaultFont;
        headerStyle.fontColor = ACCENT_COLOR;
        skin.add("header", headerStyle);

        // Create window style
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.titleFont = defaultFont;
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.background = skin.newDrawable("panel-bg");
        skin.add("default", windowStyle);

        // Create ScrollPane style
        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        scrollPaneStyle.background = skin.newDrawable("white", new Color(0.1f, 0.1f, 0.15f, 0.5f));
        scrollPaneStyle.vScroll = skin.newDrawable("white", new Color(0.3f, 0.3f, 0.4f, 1f));
        scrollPaneStyle.vScrollKnob = skin.newDrawable("white", ACCENT_COLOR);
        scrollPaneStyle.hScroll = skin.newDrawable("white", new Color(0.3f, 0.3f, 0.4f, 1f));
        scrollPaneStyle.hScrollKnob = skin.newDrawable("white", ACCENT_COLOR);
        skin.add("default", scrollPaneStyle);

        // Create TextField style
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = defaultFont;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.messageFontColor = new Color(0.7f, 0.7f, 0.7f, 1f);
        textFieldStyle.background = skin.newDrawable("panel-bg");
        textFieldStyle.focusedBackground = skin.newDrawable("white", new Color(0.15f, 0.15f, 0.25f, 1f));
        textFieldStyle.cursor = skin.newDrawable("white", ACCENT_COLOR);
        textFieldStyle.selection = skin.newDrawable("white", new Color(0.0f, 0.5f, 0.5f, 0.5f));
        skin.add("default", textFieldStyle);

        // Create TextTooltip style
        Label.LabelStyle tooltipLabelStyle = new Label.LabelStyle();
        tooltipLabelStyle.font = smallFont;
        tooltipLabelStyle.fontColor = Color.WHITE;
        skin.add("tooltip", tooltipLabelStyle);

        TextTooltip.TextTooltipStyle tooltipStyle = new TextTooltip.TextTooltipStyle();
        tooltipStyle.label = tooltipLabelStyle;
        tooltipStyle.background = skin.newDrawable("tooltip-bg");
        skin.add("default", tooltipStyle);

        return skin;
    }
}

