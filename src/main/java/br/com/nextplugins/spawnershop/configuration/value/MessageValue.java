package br.com.nextplugins.spawnershop.configuration.value;

import br.com.nextplugins.spawnershop.NextSpawnerShop;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageValue {

    private static final MessageValue instance = new MessageValue();

    private final Configuration configuration = NextSpawnerShop.getInstance().getConfigurationManager().getMessagesConfiguration();

    private final List<String> staffCommandHelp = messageList("messages.staff-help");

    private final String noPermission = message("messages.commands.no-permission");
    private final String executionError = message("messages.commands.execution-error");
    private final String incorrectTarget = message("messages.commands.incorrect-target");
    private final String incorrectUsage = message("messages.commands.incorrect-usage");

    public static <T> T get(Function<MessageValue, T> supplier) {
        return supplier.apply(MessageValue.instance);
    }

    private String colors(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String message(String key) {
        if (!configuration.contains(key)) {
            NextSpawnerShop.getInstance().getLogger().severe("O campo '" + key + "' não existe no arquivo '" + configuration.getName() + "', apague-o");
            return "";
        }

        return colors(configuration.getString(key));
    }

    private List<String> messageList(String key) {
        if (!configuration.contains(key)) {
            NextSpawnerShop.getInstance().getLogger().severe("O campo '" + key + "' não existe no arquivo '" + configuration.getName() + "', apague-o");
            return Lists.newArrayList();
        }

        return configuration.getStringList(key)
            .stream()
            .map(this::colors)
            .collect(Collectors.toList());
    }

}
