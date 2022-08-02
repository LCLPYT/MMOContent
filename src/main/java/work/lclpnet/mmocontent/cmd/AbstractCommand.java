package work.lclpnet.mmocontent.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractCommand {

    protected String name;
    protected Set<String> aliases = null;

    public AbstractCommand(String name) {
        this.name = Objects.requireNonNull(name);
    }

    protected abstract LiteralArgumentBuilder<ServerCommandSource> build(LiteralArgumentBuilder<ServerCommandSource> builder);

    public Set<String> getAliases() {
        return aliases;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAliases(Set<String> aliases) {
        this.aliases = aliases;
    }

    public void addAlias(String... aliases) {
        Objects.requireNonNull(aliases);

        for (String alias : aliases)
            addAlias(alias);
    }

    public void addAlias(String alias) {
        Objects.requireNonNull(alias);

        if (aliases == null) aliases = new HashSet<>();

        aliases.add(alias);
    }

    public void register(final CommandDispatcher<ServerCommandSource> dispatcher) {
        var node = dispatcher.register(build(CommandManager.literal(name)));

        /* Aliases */
        if (aliases != null) {
            aliases.forEach(alias -> dispatcher.register(CommandManager.literal(alias).redirect(node)));
        }
    }
}
