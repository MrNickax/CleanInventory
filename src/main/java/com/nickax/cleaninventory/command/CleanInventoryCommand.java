package com.nickax.cleaninventory.command;

import com.nickax.cleaninventory.CleanInventory;
import com.nickax.genten.command.BaseCommand;
import com.nickax.genten.command.messages.CommandMessages;
import com.nickax.genten.command.model.CommandHelp;
import com.nickax.genten.command.model.CommandProperties;
import com.nickax.genten.language.command.LanguageCommand;
import com.nickax.genten.language.operation.LanguageAccessor;
import com.nickax.genten.text.LocalizedText;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CleanInventoryCommand extends BaseCommand {

    public CleanInventoryCommand(CleanInventory plugin) {
        super("cleaninventory", createProperties(), createMessages(plugin.getLanguageAccessor()));
        addSubCommand(new ReloadCommand(plugin, this));
        addSubCommand(new MenuCommand(plugin, this));
        addSubCommand(new LanguageCommand<>(this, plugin.getLanguageAccessor(), plugin.getPlayerDataCache(), "cleaninventory.language"));
    }

    @Override
    public boolean onExecute(CommandSender sender, String name, String[] args) {
        CommandHelp help = getHelp(sender);
        help.display();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String name, String[] args) {
        return List.of();
    }

    private static CommandProperties createProperties() {
        return CommandProperties.builder()
                .setAliases(List.of("ci"))
                .build();
    }
    
    private static CommandMessages createMessages(LanguageAccessor languageAccessor) {
        return CommandMessages.builder()
                .setNoPermissionMessage(new LocalizedText<>("no-permission", languageAccessor))
                .setInvalidCommandSenderMessage(new LocalizedText<>("invalid-command-sender", languageAccessor))
                .setInvalidPageMessage(new LocalizedText<>("invalid-page", languageAccessor))
                .setCommandFormat(new LocalizedText<>("command-format", languageAccessor))
                .setHelpFormat(new LocalizedText<>("help-format", languageAccessor))
                .setHelpNotFoundMessage(new LocalizedText<>("help-not-found", languageAccessor))
                .build();
    }
}
