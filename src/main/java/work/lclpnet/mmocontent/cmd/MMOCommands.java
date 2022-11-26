package work.lclpnet.mmocontent.cmd;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicNCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.function.Predicate;

public class MMOCommands {

    public static boolean isPlayer(ServerCommandSource cs) {
        return cs.getEntity() != null && cs.getEntity() instanceof PlayerEntity;
    }

    public static boolean isEntity(ServerCommandSource cs) {
        return cs.getEntity() != null;
    }

    public static boolean isPlayerOpLevel2(ServerCommandSource cs) {
        return matchesAll(cs, Op.level(2), MMOCommands::isPlayer);
    }

    @SafeVarargs
    public static <T extends CommandSource> boolean matchesAll(T cs, Predicate<T>... predicates) {
        return Arrays.stream(predicates).allMatch(predicate -> predicate.test(cs));
    }

    public static DynamicCommandExceptionType dynamicError(String key) {
        return new DynamicCommandExceptionType(arg -> Text.translatable(key, arg));
    }

    public static DynamicNCommandExceptionType dynamicNError(String key) {
        return new DynamicNCommandExceptionType(args -> Text.translatable(key, args));
    }
}
