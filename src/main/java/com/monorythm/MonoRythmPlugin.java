package com.monorythm;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Mono Rythm"
)
public class MonoRythmPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private MonoRythmConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	private MonoRythmPanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception {
		panel = injector.getInstance(MonoRythmPanel.class);
		panel.init();

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/images/icon.png");

		navButton = NavigationButton.builder()
				.tooltip("Mono Rythm")
				.icon(icon)
				.priority(10)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		panel.deinit();
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
	}

	@Provides
	MonoRythmConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MonoRythmConfig.class);
	}
}
