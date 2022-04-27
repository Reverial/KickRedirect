package me.dreamerzero.kickredirect.configuration;

import java.nio.file.Path;
import java.util.Set;

import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import me.dreamerzero.kickredirect.enums.CheckMode;
import me.dreamerzero.kickredirect.enums.SendMode;

public class Configuration {
    private Configuration(){}
    public static Config loadMainConfig(Path path, Logger logger){
        final Path configPath = path.resolve("config.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header("KickRedirect | by 4drian3d\n")
            )
            .path(configPath)
            .build();

        final Config config;
        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(Config.class);
            node.set(Config.class, config);
            loader.save(node);
        } catch (ConfigurateException exception){
            logger.error("Could not load config.conf file, error: {}", exception.getMessage());
            return null;
        }
        return config;
    }

    public static Messages loadMessages(Path path, Logger logger){
        final Path configPath = path.resolve("messages.conf");
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .defaultOptions(opts -> opts
                .shouldCopyDefaults(true)
                .header("KickRedirect | by 4drian3d\n")
            )
            .path(configPath)
            .build();

        final Messages messages;
        try {
            final CommentedConfigurationNode node = loader.load();
            messages = node.get(Messages.class);
            node.set(Messages.class, messages);
            loader.save(node);
        } catch (ConfigurateException exception){
            logger.error("Could not load messages.conf file, error: {}", exception.getMessage());
            return null;
        }
        return messages;
    }

    @ConfigSerializable
    public static class Config {
        @Comment("Sets the list of available servers to forward to the player\nDepending on the configuration of sendMode it will be sent to one server or another")
        private String[] serversToRedirect = {"lobby1", "lobby2"};

        @Comment("Redirect the player if the expulsion message is null or empty?")
        private boolean redirectOnNullMessage = true;

        @Comment("Sets whether to perform whitelist or blacklist detection\nAvailable options:\nWHITELIST: It will check if the expulsion string contains any of this strings\nBLACKLIST: It will check if the expulsion string not contains any of this strings")
        private CheckMode checkMode = CheckMode.WHITELIST;

        @Comment("Set the messages to be checked by blacklist or whitelist in case they are present in the expulsion message")
        private Set<String> messagesToCheck = Set.of("kicked from server", "shutdown");

        @Comment("Sets the sending mode\nAvailable options:\nTO_FIRST | It will send the player to the first available server configured in serversToRedirect\nTO_EMPTIEST_SERVER | Send the player to the emptiest server that is available according to the serversToRedirect configuration\nRANDOM | Send to a random server from the configured servers")
        private SendMode sendMode = SendMode.TO_FIRST;

        @Comment("Sets the limit of times the random server will be calculated to send in case the sending mode is RANDOM")
        private int randomAttempts = 5;

        public String[] getServersToRedirect(){
            return this.serversToRedirect;
        }

        public CheckMode checkMode() {
            return this.checkMode;
        }

        public boolean redirectOnNullMessage() {
            return this.redirectOnNullMessage;
        }

        public Set<String> getMessagesToCheck(){
            return this.messagesToCheck;
        }

        public SendMode getSendMode(){
            return this.sendMode;
        }

        public int getRandomAttempts(){
            return this.randomAttempts;
        }
    }

    @ConfigSerializable
    public static class Messages {
        @Comment("Sets the message to send if no server is found to which to send the player")
        private String kickMessage = "<gradient:#FF0000:dark_red>You could not be sent to a backup server";

        @Comment("Message to send in plugin reload start")
        private String reloadingMessage = "<gradient:red:#fff494>[KickRedirect]</gradient> <gradient:#78edff:#699dff>Reloading Configuration...";

        @Comment("Message to send in plugin reload")
        private String reloadMessage = "<gradient:red:#fff494>[KickRedirect]</gradient> <gradient:#78edff:#699dff>Correctly Reloaded Configuration";

        @Comment("Message to send in failed plugin reload")
        private String failedReload = "<gradient:red:#fff494>[KickRedirect]</gradient> <gradient:#78edff:#699dff>An error ocurred in configuration reload, check your console logs";;

        @Comment("Message to send in player correctly redirect")
        private String redirectMessage = "";

        @Comment("Error message to be sent in case no server is available to send to player")
        private String noServersFoundToRedirect = "<gradient:red:#fff494>[KickRedirect]</gradient> <gradient:#b82e00:#ff4000>No servers were found to redirect the player to. <gray>SendMode: <sendmode>";

        public String kickMessage(){
            return this.kickMessage;
        }

        public String reloadingMessage() {
            return this.reloadingMessage;
        }

        public String reloadMessage() {
            return this.reloadMessage;
        }

        public String failedReload() {
            return this.failedReload;
        }

        public String redirectMessage() {
            return this.redirectMessage;
        }

        public String noServersFoundToRedirect() {
            return this.noServersFoundToRedirect;
        }
    }
}
