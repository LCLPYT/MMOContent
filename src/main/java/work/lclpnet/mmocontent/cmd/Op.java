package work.lclpnet.mmocontent.cmd;

import net.minecraft.command.CommandSource;

import java.util.function.Predicate;

public class Op {

    public static <T extends CommandSource> Predicate<T> level(int level) {
        return cs -> cs.hasPermissionLevel(level);
    }
}
