package io.github.noeppi_noeppi.libx.config.gui;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

// Must keep state between builds
public interface ConfigScreenContent<T> {
    
    boolean searchable();
    
    void init(Consumer<T> inputChanged);
    void buildGui(Screen screen, ScreenManager manager, String search, Consumer<AbstractWidget> consumer);
    
    interface ScreenManager {
        
        <T> void open(Component title, ConfigScreenContent<T> content, Consumer<T> inputChanged);
        void close();
    }
}
